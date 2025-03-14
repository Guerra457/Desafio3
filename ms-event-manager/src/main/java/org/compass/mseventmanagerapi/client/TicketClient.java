package org.compass.mseventmanagerapi.client;

import org.compass.mseventmanagerapi.web.dto.CheckTicketsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ticket-manager", url = "http://localhost:8081")
public interface TicketClient {
    @GetMapping("/check-tickets-by-event/{eventId}")
    CheckTicketsResponse checkTicketsByEvent(@PathVariable String eventId);
}
