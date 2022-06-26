package com.alkemy.ong.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface AWSS3Service {

    String uploadFile(MultipartFile file);

    List<String> getAllObjectsFromS3();

    InputStream downloadFile(String key);

    String getUrl(String key);

}

