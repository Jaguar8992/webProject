package main.service;

import main.api.response.PostMethodResponse;
import main.model.CaptchaCode;
import main.model.User;
import main.model.repositories.CaptchaRepository;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
public class PasswordRestoreService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CaptchaRepository captchaRepository;

    public PostMethodResponse getResponse(String code, String password, String captcha, String secretCaptcha) {

        PostMethodResponse response = new PostMethodResponse();
        TreeMap<String, String> errors = new TreeMap<>();
        CaptchaCode captchaCode = captchaRepository.getBySecretCode(secretCaptcha);

        if (!userRepository.findByCode(code).isPresent()) {
            errors.put("code", "Ссылка для восстановления пароля устарела.         <a href=         \"/auth/restore\">Запросить ссылку снова</a>");
        }
        if (password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (!captchaCode.getCode().equals(captcha)) {
            errors.put("captcha", "Код с картинки введён неверно");
        }

        if (errors.size() == 0) {
            User user = userRepository.findByCode(code).get();
            user.setPassword(new BCryptPasswordEncoder(12).encode(password));
            userRepository.save(user);

            response.setResult(true);
        } else {
            response.setErrors(errors);
        }
        return response;
    }
}
