package com.tone.controller;

import static com.tone.utils.ConstantsMessages.NOTBLANK_LUTHIER_ID;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tone.exception.BusinessException;
import com.tone.model.LuthierEntity;
import com.tone.model.payload.GenericResponse;
import com.tone.model.payload.Luthier;
import com.tone.service.LuthierService;
import com.tone.utils.ConstantsMessages;
import com.tone.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/luthier")
public class LuthierController extends BaseController{

	private final LuthierService luthierService;

    public LuthierController(LuthierService luthierService) {
    	super();
        this.luthierService = luthierService;
    }
    
    @GetMapping
	public ResponseEntity<?> findLuthiers(HttpServletRequest request) {
	  
    	log.debug("luthierController:findLuthiers");
    		     
	    List<LuthierEntity> list = luthierService.findAll().orElse(null).stream().collect(Collectors.toList());
	    
	    List<Luthier> listLuthiers = Utils.convertFromTo(list, Luthier.class);
	    
        return ResponseEntity.ok(listLuthiers);
    
    }
    
    @PostMapping
    public ResponseEntity<GenericResponse> createluthier(@Valid @RequestBody Luthier luthier, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	Luthier luthierSaved = null;
    	
    	if (!result.hasErrors()) {
    	
    		try {
    			LuthierEntity luthierEntity = (LuthierEntity) Utils.convertFromTo(luthier, LuthierEntity.class);
    			luthierSaved = (Luthier) Utils.convertFromTo(luthierService.save(luthierEntity), Luthier.class);

    		} catch (Exception e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
			return messageError(request, validateErrors(result), null);
		}

    	return ResponseEntity.ok(messageSuccess(luthierSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    @PutMapping
    public ResponseEntity<GenericResponse> updateluthier(@Valid @RequestBody Luthier luthier, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	Luthier luthierSaved = null;
    	
    	if (!result.hasErrors() && luthier.getId() != null) {
    	
    		try {
    			
    			LuthierEntity luthierEntity = (LuthierEntity) Utils.convertFromTo(luthier, LuthierEntity.class);    			
    			luthierSaved = (Luthier) Utils.convertFromTo(luthierService.save(luthierEntity), Luthier.class);

			} catch (Exception e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
    		
    		if(result.hasErrors()) {
    			return messageError(request, validateErrors(result), null);
    		}else {
    			return messageError(request, new String[] {NOTBLANK_LUTHIER_ID}, null);
    		}
		} 

    	return ResponseEntity.ok(messageSuccess(luthierSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    @DeleteMapping
    public ResponseEntity<GenericResponse> deleteluthier(@Valid @RequestBody Luthier luthier, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	if (!result.hasErrors()) {
    		LuthierEntity luthierEntity = (LuthierEntity) Utils.convertFromTo(luthier, LuthierEntity.class);    			
			try {
				luthierService.delete(luthierEntity);
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}else {
			return messageError(request, validateErrors(result), null);
		} 

    	return ResponseEntity.ok(messageSuccess(null, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
}
