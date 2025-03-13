package org.compass.msticketmanagerapi.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponseDto {
    private String ticketId;
    private String cpf;
    private String customerName;
    private String customerMail;
    private EventDetailsDto event;
    private String brlTotalAmount;
    private String usdTotalAmount;
    private String status;
}