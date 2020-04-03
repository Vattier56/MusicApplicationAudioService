package musicproject.file.service.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.concurrent.Executors;

@Service
public class UploadS3Service {

    /*DEFAULT MUSIC PATH DIRECTORY IN S3 */
    private static final String MUSIC_FILES_PATH = "music/";

    private final AmazonS3 s3client;

    /*S3 BUCKET NAME*/
    @Value("${bucket}")
    private String bucketName;

    public UploadS3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public String uploadAudioFile(String title, File file, int userHash) throws InterruptedException {
        MetadataAudioFileService.updateAudioFileTitle(title, file);
        String[] fileEnd = file.getName().split("\\.");

        String filePath = MUSIC_FILES_PATH + userHash + "/" + title + "." + fileEnd[fileEnd.length-1] ;

        if(fileExists(filePath)) addFileVersions(filePath);

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


    private boolean fileExists(String filePath) {
        try {
            s3client.getObjectMetadata(bucketName, filePath);
        } catch(AmazonServiceException e) {e.printStackTrace();
            return false;
        }
        return true;
    }

    public String addFileVersions(String fileName) {
        try {
            if(fileName.contains("(")) {
                String number = fileName.substring(fileName.indexOf("(") + 1, fileName.indexOf(")"));

                int versionNumber = Integer.parseInt(number);
                versionNumber++;

                fileName = fileName.replaceAll(number, String.valueOf(versionNumber));
            } else {
                String []newPath = fileName.split("\\.");
                newPath[0] += "(1).";

                fileName = newPath[0] + newPath[1];

            }
        } catch (Exception e) {
            e.printStackTrace();
            String []parts = fileName.split("\\.");

            parts[parts.length - 2] += "(1)";

            for (int i = 0; i < parts.length - 1; i++) {
                parts[i] += ".";
            }
            return String.join("", parts);
        }
        return fileName;
    }
}
