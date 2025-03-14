package org.compass.mseventmanagerapi;

import org.compass.mseventmanagerapi.controller.AddressController;
import org.compass.mseventmanagerapi.service.AddressService;
import org.compass.mseventmanagerapi.web.dto.AddressResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AddressController.class})
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Test
    @DisplayName("GET /address/{cep} - Deve retornar 200 quando o endereço for encontrado")
    public void testGetAddress_Success() throws Exception {
        String cep = "01001000";
        AddressResponseDto responseDto = AddressResponseDto.builder()
                .cep(cep)
                .logradouro("Praça da Sé")
                .complemento("lado ímpar")
                .bairro("Sé")
                .localidade("São Paulo")
                .uf("SP")
                .estado("São Paulo")
                .build();

        Mockito.when(addressService.getAddress(cep)).thenReturn(responseDto);

        mockMvc.perform(get("/address/{cep}", cep)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep", is(cep)))
                .andExpect(jsonPath("$.logradouro", is("Praça da Sé")))
                .andExpect(jsonPath("$.complemento", is("lado ímpar")))
                .andExpect(jsonPath("$.bairro", is("Sé")))
                .andExpect(jsonPath("$.localidade", is("São Paulo")))
                .andExpect(jsonPath("$.uf", is("SP")))
                .andExpect(jsonPath("$.estado", is("São Paulo")));
    }

    @Test
    @DisplayName("GET /address/{cep} - Deve retornar 404 quando o CEP não for encontrado")
    public void testGetAddress_NotFound() throws Exception {
        String cep = "99999999";
        Mockito.when(addressService.getAddress(cep)).thenReturn(null);

        mockMvc.perform(get("/address/{cep}", cep)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}