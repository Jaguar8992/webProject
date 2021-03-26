package main.service;

import main.api.response.PostMethodResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class ImageService {

    public ResponseEntity getResponse (MultipartFile image, HttpServletRequest request) throws IOException {

        final long maxFileSize = 1048576;
        PostMethodResponse response;
        TreeMap<String, String> errors = new TreeMap<>();

        int index = image.getOriginalFilename().lastIndexOf(".");
        String format = image.getOriginalFilename().substring(index + 1);

        if (!format.equals("jpg") && !format.equals("png")) {
            errors.put("image", "Неверный формат файла");
        }
        if (image.getSize() >= maxFileSize) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }


        if (errors.size() == 0) {

            String code = UUID.randomUUID().toString().replace("-", "");
            String path = "upload" + code.substring(0, 2) + "/"
                    + code.substring(3, 5) + "/" + code.substring(6, 8) + "/";
            String file = code.substring(9) + "." + format;
            String realPath = request.getServletContext().getRealPath(path);

            if (!new File(realPath).exists()){
                new File(realPath).mkdirs();
            }

            OutputStream os = new FileOutputStream(realPath + "/" + file);

            os.write(image.getBytes());
            os.flush();
            os.close();

            return ResponseEntity.ok().body(path + file);

        } else {
            response = new PostMethodResponse();
            response.setResult(false);
            response.setErrors(errors);

            return ResponseEntity.status(400).body(response);
        }
    }
}
