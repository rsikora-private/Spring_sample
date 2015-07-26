package pl.demo.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.util.PlainTextFilter;
import java.util.Date;

/**
 * Created by Robert on 22.02.15.
 */

@Service
public class CommentService {

    private final PlainTextFilter plainTextFilter;

    @Autowired
    public CommentService(final PlainTextFilter plainTextFilter) {
        this.plainTextFilter = plainTextFilter;
    }

    // @Inject private CommentMailSender mailSender;

    public void postComment(final Comment comment) {

        prepareComment(comment);

       // callback.post(comment);
       // mailSender.sendNotificationEmail(comment);
    }

    private void prepareComment(final Comment comment) {
        comment.setDateCreated(new Date());
        comment.setText(plainTextFilter.escapeHtml(comment.getText()));
    }
}
