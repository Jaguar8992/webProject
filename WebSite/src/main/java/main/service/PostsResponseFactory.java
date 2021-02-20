package main.service;

import main.api.response.PostsResponse;
import main.model.Post;
import main.model.User;
import main.model.repositories.PostCommentsRepository;
import main.model.repositories.PostRepository;
import main.model.repositories.PostVoteRepository;
import main.service.ServiceData.PostBody;
import main.service.ServiceData.UserBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class PostsResponseFactory {

    @Autowired
    private PostVoteRepository voteRepository;
    @Autowired
    private PostCommentsRepository commentsRepository;
    @Autowired
    private PostRepository repository;


    public PostsResponse getPostsResponse(Integer offset, Integer limit, String mode) {

        PostsResponse response = new PostsResponse();
        List <Post> posts = getPostList(offset, limit, mode);
        List <PostBody> responsePosts = new ArrayList<>();
        for (Post post : posts){
            User user = post.getUser();
            PostBody postBody = new PostBody();
            postBody.setId(post.getId());
            postBody.setTimestamp(post.getTime().getTime());
            postBody.setUser(new UserBody(user.getId(), user.getName()));
            postBody.setTitle(post.getTitle());
            postBody.setAnnounce(getAnnounce(post.getText()));
            postBody.setLikeCount(voteRepository.getCountForPost(post.getId(), 1));
            postBody.setDislikeCount(voteRepository.getCountForPost(post.getId(), -1));
            postBody.setCommentCount(commentsRepository.getCountForPost(post.getId()));
            postBody.setViewCount(post.getViewCount());
            responsePosts.add(postBody);
        }
        response.setCount(repository.count());
        response.setPosts(responsePosts);
        return response;
    }

    private List<Post> getPostList (Integer offset, Integer limit, String mode){
        List <Post> posts = new ArrayList<>();
        Iterable<Post> postIterator;
        switch (mode) {
            case  ("recent"):
                postIterator = repository.getRecent(limit, offset);
                break;
            case  ("popular"):
                postIterator = repository.getPopular(limit, offset);
                break;
            case  ("best"):
                postIterator = repository.getBest(limit, offset);
                break;
            case  ("early"):
                postIterator = repository.getEarly(limit, offset);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }
        for (Post post : postIterator){
            posts.add(post);
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
