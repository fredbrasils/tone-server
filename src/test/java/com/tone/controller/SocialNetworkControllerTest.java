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
import com.tone.model.SocialNetworkEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.model.payload.SocialNetwork;
import com.tone.service.SocialNetworkService;

@ExtendWith(MockitoExtension.class)
class SocialNetworkControllerTest extends AbstractRestControllerTest{

	@Mock
    SocialNetworkService socialNetworkService;

    @InjectMocks
    SocialNetworkController socialNetworkController;

    @Mock
    MessageSource messages;
    
    List<SocialNetworkEntity> socialNetworks;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    	socialNetworks = new ArrayList<SocialNetworkEntity>();
    	socialNetworks.add(SocialNetworkEntity.builder().id(1l).name("Instagram").status(StatusEnum.ACTIVE).build());
    	socialNetworks.add(SocialNetworkEntity.builder().id(2l).name("Facebook").status(StatusEnum.ACTIVE).build());

        mockMvc = MockMvcBuilders
                .standaloneSetup(socialNetworkController)
                .build();
    }    
    
    @DisplayName(value="Find all socialNetworks active.") 
    @Test
    void findAllSocialNetworksActive() throws Exception {
		
    	when(socialNetworkService.findActive()).thenReturn(new HashSet<>(socialNetworks));    	
    	
        mockMvc.perform(get("/api/socialNetwork/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
                ;        
    }
    
    @DisplayName(value="Not found socialNetworks active.") 
    @Test
    void notFoundSocialNetworksActive() throws Exception {
		
    	Set<SocialNetworkEntity> list = new HashSet<>();
    	when(socialNetworkService.findActive()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/socialNetwork/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
                ;        
    }
    
    @DisplayName(value="Find all socialNetworks inactive.") 
    @Test
    void findAllSocialNetworksInactive() throws Exception {
		
    	when(socialNetworkService.findInactive()).thenReturn(new HashSet<>(socialNetworks));    	
    	
        mockMvc.perform(get("/api/socialNetwork/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
                ;        
    }
    
    @DisplayName(value="Not found socialNetworks inactive.") 
    @Test
    void notFoundSocialNetworksInactive() throws Exception {
		
    	Set<SocialNetworkEntity> list = new HashSet<>();
    	when(socialNetworkService.findInactive()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/socialNetwork/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
                ;        
    }
    
    @DisplayName(value="Find all socialNetworks.") 
    @Test
    void findAllSocialNetworks() throws Exception {
		
    	Optional<Set<SocialNetworkEntity>> list = Optional.of(socialNetworks.stream().collect(Collectors.toSet()));
    	
    	when(socialNetworkService.findAll()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
                ;        
    }
    
    @DisplayName(value="Not found socialNetworks.") 
    @Test
    void notFoundSocialNetworks() throws Exception {
		
    	Set<SocialNetworkEntity> list = new HashSet<>();
    	when(socialNetworkService.findAll()).thenReturn(Optional.ofNullable(list));    	
    	
        mockMvc.perform(get("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
                ;        
    }
    
    @DisplayName(value="Find socialNetwork by name.") 
    @Test
    void findSocialNetworkByName() throws Exception {
		
    	SocialNetworkEntity socialNetworkEntity = SocialNetworkEntity.builder().id(1l).name("facebook").build();
    	
    	when(socialNetworkService.findByName(Mockito.anyString())).thenReturn(socialNetworkEntity);    	
    	
        mockMvc.perform(get("/api/socialNetwork/facebook")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(socialNetworkEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(socialNetworkEntity.getId().intValue())))
                ;        
    }
    
    @DisplayName(value="Not Found an socialNetwork by name.") 
    @Test
    void notFoundSocialNetworkByName() throws Exception {
		
    	when(socialNetworkService.findByName(Mockito.anyString())).thenReturn(null);    	
    	
        mockMvc.perform(get("/api/socialNetwork/myspace")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())                
                ;
    }
    
    @DisplayName(value="Create a new socialNetwork")
    @Test
    void registerSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().name("Instagram").status(StatusEnum.ACTIVE).build();
    	SocialNetworkEntity socialNetworkEntity = SocialNetworkEntity.builder().id(1l).name("Instagram").status(StatusEnum.ACTIVE).build();
    	
    	when(socialNetworkService.save(ArgumentMatchers.any(SocialNetworkEntity.class))).thenReturn(socialNetworkEntity);

        mockMvc.perform(post("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(socialNetworkEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(socialNetworkEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(socialNetworkEntity.getStatus().name())))
                ;

        verify(socialNetworkService).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont create a new socialNetwork without name")
    @Test
    void dontRegisterSocialNetworkWithoutName() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().name("").build();

        mockMvc.perform(post("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(socialNetwork)))
                .andExpect(status().isBadRequest())                
                ;

        verify(socialNetworkService, times(0)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Dont create the same socialNetwork ")
    @Test
    void dontRegisterTheSameSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().name("Instagram").status(StatusEnum.ACTIVE).build();
    	
    	when(socialNetworkService.save(ArgumentMatchers.any(SocialNetworkEntity.class))).thenThrow(BusinessException.class);    	

        mockMvc.perform(post("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isBadRequest())                
                ;

        verify(socialNetworkService, times(1)).save(ArgumentMatchers.any());

    }
    /*
    @DisplayName(value="Update socialNetwork")
    @Test
    void updateSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().id(1l).name("guitar").build();
    	SocialNetworkEntity socialNetworkEntity = SocialNetworkEntity.builder().id(1l).name("guitar").build();
    	
    	when(socialNetworkService.save(ArgumentMatchers.any(SocialNetworkEntity.class))).thenReturn(socialNetworkEntity);

        mockMvc.perform(put("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(socialNetworkEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(socialNetworkEntity.getId().intValue())))
                ;

        verify(socialNetworkService).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont Update socialNetwork without name")
    @Test
    void dontUpdateSocialNetworkWithoutName() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().id(1l).name("").build();

        mockMvc.perform(put("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(socialNetwork)))
                .andExpect(status().isBadRequest())                
                ;

        verify(socialNetworkService, times(0)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont Update socialNetwork without id")
    @Test
    void dontUpdateSocialNetworkWithoutId() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().name("SocialNetwork").build();

        mockMvc.perform(put("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(socialNetwork)))
                .andExpect(status().isBadRequest())                
                ;

        verify(socialNetworkService, times(0)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont update socialNetwork with exist name")
    @Test
    void dontRegisterAnExistsSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().name("guitar").id(1l).build();
    	
    	when(socialNetworkService.save(ArgumentMatchers.any(SocialNetworkEntity.class))).thenThrow(BusinessException.class);    	

        mockMvc.perform(put("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isBadRequest())                
                ;

        verify(socialNetworkService, times(1)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Active socialNetwork")
    @Test
    void activeSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().id(1l).name("guitar").status(StatusEnum.INACTIVE).build();
    	SocialNetworkEntity socialNetworkEntity = SocialNetworkEntity.builder().id(1l).name("guitar").status(StatusEnum.ACTIVE).build();
    	
    	when(socialNetworkService.active(ArgumentMatchers.any(SocialNetworkEntity.class))).thenReturn(socialNetworkEntity);

        mockMvc.perform(put("/api/socialNetwork/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(socialNetworkEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(socialNetworkEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(socialNetworkEntity.getStatus().name())))
                ;

        verify(socialNetworkService).active(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Inactive socialNetwork")
    @Test
    void inactiveSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().id(1l).name("guitar").status(StatusEnum.ACTIVE).build();
    	SocialNetworkEntity socialNetworkEntity = SocialNetworkEntity.builder().id(1l).name("guitar").status(StatusEnum.INACTIVE).build();
    	
    	when(socialNetworkService.inactive(ArgumentMatchers.any(SocialNetworkEntity.class))).thenReturn(socialNetworkEntity);

        mockMvc.perform(put("/api/socialNetwork/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(socialNetworkEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(socialNetworkEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(socialNetworkEntity.getStatus().name())))
                ;

        verify(socialNetworkService).inactive(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Doesnt active socialNetwork that doesnt exist")
    @Test
    void shouldntActiveSocialNetworkThatDoesntExist() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().name("guitar").status(StatusEnum.INACTIVE).build();
    	
        mockMvc.perform(put("/api/socialNetwork/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))                
                ;

        verify(socialNetworkService, times(0)).active(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Doesnt inactive socialNetwork that doesnt exist")
    @Test
    void shouldntInactiveSocialNetworkThatDoesntExist() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().name("guitar").status(StatusEnum.ACTIVE).build();
    	
        mockMvc.perform(put("/api/socialNetwork/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))                
                ;

        verify(socialNetworkService, times(0)).inactive(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Throw exception when try active socialNetwork")
    @Test
    void shouldTrowExceptionActiveSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().id(1l).name("guitar").status(StatusEnum.INACTIVE).build();
    	
    	when(socialNetworkService.active(ArgumentMatchers.any(SocialNetworkEntity.class))).thenThrow(BusinessException.class);
    	
        mockMvc.perform(put("/api/socialNetwork/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))                
                ;

        verify(socialNetworkService).active(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Throw exception when try inactive socialNetwork")
    @Test
    void shouldTrowExceptionInactiveSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().id(1l).name("guitar").status(StatusEnum.ACTIVE).build();
    	
    	when(socialNetworkService.inactive(ArgumentMatchers.any(SocialNetworkEntity.class))).thenThrow(BusinessException.class);
    	
        mockMvc.perform(put("/api/socialNetwork/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))                
                ;

        verify(socialNetworkService).inactive(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Delete socialNetwork")
    @Test
    void deleteSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().id(1l).name("guitar").build();
    	
        mockMvc.perform(delete("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isOk())
                ;

        verify(socialNetworkService).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete socialNetwork without id")
    @Test
    void shouldntDeleteSocialNetworkWithoutId() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().name("guitar").build();
    	
        mockMvc.perform(delete("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(socialNetworkService, times(0)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete socialNetwork without name")
    @Test
    void shouldntDeleteSocialNetworkWithoutName() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().id(1l).build();
    	
        mockMvc.perform(delete("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(socialNetworkService, times(0)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete socialNetwork")
    @Test
    void shouldntDeleteSocialNetwork() throws Exception {
    	
    	SocialNetwork socialNetwork = SocialNetwork.builder().id(1l).name("guitar").build();
    	
    	Mockito.doThrow(BusinessException.class).when(socialNetworkService).delete(ArgumentMatchers.any(SocialNetworkEntity.class));
    	
        mockMvc.perform(delete("/api/socialNetwork")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(socialNetwork))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(socialNetworkService, times(1)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Delete list of socialNetworks")
    @Test
    void deleteListOfSocialNetworks() throws Exception {
    	
    	SocialNetwork socialNetwork1 = SocialNetwork.builder().id(1l).name("guitar").build();
    	SocialNetwork socialNetwork2 = SocialNetwork.builder().id(2l).name("drum").build();
    	SocialNetwork socialNetwork3 = SocialNetwork.builder().id(3l).name("bass").build();
    	List<SocialNetwork> list = new ArrayList<SocialNetwork>();
    	list.add(socialNetwork1);list.add(socialNetwork2);list.add(socialNetwork3);
    	
        mockMvc.perform(delete("/api/socialNetwork/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(list))).andDo(print())
                .andExpect(status().isOk())
                ;

        verify(socialNetworkService, times(3)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete list of socialNetworks without id")
    @Test
    void shouldntDeleteListOfSocialNetworksWithoutId() throws Exception {
    	
    	SocialNetwork socialNetwork1 = SocialNetwork.builder().id(1l).name("guitar").build();
    	SocialNetwork socialNetwork2 = SocialNetwork.builder().name("drum").build();
    	SocialNetwork socialNetwork3 = SocialNetwork.builder().id(3l).name("bass").build();
    	List<SocialNetwork> list = new ArrayList<SocialNetwork>();
    	list.add(socialNetwork1);list.add(socialNetwork2);list.add(socialNetwork3);
    	
        mockMvc.perform(delete("/api/socialNetwork/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(list))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(socialNetworkService, times(0)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete list of socialNetworks")
    @Test
    void shouldntDeleteListOfSocialNetworks() throws Exception {
    	
    	SocialNetwork socialNetwork1 = SocialNetwork.builder().id(1l).name("guitar").build();
    	SocialNetwork socialNetwork2 = SocialNetwork.builder().id(2l).name("drum").build();
    	SocialNetwork socialNetwork3 = SocialNetwork.builder().id(3l).name("bass").build();
    	List<SocialNetwork> list = new ArrayList<SocialNetwork>();
    	list.add(socialNetwork1);list.add(socialNetwork2);list.add(socialNetwork3);
    	
    	Mockito.doThrow(BusinessException.class).when(socialNetworkService).delete(ArgumentMatchers.any(SocialNetworkEntity.class));
    	
        mockMvc.perform(delete("/api/socialNetwork/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(list))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(socialNetworkService, times(1)).delete(ArgumentMatchers.any());
    }
    */
}
