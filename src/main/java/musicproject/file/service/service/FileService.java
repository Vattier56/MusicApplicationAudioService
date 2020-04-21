package musicproject.file.service.service;

import musicproject.file.service.exception.FileProcessingException;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;


public class FileService {

    public static File convert(MultipartFile file) {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(convFile);
            fileOutputStream.write(file.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            throw new FileProcessingException("File conversion error !");
        }
        return convFile;
    }
}
