package com.vinod.ptcp_app.controller;

import com.vinod.ptcp_app.entity.Event;
import com.vinod.ptcp_app.entity.User;
import com.vinod.ptcp_app.repository.EventRepository;
import com.vinod.ptcp_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    // Create Event
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        System.out.println("Event: " + event);
        Optional<User> organizer = userRepository.findById(event.getOrganizer().getId());
        if (organizer.isEmpty()) {
            return ResponseEntity.badRequest().body("Organizer not found!");
        }
        event.setOrganizer(organizer.get());
        return ResponseEntity.ok(eventRepository.save(event));
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        return eventRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get events by date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Event>> getEventsByDate(@PathVariable String date) {
        return ResponseEntity.ok(eventRepository.findByDate(LocalDate.parse(date)));
    }

    // Get events by organizer
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<Event>> getEventsByOrganizer(@PathVariable Long organizerId) {
        return ResponseEntity.ok(eventRepository.findByOrganizerId(organizerId));
    }

    // Update an event
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventRepository.findById(id).map(event -> {
            event.setTitle(eventDetails.getTitle());
            event.setDescription(eventDetails.getDescription());
            event.setDate(eventDetails.getDate());
            event.setOrganizer(eventDetails.getOrganizer());
            return ResponseEntity.ok(eventRepository.save(event));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete an event
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        return eventRepository.findById(id).map(event -> {
            eventRepository.delete(event);
            return ResponseEntity.ok("Event deleted successfully");
        }).orElse(ResponseEntity.notFound().build());
    }
}
