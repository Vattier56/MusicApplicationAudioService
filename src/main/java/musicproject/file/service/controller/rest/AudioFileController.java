package musicproject.file.service.controller.rest;

import musicproject.file.service.exception.FileProcessingException;
import musicproject.file.service.exception.MissingFileParameterException;
import musicproject.file.service.model.FileMetaResponse;
import musicproject.file.service.service.FileService;
import musicproject.file.service.service.MetadataAudioFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class AudioFileController {

    private final MetadataAudioFileService metaFileService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public AudioFileController(MetadataAudioFileService metaFileService) {
        this.metaFileService = metaFileService;
    }

    @PostMapping("/file-service")
    public ResponseEntity retrieveFileMetaInfo(File file) {

        /*NOTE isFile - returns true if file exists and is a regular file, note that if file doesn’t exist then it returns false. */
        if(!file.isFile())
            throw new MissingFileParameterException("File parameter not found");

        try {
            FileMetaResponse metaResponse = metaFileService.getSongInfo(file);

            logger.info("{}", metaResponse);
            return ResponseEntity.ok(metaResponse);
        } catch (Exception e) {
            throw new FileProcessingException("Cannot read metadata");
        }
    }

    @PostMapping("/file-service/multipart")
    public ResponseEntity retrieveMultipartFileMetaInfo(MultipartFile multipartFile) {

        /*NOTE isFile - returns true if file exists and is a regular file, note that if file doesn’t exist then it returns false. */
        if (multipartFile.isEmpty())
            throw new MissingFileParameterException("File parameter not found");

        try {
            FileMetaResponse metaResponse = metaFileService.getSongInfo(FileService.convert(multipartFile));

            logger.info("{}", metaResponse);
            return ResponseEntity.ok(metaResponse);
        } catch (Exception e) {
            throw new FileProcessingException("Cannot read metadata");
        }
    }
}
