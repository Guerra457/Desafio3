package org.compass.mseventmanagerapi.controller;

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
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/{cep}")
    public ResponseEntity<AddressResponseDto> getAddress(@PathVariable String cep) {
        AddressResponseDto address = addressService.getAddress(cep);
        return ResponseEntity.ok(address);
    }
}

