package com.alkemy.ong.services.impl;

import com.alkemy.ong.services.AWSS3Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AWSS3ServiceImpl implements AWSS3Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3ServiceImpl.class);

    private final AmazonS3 amazonS3;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    public String uploadFile(@NotNull MultipartFile mFile) {
        String extension = StringUtils.getFilenameExtension(mFile.getOriginalFilename());
        assert extension != null;
        if (!isImage(extension)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is not an image");
        }
        String key = String.format("%s.%s", UUID.randomUUID().toString().replace("-", ""), extension);
        try {
            File myFile = new File(Objects.requireNonNull(mFile.getOriginalFilename()));
            FileOutputStream fos = new FileOutputStream(myFile);
            fos.write(mFile.getBytes());
            fos.close();
            LOGGER.info("Uploading file to S3 bucket: {}", key);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, myFile);
            amazonS3.putObject(putObjectRequest);
            myFile.delete();
            return this.getUrl(key);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

    @Override
    public List<String> getAllObjectsFromS3() {
        ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);
        return result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    @Override
    public InputStream downloadFile(String key) {
        S3Object s3Object = amazonS3.getObject(bucketName, key);
        try {
            return s3Object.getObjectContent();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error downloading file from S3", e);
        }
    }

    @Override
    public String getUrl(String key) {
        return amazonS3.getUrl(bucketName, key).toString();
    }

    private boolean isImage(String extension) {
        String ext = extension.toLowerCase();
        return ext.equals("jpg")
                || ext.equals("png")
                || ext.equals("jpeg")
                || ext.equals("gif")
                || ext.equals("bmp")
                || ext.equals("tiff")
                || ext.equals("tif")
                || ext.equals("webp")
                || ext.equals("svg");
    }

}
