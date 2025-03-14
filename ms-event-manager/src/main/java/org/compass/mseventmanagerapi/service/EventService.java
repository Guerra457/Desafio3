package org.compass.mseventmanagerapi.service;

import lombok.RequiredArgsConstructor;
import org.compass.mseventmanagerapi.client.TicketClient;
import org.compass.mseventmanagerapi.entity.Event;
import org.compass.mseventmanagerapi.exception.EventHasTicketsException;
import org.compass.mseventmanagerapi.exception.EventNotFoundException;
import org.compass.mseventmanagerapi.repository.EventRepository;
import org.compass.mseventmanagerapi.web.dto.AddressResponseDto;
import org.compass.mseventmanagerapi.web.dto.CheckTicketsResponse;
import org.compass.mseventmanagerapi.web.dto.EventRequestDto;
import org.compass.mseventmanagerapi.web.dto.EventResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AddressService addressService;
    private final TicketClient ticketClient;

    public EventResponseDto createEvent(EventRequestDto eventRequest) {
        AddressResponseDto address = addressService.getAddress(eventRequest.getCep());

        Event event = Event.builder()
                .eventName(eventRequest.getEventName())
                .dateTime(eventRequest.getDateTime())
                .cep(eventRequest.getCep())
                .logradouro(address.getLogradouro())
                .bairro(address.getBairro())
                .cidade(address.getLocalidade())
                .uf(address.getUf())
                .build();

        Event savedEvent = eventRepository.save(event);

        return mapToEventResponseDto(savedEvent);
    }

    public EventResponseDto getEventById(String id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado!"));
        return mapToEventResponseDto(event);
    }

    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapToEventResponseDto)
                .collect(Collectors.toList());
    }

    public List<EventResponseDto> getAllEventsSorted() {
        return eventRepository.findAllByOrderByEventNameAsc().stream()
                .map(this::mapToEventResponseDto)
                .collect(Collectors.toList());
    }

    public EventResponseDto updateEvent(String id, EventRequestDto updatedEvent) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado!"));

        event.setEventName(updatedEvent.getEventName());
        event.setDateTime(updatedEvent.getDateTime());
        event.setCep(updatedEvent.getCep());

        AddressResponseDto address = addressService.getAddress(updatedEvent.getCep());
        event.setLogradouro(address.getLogradouro());
        event.setBairro(address.getBairro());
        event.setCidade(address.getLocalidade());
        event.setUf(address.getUf());

        Event savedEvent = eventRepository.save(event);

        return mapToEventResponseDto(savedEvent);
    }

    public void deleteEvent(String id) {
        CheckTicketsResponse response = ticketClient.checkTicketsByEvent(id);

        if (response.isHasTickets()) {
            throw new EventHasTicketsException("Evento possui ingressos vendidos");
        }

        eventRepository.deleteById(id);
    }

    private EventResponseDto mapToEventResponseDto(Event event) {
        return EventResponseDto.builder()
                .id(event.getId())
                .eventName(event.getEventName())
                .dateTime(event.getDateTime())
                .cep(event.getCep())
                .logradouro(event.getLogradouro())
                .bairro(event.getBairro())
                .cidade(event.getCidade())
                .uf(event.getUf())
                .build();
    }
}
