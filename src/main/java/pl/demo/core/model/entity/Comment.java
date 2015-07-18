package pl.demo.core.model.entity;

import org.hibernate.validator.constraints.NotEmpty;

import pl.demo.core.model.entity.Advert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

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

    @Size(min = 1, max = 100)
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Size(min = 1, max = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Size(max = 250)
    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Size(max = 20)
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
}
