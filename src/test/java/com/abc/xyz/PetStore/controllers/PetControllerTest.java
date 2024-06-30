package com.abc.xyz.PetStore.controllers;

import com.abc.xyz.PetStore.constants.Constants;
import com.abc.xyz.PetStore.models.PetDto;
import com.abc.xyz.PetStore.services.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@WebMvcTest(PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PetService petService;

    private static String petDtoPath;

    private String petDtoJsonString;
    private static String transactionId;
    private PetDto petDto;


    @BeforeAll
    public static void init() {
        petDtoPath = "classpath:/petDto.json";
        transactionId = UUID.randomUUID().toString();
    }

    @BeforeEach
    public void setUp() throws IOException {
        petDto = objectMapper.readValue(resourceLoader.getResource(petDtoPath).getFile(), PetDto.class);
        petDtoJsonString = objectMapper.writeValueAsString(petDto);
    }

    @Test
    public void uploadImageTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/pet-inventory/v1/pet/{petId}/upload", "3").contentType(MediaType.MULTIPART_FORM_DATA_VALUE).header(Constants.TRANSACTION_ID, transactionId)).andDo(MockMvcResultHandlers.log()).andExpect(MockMvcResultMatchers.status().isNotImplemented());
    }

    @Test
    public void createPetTest() throws Exception {
        ResponseEntity<PetDto> expectedResponseEntity = new ResponseEntity<>(petDto, HttpStatus.CREATED);
        Mockito.when(petService.createPet(petDto, transactionId)).thenReturn(expectedResponseEntity);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/pet-inventory/v1/pet")
                        .content(petDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.TRANSACTION_ID, transactionId))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(petDtoJsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void findByStatusTest() throws Exception {

        ResponseEntity<List<PetDto>> expectedResponseEntity = new ResponseEntity<>(List.<PetDto>of(petDto), HttpStatus.OK);
        Mockito.when(petService.findPetsByStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(expectedResponseEntity);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/pet-inventory/v1/pet/findByStatus")
                        .param("status", "available,sold")
                        .header(Constants.TRANSACTION_ID, transactionId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.status().isOk());

        //Mockito.verify(petService, Mockito.times(1)).findPetsByStatus(Mockito.anyString(),Mockito.anyString());
    }

    @Test
    public void findByIdTest() throws Exception {

        ResponseEntity<PetDto> expectedResponseEntity = new ResponseEntity<>(petDto, HttpStatus.OK);
        Mockito.when(petService.findPetById(Mockito.anyInt(), Mockito.anyString())).thenReturn(expectedResponseEntity);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/pet-inventory/v1/pet/{petId}", "3")
                        .header(Constants.TRANSACTION_ID, transactionId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void updatePetTest() throws Exception {
        ResponseEntity<PetDto> expectedResponseEntity = new ResponseEntity<>(petDto, HttpStatus.OK);
        Mockito.when(petService.updatePet(petDto, transactionId)).thenReturn(expectedResponseEntity);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/pet-inventory/v1/pet")
                        .content(petDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.TRANSACTION_ID, transactionId))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(petDtoJsonString))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteByIdTest() throws Exception {

        ResponseEntity expectedResponseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        Mockito.when(petService.deletePetById(Mockito.anyInt(), Mockito.anyString())).thenReturn(expectedResponseEntity);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/pet-inventory/v1/pet/{petId}", "3")
                        .header(Constants.TRANSACTION_ID, transactionId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


        Mockito.verify(petService, Mockito.times(1)).deletePetById(Mockito.anyInt(), Mockito.anyString());
    }

    @Test
    public void UpdatePetWithFormDataTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/pet-inventory/v1/pet/{petId}", "3").contentType(MediaType.MULTIPART_FORM_DATA_VALUE).header(Constants.TRANSACTION_ID, transactionId)).andDo(MockMvcResultHandlers.log()).andExpect(MockMvcResultMatchers.status().isNotImplemented());
    }

    @Test
    public void findPetById_PetExceptionTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/pet-inventory/v1/pet/{petId}", "abc")
                        .header(Constants.TRANSACTION_ID, transactionId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
