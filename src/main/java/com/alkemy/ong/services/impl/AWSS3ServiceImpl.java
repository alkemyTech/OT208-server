package com.alkemy.ong.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import com.alkemy.ong.services.AWSS3Service;

@Service
@RequiredArgsConstructor
public class AWSS3ServiceImpl implements AWSS3Service {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3ServiceImpl.class);

    private AmazonS3 amazonS3;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Override
    public void uploadFile(@NotNull MultipartFile file) {
        File myFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(myFile)) {
            fos.write(file.getBytes());
            String newFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            LOGGER.info("Uploading file to S3 bucket: {}", newFileName);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, newFileName, myFile);
            amazonS3.putObject(putObjectRequest);
        }catch (IOException e){
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public List<String> getObjectsFromS3() {
        ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);
        return result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    @Override
    public InputStream downloadFile(String key) {
        S3Object s3Object = amazonS3.getObject(bucketName, key);
        return s3Object.getObjectContent();
    }
}
