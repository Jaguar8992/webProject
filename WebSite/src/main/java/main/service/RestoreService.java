package main.service;

import main.api.response.ResultResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

@Service
public class RestoreService{

    private String username = "webproject8992@gmail.com";
    private String password = "jaguar60893";
    @Autowired
    private UserRepository repository;

    public ResultResponse getResponse(String email, String address) throws MessagingException {

        User user = repository.findByEmail(email);
        ResultResponse response = new ResultResponse();

        if (user != null) {
            response.setResult(true);
            String code = UUID.randomUUID().toString().replace("-", "");

            sendMessage(email, address + "/login/change-password/" + code);

            user.setCode(code);
            repository.save(user);
        }

        return response;
    }

    private void sendMessage(String email, String text)  {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setText(text);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    }

