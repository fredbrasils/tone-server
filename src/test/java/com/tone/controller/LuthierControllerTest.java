package com.tone.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    	luthiers = new ArrayList<LuthierEntity>();
    	luthiers.add(LuthierEntity.builder().id(1l).name("Luthier 1").build());
    	luthiers.add(LuthierEntity.builder().id(2l).name("Luthier 2").build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(luthierController)
                .build();
    }    
    
    //Just replace the .p2 version of the hamcrest jar by the maven hamcrest-core 1.3.
    
    @DisplayName(value="Find all luthiers.") 
    @Test
    void findAllLuthiers() throws Exception {
		
    	when(luthierService.findAll()).thenReturn(new HashSet<>(luthiers));    	
    	
        mockMvc.perform(get("/api/luthier")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                ;        
    }
    
    @DisplayName(value="Create a new luthier")
    @Test
    void registerLuthier() throws Exception {
    	
    	Luthier luthier = Luthier.builder().name("home").build();
    	LuthierEntity luthierEntity = LuthierEntity.builder().id(1l).name("home").build();
    	
    	when(luthierService.save(ArgumentMatchers.any(LuthierEntity.class))).thenReturn(luthierEntity);

        mockMvc.perform(post("/api/luthier")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(luthier))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(luthierEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(luthierEntity.getId().intValue())))
                ;

        verify(luthierService).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont create a new luthier")
    @Test
    void dontRegisterLuthier() throws Exception {
    	
    	Luthier luthier = Luthier.builder().name("").build();

        mockMvc.perform(post("/api/luthier")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(luthier)))
                .andExpect(status().isBadRequest())                
                ;

        verify(luthierService, times(0)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Update luthier")
    @Test
    void updateLuthier() throws Exception {
    	
    	Luthier luthier = Luthier.builder().id(1l).name("home").build();
    	LuthierEntity luthierEntity = LuthierEntity.builder().id(1l).name("home").build();
    	
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
    	
    	Luthier luthier = Luthier.builder().id(1l).name("").build();

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
    	
    	Luthier luthier = Luthier.builder().name("Luthier").build();

        mockMvc.perform(put("/api/luthier")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(luthier)))
                .andExpect(status().isBadRequest())                
                ;

        verify(luthierService, times(0)).save(ArgumentMatchers.any());

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
    
}
