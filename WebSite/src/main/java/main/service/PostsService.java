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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


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


    public PostsResponse getPostsResponse(Integer offset, Integer limit, String mode, String query) {

        PostsResponse response = new PostsResponse();
        List <Post> posts = getPostList(offset, limit, mode, query);
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
        response.setCount(repository.count());
        response.setPosts(responsePosts);
        return response;
    }

    private List<Post> getPostList (Integer offset, Integer limit, String mode, String query){
        List <Post> posts;
        switch (mode) {
            case  ("recent"):
                posts = repository.getRecent(query, PageRequest.of(offset, limit));
                break;
            case  ("popular"):
                posts = repository.getPopular(PageRequest.of(offset, limit));
                break;
            case  ("best"):
                posts = repository.getBest( PageRequest.of(offset, limit));
                break;
            case  ("early"):
                posts = repository.getEarly(PageRequest.of(offset, limit));
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
