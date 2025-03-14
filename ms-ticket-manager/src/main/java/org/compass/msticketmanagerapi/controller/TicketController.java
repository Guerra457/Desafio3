package org.compass.msticketmanagerapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.compass.msticketmanagerapi.service.TicketService;
import org.compass.msticketmanagerapi.web.dto.CheckTicketsResponse;
import org.compass.msticketmanagerapi.web.dto.TicketRequestDto;
import org.compass.msticketmanagerapi.web.dto.TicketResponseDto;
import org.compass.msticketmanagerapi.web.dto.TicketUpdateRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/create-ticket")
    public ResponseEntity<TicketResponseDto> createTicket(@Valid @RequestBody TicketRequestDto ticketRequest) {
        TicketResponseDto createdTicket = ticketService.createTicket(ticketRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<TicketResponseDto> getTicketById(@PathVariable String id) {
        TicketResponseDto ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/update-ticket/{id}")
    public ResponseEntity<TicketResponseDto> updateTicket(@Valid @PathVariable String id, @RequestBody TicketUpdateRequestDto ticketRequest) {
        TicketResponseDto updatedTicket = ticketService.updateTicket(id, ticketRequest);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/cancel-ticket/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable String id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-tickets-by-event/{eventId}")
    public ResponseEntity<CheckTicketsResponse> checkTicketsByEvent(@PathVariable String eventId) {
        boolean hasTickets = ticketService.hasTicketsForEvent(eventId);
        return ResponseEntity.ok(new CheckTicketsResponse(eventId, hasTickets));
    }
}