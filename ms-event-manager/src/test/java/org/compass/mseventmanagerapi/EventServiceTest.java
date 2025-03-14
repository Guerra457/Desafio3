package org.compass.mseventmanagerapi;

import org.compass.mseventmanagerapi.client.TicketClient;
import org.compass.mseventmanagerapi.entity.Event;
import org.compass.mseventmanagerapi.exception.EventHasTicketsException;
import org.compass.mseventmanagerapi.exception.EventNotFoundException;
import org.compass.mseventmanagerapi.repository.EventRepository;
import org.compass.mseventmanagerapi.service.AddressService;
import org.compass.mseventmanagerapi.service.EventService;
import org.compass.mseventmanagerapi.web.dto.AddressResponseDto;
import org.compass.mseventmanagerapi.web.dto.CheckTicketsResponse;
import org.compass.mseventmanagerapi.web.dto.EventRequestDto;
import org.compass.mseventmanagerapi.web.dto.EventResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private TicketClient ticketClient;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventRequestDto eventRequestDto;
    private AddressResponseDto addressResponseDto;

    @BeforeEach
    void setUp() {
        event = Event.builder()
                .id("1")
                .eventName("Test Event")
                .cep("01001-000")
                .dateTime(LocalDateTime.now().plusDays(1))
                .logradouro("Rua Teste")
                .bairro("Bairro Teste")
                .cidade("Cidade Teste")
                .uf("TS")
                .build();

        eventRequestDto = new EventRequestDto();
        eventRequestDto.setEventName("Updated Event");
        eventRequestDto.setCep("02002000");
        eventRequestDto.setDateTime(LocalDateTime.now().plusDays(5));

        addressResponseDto = AddressResponseDto.builder()
                .logradouro("Rua Atualizada")
                .bairro("Bairro Atualizado")
                .localidade("Cidade Atualizada")
                .uf("RJ")
                .build();
    }

    @Test
    public void testCreateEvent_Success() {
        when(addressService.getAddress(anyString())).thenReturn(addressResponseDto);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventRequestDto.setCep("02002000"); // Certifique-se de que o CEP corresponde ao da execução real

        EventResponseDto response = eventService.createEvent(eventRequestDto);

        assertNotNull(response);
        assertEquals("Test Event", response.getEventName());

        verify(addressService, times(1)).getAddress(anyString());
        verify(eventRepository, times(1)).save(any(Event.class));
    }


    @Test
    public void testGetEventById_Success() {
        when(eventRepository.findById("1")).thenReturn(Optional.of(event));

        EventResponseDto response = eventService.getEventById("1");

        assertNotNull(response);
        assertEquals("Test Event", response.getEventName());
    }

    @Test
    public void testGetEventById_NotFound() {
        when(eventRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.getEventById("999"));
    }

    @Test
    void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));

        List<EventResponseDto> events = eventService.getAllEvents();

        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        assertEquals("Test Event", events.get(0).getEventName());
    }

    @Test
    void testGetAllEventsSorted() {
        when(eventRepository.findAllByOrderByEventNameAsc()).thenReturn(Arrays.asList(event));

        List<EventResponseDto> events = eventService.getAllEventsSorted();

        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
        assertEquals("Test Event", events.get(0).getEventName());
    }

    @Test
    void testUpdateEvent_Success() {
        when(eventRepository.findById("1")).thenReturn(Optional.of(event));
        when(addressService.getAddress("02002000")).thenReturn(addressResponseDto);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventResponseDto updatedEvent = eventService.updateEvent("1", eventRequestDto);

        assertNotNull(updatedEvent);
        assertEquals("Updated Event", updatedEvent.getEventName());
        assertEquals("02002000", updatedEvent.getCep());
        assertEquals("Rua Atualizada", updatedEvent.getLogradouro());
        assertEquals("Bairro Atualizado", updatedEvent.getBairro());
        assertEquals("Cidade Atualizada", updatedEvent.getCidade());
        assertEquals("RJ", updatedEvent.getUf());
    }

    @Test
    void testUpdateEvent_NotFound() {
        when(eventRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.updateEvent("1", eventRequestDto));
    }

    @Test
    public void testDeleteEvent_Success() {
        CheckTicketsResponse checkResponse = new CheckTicketsResponse();
        checkResponse.setHasTickets(false);

        when(ticketClient.checkTicketsByEvent("1")).thenReturn(checkResponse);
        doNothing().when(eventRepository).deleteById("1");

        assertDoesNotThrow(() -> eventService.deleteEvent("1"));
        verify(eventRepository, times(1)).deleteById("1");
    }

    @Test
    public void testDeleteEvent_Failed_HasTickets() {
        CheckTicketsResponse checkResponse = new CheckTicketsResponse();
        checkResponse.setHasTickets(true);

        when(ticketClient.checkTicketsByEvent("1")).thenReturn(checkResponse);

        assertThrows(EventHasTicketsException.class, () -> eventService.deleteEvent("1"));
    }
}

