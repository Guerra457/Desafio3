package org.compass.msticketmanagerapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketUpdateRequestDto {
    @NotBlank(message = "O nome do cliente é obrigatório")
    private String customerName;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "O e-mail do cliente é obrigatório")
    private String customerMail;

    @NotBlank(message = "O valor é obrigatório")
    private String brlAmount;

    @NotBlank(message = "O valor é obrigatório")
    private String usdAmount;
}