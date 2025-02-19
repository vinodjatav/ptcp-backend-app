package com.vinod.ptcp_app.service;

import com.vinod.ptcp_app.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    public void sendReminder(Event event) {
        System.out.println("Reminder: " + event.getTitle() + " on " + event.getDate());
        // TODO: Implement email/SMS notification logic
    }
}
