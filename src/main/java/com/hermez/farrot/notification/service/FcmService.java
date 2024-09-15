package com.hermez.farrot.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.hermez.farrot.notification.dto.NotificationRequest;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FcmService {

  public void send(NotificationRequest notificationRequest)
      throws ExecutionException, InterruptedException {
    Message message = Message.builder()
        .setToken(notificationRequest.token())
        .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
            .setNotification(new WebpushNotification(notificationRequest.title(),
                notificationRequest.message())).build()
        ).build();
    String response = FirebaseMessaging.getInstance().sendAsync(message).get();
    log.info("Sent message ==== {}",response);
  }

}
