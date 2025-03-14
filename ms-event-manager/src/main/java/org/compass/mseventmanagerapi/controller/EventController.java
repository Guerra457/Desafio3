package org.compass.mseventmanagerapi.controller;

import lombok.RequiredArgsConstructor;
import org.compass.mseventmanagerapi.entity.Event;
import org.compass.mseventmanagerapi.exception.EventHasTicketsException;
import org.compass.mseventmanagerapi.service.EventService;
import org.compass.mseventmanagerapi.web.dto.EventRequestDto;
import org.compass.mseventmanagerapi.web.dto.EventResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto eventRequest) {
        EventResponseDto createdEvent = eventService.createEvent(eventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @GetMapping("/get-event/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable String id) {
        EventResponseDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        List<EventResponseDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/get-all-events/sorted")
    public ResponseEntity<List<EventResponseDto>> getAllEventsSorted() {
        List<EventResponseDto> events = eventService.getAllEventsSorted();
        return ResponseEntity.ok(events);
    }

    @PutMapping("/update-event/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable String id, @RequestBody EventRequestDto eventRequest) {
        EventResponseDto updatedEvent = eventService.updateEvent(id, eventRequest);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable String id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (EventHasTicketsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\"error\": \"O evento não pode ser deletado porque possui ingressos vendidos.\"}");
        }
    }
}
