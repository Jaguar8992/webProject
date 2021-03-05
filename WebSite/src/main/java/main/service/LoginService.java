package main.service;

import main.api.response.LoginResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import main.service.dto.DTOUserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository repository;

    public LoginResponse getLoginResponse (User user){

        LoginResponse response = new LoginResponse();

        if (user == null) {
            response.setResult(false);
        } else {
            DTOUserLoginResponse userResponse = new DTOUserLoginResponse();

            userResponse.setId(user.getId());
            userResponse.setEmail(user.getEmail());
            userResponse.setName(user.getName());

            if (user.getIsModerator() != 1) {
                userResponse.setModeration(false);
                userResponse.setModerationCount(0);
            }

            response.setResult(true);
            response.setUser(userResponse);
        }
        return response;
    }
}