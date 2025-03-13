package org.compass.mseventmanagerapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.compass.mseventmanagerapi.web.dto.AddressResponseDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    private String id;
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
}
