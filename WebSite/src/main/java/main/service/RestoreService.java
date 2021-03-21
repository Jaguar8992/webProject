package main.service;

import main.api.response.ResultResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.UUID;

@Service
public class RestoreService {

    @Autowired
    private UserRepository repository;

    public ResultResponse getResponse (String email){

        User user = repository.findByEmail(email);
        ResultResponse response = new ResultResponse();

        if (user != null){
            response.setResult(true);
            String code = UUID.randomUUID().toString().replace("-", "");
            sendEmail(email, "", "/login/change-password/" + code);

            user.setCode(code);
        }

        return response;
    }

    private void sendEmail(String to, String subject, String message) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.mail.ru");
        mailSender.setPort(25);
        mailSender.setUsername("jaguar1189@mail.ru");
        mailSender.setPassword("jaguar608");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("jaguar1189@mail.ru");
        mailMessage.setTo(to);

        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
