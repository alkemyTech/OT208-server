package com.alkemy.ong.services;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AWSS3Service {

    String uploadFile(MultipartFile file);

    List<String> getAllObjectsFromS3();

    InputStream downloadFile(String key);

    String getUrl(String key);

}

