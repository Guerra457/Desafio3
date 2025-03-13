package org.compass.msticketmanagerapi.client;

import org.compass.msticketmanagerapi.web.dto.EventDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-event-manager", url = "http://localhost:8080")
public interface EventClient {
    @GetMapping("/get-event/{id}")
    EventDetailsDto getEventDetails(@PathVariable String id);
}