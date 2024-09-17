package com.hermez.farrot.image.controller;

import com.hermez.farrot.image.service.FirebaseService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ImageController {

  private final FirebaseService firebaseService;

  @PostMapping("/upload")
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
    log.info("Uploading image to Firebase: {} ", file.getOriginalFilename());
    String saveFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    firebaseService.uploadImage(file, saveFileName);
    return ResponseEntity.ok(firebaseService.getPublicImageUrl(saveFileName));
  }

}
