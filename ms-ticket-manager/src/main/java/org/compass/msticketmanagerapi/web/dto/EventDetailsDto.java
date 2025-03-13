package org.compass.msticketmanagerapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class EventDetailsDto {
    @JsonProperty("id")
    private String eventId;

    private String eventName;

    @JsonProperty("dateTime")
    private LocalDateTime eventDateTime;

    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
}