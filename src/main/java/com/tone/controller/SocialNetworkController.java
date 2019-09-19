package com.tone.controller;

import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_NOTFOUND;
import static com.tone.utils.ConstantsMessages.NOTBLANK_SOCIAL_NETWORK_ID;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tone.exception.BusinessException;
import com.tone.model.SocialNetworkEntity;
import com.tone.model.payload.GenericResponse;
import com.tone.model.payload.SocialNetwork;
import com.tone.service.SocialNetworkService;
import com.tone.utils.ConstantsMessages;
import com.tone.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/socialNetwork")
public class SocialNetworkController extends BaseController{

	private final SocialNetworkService socialNetworkService;

    public SocialNetworkController(SocialNetworkService socialNetworkService) {
    	super();
        this.socialNetworkService = socialNetworkService;
    }
    
    /**
     * @param request
     * @return Return all socialNetworks
     */
    @SuppressWarnings("unchecked")
	@GetMapping
	public ResponseEntity<?> findSocialNetworks(HttpServletRequest request) {
	  
    	log.debug("socialNetworkController:findAllSocialNetworks");
    		     
    	Set<SocialNetworkEntity> list = socialNetworkService.findAll().orElse(null);
	    
    	Set<SocialNetwork> listSocialNetworks = Utils.convertFromTo(list, SocialNetwork.class);
	    
        return ResponseEntity.ok(messageSuccess(listSocialNetworks, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * @param request
     * @return Return all socialNetworks actived
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/active")
	public ResponseEntity<?> findSocialNetworkActive(HttpServletRequest request) {
	  
    	log.debug("socialNetworkController:findSocialNetworkActive");
    		     
    	Set<SocialNetworkEntity> list = socialNetworkService.findActive();
	    
    	Set<SocialNetwork> listSocialNetworks = Utils.convertFromTo(list, SocialNetwork.class);
	    
        return ResponseEntity.ok(messageSuccess(listSocialNetworks, request, new String[] {ConstantsMessages.SUCCESS}, null));	    
    }
    
    /**
     * @param request
     * @return Return all socialNetworks inactived
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/inactive")
	public ResponseEntity<?> findSocialNetworkInactive(HttpServletRequest request) {
	  
    	log.debug("socialNetworkController:findSocialNetworkInactive");
    		     
    	Set<SocialNetworkEntity> list = socialNetworkService.findInactive();
	    
    	Set<SocialNetwork> listSocialNetworks = Utils.convertFromTo(list, SocialNetwork.class);
	    
        return ResponseEntity.ok(messageSuccess(listSocialNetworks, request, new String[] {ConstantsMessages.SUCCESS}, null));	    
    }
    
    /**
     * @param name SocialNetwork's name
     * @param request
     * @return Return the socialNetwork required
     */
    @GetMapping("/{name}")
	public ResponseEntity<?> findSocialNetworkByName(@PathVariable String name, HttpServletRequest request) {
	  
    	log.debug("socialNetworkController:findSocialNetworkByName");
    		     
	   SocialNetworkEntity socialNetworkEntity = socialNetworkService.findByName(name);
	    
	   if(socialNetworkEntity != null) {
		   SocialNetwork socialNetwork = (SocialNetwork) Utils.convertFromTo(socialNetworkEntity, SocialNetwork.class);
		   return ResponseEntity.ok(messageSuccess(socialNetwork, request, new String[] {ConstantsMessages.SUCCESS}, null));
	   }else {
		   return messageError(request, new String[] {MSG_ERROR_SOCIAL_NETWORK_NOTFOUND}, null);
	   }	    
    }
    
    /**
     * Save socialNetwork
     * @param socialNetwork SocialNetwork that will be saved
     * @param result
     * @param request
     * @param errors
     * @return return socialNetwork saved
     */
    @PostMapping
    public ResponseEntity<GenericResponse> createSocialNetwork(@Valid @RequestBody SocialNetwork socialNetwork, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	SocialNetwork socialNetworkSaved = null;
    	
    	if (!result.hasErrors()) {
    	
    		try {
    			SocialNetworkEntity socialNetworkEntity = (SocialNetworkEntity) Utils.convertFromTo(socialNetwork, SocialNetworkEntity.class);
    			socialNetworkSaved = (SocialNetwork) Utils.convertFromTo(socialNetworkService.save(socialNetworkEntity), SocialNetwork.class);

    		} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
			return messageError(request, validateErrors(result), null);
		}

    	return ResponseEntity.ok(messageSuccess(socialNetworkSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * Update socialNetwork
     * @param socialNetwork SocialNetwork that will be updated
     * @param result
     * @param request
     * @param errors
     * @return return socialNetwork updated
     */
    @PutMapping
    public ResponseEntity<GenericResponse> updatesocialNetwork(@Valid @RequestBody SocialNetwork socialNetwork, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	SocialNetwork socialNetworkSaved = null;
    	
    	if (!result.hasErrors() && socialNetwork.getId() != null) {
    	
    		try {
    			
    			SocialNetworkEntity socialNetworkEntity = (SocialNetworkEntity) Utils.convertFromTo(socialNetwork, SocialNetworkEntity.class);    			
    			socialNetworkSaved = (SocialNetwork) Utils.convertFromTo(socialNetworkService.save(socialNetworkEntity), SocialNetwork.class);

			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
    		
    		if(result.hasErrors()) {
    			return messageError(request, validateErrors(result), null);
    		}else {
    			return messageError(request, new String[] {NOTBLANK_SOCIAL_NETWORK_ID}, null);
    		}
		} 

    	return ResponseEntity.ok(messageSuccess(socialNetworkSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * 
     * @param socialNetwork SocialNetwork that will be deleted
     * @param result
     * @param request
     * @param errors
     * @return 
     */
    @DeleteMapping
    public ResponseEntity<GenericResponse> deleteSocialNetwork(@Valid @RequestBody SocialNetwork socialNetwork, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	if (!result.hasErrors() && socialNetwork.getId() != null) {
    	
    		try {
    			
    			SocialNetworkEntity socialNetworkEntity = (SocialNetworkEntity) Utils.convertFromTo(socialNetwork, SocialNetworkEntity.class);    			
    			socialNetworkService.delete(socialNetworkEntity);

			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
    		
    		if(result.hasErrors()) {
    			return messageError(request, validateErrors(result), null);
    		}else {
    			return messageError(request, new String[] {NOTBLANK_SOCIAL_NETWORK_ID}, null);
    		}
		} 

    	return ResponseEntity.ok(messageSuccess(null, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * 
     * @param listSocialNetwork SocialNetworks that will be deleted
     * @param request
     * @return
     */
    @DeleteMapping("/list")
    public ResponseEntity<GenericResponse> deleteAllSocialNetwork(@RequestBody List<SocialNetwork> listSocialNetwork,
			HttpServletRequest request) {    	
    	    
    	for(SocialNetwork socialNetwork : listSocialNetwork) {    		
    		if(socialNetwork.getId() == null) {
    			return messageError(request, new String[] {NOTBLANK_SOCIAL_NETWORK_ID}, null);
    		}
    	}
    	
		try {
			
			SocialNetworkEntity socialNetworkEntity = null;
			
			for(SocialNetwork socialNetwork : listSocialNetwork) {
				socialNetworkEntity = (SocialNetworkEntity) Utils.convertFromTo(socialNetwork, SocialNetworkEntity.class);    			
				socialNetworkService.delete(socialNetworkEntity);
			}

		} catch (BusinessException e) {
			return messageError(request, new String[] {e.getMessage()}, null);
		}

    	return ResponseEntity.ok(messageSuccess(null, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * 
     * @param socialNetwork SocialNetwork that will be actived
     * @param request
     * @return return socialNetwork actived
     */
    @PutMapping("/active")
    public ResponseEntity<GenericResponse> active(@RequestBody SocialNetwork socialNetwork, HttpServletRequest request) {
    	
    	SocialNetwork socialNetworkSaved = null;
    	
    	if (socialNetwork.getId() != null) {
    	
    		try {    			
    			SocialNetworkEntity socialNetworkEntity = (SocialNetworkEntity) Utils.convertFromTo(socialNetwork, SocialNetworkEntity.class);    			
    			socialNetworkSaved = (SocialNetwork) Utils.convertFromTo(socialNetworkService.active(socialNetworkEntity), SocialNetwork.class);
			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
   			return messageError(request, new String[] {NOTBLANK_SOCIAL_NETWORK_ID}, null);
		} 

    	return ResponseEntity.ok(messageSuccess(socialNetworkSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * 
     * @param socialNetwork SocialNetwork that will be inactived
     * @param request
     * @return return socialNetwork inactived
     */
    @PutMapping("/inactive")
    public ResponseEntity<GenericResponse> inactive(@RequestBody SocialNetwork socialNetwork, HttpServletRequest request) {
    	
    	SocialNetwork socialNetworkSaved = null;
    	
    	if (socialNetwork.getId() != null) {
    	
    		try {    			
    			SocialNetworkEntity socialNetworkEntity = (SocialNetworkEntity) Utils.convertFromTo(socialNetwork, SocialNetworkEntity.class);    			
    			socialNetworkSaved = (SocialNetwork) Utils.convertFromTo(socialNetworkService.inactive(socialNetworkEntity), SocialNetwork.class);
			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
   			return messageError(request, new String[] {NOTBLANK_SOCIAL_NETWORK_ID}, null);
		} 

    	return ResponseEntity.ok(messageSuccess(socialNetworkSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
}
