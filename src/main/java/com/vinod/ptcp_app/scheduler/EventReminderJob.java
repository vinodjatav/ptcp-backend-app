package com.vinod.ptcp_app.scheduler;

import com.vinod.ptcp_app.entity.Event;
import com.vinod.ptcp_app.repository.EventRepository;
import com.vinod.ptcp_app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventReminderJob implements Job {

    private final EventRepository eventRepository;
    private final NotificationService notificationService;

    @Override
    public void execute(JobExecutionContext context) {
        LocalDate today = LocalDate.now();
        List<Event> upcomingEvents = eventRepository.findByDate(today);

        for (Event event : upcomingEvents) {
            notificationService.sendReminder(event);
        }
    }
}
