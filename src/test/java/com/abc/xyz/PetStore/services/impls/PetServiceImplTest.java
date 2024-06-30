package com.abc.xyz.PetStore.services.impls;

import com.abc.xyz.PetStore.entities.Pet;
import com.abc.xyz.PetStore.exceptions.PetException;
import com.abc.xyz.PetStore.mappers.PetMapper;
import com.abc.xyz.PetStore.models.PetDto;
import com.abc.xyz.PetStore.repositories.PetRepository;
import com.abc.xyz.PetStore.services.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
public class PetServiceImplTest {

    public static String petDtoPath;
    public static String petPath;
    private static String invalidStatus;
    @Mock
    private PetRepository petRepository;
    private final PetMapper petMapper = Mappers.getMapper(PetMapper.class);
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private PetService petService = new PetServiceImpl(petRepository, petMapper);
    private static String transactionId;
    private PetDto petDto;
    private Pet pet;


    @BeforeAll
    public static void init() {
        invalidStatus = "test1,test2,test3";
        petDtoPath = "classpath:/petDto.json";
        petPath = "classpath:/pet.json";
        transactionId = UUID.randomUUID().toString();
    }

    @BeforeEach
    public void setUp() throws IOException {
        petDto = objectMapper.readValue(resourceLoader.getResource(petDtoPath).getFile(), PetDto.class);
        pet = objectMapper.readValue(resourceLoader.getResource(petPath).getFile(), Pet.class);
    }


    @Test
    public void createPetTest_Success() {

        Mockito.when(petRepository.findByName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(petRepository.save(Mockito.any(Pet.class))).thenReturn(pet);

        ResponseEntity<PetDto> expectedResponseEntity = new ResponseEntity<>(petDto, HttpStatus.CREATED);
        Assertions.assertEquals(expectedResponseEntity, petService.createPet(petDto, transactionId));
    }

    @Test
    public void createPetTest_Failure() {
        Mockito.when(petRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(pet));
        Assertions.assertThrows(PetException.class, () -> petService.createPet(petDto, transactionId));

    }

    @Test
    public void findPetsByStatusTest_Failure() {
        Assertions.assertThrows(PetException.class, () -> petService.findPetsByStatus(invalidStatus, transactionId));
    }

    @Test
    public void findPetsByStatusTest_Failure_PetsNotFoundInTheInventory() {

        Mockito.when(petRepository.findByStatusIn(Mockito.anySet())).thenReturn(Optional.<List<Pet>>of(Collections.emptyList()));
        Assertions.assertThrows(PetException.class, () -> petService.findPetsByStatus("available,pending,sold", transactionId));
    }


    @ParameterizedTest
    @ValueSource(strings = {"available", "available,pending", "available,sold", "available,pending,sold"})
    public void findPetsByStatusTest_Success(String status) {
        Mockito.when(petRepository.findByStatusIn(Mockito.anySet())).thenReturn(Optional.<List<Pet>>of(List.of(pet)));
        Assertions.assertEquals(HttpStatus.OK, petService.findPetsByStatus(status, transactionId).getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(strings = {"available,test1", "test1,test2,sold"})
    public void findPetsByStatusTest_PartialSuccess(String status) {
        Mockito.when(petRepository.findByStatusIn(Mockito.anySet())).thenReturn(Optional.<List<Pet>>of(List.of(pet)));
        Assertions.assertEquals(HttpStatus.PARTIAL_CONTENT, petService.findPetsByStatus(status, transactionId).getStatusCode());
    }

    @Test
    public void findPetById_Success() {
        Mockito.when(petRepository.findByPetId(Mockito.anyInt())).thenReturn(Optional.of(pet));
        ResponseEntity<PetDto> expectedResponseEntity = new ResponseEntity<>(petDto, HttpStatus.OK);
        Assertions.assertEquals(expectedResponseEntity, petService.findPetById(petDto.getId(), transactionId));
    }

    @Test
    public void findPetById_Failure() {
        Mockito.when(petRepository.findByPetId(Mockito.anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(PetException.class, () -> petService.findPetById(petDto.getId(), transactionId));
    }


    @Test
    public void updatePet_Success() throws IOException {
        PetDto petDto = objectMapper.readValue(resourceLoader.getResource(petDtoPath).getFile(), PetDto.class);
        petDto.setStatus("sold");
        petDto.getTagDtos().stream().forEach(tag -> tag.setName("Puffy"));
        ResponseEntity<PetDto> expectedResponseEntity = new ResponseEntity<>(petDto, HttpStatus.OK);
        Mockito.when(petRepository.findByName(petDto.getName())).thenReturn(Optional.of(pet));
        Assertions.assertEquals(expectedResponseEntity, petService.updatePet(petDto, transactionId));
    }

    @Test
    public void updatePet_Failure() {
        Mockito.when(petRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(PetException.class, () -> petService.updatePet(petDto, transactionId));

    }

    @Test
    public void deletePet_Success() {
        Mockito.when(petRepository.findByPetId(Mockito.anyInt())).thenReturn(Optional.<Pet>of(pet));
        Mockito.doNothing().when(petRepository).deleteByPetId(Mockito.anyInt());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, petService.deletePetById(petDto.getId(), transactionId).getStatusCode());
    }

    @Test
    public void deletePet_Failure() {
        Mockito.when(petRepository.findByPetId(Mockito.anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(PetException.class, () -> petService.deletePetById(petDto.getId(), transactionId));
    }
}
