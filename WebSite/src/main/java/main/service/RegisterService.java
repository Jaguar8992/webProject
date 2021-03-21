package main.service;

import main.api.response.PostMethodResponse;
import main.model.User;
import main.model.repositories.CaptchaRepository;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.TreeMap;
import java.util.regex.Pattern;

@Service
public class RegisterService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CaptchaRepository captchaRepository;

    public PostMethodResponse getResponse (String email, String password, String name, String captcha, String captchaSecret) {

        TreeMap <String, String> errors = new TreeMap<>();

        Pattern namePattern = Pattern.compile("^[а-яА-Я ]*$");

        if (userRepository.findByEmail(email) != null){
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (userRepository.findByName(name) != null){
            errors.put("name", "Это имя уже занято");
        }
        if (!namePattern.matcher(name).find()){
            errors.put("name", "Имя указано неверно");
        }
        if (password.length() < 6){
            errors.put("password", "Пароль короче 6-ти символов");
        }
        if (!captchaRepository.getBySecretCode(captchaSecret).getCode().equals(captcha)){
            errors.put("captcha", "Код с картинки введён неверно");
        }

        PostMethodResponse response = new PostMethodResponse();

        if (errors.size() == 0){
            response.setResult(true);
            addUser(email, password, name);
        } else {
            response.setResult(false);
            response.setErrors(errors);
        }
        return response;
    }

    private void addUser (String email, String password, String name){
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(new BCryptPasswordEncoder(12).encode(password));
        user.setRegTime(new Date());
        userRepository.save(user);
    }
}
