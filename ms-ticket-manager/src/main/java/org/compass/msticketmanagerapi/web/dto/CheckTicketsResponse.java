package org.compass.msticketmanagerapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckTicketsResponse {
    private String eventId;
    public boolean hasTickets;
}
