package org.compass.msticketmanagerapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.compass.msticketmanagerapi.controller.TicketController;
import org.compass.msticketmanagerapi.service.TicketService;
import org.compass.msticketmanagerapi.web.dto.TicketRequestDto;
import org.compass.msticketmanagerapi.web.dto.TicketResponseDto;
import org.compass.msticketmanagerapi.web.dto.TicketUpdateRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTicket() throws Exception {
        TicketRequestDto ticketRequestDto = new TicketRequestDto();
        ticketRequestDto.setCustomerName("Maria Silva");
        ticketRequestDto.setCpf("12345678901");
        ticketRequestDto.setCustomerMail("maria@example.com");
        ticketRequestDto.setEventId("1");
        ticketRequestDto.setBrlAmount("100.00");
        ticketRequestDto.setUsdAmount("20.00");

        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .ticketId("ticket-1")
                .customerName("Maria Silva")
                .cpf("12345678901")
                .customerMail("maria@example.com")
                .event(null)
                .brlTotalAmount("100.00")
                .usdTotalAmount("20.00")
                .status("concluído")
                .build();

        when(ticketService.createTicket(any(TicketRequestDto.class))).thenReturn(ticketResponseDto);

        mockMvc.perform(post("/create-ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId").value("ticket-1"))
                .andExpect(jsonPath("$.customerName").value("Maria Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.customerMail").value("maria@example.com"))
                .andExpect(jsonPath("$.brlTotalAmount").value("100.00"))
                .andExpect(jsonPath("$.usdTotalAmount").value("20.00"))
                .andExpect(jsonPath("$.status").value("concluído"));
    }

    @Test
    public void testGetTicketById() throws Exception {
        String ticketId = "ticket-1";

        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .ticketId(ticketId)
                .customerName("Pedro Alencar")
                .cpf("98765432100")
                .customerMail("pedro@example.com")
                .event(null)
                .brlTotalAmount("150.00")
                .usdTotalAmount("30.00")
                .status("concluído")
                .build();

        when(ticketService.getTicketById(ticketId)).thenReturn(ticketResponseDto);

        mockMvc.perform(get("/get-ticket/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(ticketId))
                .andExpect(jsonPath("$.customerName").value("Pedro Alencar"))
                .andExpect(jsonPath("$.cpf").value("98765432100"))
                .andExpect(jsonPath("$.customerMail").value("pedro@example.com"))
                .andExpect(jsonPath("$.brlTotalAmount").value("150.00"))
                .andExpect(jsonPath("$.usdTotalAmount").value("30.00"))
                .andExpect(jsonPath("$.status").value("concluído"));
    }

    @Test
    public void testUpdateTicket() throws Exception {
        String ticketId = "ticket-1";
        TicketUpdateRequestDto updateRequest = new TicketUpdateRequestDto();
        updateRequest.setCustomerName("Updated Name");
        updateRequest.setCpf("11122233344");
        updateRequest.setCustomerMail("updated@example.com");
        updateRequest.setBrlAmount("200.00");
        updateRequest.setUsdAmount("40.00");

        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .ticketId(ticketId)
                .customerName("Updated Name")
                .cpf("11122233344")
                .customerMail("updated@example.com")
                .event(null)
                .brlTotalAmount("200.00")
                .usdTotalAmount("40.00")
                .status("concluído")
                .build();

        when(ticketService.updateTicket(eq(ticketId), any(TicketUpdateRequestDto.class))).thenReturn(ticketResponseDto);

        mockMvc.perform(put("/update-ticket/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(ticketId))
                .andExpect(jsonPath("$.customerName").value("Updated Name"))
                .andExpect(jsonPath("$.cpf").value("11122233344"))
                .andExpect(jsonPath("$.customerMail").value("updated@example.com"))
                .andExpect(jsonPath("$.brlTotalAmount").value("200.00"))
                .andExpect(jsonPath("$.usdTotalAmount").value("40.00"))
                .andExpect(jsonPath("$.status").value("concluído"));
    }

    @Test
    public void testCancelTicket() throws Exception {
        String ticketId = "ticket-1";
        doNothing().when(ticketService).cancelTicket(ticketId);

        mockMvc.perform(delete("/cancel-ticket/{id}", ticketId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testCheckTicketsByEvent() throws Exception {
        String eventId = "event-1";
        when(ticketService.hasTicketsForEvent(eventId)).thenReturn(true);

        mockMvc.perform(get("/check-tickets-by-event/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(eventId))
                .andExpect(jsonPath("$.hasTickets").value(true));
    }
}

