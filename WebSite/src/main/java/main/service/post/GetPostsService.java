package main.service.post;

import main.api.response.PostsResponse;
import main.model.Post;
import main.model.User;
import main.model.repositories.PostCommentsRepository;
import main.model.repositories.PostRepository;
import main.model.repositories.PostVoteRepository;
import main.model.repositories.UserRepository;
import main.service.dto.DTOPost;
import main.service.dto.DTOUser;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GetPostsService {

    @Autowired
    private PostVoteRepository voteRepository;
    @Autowired
    private PostCommentsRepository commentsRepository;
    @Autowired
    private PostRepository repository;
    @Autowired
    private UserRepository userRepository;


    public PostsResponse getPostsResponse(Pageable page, String mode) {

        Page<Post> posts = getPostList(page, mode);
        return createResponse(posts);
    }

    public PostsResponse getPostsResponseByQuery (Pageable page, String query) {

        Page<Post> posts = repository.getByQuery(query, page, new Date());
        return createResponse(posts);
    }

    public PostsResponse getPostsResponseByDate (Pageable page, String date) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Page<Post> posts = repository.getPostsByDate(format.parse(date), page, new Date());
        return createResponse(posts);
    }


    public PostsResponse getPostsResponseByTag (Pageable page, String tag) {

        Page<Post> posts = repository.getPostsByTag(tag, page);
        return createResponse(posts);
    }

    public PostsResponse getPostsResponseForUser (Pageable page, String email, String status) {

        User user = userRepository.findByEmail(email);
        Page<Post> posts = getPostListForUser(page, user, status);
        return createResponse(posts);
    }

    public PostsResponse getPostsResponseForModeration (Pageable page, String email, String status) {

        User user = userRepository.findByEmail(email);
        Page<Post> posts = getPostListForModeration(page, user.getId(), status);
        return createResponse(posts);
    }

    private PostsResponse createResponse (Page<Post> posts){

        PostsResponse response = new PostsResponse();
        List <DTOPost> responsePosts = new ArrayList<>();

        if (posts != null) {
            for (Post post : posts) {
                User user = post.getUser();
                DTOUser dtoUser = new DTOUser(user.getId(), user.getName());
                DTOPost dtoPost = new DTOPost(post.getId(),
                        post.getTime().getTime() / 1000, dtoUser,
                        post.getTitle(), getAnnounce(post.getText()),
                        voteRepository.getCountForPost(post, 1),
                        voteRepository.getCountForPost(post, -1),
                        commentsRepository.getCountForPost(post), post.getViewCount());
                responsePosts.add(dtoPost);
            }
            response.setCount(posts.getTotalElements());
        } else {
            response.setCount(0);
        }
        response.setPosts(responsePosts);
        return response;
    }

    private Page<Post> getPostList (Pageable page, String mode){
        Page<Post> posts;
        switch (mode) {
            case  ("recent"):
                posts = repository.getRecent(page, new Date());
                break;
            case  ("popular"):
                posts = repository.getPopular(page, new Date());
                break;
            case  ("best"):
                posts = repository.getBest(page, new Date());
                break;
            case  ("early"):
                posts = repository.getEarly(page, new Date());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }
        return posts;
    }

    private Page<Post> getPostListForUser (Pageable page, User user, String status){
        Page<Post> posts;
        switch (status) {
            case  ("inactive"):
                posts = repository.findInactive(user, page);
                break;
            case  ("pending"):
                posts = repository.findPending(user, page);
                break;
            case  ("declined"):
                posts = repository.findDeclined(user, page);
                break;
            case  ("published"):
                posts = repository.findPublished(user, page);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
        return posts;
    }

    private Page<Post> getPostListForModeration (Pageable page, Integer id, String status){
        Page<Post> posts;
        switch (status) {
            case  ("new"):
                posts = repository.findNewPostsForModeration(page);
                break;
            case  ("accepted"):
                posts = repository.findAcceptedPostsForModeration(id, page);
                break;
            case  ("declined"):
                posts = repository.findDeclinedPostsForModeration(id, page);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
        return posts;
    }

    private String getAnnounce (String announce){
        String text = Jsoup.parse(announce).text();
        if (text.length() < 150){
            return text;
        } else {
            return text.substring(0, 149)+ "...";
        }
    }
}
