package main.service;

import main.api.response.PostMethodResponse;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import main.model.repositories.PostRepository;
import main.model.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    public PostMethodResponse getResponse (long timestamp, boolean active,
                                           String title, List <String> tags, String text, User user){

        PostMethodResponse response = new PostMethodResponse();
        TreeMap <String, String> errors = checkErrors(title, text);

        Date now = new Date();
        Date time = new Date(timestamp * 1000);

        if (now.after(time)){
            time = now;
        }

        if (errors.size() == 0) {
            Post post = new Post();

            post.setIsActive(active ? 1 : 0);
            post.setUser(user);
            post.setText(text);
            post.setTitle(title);
            post.setTime(time);
            post.setTags(getTags(tags, post));

            postRepository.save(post);
            response.setResult(true);

        } else {
            response.setResult(false);
            response.setErrors(errors);
        }

        return response;
    }

    public PostMethodResponse putPost (long timestamp, boolean active,
                                       String title, List <String> tags, String text,
                                       Post post, User user) {

        PostMethodResponse response = new PostMethodResponse();
        TreeMap <String, String> errors = checkErrors(title, text);

        Date now = new Date();
        Date time = new Date(timestamp * 1000);

        if (now.after(time)){
            time = now;
        }

        if (errors.size() == 0) {
            post.setTime(time);
            post.setIsActive(active ? 1 : 0);
            post.setTitle(title);
            post.setText(text);
            post.setTags(getTags(tags, post));

            if (user.getIsModerator() == 0){
                post.setModerationStatus("NEW");
            }
            postRepository.save(post);
            response.setResult(true);

        } else {
            response.setResult(false);
            response.setErrors(errors);
        }

        return response;
    }

    private TreeMap <String, String> checkErrors (String title, String text){

        TreeMap <String, String> errors = new TreeMap<>();

        if (title.length() == 0){
            errors.put("title", "Заголовок не установлен");
        }
        if (title.length() > 0 && title.length() < 3){
            errors.put("title", "Заголовок слишком короткий");
        }
        if (text.length() == 0){
            errors.put("text", "Текст не заполнен");
        }
        if (text.length() > 0 && text.length() < 50){
            errors.put("text", "Текст не заполнен");
        }

        return errors;
    }

    private List <Tag> getTags (List <String> tags, Post post){
        List <Tag> tagList = new ArrayList<>();

        for (String tagName : tags){
            Tag tag = tagRepository.findByName(tagName);

            if (tag == null){
                Tag newTag = new Tag();
                newTag.setName(tagName);
                tagList.add(newTag);
            } else {
                tagList.add(tag);
            }
        } return tagList;
    }
}
