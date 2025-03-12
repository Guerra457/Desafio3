package org.compass.mseventmanagerapi.service;

import lombok.RequiredArgsConstructor;
import org.compass.mseventmanagerapi.client.ViaCepClient;
import org.compass.mseventmanagerapi.web.dto.AddressResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final ViaCepClient viaCepClient;

    public AddressResponseDto getAddress(String cep) {
        return viaCepClient.getAddress(cep);
    }
}
