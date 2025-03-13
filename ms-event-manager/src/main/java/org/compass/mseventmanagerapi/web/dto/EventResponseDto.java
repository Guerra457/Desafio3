package org.compass.mseventmanagerapi.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventResponseDto {
    private String id;
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
}
