package com.hermez.farrot.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FirebaseConfig {

  @Value("${firebase.store.bucketpath}")
  private String bucketpath;

  @Value("${firebase.store.bucketname}")
  private String bucketname;

  @PostConstruct
  public FirebaseApp firebaseApp() throws IOException {
    log.info("Firebase config loaded");
    if (FirebaseApp.getApps().isEmpty()) {

      FileInputStream serviceAccount = new FileInputStream(bucketpath);

      FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .setStorageBucket(bucketname)
          .build();

      return FirebaseApp.initializeApp(options);
    }
    return FirebaseApp.getInstance();
  }

  @Bean
  public FirebaseAuth firebaseAuth() throws IOException {
    return FirebaseAuth.getInstance(firebaseApp());
  }

  @Bean
  public Bucket bucket() throws IOException {
    return StorageClient.getInstance(firebaseApp()).bucket();
  }
}
