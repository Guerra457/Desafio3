package org.compass.mseventmanagerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Eventos", description = "Operações completas para gerenciamento de eventos")
public class EventController {
    private final EventService eventService;

    @Operation(
            summary = "Criar novo evento",
            description = "Cadastra um novo evento com validação de endereço via CEP"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "CEP não encontrado")
    })
    @PostMapping("/create-event")
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto eventRequest) {
        EventResponseDto createdEvent = eventService.createEvent(eventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @Operation(
            summary = "Buscar evento por ID",
            description = "Recupera todos os detalhes de um evento específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @GetMapping("/get-event/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable String id) {
        EventResponseDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @Operation(
            summary = "Listar todos os eventos",
            description = "Retorna uma lista paginada de todos os eventos cadastrados"
    )
    @ApiResponse(responseCode = "200", description = "Lista de eventos recuperada com sucesso")
    @GetMapping("/get-all-events")
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        List<EventResponseDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @Operation(
            summary = "Listar eventos ordenados",
            description = "Retorna eventos ordenados alfabeticamente pelo nome"
    )
    @ApiResponse(responseCode = "200", description = "Lista ordenada recuperada com sucesso")
    @GetMapping("/get-all-events/sorted")
    public ResponseEntity<List<EventResponseDto>> getAllEventsSorted() {
        List<EventResponseDto> events = eventService.getAllEventsSorted();
        return ResponseEntity.ok(events);
    }

    @Operation(
            summary = "Atualizar evento",
            description = "Atualiza dados de um evento existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @PutMapping("/update-event/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable String id, @RequestBody EventRequestDto eventRequest) {
        EventResponseDto updatedEvent = eventService.updateEvent(id, eventRequest);
        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(
            summary = "Excluir evento",
            description = "Exclui um evento após verificar se há ingressos vinculados"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Evento excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado"),
            @ApiResponse(responseCode = "409", description = "Evento possui ingressos vendidos")
    })
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
