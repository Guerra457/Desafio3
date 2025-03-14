package org.compass.mseventmanagerapi.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventRequestDto {
    @NotBlank(message = "Nome do evento é obrigatório")
    private String eventName;

    @Future(message = "Data deve ser no futuro")
    @NotNull(message = "Data e hora são obrigatórias")
    private LocalDateTime dateTime;

    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
    private String cep;
}