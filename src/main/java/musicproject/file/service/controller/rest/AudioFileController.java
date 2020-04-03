package musicproject.file.service.controller.rest;

import musicproject.file.service.exception.FileNotFoundException;
import musicproject.file.service.exception.FileProcessingException;
import musicproject.file.service.exception.MissingFileParameterException;
import musicproject.file.service.model.FileMetaResponse;
import musicproject.file.service.service.FileService;
import musicproject.file.service.service.MetadataAudioFileService;
import musicproject.file.service.service.UploadS3Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class AudioFileController {

    private final MetadataAudioFileService metaFileService;
    private final UploadS3Service uploadService;

    public AudioFileController(MetadataAudioFileService metaFileService, UploadS3Service uploadService) {
        this.metaFileService = metaFileService;
        this.uploadService = uploadService;
    }

    @PostMapping("/file-service")
    public FileMetaResponse retrieveFileMetaInfo(File file) {

        /*NOTE isFile - returns true if file exists and is a regular file, note that if file doesn’t exist then it returns false. */
        if(!file.isFile())
            throw new MissingFileParameterException("File parameter not found");

        FileService.save(file);

        return metaFileService.getSongInfo(file);
    }

    @PostMapping("/file-service/multipart")
    public FileMetaResponse retrieveMultipartFileMetaInfo(MultipartFile multipartFile) {
        File file = FileService.convert(multipartFile);

        /*NOTE isFile - returns true if file exists and is a regular file, note that if file doesn’t exist then it returns false. */
        if(!file.isFile())
            throw new MissingFileParameterException("File parameter not found");

        FileService.save(file);

        return metaFileService.getSongInfo(file);
    }

    @PostMapping("/upload-service")
    public String uploadFile(String fileName, String originalFileName, int userHash) {
        String path = "";

        try {
            File file = FileService.findFileByPath(originalFileName);

            if(!file.exists())
                throw new FileNotFoundException("File not found");

            path = uploadService.uploadAudioFile(fileName, file, userHash);

        } catch (InterruptedException e) {e.printStackTrace();
        e.printStackTrace();
            throw new FileProcessingException("Unable to save file");
        }

        return path;
    }
}
