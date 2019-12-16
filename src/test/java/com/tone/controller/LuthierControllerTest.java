package com.tone.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import java.util.*;
import java.util.stream.Collectors;

import com.tone.exception.BusinessException;
import com.tone.model.*;
import com.tone.model.LuthierEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.model.payload.Feature;
import com.tone.model.payload.Luthier;
import com.tone.utils.ConstantsMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @DisplayName(value="Find all luthiers active.")
    @Test
    void findAllLuthiersActive() throws Exception {

        when(luthierService.findActive()).thenReturn(new HashSet<>(luthiers));

        mockMvc.perform(get("/api/luthier/active")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
        ;
    }

    @DisplayName(value="Not found luthiers active.")
    @Test
    void notFoundLuthiersActive() throws Exception {

        Set<LuthierEntity> list = new HashSet<>();
        when(luthierService.findActive()).thenReturn(list);

        mockMvc.perform(get("/api/luthier/active")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
        ;
    }

    @DisplayName(value="Find all luthiers inactive.")
    @Test
    void findAllLuthiersInactive() throws Exception {

        when(luthierService.findInactive()).thenReturn(new HashSet<>(luthiers));

        mockMvc.perform(get("/api/luthier/inactive")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
        ;
    }

    @DisplayName(value="Not found luthiers inactive.")
    @Test
    void notFoundLuthiersInactive() throws Exception {

        Set<LuthierEntity> list = new HashSet<>();
        when(luthierService.findInactive()).thenReturn(list);

        mockMvc.perform(get("/api/luthier/inactive")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
        ;
    }

    @DisplayName(value="Not found luthiers.")
    @Test
    void notFoundLuthiers() throws Exception {

        Set<LuthierEntity> list = new HashSet<>();
        when(luthierService.findAll()).thenReturn(Optional.ofNullable(list));

        mockMvc.perform(get("/api/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
        ;
    }

    @DisplayName(value="Find luthier by name.")
    @Test
    void findLuthierByName() throws Exception {

        LuthierEntity luthierEntity = LuthierEntity.builder().id(1l).name("luthier").build();
        List<LuthierEntity> list = new ArrayList<LuthierEntity>();
        list.add(luthierEntity);

        Optional<List<LuthierEntity>> luthiersEntities = Optional.of(list);

        when(luthierService.findByName(Mockito.anyString())).thenReturn(luthiersEntities);

        mockMvc.perform(get("/api/luthier/luthier")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(1)))
        ;
    }

    @DisplayName(value="Not Found an luthier by name.")
    @Test
    void notFoundLuthierByName() throws Exception {

        Optional<List<LuthierEntity>> luthiersEntities = Optional.empty();

        when(luthierService.findByName(Mockito.anyString())).thenReturn(luthiersEntities);

        mockMvc.perform(get("/api/luthier/joao")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())
        ;
    }

    @DisplayName(value="Active luthier")
    @Test
    void activeLuthier() throws Exception {

        Luthier luthier = Luthier.builder().id(1l).name("luthier").email("luthier@tone.com").status(StatusEnum.INACTIVE).build();
        LuthierEntity luthierEntity = LuthierEntity.builder().id(1l).name("luthier").email("luthier@tone.com").status(StatusEnum.ACTIVE).build();

        when(luthierService.active(ArgumentMatchers.any(LuthierEntity.class))).thenReturn(luthierEntity);

        mockMvc.perform(put("/api/luthier/active")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(luthierEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(luthierEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(luthierEntity.getStatus().name())))
        ;

        verify(luthierService).active(ArgumentMatchers.any());
    }

    @DisplayName(value="Inactive luthier")
    @Test
    void inactiveLuthier() throws Exception {

        Luthier luthier = Luthier.builder().id(1l).name("luthier").email("luthier@tone.com").status(StatusEnum.ACTIVE).build();
        LuthierEntity luthierEntity = LuthierEntity.builder().id(1l).email("luthier@tone.com").name("luthier").status(StatusEnum.INACTIVE).build();

        when(luthierService.inactive(ArgumentMatchers.any(LuthierEntity.class))).thenReturn(luthierEntity);

        mockMvc.perform(put("/api/luthier/inactive")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(luthierEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(luthierEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(luthierEntity.getStatus().name())))
        ;

        verify(luthierService).inactive(ArgumentMatchers.any());
    }

    @DisplayName(value="Doesnt active luthier that doesnt exist")
    @Test
    void shouldntActiveLuthierThatDoesntExist() throws Exception {

        Luthier luthier = Luthier.builder().name("luthier").email("luthier@tone.com").status(StatusEnum.INACTIVE).build();

        mockMvc.perform(put("/api/luthier/active")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
        ;

        verify(luthierService, times(0)).active(ArgumentMatchers.any());
    }

    @DisplayName(value="Doesnt inactive luthier that doesnt exist")
    @Test
    void shouldntInactiveLuthierThatDoesntExist() throws Exception {

        Luthier luthier = Luthier.builder().name("luthier").email("luthier@tone.com").status(StatusEnum.ACTIVE).build();

        mockMvc.perform(put("/api/luthier/inactive")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
        ;

        verify(luthierService, times(0)).inactive(ArgumentMatchers.any());
    }

    @DisplayName(value="Throw exception when try active luthier")
    @Test
    void shouldTrowExceptionActiveLuthier() throws Exception {

        Luthier luthier = Luthier.builder().id(1l).name("luthier").email("luthier@tone.com").status(StatusEnum.INACTIVE).build();

        when(luthierService.active(ArgumentMatchers.any(LuthierEntity.class))).thenThrow(BusinessException.class);

        mockMvc.perform(put("/api/luthier/active")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
        ;

        verify(luthierService).active(ArgumentMatchers.any());
    }

    @DisplayName(value="Throw exception when try inactive luthier")
    @Test
    void shouldTrowExceptionInactiveLuthier() throws Exception {

        Luthier luthier = Luthier.builder().id(1l).name("luthier").email("luthier@tone.com").status(StatusEnum.ACTIVE).build();

        when(luthierService.inactive(ArgumentMatchers.any(LuthierEntity.class))).thenThrow(BusinessException.class);

        mockMvc.perform(put("/api/luthier/inactive")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
        ;

        verify(luthierService).inactive(ArgumentMatchers.any());
    }
/*

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
