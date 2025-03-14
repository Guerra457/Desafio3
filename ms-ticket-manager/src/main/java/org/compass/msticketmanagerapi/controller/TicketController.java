package org.compass.msticketmanagerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Ingressos", description = "Operações completas para gerenciamento de ingressos")
public class TicketController {
    private final TicketService ticketService;

    @Operation(
            summary = "Criar novo ingresso",
            description = "Cadastra um novo ingresso vinculado a um evento"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ingresso criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @PostMapping("/create-ticket")
    public ResponseEntity<TicketResponseDto> createTicket(@Valid @RequestBody TicketRequestDto ticketRequest) {
        TicketResponseDto createdTicket = ticketService.createTicket(ticketRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @Operation(
            summary = "Buscar ingresso por ID",
            description = "Recupera detalhes completos de um ingresso específico"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingresso encontrado"),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<TicketResponseDto> getTicketById(@PathVariable String id) {
        TicketResponseDto ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @Operation(
            summary = "Atualizar ingresso",
            description = "Atualiza informações de um ingresso existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingresso atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    @PutMapping("/update-ticket/{id}")
    public ResponseEntity<TicketResponseDto> updateTicket(@Valid @PathVariable String id, @RequestBody TicketUpdateRequestDto ticketRequest) {
        TicketResponseDto updatedTicket = ticketService.updateTicket(id, ticketRequest);
        return ResponseEntity.ok(updatedTicket);
    }

    @Operation(
            summary = "Cancelar ingresso",
            description = "Realiza soft-delete de um ingresso (marca como cancelado)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ingresso cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado")
    })
    @DeleteMapping("/cancel-ticket/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable String id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar ingressos por evento",
            description = "Verifica se existem ingressos vinculados a um evento"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @GetMapping("/check-tickets-by-event/{eventId}")
    public ResponseEntity<CheckTicketsResponse> checkTicketsByEvent(@PathVariable String eventId) {
        boolean hasTickets = ticketService.hasTicketsForEvent(eventId);
        return ResponseEntity.ok(new CheckTicketsResponse(eventId, hasTickets));
    }
}