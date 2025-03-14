package org.compass.msticketmanagerapi;

import org.compass.msticketmanagerapi.client.EventClient;
import org.compass.msticketmanagerapi.entity.Ticket;
import org.compass.msticketmanagerapi.exception.EventNotFoundException;
import org.compass.msticketmanagerapi.exception.TicketNotFoundException;
import org.compass.msticketmanagerapi.repository.TicketRepository;
import org.compass.msticketmanagerapi.service.TicketService;
import org.compass.msticketmanagerapi.web.dto.EventDetailsDto;
import org.compass.msticketmanagerapi.web.dto.TicketRequestDto;
import org.compass.msticketmanagerapi.web.dto.TicketResponseDto;
import org.compass.msticketmanagerapi.web.dto.TicketUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventClient eventClient;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private TicketRequestDto ticketRequestDto;
    private TicketUpdateRequestDto ticketUpdateRequestDto;
    private EventDetailsDto eventDetailsDto;

    @BeforeEach
    void setUp() {
        ticket = Ticket.builder()
                .id("ticket-1")
                .customerName("Pedro Alencar")
                .cpf("12345678901")
                .customerMail("pedro@example.com")
                .eventId("event-1")
                .brlAmount("100.00")
                .usdAmount("20.00")
                .status("concluído")
                .build();

        ticketRequestDto = new TicketRequestDto();
        ticketRequestDto.setCustomerName("Pedro Alencar");
        ticketRequestDto.setCpf("12345678901");
        ticketRequestDto.setCustomerMail("pedro@example.com");
        ticketRequestDto.setEventId("event-1");
        ticketRequestDto.setBrlAmount("100.00");
        ticketRequestDto.setUsdAmount("20.00");

        ticketUpdateRequestDto = new TicketUpdateRequestDto();
        ticketUpdateRequestDto.setCustomerName("Pedro Alencar");
        ticketUpdateRequestDto.setCpf("98765432100");
        ticketUpdateRequestDto.setCustomerMail("pedro@example.com");
        ticketUpdateRequestDto.setBrlAmount("150.00");
        ticketUpdateRequestDto.setUsdAmount("30.00");

        eventDetailsDto = EventDetailsDto.builder().build();
        eventDetailsDto.setEventName("Test Event");
    }

    @Test
    public void testCreateTicket_Success() {
        when(eventClient.getEventDetails(ticketRequestDto.getEventId())).thenReturn(eventDetailsDto);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketResponseDto response = ticketService.createTicket(ticketRequestDto);

        assertNotNull(response);
        assertEquals(ticket.getId(), response.getTicketId());
        assertEquals(ticket.getCustomerName(), response.getCustomerName());
        assertEquals(ticket.getCpf(), response.getCpf());
        assertEquals(ticket.getCustomerMail(), response.getCustomerMail());
        assertEquals(ticket.getBrlAmount(), response.getBrlTotalAmount());
        assertEquals(ticket.getUsdAmount(), response.getUsdTotalAmount());
        assertEquals(ticket.getStatus(), response.getStatus());
        assertEquals(eventDetailsDto, response.getEvent());

        verify(eventClient, times(1)).getEventDetails(ticketRequestDto.getEventId());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    public void testCreateTicket_EventNotFound() {
        when(eventClient.getEventDetails(ticketRequestDto.getEventId())).thenReturn(null);

        assertThrows(EventNotFoundException.class, () -> ticketService.createTicket(ticketRequestDto));

        verify(eventClient, times(1)).getEventDetails(ticketRequestDto.getEventId());
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void testGetTicketById_Success() {
        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.of(ticket));
        when(eventClient.getEventDetails(ticket.getEventId())).thenReturn(eventDetailsDto);

        TicketResponseDto response = ticketService.getTicketById("ticket-1");

        assertNotNull(response);
        assertEquals(ticket.getId(), response.getTicketId());
        assertEquals(ticket.getCustomerName(), response.getCustomerName());
        assertEquals(ticket.getCpf(), response.getCpf());
        assertEquals(ticket.getCustomerMail(), response.getCustomerMail());
        assertEquals(ticket.getBrlAmount(), response.getBrlTotalAmount());
        assertEquals(ticket.getUsdAmount(), response.getUsdTotalAmount());
        assertEquals(ticket.getStatus(), response.getStatus());
        assertEquals(eventDetailsDto, response.getEvent());

        verify(ticketRepository, times(1)).findById("ticket-1");
        verify(eventClient, times(1)).getEventDetails(ticket.getEventId());
    }

    @Test
    public void testGetTicketById_NotFound() {
        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById("ticket-1"));

        verify(ticketRepository, times(1)).findById("ticket-1");
        verify(eventClient, never()).getEventDetails(anyString());
    }

    @Test
    public void testUpdateTicket_Success() {
        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.of(ticket));

        Ticket updatedTicket = Ticket.builder()
                .id("ticket-1")
                .customerName(ticketUpdateRequestDto.getCustomerName())
                .cpf(ticketUpdateRequestDto.getCpf())
                .customerMail(ticketUpdateRequestDto.getCustomerMail())
                .eventId(ticket.getEventId())
                .brlAmount(ticketUpdateRequestDto.getBrlAmount())
                .usdAmount(ticketUpdateRequestDto.getUsdAmount())
                .status(ticket.getStatus())
                .build();

        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);
        when(eventClient.getEventDetails(ticket.getEventId())).thenReturn(eventDetailsDto);

        TicketResponseDto response = ticketService.updateTicket("ticket-1", ticketUpdateRequestDto);

        assertNotNull(response);
        assertEquals(updatedTicket.getId(), response.getTicketId());
        assertEquals(ticketUpdateRequestDto.getCustomerName(), response.getCustomerName());
        assertEquals(ticketUpdateRequestDto.getCpf(), response.getCpf());
        assertEquals(ticketUpdateRequestDto.getCustomerMail(), response.getCustomerMail());
        assertEquals(ticketUpdateRequestDto.getBrlAmount(), response.getBrlTotalAmount());
        assertEquals(ticketUpdateRequestDto.getUsdAmount(), response.getUsdTotalAmount());
        assertEquals(ticket.getStatus(), response.getStatus());
        assertEquals(eventDetailsDto, response.getEvent());

        verify(ticketRepository, times(1)).findById("ticket-1");
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(eventClient, times(1)).getEventDetails(ticket.getEventId());
    }

    @Test
    public void testUpdateTicket_NotFound() {
        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.updateTicket("ticket-1", ticketUpdateRequestDto));

        verify(ticketRepository, times(1)).findById("ticket-1");
        verify(ticketRepository, never()).save(any(Ticket.class));
        verify(eventClient, never()).getEventDetails(anyString());
    }

    @Test
    public void testCancelTicket_Success() {
        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        ticketService.cancelTicket("ticket-1");

        assertEquals("cancelado", ticket.getStatus());

        verify(ticketRepository, times(1)).findById("ticket-1");
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testCancelTicket_NotFound() {
        when(ticketRepository.findById("ticket-1")).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.cancelTicket("ticket-1"));

        verify(ticketRepository, times(1)).findById("ticket-1");
        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void testHasTicketsForEvent() {
        String eventId = "event-1";
        when(ticketRepository.existsByEventId(eventId)).thenReturn(true);

        boolean result = ticketService.hasTicketsForEvent(eventId);

        assertTrue(result);
        verify(ticketRepository, times(1)).existsByEventId(eventId);
    }
}
