package org.compass.mseventmanagerapi.web.dto;

import lombok.Data;

@Data
public class CheckTicketsResponse {
    private String eventId;
    private boolean hasTickets;
}
