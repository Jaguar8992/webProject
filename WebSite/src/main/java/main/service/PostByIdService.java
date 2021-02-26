package main.service;

import main.api.response.PostByIdResponse;
import main.model.Post;
import main.model.PostComment;
import main.model.User;
import main.model.repositories.*;
import main.service.dto.DTOComment;
import main.service.dto.DTOCommentUser;
import main.service.dto.DTOUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostByIdService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostCommentsRepository postCommentsRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostVoteRepository voteRepository;

    public PostByIdResponse getPostByIdResponse (int id){

        PostByIdResponse response = new PostByIdResponse();

        Post post = postRepository.findById(id).get();
        User user = userRepository.getByPost(post);
        List<DTOComment> comments = getComments(post);
        List <String> tags = tagRepository.getNameByPost(post);

        response.setId(post.getId());
        response.setTimestamp(post.getTime().getTime() / 1000);
        response.setActive(post.getIsActive()==1);
        response.setUser(new DTOUser(user.getId(), user.getName()));
        response.setTitle(post.getTitle());
        response.setText(post.getText());
        response.setLikeCount(voteRepository.getCountForPost(post, 1));
        response.setDislikeCount(voteRepository.getCountForPost(post, -1));
        response.setViewCount(post.getViewCount());
        response.setComments(comments);
        response.setTags(tags);

        return response;
    }

    private List<DTOComment> getComments (Post post){

        List <DTOComment> comments = new ArrayList<>();
        List <PostComment> postComments = postCommentsRepository.findByPost(post);

        for (PostComment comment : postComments){
            User user = userRepository.getByComment(comment);
            DTOCommentUser dtoCommentUser = new DTOCommentUser(user.getId(), user.getName(), user.getPhoto());
            DTOComment dtoComment = new DTOComment(comment.getId(),
                    comment.getTime().getTime() / 1000,
                    comment.getText(), dtoCommentUser);
            comments.add(dtoComment);
        }
        return comments;
    }
}
