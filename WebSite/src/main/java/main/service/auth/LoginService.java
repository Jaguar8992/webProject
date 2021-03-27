package main.service.auth;

import main.api.response.LoginResponse;
import main.model.User;
import main.model.repositories.PostRepository;
import main.model.repositories.UserRepository;
import main.service.dto.DTOUserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PostRepository postRepository;

    public ResponseEntity<LoginResponse> getLoginResponse(User user) {

        LoginResponse response = new LoginResponse();

        DTOUserLoginResponse userResponse = new DTOUserLoginResponse();

        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        userResponse.setPhoto(user.getPhoto());

        if (user.getIsModerator() != 1) {
            userResponse.setModeration(false);
            userResponse.setModerationCount(0);
            userResponse.setSettings(false);
        } else {
            userResponse.setModeration(true);
            userResponse.setSettings(true);
            userResponse.setModerationCount(postRepository.countNewPostsForModeration());
        }

        response.setResult(true);
        response.setUser(userResponse);

        return ResponseEntity.ok().body(response);
    }
}
