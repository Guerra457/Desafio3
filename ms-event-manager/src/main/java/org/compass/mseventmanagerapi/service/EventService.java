package org.compass.mseventmanagerapi.service;

import lombok.RequiredArgsConstructor;
import org.compass.mseventmanagerapi.entity.Event;
import org.compass.mseventmanagerapi.exception.EventNotFoundException;
import org.compass.mseventmanagerapi.repository.EventRepository;
import org.compass.mseventmanagerapi.web.dto.AddressResponseDto;
import org.compass.mseventmanagerapi.web.dto.EventResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AddressService addressService;

    public EventResponseDto createEvent(Event event) {
        AddressResponseDto address = addressService.getAddress(event.getCep());

        event.setLogradouro(address.getLogradouro());
        event.setBairro(address.getBairro());
        event.setCidade(address.getLocalidade());
        event.setUf(address.getUf());

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

    public EventResponseDto updateEvent(String id, Event updatedEvent) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado!"));

        // Atualiza os campos do evento
        event.setEventName(updatedEvent.getEventName());
        event.setDateTime(updatedEvent.getDateTime());
        event.setCep(updatedEvent.getCep());

        // Busca os novos detalhes do endereço com base no CEP atualizado
        AddressResponseDto address = addressService.getAddress(updatedEvent.getCep());
        event.setLogradouro(address.getLogradouro());
        event.setBairro(address.getBairro());
        event.setCidade(address.getLocalidade());
        event.setUf(address.getUf());

        // Salva o evento atualizado
        Event savedEvent = eventRepository.save(event);

        // Converte a entidade Event para EventResponseDto
        return mapToEventResponseDto(savedEvent);
    }

    public void deleteEvent(String id) {
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
