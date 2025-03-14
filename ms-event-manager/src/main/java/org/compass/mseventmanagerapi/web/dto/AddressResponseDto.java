package org.compass.mseventmanagerapi.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponseDto {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String estado;
}
