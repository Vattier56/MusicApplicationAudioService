package musicproject.file.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MusicAudioFileServiceApplication {

    /*S3 ACCESS ID*/
    @Value("${access_id}")
    private String awsId;

    /*S3 SECRET KEY*/
    @Value("${access_key}")
    private String awsKey;

    /*S3 BUCKET REGION*/
    @Value("${region}")
    private String region;

    @Bean
    public AmazonS3 s3client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);

        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(MusicAudioFileServiceApplication.class, args);
    }

}
