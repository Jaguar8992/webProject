package main.service.general;

import main.api.response.CommentResponse;
import main.api.response.PostMethodResponse;
import main.model.PostComment;
import main.model.User;
import main.model.repositories.PostCommentsRepository;
import main.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.TreeMap;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCommentsRepository postCommentsRepository;
    private final int minTextSize = 2;

    public ResponseEntity getResponse (Integer parentId, int postId, String text, User user) {

        TreeMap<String, String> errors = new TreeMap<>();
        PostMethodResponse response;

        if (text.length() <= minTextSize) {
            errors.put("text", "Текст комментария не задан или слишком короткий");
        }

        if (parentId != null && !postCommentsRepository.findById(parentId).isPresent()) {
            errors.put("message", "Коментарий не существует");
        }

        if (!postRepository.findById(postId).isPresent()) {
            errors.put("message", "Пост не сущесвует");
        }

        if (errors.size() == 0) {

            PostComment comment = new PostComment();

            comment.setTime(new Date());
            comment.setUser(user);
            comment.setPost(postRepository.findById(postId).get());
            comment.setText(text);

            if (parentId != null) {
                comment.setParentId(parentId);
            }

            postCommentsRepository.save(comment);

            return ResponseEntity.ok().body(new CommentResponse(comment.getId()));

        } else {
            response = new PostMethodResponse();
            response.setErrors(errors);

            return ResponseEntity.status(400).body(response);
        }
    }
}
