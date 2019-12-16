package com.tone.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.tone.model.LuthierEntity;
import com.tone.model.payload.Luthier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import com.tone.exception.BusinessException;
import com.tone.model.LuthierEntity;
import com.tone.model.payload.GenericResponse;
import com.tone.model.payload.Luthier;
import com.tone.service.LuthierService;
import com.tone.utils.ConstantsMessages;
import com.tone.utils.Utils;

import lombok.extern.slf4j.Slf4j;

import static com.tone.utils.ConstantsMessages.*;

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

	/**
	 * @param request
	 * @return Return all luthier actived
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/active")
	public ResponseEntity<?> findLuthiersActive(HttpServletRequest request) {

		log.debug("luthierController:findLuthiersActive");

		Set<LuthierEntity> list = luthierService.findActive();

		Set<Luthier> listLuthier = Utils.convertFromTo(list, Luthier.class);

		return ResponseEntity.ok(messageSuccess(listLuthier, request, new String[] {ConstantsMessages.SUCCESS}, null));
	}

	/**
	 * @param request
	 * @return Return all luthiers inactived
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/inactive")
	public ResponseEntity<?> findLuthiersInactive(HttpServletRequest request) {

		log.debug("luthierController:findLuthiersInactive");

		Set<LuthierEntity> list = luthierService.findInactive();

		Set<Luthier> listLuthier = Utils.convertFromTo(list, Luthier.class);

		return ResponseEntity.ok(messageSuccess(listLuthier, request, new String[] {ConstantsMessages.SUCCESS}, null));
	}

	/**
	 * @param name Luthier's name
	 * @param request
	 * @return Return the luthier required
	 */
	@GetMapping("/{name}")
	public ResponseEntity<?> findLuthierByName(@PathVariable String name, HttpServletRequest request) {

		log.debug("luthierController:findLuthierByName");

		Optional<List<LuthierEntity>> luthierEntity = luthierService.findByName(name);

		if(luthierEntity.isPresent()) {
			List<LuthierEntity> list = luthierEntity.get();
			List<Luthier> listLuthier = new ArrayList<Luthier>();

			for(LuthierEntity entity : list) {
				listLuthier.add((Luthier) Utils.convertFromTo(entity, Luthier.class));
			}

			return ResponseEntity.ok(messageSuccess(listLuthier, request, new String[] {ConstantsMessages.SUCCESS}, null));

		}else {
			return messageError(request, new String[] {MSG_ERROR_LUTHIER_NOTFOUND}, null);
		}

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

	/**
	 *
	 * @param luthier Luthier that will be actived
	 * @param request
	 * @return return luthier actived
	 */
	@PutMapping("/active")
	public ResponseEntity<GenericResponse> active(@RequestBody Luthier luthier, HttpServletRequest request) {

		Luthier luthierSaved = null;

		if (luthier.getId() != null) {

			try {
				LuthierEntity luthierEntity = (LuthierEntity) Utils.convertFromTo(luthier, LuthierEntity.class);
				luthierSaved = (Luthier) Utils.convertFromTo(luthierService.active(luthierEntity), Luthier.class);
			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
		}else {
			return messageError(request, new String[] {NOTBLANK_LUTHIER_ID}, null);
		}

		return ResponseEntity.ok(messageSuccess(luthierSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
	}

	/**
	 *
	 * @param luthier Luthier that will be inactived
	 * @param request
	 * @return return luthier inactived
	 */
	@PutMapping("/inactive")
	public ResponseEntity<GenericResponse> inactive(@RequestBody Luthier luthier, HttpServletRequest request) {

		Luthier luthierSaved = null;

		if (luthier.getId() != null) {

			try {
				LuthierEntity luthierEntity = (LuthierEntity) Utils.convertFromTo(luthier, LuthierEntity.class);
				luthierSaved = (Luthier) Utils.convertFromTo(luthierService.inactive(luthierEntity), Luthier.class);
			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
		}else {
			return messageError(request, new String[] {NOTBLANK_LUTHIER_ID}, null);
		}

		return ResponseEntity.ok(messageSuccess(luthierSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
	}
}
