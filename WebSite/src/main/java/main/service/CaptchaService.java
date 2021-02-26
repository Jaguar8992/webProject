package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.CaptchaResponse;
import main.model.CaptchaCode;
import main.model.repositories.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CaptchaService {

    private final String IMAGE_NAME = "data:image/png;base64,";
    @Autowired
    private CaptchaRepository repository;

    public CaptchaResponse getResponse ()  {

        Cage cage = new GCage();
        String code = cage.getTokenGenerator().next();
        String encodingString = Base64.getEncoder().encodeToString(cage.draw(code));
        String secret = UUID.randomUUID().toString().replace("-", "");

        CaptchaCode captcha = new CaptchaCode();
        captcha.setCode(code);
        captcha.setSecretCode(secret);
        captcha.setTime(new Date());
        repository.save(captcha);

        List<CaptchaCode> staleCaptcha = repository.getStaleCaptcha();
        staleCaptcha.forEach((o) -> repository.delete(o));

        return new CaptchaResponse(secret, IMAGE_NAME + encodingString);
    }
}
