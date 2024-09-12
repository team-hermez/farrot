package com.hermez.farrot.image.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FirebaseService {

  private final Bucket bucket;

  public void uploadImage(MultipartFile file,String saveFileName) throws IOException {
    Blob blob = bucket.create(saveFileName, file.getInputStream(), file.getContentType());
    blob.getMediaLink();
  }

  public void uploadMultipleImages(MultipartFile[] files, String saveFileName) throws IOException {
    for (MultipartFile file : files) {
      uploadImage(file,saveFileName);
    }
  }

  public String getPublicImageUrl(String saveFileName) {
    String bucketName = bucket.getName();
    String encodedFileName = URLEncoder.encode(saveFileName, StandardCharsets.UTF_8);
    return "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" + encodedFileName + "?alt=media";
  }

}
