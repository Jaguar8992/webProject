package main.service;

import main.api.response.ResultResponse;
import main.model.Post;
import main.model.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModerationDecisionService {

    @Autowired
    private PostRepository postRepository;

    public ResultResponse getResponse (int postId, String decision, int moderationId){

        ResultResponse response = new ResultResponse();
        Post post = postRepository.findById(postId).get();

        post.setModerationId(moderationId);

        switch (decision){
            case ("accept"):
                post.setModerationStatus("ACCEPTED");
                break;
            case ("decline"):
                post.setModerationStatus("DECLINED");
                break;
        }

        postRepository.save(post);

        if (!postRepository.findById(postId).get().equals("NEW")){
            response.setResult(true);
        }

        return response;
    }
}
