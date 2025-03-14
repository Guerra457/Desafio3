package org.compass.mseventmanagerapi;

import org.compass.mseventmanagerapi.client.ViaCepClient;
import org.compass.mseventmanagerapi.service.AddressService;
import org.compass.mseventmanagerapi.web.dto.AddressResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private AddressService addressService;

    private AddressResponseDto addressResponseDto;

    @BeforeEach
    void setUp() {
        addressResponseDto = AddressResponseDto.builder()
                .cep("01001-000")
                .logradouro("Praça da Sé")
                .bairro("Sé")
                .localidade("São Paulo")
                .uf("SP")
                .estado("São Paulo")
                .build();
    }

    @Test
    void testGetAddress_Success() {
        when(viaCepClient.getAddress("01001-000")).thenReturn(addressResponseDto);

        AddressResponseDto response = addressService.getAddress("01001-000");

        assertNotNull(response);
        assertEquals("01001-000", response.getCep());
        assertEquals("Praça da Sé", response.getLogradouro());
        assertEquals("Sé", response.getBairro());
        assertEquals("São Paulo", response.getLocalidade());
        assertEquals("SP", response.getUf());
        assertEquals("São Paulo", response.getEstado());

        verify(viaCepClient, times(1)).getAddress("01001-000");
    }

    @Test
    void testGetAddress_NotFound() {
        when(viaCepClient.getAddress("99999999")).thenReturn(null);

        AddressResponseDto response = addressService.getAddress("99999999");

        assertNull(response);
        verify(viaCepClient, times(1)).getAddress("99999999");
    }
}
