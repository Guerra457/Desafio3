package org.compass.mseventmanagerapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.compass.mseventmanagerapi.controller.EventController;
import org.compass.mseventmanagerapi.exception.EventNotFoundException;
import org.compass.mseventmanagerapi.exception.EventHasTicketsException;
import org.compass.mseventmanagerapi.service.EventService;
import org.compass.mseventmanagerapi.web.dto.EventRequestDto;
import org.compass.mseventmanagerapi.web.dto.EventResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {EventController.class})
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /create-event - Deve criar um evento com sucesso (201)")
    public void testCreateEvent() throws Exception {
        EventRequestDto requestDto = new EventRequestDto();
        requestDto.setEventName("Teste");
        requestDto.setCep("01001000");
        requestDto.setDateTime(LocalDateTime.now());

        EventResponseDto responseDto = EventResponseDto.builder()
                .id("1")
                .eventName("Teste")
                .cep("01001000")
                .dateTime(requestDto.getDateTime())
                .logradouro("Rua Teste")
                .bairro("Bairro Teste")
                .cidade("Cidade Teste")
                .uf("TS")
                .build();

        Mockito.when(eventService.createEvent(Mockito.any(EventRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/create-event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /get-event/{id} - Deve retornar 200 quando o evento for encontrado")
    public void testGetEventById_Success() throws Exception {
        String eventId = "1";
        EventResponseDto responseDto = EventResponseDto.builder()
                .id(eventId)
                .eventName("Evento Sucesso")
                .build();

        Mockito.when(eventService.getEventById(eventId)).thenReturn(responseDto);

        mockMvc.perform(get("/get-event/{id}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(eventId)))
                .andExpect(jsonPath("$.eventName", is("Evento Sucesso")));
    }

    @Test
    @DisplayName("GET /get-event/{id} - Deve retornar 404 quando o evento não for encontrado")
    public void testGetEventById_NotFound() throws Exception {
        String eventId = "999";
        Mockito.when(eventService.getEventById(eventId))
                .thenThrow(new EventNotFoundException("Evento não encontrado!"));

        mockMvc.perform(get("/get-event/{id}", eventId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /get-all-events - Deve retornar 200 e a lista de eventos")
    public void testGetAllEvents() throws Exception {
        EventResponseDto event1 = EventResponseDto.builder().id("1").eventName("Evento 1").build();
        EventResponseDto event2 = EventResponseDto.builder().id("2").eventName("Evento 2").build();
        List<EventResponseDto> events = Arrays.asList(event1, event2);

        Mockito.when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(get("/get-all-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[1].id", is("2")));
    }

    @Test
    @DisplayName("GET /get-all-events - Deve retornar lista vazia se não houver eventos")
    public void testGetAllEvents_Empty() throws Exception {
        Mockito.when(eventService.getAllEvents()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/get-all-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /get-all-events/sorted - Deve retornar lista ordenada")
    public void testGetAllEventsSorted() throws Exception {
        EventResponseDto eventA = EventResponseDto.builder().id("A").eventName("Alpha").build();
        EventResponseDto eventB = EventResponseDto.builder().id("B").eventName("Beta").build();
        Mockito.when(eventService.getAllEventsSorted()).thenReturn(Arrays.asList(eventA, eventB));

        mockMvc.perform(get("/get-all-events/sorted"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("Alpha")))
                .andExpect(jsonPath("$[1].eventName", is("Beta")));
    }

    @Test
    @DisplayName("PUT /update-event/{id} - Deve atualizar o evento e retornar 200")
    public void testUpdateEvent_Success() throws Exception {
        String eventId = "1";
        EventRequestDto requestDto = new EventRequestDto();
        requestDto.setEventName("Evento Atualizado");
        requestDto.setCep("12345678");
        requestDto.setDateTime(LocalDateTime.now());

        EventResponseDto responseDto = EventResponseDto.builder()
                .id(eventId)
                .eventName("Evento Atualizado")
                .cep("12345678")
                .build();

        Mockito.when(eventService.updateEvent(Mockito.eq(eventId), Mockito.any(EventRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/update-event/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("Evento Atualizado")))
                .andExpect(jsonPath("$.cep", is("12345678")));
    }

    @Test
    @DisplayName("PUT /update-event/{id} - Deve retornar 404 se o evento não for encontrado")
    public void testUpdateEvent_NotFound() throws Exception {
        String eventId = "999";
        EventRequestDto requestDto = new EventRequestDto();
        requestDto.setEventName("Evento Inexistente");
        requestDto.setCep("99999999");
        requestDto.setDateTime(LocalDateTime.now());

        Mockito.when(eventService.updateEvent(Mockito.eq(eventId), Mockito.any(EventRequestDto.class)))
                .thenThrow(new EventNotFoundException("Evento não encontrado!"));

        mockMvc.perform(put("/update-event/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /delete-event/{id} - Deve excluir evento com sucesso (204)")
    public void testDeleteEvent_Success() throws Exception {
        String eventId = "1";
        Mockito.doNothing().when(eventService).deleteEvent(eventId);

        mockMvc.perform(delete("/delete-event/{id}", eventId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /delete-event/{id} - Deve retornar 409 se o evento possuir ingressos vendidos")
    public void testDeleteEvent_HasTickets() throws Exception {
        String eventId = "2";
        Mockito.doThrow(new EventHasTicketsException("Evento possui ingressos vendidos"))
                .when(eventService).deleteEvent(eventId);

        mockMvc.perform(delete("/delete-event/{id}", eventId))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("DELETE /delete-event/{id} - Deve retornar 404 se o evento não for encontrado")
    public void testDeleteEvent_NotFound() throws Exception {
        String eventId = "999";
        Mockito.doThrow(new EventNotFoundException("Evento não encontrado!"))
                .when(eventService).deleteEvent(eventId);

        mockMvc.perform(delete("/delete-event/{id}", eventId))
                .andExpect(status().isNotFound());
    }
}
