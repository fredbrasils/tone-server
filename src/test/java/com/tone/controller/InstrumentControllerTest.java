package com.tone.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.tone.exception.BusinessException;
import com.tone.model.InstrumentEntity;
import com.tone.model.payload.Instrument;
import com.tone.service.InstrumentService;

@ExtendWith(MockitoExtension.class)
class InstrumentControllerTest extends AbstractRestControllerTest{

	@Mock
    InstrumentService instrumentService;

    @InjectMocks
    InstrumentController instrumentController;

    @Mock
    MessageSource messages;
    
    List<InstrumentEntity> instruments;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    	instruments = new ArrayList<InstrumentEntity>();
    	instruments.add(InstrumentEntity.builder().id(1l).name("Instrument 1").build());
    	instruments.add(InstrumentEntity.builder().id(2l).name("Instrument 2").build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(instrumentController)
                .build();
    }    
    
    @DisplayName(value="Find all instruments active.") 
    @Test
    void findAllInstrumentsActive() throws Exception {
		
    	when(instrumentService.findActive()).thenReturn(new HashSet<>(instruments));    	
    	
        mockMvc.perform(get("/api/instrument/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
                ;        
    }
    
    @DisplayName(value="Not found instruments active.") 
    @Test
    void notFoundInstrumentsActive() throws Exception {
		
    	Set<InstrumentEntity> list = new HashSet<>();
    	when(instrumentService.findActive()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/instrument/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
                ;        
    }
    
    @DisplayName(value="Find all instruments inactive.") 
    @Test
    void findAllInstrumentsInactive() throws Exception {
		
    	when(instrumentService.findInactive()).thenReturn(new HashSet<>(instruments));    	
    	
        mockMvc.perform(get("/api/instrument/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
                ;        
    }
    
    @DisplayName(value="Not found instruments inactive.") 
    @Test
    void notFoundInstrumentsInactive() throws Exception {
		
    	Set<InstrumentEntity> list = new HashSet<>();
    	when(instrumentService.findInactive()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/instrument/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
                ;        
    }
    
    @DisplayName(value="Find all instruments.") 
    @Test
    void findAllInstruments() throws Exception {
		
    	Optional<Set<InstrumentEntity>> list = Optional.of(instruments.stream().collect(Collectors.toSet()));
    	
    	when(instrumentService.findAll()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
                ;        
    }
    
    @DisplayName(value="Not found instruments.") 
    @Test
    void notFoundInstruments() throws Exception {
		
    	Set<InstrumentEntity> list = new HashSet<>();
    	when(instrumentService.findAll()).thenReturn(Optional.ofNullable(list));    	
    	
        mockMvc.perform(get("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
                ;        
    }
    
    @DisplayName(value="Find instrument by name.") 
    @Test
    void findInstrumentByName() throws Exception {
		
    	InstrumentEntity instrumentEntity = InstrumentEntity.builder().id(1l).name("guitar").build();
    	
    	when(instrumentService.findOptionalByName(Mockito.anyString())).thenReturn(instrumentEntity);    	
    	
        mockMvc.perform(get("/api/instrument/guitar")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(instrumentEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(instrumentEntity.getId().intValue())))
                ;        
    }
    
    @DisplayName(value="Not Found an instrument by name.") 
    @Test
    void notFoundInstrumentByName() throws Exception {
		
    	when(instrumentService.findOptionalByName(Mockito.anyString())).thenReturn(null);    	
    	
        mockMvc.perform(get("/api/instrument/batuque")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())                
                ;
    }
    
    @DisplayName(value="Create a new instrument")
    @Test
    void registerInstrument() throws Exception {
    	
    	Instrument instrument = Instrument.builder().name("guitar").build();
    	InstrumentEntity instrumentEntity = InstrumentEntity.builder().id(1l).name("guitar").build();
    	
    	when(instrumentService.save(ArgumentMatchers.any(InstrumentEntity.class))).thenReturn(instrumentEntity);

        mockMvc.perform(post("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(instrument))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(instrumentEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(instrumentEntity.getId().intValue())))
                ;

        verify(instrumentService).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont create a new instrument without name")
    @Test
    void dontRegisterInstrumentWithoutName() throws Exception {
    	
    	Instrument instrument = Instrument.builder().name("").build();

        mockMvc.perform(post("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(instrument)))
                .andExpect(status().isBadRequest())                
                ;

        verify(instrumentService, times(0)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Dont create the same instrument ")
    @Test
    void dontRegisterTheSameInstrument() throws Exception {
    	
    	Instrument instrument = Instrument.builder().name("guitar").build();
    	
    	when(instrumentService.save(ArgumentMatchers.any(InstrumentEntity.class))).thenThrow(BusinessException.class);    	

        mockMvc.perform(post("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(instrument))).andDo(print())
                .andExpect(status().isBadRequest())                
                ;

        verify(instrumentService, times(1)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Update instrument")
    @Test
    void updateInstrument() throws Exception {
    	
    	Instrument instrument = Instrument.builder().id(1l).name("guitar").build();
    	InstrumentEntity instrumentEntity = InstrumentEntity.builder().id(1l).name("guitar").build();
    	
    	when(instrumentService.save(ArgumentMatchers.any(InstrumentEntity.class))).thenReturn(instrumentEntity);

        mockMvc.perform(put("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(instrument))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(instrumentEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(instrumentEntity.getId().intValue())))
                ;

        verify(instrumentService).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont Update instrument without name")
    @Test
    void dontUpdateInstrumentWithoutName() throws Exception {
    	
    	Instrument instrument = Instrument.builder().id(1l).name("").build();

        mockMvc.perform(put("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(instrument)))
                .andExpect(status().isBadRequest())                
                ;

        verify(instrumentService, times(0)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont Update instrument without id")
    @Test
    void dontUpdateInstrumentWithoutId() throws Exception {
    	
    	Instrument instrument = Instrument.builder().name("Instrument").build();

        mockMvc.perform(put("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(instrument)))
                .andExpect(status().isBadRequest())                
                ;

        verify(instrumentService, times(0)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont update instrument with exist name")
    @Test
    void dontRegisterAnExistsInstrument() throws Exception {
    	
    	Instrument instrument = Instrument.builder().name("guitar").id(1l).build();
    	
    	when(instrumentService.save(ArgumentMatchers.any(InstrumentEntity.class))).thenThrow(BusinessException.class);    	

        mockMvc.perform(put("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(instrument))).andDo(print())
                .andExpect(status().isBadRequest())                
                ;

        verify(instrumentService, times(1)).save(ArgumentMatchers.any());

    }
    
    /*
    @DisplayName(value="Delete instrument")
    @Test
    void deleteInstrument() throws Exception {
    	
    	Instrument instrument = Instrument.builder().id(1l).name("home").build();
    	
        mockMvc.perform(delete("/api/instrument")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(instrument))).andDo(print())
                .andExpect(status().isOk())
                ;

        verify(instrumentService).delete(ArgumentMatchers.any());

    }
    */
}
