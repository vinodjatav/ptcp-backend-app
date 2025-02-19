package com.vinod.ptcp_app.repository;

import com.vinod.ptcp_app.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDate(LocalDate date);

    List<Event> findByOrganizerId(Long organizerId);
}
