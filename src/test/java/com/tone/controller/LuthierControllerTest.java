package com.tone.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tone.exception.BusinessException;
import com.tone.model.FeatureEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.model.payload.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tone.model.LuthierEntity;
import com.tone.model.payload.Luthier;
import com.tone.service.LuthierService;

@ExtendWith(MockitoExtension.class)
class LuthierControllerTest extends AbstractRestControllerTest{

	@Mock
    LuthierService luthierService;

    @InjectMocks
    LuthierController luthierController;

    @Mock
    MessageSource messages;
    
    List<LuthierEntity> luthiers;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        LuthierEntity luthier1 = LuthierEntity.builder().name("Luthier 1").email("luthier1@tone.com").status(StatusEnum.ACTIVE).build();
        LuthierEntity luthier2 = LuthierEntity.builder().name("Luthier 2").email("luthier2@tone.com").status(StatusEnum.INACTIVE).build();

    	luthiers = new ArrayList<LuthierEntity>();
    	luthiers.add(luthier1);
    	luthiers.add(luthier2);

        mockMvc = MockMvcBuilders
                .standaloneSetup(luthierController)
                .build();
    }

    @DisplayName(value="Create a new luthier")
    @Test
    void registerLuthier() throws Exception {

        Luthier luthier = Luthier.builder().name("luthier").email("luthier1@tone.com").build();
        LuthierEntity luthierEntity = LuthierEntity.builder().id(1l).name("home").email("luthier1@tone.com").status(StatusEnum.ACTIVE).build();

        when(luthierService.save(ArgumentMatchers.any(LuthierEntity.class))).thenReturn(luthierEntity);

        mockMvc.perform(post("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(luthierEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(luthierEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(luthierEntity.getStatus().name())))
        ;

        verify(luthierService).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Dont create a new luthier without name")
    @Test
    void dontRegisterLuthierWithoutName() throws Exception {

        Luthier luthier = Luthier.builder().name("").email("luthier1@tone.com").build();

        mockMvc.perform(post("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier)))
                .andExpect(status().isBadRequest())
        ;

        verify(luthierService, times(0)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Dont create a new luthier without email")
    @Test
    void dontRegisterLuthierWithoutEmail() throws Exception {

        Luthier luthier = Luthier.builder().name("luthier").email("").build();

        mockMvc.perform(post("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier)))
                .andExpect(status().isBadRequest())
        ;

        verify(luthierService, times(0)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Dont create the same luthier ")
    @Test
    void dontRegisterTheSameLuthier() throws Exception {

        Luthier luthier = Luthier.builder().name("luthier").email("luthier1@tone.com").build();

        when(luthierService.save(ArgumentMatchers.any(LuthierEntity.class))).thenThrow(BusinessException.class);

        mockMvc.perform(post("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isBadRequest())
        ;

        verify(luthierService, times(1)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Update luthier")
    @Test
    void updateLuthier() throws Exception {

        Luthier luthier = Luthier.builder().id(1l).name("luthier1").email("luthier1@tone.com").build();
        LuthierEntity luthierEntity = LuthierEntity.builder().id(1l).name("luthier1").email("luthier1@tone.com").build();

        when(luthierService.save(ArgumentMatchers.any(LuthierEntity.class))).thenReturn(luthierEntity);

        mockMvc.perform(put("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(luthierEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(luthierEntity.getId().intValue())))
        ;

        verify(luthierService).save(ArgumentMatchers.any());
    }

    @DisplayName(value="Dont Update luthier without name")
    @Test
    void dontUpdateLuthierWithoutName() throws Exception {

        Luthier luthier = Luthier.builder().id(1l).name("").email("luthier1@tone.com").build();

        mockMvc.perform(put("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier)))
                .andExpect(status().isBadRequest())
        ;

        verify(luthierService, times(0)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Dont Update luthier without id")
    @Test
    void dontUpdateLuthierWithoutId() throws Exception {

        Luthier luthier = Luthier.builder().name("Luthier").email("luthier1@tone.com").build();

        mockMvc.perform(put("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier)))
                .andExpect(status().isBadRequest())
        ;

        verify(luthierService, times(0)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Dont Update luthier without email")
    @Test
    void dontUpdateLuthierWithoutEmail() throws Exception {

        Luthier luthier = Luthier.builder().id(1l).name("Luthier").build();

        mockMvc.perform(put("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier)))
                .andExpect(status().isBadRequest())
        ;

        verify(luthierService, times(0)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Dont update luthier ")
    @Test
    void dontUpdateLuthier() throws Exception {

        Luthier luthier = Luthier.builder().id(1l).name("luthier").email("luthier1@tone.com").build();

        when(luthierService.save(ArgumentMatchers.any(LuthierEntity.class))).thenThrow(BusinessException.class);

        mockMvc.perform(put("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isBadRequest())
        ;

        verify(luthierService, times(1)).save(ArgumentMatchers.any());

    }
/*
    @DisplayName(value="Find all luthiers.") 
    @Test
    void findAllLuthiers() throws Exception {
    	Optional<Set<LuthierEntity>> list = Optional.of(luthiers.stream().collect(Collectors.toSet()));
    	
    	when(luthierService.findAll()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/luthier")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                ;        
    }



    
    @DisplayName(value="Delete luthier")
    @Test
    void deleteLuthier() throws Exception {
    	
    	Luthier luthier = Luthier.builder().id(1l).name("home").build();
    	
        mockMvc.perform(delete("/api/luthier")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isOk())
                ;

        verify(luthierService).delete(ArgumentMatchers.any());

    }
    */
}
