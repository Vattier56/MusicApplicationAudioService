package musicproject.file.service.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.Random;
import java.util.concurrent.Executors;

public class UploadS3Service {

    private static final String SONG_PATH = "music/";

    private final AmazonS3 s3client;
    private Random rand = new Random();

    @Value("${bucket}")
    private String bucketName;

    public UploadS3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public String uploadAudioFile(String []properties, File file, int userHash) throws InterruptedException {
        String fileName = MetadataAudioFileService.saveAudioFileProperties(properties, file);

        String filePath = SONG_PATH + userHash + "/" + fileName;

        PutObjectRequest request = new PutObjectRequest(bucketName, filePath, file).withCannedAcl(CannedAccessControlList.PublicRead);
        TransferManager transferManager = TransferManagerBuilder
                .standard()
                .withS3Client(s3client)
                .withMultipartUploadThreshold((long) (5 * 1024 * 1024))
                .withExecutorFactory(() -> Executors.newFixedThreadPool(5))
                .build();

        Upload upload = transferManager.upload(request);

        upload.waitForCompletion();
        FileService.cleanup(file);
        return transferManager.getAmazonS3Client().getUrl(bucketName, filePath).toString();
    }
}
