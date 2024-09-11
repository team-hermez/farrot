package com.hermez.farrot.notification.controller;

import com.hermez.farrot.notification.service.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/notification")
public class NotificationController {
    private NotificationService notificationService;

    @GetMapping("/list")
    public String getList(Model model) {
        return "notification/notification-list";
    }

//    @GetMapping(value = "/subscribe/{email}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter subscribe(@PathVariable String email, @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
//        return notificationService.subscribe(email, lastEventId);
//    }

}
