package main.service;

import main.api.response.PostMethodResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class ImageService {

    public Object getResponse (MultipartFile image) throws IOException {

        final long maxFileSize = 5242880;
        String code = UUID.randomUUID().toString().replace("-", "");
        PostMethodResponse response;
        TreeMap<String, String> errors = new TreeMap<>();

        int index = image.getOriginalFilename().lastIndexOf(".");
        String format = image.getOriginalFilename().substring(index + 1);

        if (!format.equals("jpg") && !format.equals("png")) {
            errors.put("image", "Неверный формат файла");
        }

        if (image.getSize() > maxFileSize) {
            errors.put("image", "Размер файла превышает допустимый размер");
        }

        if (errors.size() == 0) {

            File upload = new File("upload/");
            File firstPackage = new File(upload.getPath()  + "/" + code.substring(0, 2) + "/");
            File secondPackage = new File(firstPackage.getPath() + "/"
                    + code.substring(3, 5) + "/");
            File thirdPackage = new File(secondPackage.getPath() + "/"
                    + code.substring(6, 8) + "/");
            String file = thirdPackage + "/" + code.substring(9) + "." + format;

            if (!upload.exists()) {
                upload.mkdir();
            }

            if (!firstPackage.exists()) {
                firstPackage.mkdir();
            }

            if (!secondPackage.exists()) {
                secondPackage.mkdir();
            }

            if (!thirdPackage.exists()) {
                thirdPackage.mkdir();
            }

            OutputStream os = new FileOutputStream(file);

            os.write(image.getBytes());
            os.flush();
            os.close();

            return file;

        } else {
            response = new PostMethodResponse();
            response.setResult(false);
            response.setErrors(errors);

            return ResponseEntity.status(400).body(response);
        }
    }
}
