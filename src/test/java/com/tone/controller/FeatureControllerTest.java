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
import com.tone.model.FeatureEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.model.payload.Feature;
import com.tone.service.FeatureService;

@ExtendWith(MockitoExtension.class)
class FeatureControllerTest extends AbstractRestControllerTest{

	@Mock
    FeatureService featureService;

    @InjectMocks
    FeatureController featureController;

    @Mock
    MessageSource messages;
    
    List<FeatureEntity> featuresActive;
    List<FeatureEntity> featuresInactive;
    
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    	featuresActive = new ArrayList<FeatureEntity>();
    	featuresActive.add(FeatureEntity.builder().id(1l).name("style").status(StatusEnum.ACTIVE).build());
    	featuresActive.add(FeatureEntity.builder().id(2l).name("material").status(StatusEnum.ACTIVE).build());
    	
    	featuresInactive = new ArrayList<FeatureEntity>();
    	featuresInactive.add(FeatureEntity.builder().id(3l).name("price").status(StatusEnum.INACTIVE).build());
    	
        mockMvc = MockMvcBuilders
                .standaloneSetup(featureController)
                .build();
    }    
    
    
    @DisplayName(value="Find all features active.") 
    @Test
    void findAllFeaturesActive() throws Exception {
		
    	when(featureService.findActive()).thenReturn(new HashSet<>(featuresActive));    	
    	
        mockMvc.perform(get("/api/feature/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
                ;        
    }
    
    @DisplayName(value="Not found features active.") 
    @Test
    void notFoundFeaturesActive() throws Exception {
		
    	Set<FeatureEntity> list = new HashSet<>();
    	when(featureService.findActive()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/feature/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
                ;        
    }
    
    @DisplayName(value="Find all features inactive.") 
    @Test
    void findAllFeaturesInactive() throws Exception {
		
    	when(featureService.findInactive()).thenReturn(new HashSet<>(featuresInactive));    	
    	
        mockMvc.perform(get("/api/feature/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(1)))
                ;        
    }
    
    @DisplayName(value="Not found features inactive.") 
    @Test
    void notFoundFeaturesInactive() throws Exception {
		
    	Set<FeatureEntity> list = new HashSet<>();
    	when(featureService.findInactive()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/feature/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
                ;        
    }
    
    @DisplayName(value="Find all features.") 
    @Test
    void findAllFeatures() throws Exception {
		
    	Optional<Set<FeatureEntity>> list = Optional.of(featuresActive.stream().collect(Collectors.toSet()));
    	
    	when(featureService.findAll()).thenReturn(list);    	
    	
        mockMvc.perform(get("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(2)))
                ;        
    }
    
    @DisplayName(value="Not found features.") 
    @Test
    void notFoundFeatures() throws Exception {
		
    	Set<FeatureEntity> list = new HashSet<>();
    	when(featureService.findAll()).thenReturn(Optional.ofNullable(list));    	
    	
        mockMvc.perform(get("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").doesNotExist())
                ;        
    }
    
    @DisplayName(value="Find feature by name.") 
    @Test
    void findFeatureByName() throws Exception {
		
    	FeatureEntity featureEntity = FeatureEntity.builder().id(1l).name("style").build();
    	List<FeatureEntity> list = new ArrayList<FeatureEntity>();
    	list.add(featureEntity);
    	
    	Optional<List<FeatureEntity>> featureEntities = Optional.of(list);
    	
    	when(featureService.findByName(Mockito.anyString())).thenReturn(featureEntities);    	
    	
        mockMvc.perform(get("/api/feature/style")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.object").isArray())
                .andExpect(jsonPath("$.object", hasSize(1)))
                ;        
    }
    
    @DisplayName(value="Not Found an feature by name.") 
    @Test
    void notFoundFeatureByName() throws Exception {
		
    	Optional<List<FeatureEntity>> featureEntities = Optional.empty();
    	
    	when(featureService.findByName(Mockito.anyString())).thenReturn(featureEntities);    	
    	
        mockMvc.perform(get("/api/feature/myspace")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())                
                ;
    }
    
    @DisplayName(value="Create a new feature")
    @Test
    void registerFeature() throws Exception {
    	
    	Feature feature = Feature.builder().name("style").status(StatusEnum.ACTIVE).build();
    	FeatureEntity featureEntity = FeatureEntity.builder().id(1l).name("style").status(StatusEnum.ACTIVE).build();
    	
    	when(featureService.save(ArgumentMatchers.any(FeatureEntity.class))).thenReturn(featureEntity);

        mockMvc.perform(post("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(featureEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(featureEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(featureEntity.getStatus().name())))
                ;

        verify(featureService).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont create a new feature without name")
    @Test
    void dontRegisterFeatureWithoutName() throws Exception {
    	
    	Feature feature = Feature.builder().name("").build();

        mockMvc.perform(post("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(feature)))
                .andExpect(status().isBadRequest())                
                ;

        verify(featureService, times(0)).save(ArgumentMatchers.any());

    }

    @DisplayName(value="Dont create the same feature ")
    @Test
    void dontRegisterTheSameFeature() throws Exception {
    	
    	Feature feature = Feature.builder().name("style").status(StatusEnum.ACTIVE).build();
    	
    	when(featureService.save(ArgumentMatchers.any(FeatureEntity.class))).thenThrow(BusinessException.class);    	

        mockMvc.perform(post("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isBadRequest())                
                ;

        verify(featureService, times(1)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Update feature")
    @Test
    void updateFeature() throws Exception {
    	
    	Feature feature = Feature.builder().id(1l).name("style").status(StatusEnum.ACTIVE).build();
    	FeatureEntity featureEntity = FeatureEntity.builder().id(1l).name("style").status(StatusEnum.ACTIVE).build();
    	
    	when(featureService.save(ArgumentMatchers.any(FeatureEntity.class))).thenReturn(featureEntity);

        mockMvc.perform(put("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(featureEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(featureEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(featureEntity.getStatus().name())))
                ;

        verify(featureService).save(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Dont Update feature without name")
    @Test
    void dontUpdateFeatureWithoutName() throws Exception {
    	
    	Feature feature = Feature.builder().id(1l).name("").build();

        mockMvc.perform(put("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(feature)))
                .andExpect(status().isBadRequest())                
                ;

        verify(featureService, times(0)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont Update feature without id")
    @Test
    void dontUpdateFeatureWithoutId() throws Exception {
    	
    	Feature feature = Feature.builder().name("style").build();

        mockMvc.perform(put("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(feature)))
                .andExpect(status().isBadRequest())                
                ;

        verify(featureService, times(0)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Dont update feature with exist name")
    @Test
    void dontRegisterAnExistsFeature() throws Exception {
    	
    	Feature feature = Feature.builder().name("style").id(1l).build();
    	
    	when(featureService.save(ArgumentMatchers.any(FeatureEntity.class))).thenThrow(BusinessException.class);    	

        mockMvc.perform(put("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isBadRequest())                
                ;

        verify(featureService, times(1)).save(ArgumentMatchers.any());

    }
    
    @DisplayName(value="Active feature")
    @Test
    void activeFeature() throws Exception {
    	
    	Feature feature = Feature.builder().id(1l).name("style").status(StatusEnum.INACTIVE).build();
    	FeatureEntity featureEntity = FeatureEntity.builder().id(1l).name("style").status(StatusEnum.ACTIVE).build();
    	
    	when(featureService.active(ArgumentMatchers.any(FeatureEntity.class))).thenReturn(featureEntity);

        mockMvc.perform(put("/api/feature/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(featureEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(featureEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(featureEntity.getStatus().name())))
                ;

        verify(featureService).active(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Inactive feature")
    @Test
    void inactiveFeature() throws Exception {
    	
    	Feature feature = Feature.builder().id(1l).name("style").status(StatusEnum.ACTIVE).build();
    	FeatureEntity featureEntity = FeatureEntity.builder().id(1l).name("style").status(StatusEnum.INACTIVE).build();
    	
    	when(featureService.inactive(ArgumentMatchers.any(FeatureEntity.class))).thenReturn(featureEntity);

        mockMvc.perform(put("/api/feature/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.name", equalTo(featureEntity.getName())))
                .andExpect(jsonPath("$.object.id", equalTo(featureEntity.getId().intValue())))
                .andExpect(jsonPath("$.object.status", equalTo(featureEntity.getStatus().name())))
                ;

        verify(featureService).inactive(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Doesnt active feature that doesnt exist")
    @Test
    void shouldntActiveFeatureThatDoesntExist() throws Exception {
    	
    	Feature feature = Feature.builder().name("xyz").status(StatusEnum.INACTIVE).build();
    	
        mockMvc.perform(put("/api/feature/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))                
                ;

        verify(featureService, times(0)).active(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Doesnt inactive feature that doesnt exist")
    @Test
    void shouldntInactiveFeatureThatDoesntExist() throws Exception {
    	
    	Feature feature = Feature.builder().name("zyx").status(StatusEnum.ACTIVE).build();
    	
        mockMvc.perform(put("/api/feature/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))                
                ;

        verify(featureService, times(0)).inactive(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Throw exception when try active feature")
    @Test
    void shouldTrowExceptionActiveFeature() throws Exception {
    	
    	Feature feature = Feature.builder().id(1l).name("style").status(StatusEnum.INACTIVE).build();
    	
    	when(featureService.active(ArgumentMatchers.any(FeatureEntity.class))).thenThrow(BusinessException.class);
    	
        mockMvc.perform(put("/api/feature/active")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))                
                ;

        verify(featureService).active(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Throw exception when try inactive feature")
    @Test
    void shouldTrowExceptionInactiveFeature() throws Exception {
    	
    	Feature feature = Feature.builder().id(1l).name("style").status(StatusEnum.ACTIVE).build();
    	
    	when(featureService.inactive(ArgumentMatchers.any(FeatureEntity.class))).thenThrow(BusinessException.class);
    	
        mockMvc.perform(put("/api/feature/inactive")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))                
                ;

        verify(featureService).inactive(ArgumentMatchers.any());
    }
    /*
    @DisplayName(value="Delete feature")
    @Test
    void deleteFeature() throws Exception {
    	
    	Feature feature = Feature.builder().id(1l).name("instagram").build();
    	
        mockMvc.perform(delete("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isOk())
                ;

        verify(featureService).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete feature without id")
    @Test
    void shouldntDeleteFeatureWithoutId() throws Exception {
    	
    	Feature feature = Feature.builder().name("instagram").build();
    	
        mockMvc.perform(delete("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(featureService, times(0)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete feature without name")
    @Test
    void shouldntDeleteFeatureWithoutName() throws Exception {
    	
    	Feature feature = Feature.builder().id(1l).build();
    	
        mockMvc.perform(delete("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(featureService, times(0)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete feature")
    @Test
    void shouldntDeleteFeature() throws Exception {
    	
    	Feature feature = Feature.builder().id(1l).name("instagram").build();
    	
    	Mockito.doThrow(BusinessException.class).when(featureService).delete(ArgumentMatchers.any(FeatureEntity.class));
    	
        mockMvc.perform(delete("/api/feature")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(feature))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(featureService, times(1)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Delete list of features")
    @Test
    void deleteListOfFeatures() throws Exception {
    	
    	Feature feature1 = Feature.builder().id(1l).name("instagram").build();
    	Feature feature2 = Feature.builder().id(2l).name("facebook").build();
    	Feature feature3 = Feature.builder().id(3l).name("other").build();
    	List<Feature> list = new ArrayList<Feature>();
    	list.add(feature1);list.add(feature2);list.add(feature3);
    	
        mockMvc.perform(delete("/api/feature/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(list))).andDo(print())
                .andExpect(status().isOk())
                ;

        verify(featureService, times(3)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete list of features without id")
    @Test
    void shouldntDeleteListOfFeaturesWithoutId() throws Exception {
    	
    	Feature feature1 = Feature.builder().id(1l).name("instagram").build();
    	Feature feature2 = Feature.builder().name("facebook").build();
    	Feature feature3 = Feature.builder().id(3l).name("other").build();
    	List<Feature> list = new ArrayList<Feature>();
    	list.add(feature1);list.add(feature2);list.add(feature3);
    	
        mockMvc.perform(delete("/api/feature/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(list))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(featureService, times(0)).delete(ArgumentMatchers.any());
    }
    
    @DisplayName(value="Cant delete list of features")
    @Test
    void shouldntDeleteListOfFeatures() throws Exception {
    	
    	Feature feature1 = Feature.builder().id(1l).name("instagram").build();
    	Feature feature2 = Feature.builder().id(2l).name("facebook").build();
    	Feature feature3 = Feature.builder().id(3l).name("other").build();
    	List<Feature> list = new ArrayList<Feature>();
    	list.add(feature1);list.add(feature2);list.add(feature3);
    	
    	Mockito.doThrow(BusinessException.class).when(featureService).delete(ArgumentMatchers.any(FeatureEntity.class));
    	
        mockMvc.perform(delete("/api/feature/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.contentType(MediaType.APPLICATION_JSON)        		
        		.content(asJsonString(list))).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", equalTo(false)))
                ;

        verify(featureService, times(1)).delete(ArgumentMatchers.any());
    }
    */
}
