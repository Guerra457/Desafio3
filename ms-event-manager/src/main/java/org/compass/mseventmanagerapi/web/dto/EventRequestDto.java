package org.compass.mseventmanagerapi.web.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventRequestDto {
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;
}