package org.compass.msticketmanagerapi.service;

import lombok.RequiredArgsConstructor;
import org.compass.msticketmanagerapi.client.EventClient;
import org.compass.msticketmanagerapi.entity.Ticket;
import org.compass.msticketmanagerapi.exception.EventNotFoundException;
import org.compass.msticketmanagerapi.exception.TicketNotFoundException;
import org.compass.msticketmanagerapi.repository.TicketRepository;
import org.compass.msticketmanagerapi.web.dto.EventDetailsDto;
import org.compass.msticketmanagerapi.web.dto.TicketRequestDto;
import org.compass.msticketmanagerapi.web.dto.TicketResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventClient eventClient;

    public TicketResponseDto createTicket(TicketRequestDto ticketRequest) {
        EventDetailsDto eventDetails = eventClient.getEventDetails(ticketRequest.getEventId());
        if (eventDetails == null) {
            throw new EventNotFoundException("Evento não encontrado!");
        }

        Ticket ticket = Ticket.builder()
                .customerName(ticketRequest.getCustomerName())
                .cpf(ticketRequest.getCpf())
                .customerMail(ticketRequest.getCustomerMail())
                .eventId(ticketRequest.getEventId())
                .brlAmount(ticketRequest.getBrlAmount())
                .usdAmount(ticketRequest.getUsdAmount())
                .status("concluído")
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        return mapToTicketResponseDto(savedTicket, eventDetails);
    }

    public TicketResponseDto getTicketById(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ingresso não encontrado!"));

        EventDetailsDto eventDetails = eventClient.getEventDetails(ticket.getEventId());
        return mapToTicketResponseDto(ticket, eventDetails);
    }

    public TicketResponseDto updateTicket(String id, TicketRequestDto ticketRequest) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ingresso não encontrado!"));

        ticket.setCustomerName(ticketRequest.getCustomerName());
        ticket.setCpf(ticketRequest.getCpf());
        ticket.setCustomerMail(ticketRequest.getCustomerMail());
        ticket.setBrlAmount(ticketRequest.getBrlAmount());
        ticket.setUsdAmount(ticketRequest.getUsdAmount());

        Ticket updatedTicket = ticketRepository.save(ticket);

        EventDetailsDto eventDetails = eventClient.getEventDetails(ticket.getEventId());

        return mapToTicketResponseDto(updatedTicket, eventDetails);
    }

    public void cancelTicket(String id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ingresso não encontrado!"));
        ticket.setStatus("cancelado");
        ticketRepository.save(ticket);
    }

    public boolean hasTicketsForEvent(String eventId) {
        return ticketRepository.existsByEventId(eventId);
    }

    private TicketResponseDto mapToTicketResponseDto(Ticket ticket, EventDetailsDto eventDetails) {
        return TicketResponseDto.builder()
                .ticketId(ticket.getId())
                .cpf(ticket.getCpf())
                .customerName(ticket.getCustomerName())
                .customerMail(ticket.getCustomerMail())
                .event(eventDetails)
                .brlTotalAmount(ticket.getBrlAmount())
                .usdTotalAmount(ticket.getUsdAmount())
                .status(ticket.getStatus())
                .build();
    }
}