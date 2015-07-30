package pl.demo.core.model.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_25;
import static pl.demo.core.model.entity.ModelConstans.TEXT_LENGTH_250;

/**
 * Created by Robert on 22.02.15.
 */

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity implements Comparable<Comment> {

    private String name;
    private String email;
    private String web;
    private String ipAddr;
    private Date dateCreated;
    private String text;

    @Size(min=1, max=TEXT_LENGTH_250)
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(min=1, max=TEXT_LENGTH_250)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(max=TEXT_LENGTH_250)
    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Size(max=TEXT_LENGTH_25)
    @Column(name="ip_addr")
    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    @NotNull
    @Column(name="date_created", nullable = false)
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @NotEmpty
    @Lob
    @Column(name="text", columnDefinition="CLOB NOT NULL", table="comments")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(Comment o) {
        return getDateCreated().compareTo(o.getDateCreated());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return new EqualsBuilder()
                .append(name, comment.name)
                .append(email, comment.email)
                .append(web, comment.web)
                .append(ipAddr, comment.ipAddr)
                .append(dateCreated, comment.dateCreated)
                .append(text, comment.text)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(email)
                .append(web)
                .append(ipAddr)
                .append(dateCreated)
                .append(text)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("email", email)
                .append("web", web)
                .append("ipAddr", ipAddr)
                .append("dateCreated", dateCreated)
                .append("text", text)
                .toString();
    }
}
