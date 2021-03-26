package main.service;

import main.api.response.PostMethodResponse;
import main.model.User;
import main.model.repositories.UserRepository;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class MyProfileService {

    @Autowired
    private UserRepository userRepository;
    private final long maxPhotoSize = 5242880;
    private final int photoSize = 36;

    public PostMethodResponse getResponse(String email, String name, String password,
                                          Integer removePhoto, MultipartFile photo,
                                          User user, HttpServletRequest request) throws IOException {

        PostMethodResponse response = new PostMethodResponse();
        TreeMap<String, String> errors = new TreeMap<>();
        Pattern namePattern = Pattern.compile("^[а-яА-Я ]*$");

        if (email != null && !user.getEmail().equals(email)
                && userRepository.findByEmail(email) != null) {
            errors.put("email", "Этот e-mail уже зарегистрирован");
        }
        if (photo != null && photo.getSize() >= maxPhotoSize) {
            errors.put("photo", "Фото слишком большое, нужно не более 5 Мб");
        }
        if (name != null && !user.getName().equals(name) && userRepository.findByName(name) != null) {
            errors.put("name", "Это имя уже занято");
        }
        if (name != null && !namePattern.matcher(name).find()) {
            errors.put("name", "Имя указано неверно");
        }
        if (password != null && password.length() < 6) {
            errors.put("password", "Пароль короче 6-ти символов");
        }

        if (errors.size() == 0) {

            response.setResult(true);

            if (email != null) {
                user.setEmail(email);
            }
            if (name != null) {
                user.setName(name);
            }
            if (password != null) {
                user.setPassword(new BCryptPasswordEncoder(12).encode(password));
            }

            if (removePhoto != null) {
                if (removePhoto == 1) {
                    user.setPhoto(null);
                }
                    else if (removePhoto == 0 && photo != null) {
                        String linkPhoto = downloadImage(photo, request);
                        user.setPhoto(linkPhoto);
                }
            }

            userRepository.save(user);

        } else {
            response.setErrors(errors);
        }

        return response;
    }

    private String downloadImage(MultipartFile image, HttpServletRequest request) throws IOException {

        int index = image.getOriginalFilename().lastIndexOf(".");
        String format = image.getOriginalFilename().substring(index + 1);

        String code = UUID.randomUUID().toString().replace("-", "");
        String path = "upload" + code.substring(0, 2) + "/"
                + code.substring(3, 5) + "/" + code.substring(6, 8) + "/";
        String file = code.substring(9) + "." + format;
        String realPath = request.getServletContext().getRealPath(path);

        if (!new File(realPath).exists()) {
            new File(realPath).mkdirs();
        }

        BufferedImage photo = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
        BufferedImage resizeImage = Scalr.resize(photo, Scalr.Method.BALANCED,
                Scalr.Mode.AUTOMATIC, photoSize, photoSize);

        ImageIO.write(resizeImage, format, new File(realPath + "/" + file));

        return path + file;
    }
}
