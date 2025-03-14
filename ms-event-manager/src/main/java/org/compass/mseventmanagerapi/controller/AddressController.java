package org.compass.mseventmanagerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.compass.mseventmanagerapi.service.AddressService;
import org.compass.mseventmanagerapi.web.dto.AddressResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@Tag(name = "Endereços", description = "Operações relacionadas a consulta de CEPs")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "Consultar endereço por CEP", description = "Busca informações de endereço usando ViaCEP")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
            @ApiResponse(responseCode = "404", description = "CEP não encontrado")
    })
    @GetMapping("/{cep}")
    public ResponseEntity<AddressResponseDto> getAddress(@PathVariable String cep) {
        AddressResponseDto address = addressService.getAddress(cep);
        return ResponseEntity.ok(address);
    }
}

