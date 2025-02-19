package com.vinod.ptcp_app.config;

import com.vinod.ptcp_app.scheduler.EventReminderJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail eventReminderJobDetail() {
        return JobBuilder.newJob(EventReminderJob.class).withIdentity("eventReminderJob").storeDurably().build();
    }

    @Bean
    public Trigger eventReminderTrigger() {
        return TriggerBuilder.newTrigger().forJob(eventReminderJobDetail()).withIdentity("eventReminderTrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24) // Runs once a day
                .repeatForever()).build();
    }
}
