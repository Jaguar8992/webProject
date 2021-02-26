package main.service;

import main.api.response.PostsResponse;
import main.model.Post;
import main.model.User;
import main.model.repositories.PostCommentsRepository;
import main.model.repositories.PostRepository;
import main.model.repositories.PostVoteRepository;
import main.service.dto.DTOPost;
import main.service.dto.DTOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostsService {

    @Autowired
    private PostVoteRepository voteRepository;
    @Autowired
    private PostCommentsRepository commentsRepository;
    @Autowired
    private PostRepository repository;


    public PostsResponse getPostsResponse(Pageable page, String mode) {

        long count = repository.count();
        List <Post> posts = getPostList(page, mode);

        return createResponse(posts, count);
    }

    public PostsResponse getPostsResponseByQuery (Pageable page, String query) {

        long count = repository.countByQuery(query);
        List <Post> posts = repository.getByQuery(query, page);

        return createResponse(posts, count);
    }

    public PostsResponse getPostsResponseByDate (Pageable page, String date) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long count = repository.countByDate(format.parse(date));
        List <Post> posts = repository.getPostsByDate(format.parse(date), page);

        return createResponse(posts, count);
    }


    public PostsResponse getPostsResponseByTag (Pageable page, String tag) {

        long count = repository.countByTagName(tag);
        List <Post> posts = repository.getPostsByTag(tag, page);

        return createResponse(posts, count);
    }

    private PostsResponse createResponse (List <Post> posts, long count){

        PostsResponse response = new PostsResponse();
        List <DTOPost> responsePosts = new ArrayList<>();

        for (Post post : posts){
            User user = post.getUser();
            DTOUser dtoUser = new DTOUser(user.getId(), user.getName());
            DTOPost dtoPost = new DTOPost(post.getId(),
                    post.getTime().getTime() / 1000, dtoUser,
                    post.getTitle(), getAnnounce(post.getText()),
                    voteRepository.getCountForPost(post, 1),
                    voteRepository.getCountForPost(post, -1),
                    commentsRepository.getCountForPost(post),post.getViewCount());
            responsePosts.add(dtoPost);
        }
        response.setCount(count);
        response.setPosts(responsePosts);
        return response;
    }

    private List<Post> getPostList (Pageable page, String mode){
        List <Post> posts;
        switch (mode) {
            case  ("recent"):
                posts = repository.getRecent(page);
                break;
            case  ("popular"):
                posts = repository.getPopular(page);
                break;
            case  ("best"):
                posts = repository.getBest(page);
                break;
            case  ("early"):
                posts = repository.getEarly(page);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }
        return posts;
    }


    private String getAnnounce (String announce){
        String result;
        announce.replaceAll("\\<[^>]*>","");
        if (announce.length() < 150){
            result = announce;
        } else {
            result = announce.substring(0, 149)+ "...";
        }
            return result;
    }
}