package pl.demo.core.service.mail;

import pl.demo.core.util.EnumValuable;

/**
 *
 * Keeps path to templates using by mailing service.
 *
 * Created by robertsikora on 05.11.2015.
 */
public enum TemplateType implements EnumValuable {

    REGISTRATION_TEMPLATE("comment_template.vm"),
    ADVERT_TEMPLATE("email_template.vm"),
    COMMENT_TEMPLATE("comment_template.vm");

    private final static String FOLDER = "/velocity/";
    private final String value;

    TemplateType(String templateName){
        this.value = templateName;
    }

    @Override
    public String getValue() {
        return FOLDER.concat(value);
    }
}
