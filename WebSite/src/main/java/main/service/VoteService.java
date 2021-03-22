package main.service;

import main.api.response.ResultResponse;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.model.repositories.PostRepository;
import main.model.repositories.PostVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VoteService {

    @Autowired
    private PostVoteRepository repository;
    @Autowired
    private PostRepository postRepository;

    public ResultResponse getResponse(boolean vote, int id, User user) {

        ResultResponse response = new ResultResponse();
        Post post = postRepository.findById(id).get();
        PostVote postVote = repository.findVote(post, user);

        //like
        if (vote) {
            if (postVote != null) {
                if (postVote.getValue() == -1) {
                    repository.delete(postVote);
                    createVote(true, post, user);
                    response.setResult(true);
                } else if (postVote.getValue() == 1) {
                    response.setResult(false);
                }
            } else {
                createVote(true, post, user);
                response.setResult(true);
            }
        }

        //dislike
        if (!vote) {
            if (postVote != null) {
                if (postVote.getValue() == 1) {
                    repository.delete(postVote);
                    createVote(false, post, user);
                    response.setResult(true);
                } else if (postVote.getValue() == -1) {
                    response.setResult(false);
                }
            } else {
                createVote(false, post, user);
                response.setResult(true);
            }
        }
        return response;
    }

    private void createVote(boolean vote, Post post, User user) {
        PostVote postVote = new PostVote();

        postVote.setTime(new Date());
        postVote.setValue(vote ? 1 : -1);
        postVote.setPost(post);
        postVote.setUser(user);

        repository.save(postVote);
    }
}