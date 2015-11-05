package pl.demo.core.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by robertsikora on 22.10.15.
 */

@Component
public class MailSenderListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MailSenderListener.class);

    @Autowired
    private MailService mailService;

    @EventListener
    public void handleCreationEventAdvert(final SendMailEvent sendMailEvent) {
        LOGGER.debug("sending new email");
        mailService.sendMail(sendMailEvent.getTarget(), sendMailEvent.getTemplate());
    }
}
