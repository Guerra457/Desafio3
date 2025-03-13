package org.compass.msticketmanagerapi.web.dto;

import lombok.Data;

@Data
public class TicketRequestDto {
    private String customerName;
    private String cpf;
    private String customerMail;
    private String eventId;
    private String eventName;
    private String brlAmount;
    private String usdAmount;
}