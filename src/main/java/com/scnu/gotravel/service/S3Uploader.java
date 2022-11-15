package com.scnu.gotravel.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String dirName) throws IOException {

        List<String> resultUrlList = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles){

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(MediaType.IMAGE_JPEG_VALUE);

            String fileName = dirName + "/" + UUID.randomUUID() + multipartFile.getName();   // S3에 저장된 파일 이름
            String uploadImageUrl = putS3(multipartFile.getInputStream(), fileName, objectMetadata); // s3로 업로드
            log.info("url : " + uploadImageUrl);

            resultUrlList.add(uploadImageUrl);

        }
        return resultUrlList;
    }

    // S3로 업로드
    private String putS3(InputStream file, String fileName, ObjectMetadata objectMetadata) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file,objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // S3에서 삭제
    public void deleteS3(String fileName) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
        amazonS3Client.deleteObject(request);
    }

}