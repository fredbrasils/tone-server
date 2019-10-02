package com.tone.controller;

import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_NOTFOUND;
import static com.tone.utils.ConstantsMessages.NOTBLANK_FEATURE_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tone.exception.BusinessException;
import com.tone.model.FeatureEntity;
import com.tone.model.payload.Feature;
import com.tone.model.payload.GenericResponse;
import com.tone.service.FeatureService;
import com.tone.utils.ConstantsMessages;
import com.tone.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/feature")
public class FeatureController extends BaseController{

	private final FeatureService featureService;

    public FeatureController(FeatureService featureService) {
    	super();
        this.featureService = featureService;
    }
    
    /**
     * @param request
     * @return Return all features
     */
    @SuppressWarnings("unchecked")
	@GetMapping
	public ResponseEntity<?> findFeatures(HttpServletRequest request) {
	  
    	log.debug("featureController:findAllFeatures");
    		     
    	Set<FeatureEntity> list = featureService.findAll().orElse(null);
	    
    	Set<Feature> listFeatures = Utils.convertFromTo(list, Feature.class);
	    
        return ResponseEntity.ok(messageSuccess(listFeatures, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * @param request
     * @return Return all features actived
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/active")
	public ResponseEntity<?> findFeatureActive(HttpServletRequest request) {
	  
    	log.debug("featureController:findFeatureActive");
    		     
    	Set<FeatureEntity> list = featureService.findActive();
	    
    	Set<Feature> listFeatures = Utils.convertFromTo(list, Feature.class);
	    
        return ResponseEntity.ok(messageSuccess(listFeatures, request, new String[] {ConstantsMessages.SUCCESS}, null));	    
    }
    
    /**
     * @param request
     * @return Return all features inactived
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/inactive")
	public ResponseEntity<?> findFeatureInactive(HttpServletRequest request) {
	  
    	log.debug("featureController:findFeatureInactive");
    		     
    	Set<FeatureEntity> list = featureService.findInactive();
	    
    	Set<Feature> listFeatures = Utils.convertFromTo(list, Feature.class);
	    
        return ResponseEntity.ok(messageSuccess(listFeatures, request, new String[] {ConstantsMessages.SUCCESS}, null));	    
    }
    
    /**
     * @param name Feature's name
     * @param request
     * @return Return the feature required
     */
    @GetMapping("/{name}")
	public ResponseEntity<?> findFeatureByName(@PathVariable String name, HttpServletRequest request) {
	  
    	log.debug("featureController:findFeatureByName");
    		     
    	Optional<List<FeatureEntity>> featureEntity = featureService.findByName(name);
	    
	   if(featureEntity.isPresent()) {
		   List<FeatureEntity> list = featureEntity.get();
		   List<Feature> listFeature = new ArrayList<Feature>();
		   
		   for(FeatureEntity entity : list) {
			   listFeature.add((Feature) Utils.convertFromTo(entity, Feature.class));
		   }	   
		   
		   return ResponseEntity.ok(messageSuccess(listFeature, request, new String[] {ConstantsMessages.SUCCESS}, null));
		   
	   }else {
		   return messageError(request, new String[] {MSG_ERROR_FEATURE_NOTFOUND}, null);
	   }	    
    }
    
    /**
     * Save feature
     * @param feature Feature that will be saved
     * @param result
     * @param request
     * @param errors
     * @return return feature saved
     */
    @PostMapping
    public ResponseEntity<GenericResponse> createFeature(@Valid @RequestBody Feature feature, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	Feature featureSaved = null;
    	
    	if (!result.hasErrors()) {
    	
    		try {
    			FeatureEntity featureEntity = (FeatureEntity) Utils.convertFromTo(feature, FeatureEntity.class);
    			featureSaved = (Feature) Utils.convertFromTo(featureService.save(featureEntity), Feature.class);

    		} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
			return messageError(request, validateErrors(result), null);
		}

    	return ResponseEntity.ok(messageSuccess(featureSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * Update feature
     * @param feature Feature that will be updated
     * @param result
     * @param request
     * @param errors
     * @return return feature updated
     */
    @PutMapping
    public ResponseEntity<GenericResponse> updatefeature(@Valid @RequestBody Feature feature, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	Feature featureSaved = null;
    	
    	if (!result.hasErrors() && feature.getId() != null) {
    	
    		try {
    			
    			FeatureEntity featureEntity = (FeatureEntity) Utils.convertFromTo(feature, FeatureEntity.class);    			
    			featureSaved = (Feature) Utils.convertFromTo(featureService.save(featureEntity), Feature.class);

			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
    		
    		if(result.hasErrors()) {
    			return messageError(request, validateErrors(result), null);
    		}else {
    			return messageError(request, new String[] {NOTBLANK_FEATURE_ID}, null);
    		}
		} 

    	return ResponseEntity.ok(messageSuccess(featureSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * 
     * @param feature Feature that will be deleted
     * @param result
     * @param request
     * @param errors
     * @return 
     */
    /*@DeleteMapping
    public ResponseEntity<GenericResponse> deleteFeature(@Valid @RequestBody Feature feature, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	if (!result.hasErrors() && feature.getId() != null) {
    	
    		try {
    			
    			FeatureEntity featureEntity = (FeatureEntity) Utils.convertFromTo(feature, FeatureEntity.class);    			
    			featureService.delete(featureEntity);

			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
    		
    		if(result.hasErrors()) {
    			return messageError(request, validateErrors(result), null);
    		}else {
    			return messageError(request, new String[] {NOTBLANK_FEATURE_ID}, null);
    		}
		} 

    	return ResponseEntity.ok(messageSuccess(null, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    */
    /**
     * 
     * @param listFeature Features that will be deleted
     * @param request
     * @return
     */
    /*@DeleteMapping("/list")
    public ResponseEntity<GenericResponse> deleteAllFeature(@RequestBody List<Feature> listFeature,
			HttpServletRequest request) {    	
    	    
    	for(Feature feature : listFeature) {    		
    		if(feature.getId() == null) {
    			return messageError(request, new String[] {NOTBLANK_FEATURE_ID}, null);
    		}
    	}
    	
		try {
			
			FeatureEntity featureEntity = null;
			
			for(Feature feature : listFeature) {
				featureEntity = (FeatureEntity) Utils.convertFromTo(feature, FeatureEntity.class);    			
				featureService.delete(featureEntity);
			}

		} catch (BusinessException e) {
			return messageError(request, new String[] {e.getMessage()}, null);
		}

    	return ResponseEntity.ok(messageSuccess(null, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    */
    /**
     * 
     * @param feature Feature that will be actived
     * @param request
     * @return return feature actived
     */
    /*@PutMapping("/active")
    public ResponseEntity<GenericResponse> active(@RequestBody Feature feature, HttpServletRequest request) {
    	
    	Feature featureSaved = null;
    	
    	if (feature.getId() != null) {
    	
    		try {    			
    			FeatureEntity featureEntity = (FeatureEntity) Utils.convertFromTo(feature, FeatureEntity.class);    			
    			featureSaved = (Feature) Utils.convertFromTo(featureService.active(featureEntity), Feature.class);
			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
   			return messageError(request, new String[] {NOTBLANK_FEATURE_ID}, null);
		} 

    	return ResponseEntity.ok(messageSuccess(featureSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    */
    /**
     * 
     * @param feature Feature that will be inactived
     * @param request
     * @return return feature inactived
     */
    /*@PutMapping("/inactive")
    public ResponseEntity<GenericResponse> inactive(@RequestBody Feature feature, HttpServletRequest request) {
    	
    	Feature featureSaved = null;
    	
    	if (feature.getId() != null) {
    	
    		try {    			
    			FeatureEntity featureEntity = (FeatureEntity) Utils.convertFromTo(feature, FeatureEntity.class);    			
    			featureSaved = (Feature) Utils.convertFromTo(featureService.inactive(featureEntity), Feature.class);
			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
   			return messageError(request, new String[] {NOTBLANK_FEATURE_ID}, null);
		} 

    	return ResponseEntity.ok(messageSuccess(featureSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    */
}
