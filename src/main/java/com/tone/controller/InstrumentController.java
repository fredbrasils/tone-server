package com.tone.controller;

import static com.tone.utils.ConstantsMessages.MSG_ERROR_INSTRUMENT_NOTFOUND;
import static com.tone.utils.ConstantsMessages.NOTBLANK_INSTRUMENT_ID;

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
import com.tone.model.InstrumentEntity;
import com.tone.model.payload.GenericResponse;
import com.tone.model.payload.Instrument;
import com.tone.service.InstrumentService;
import com.tone.utils.ConstantsMessages;
import com.tone.utils.Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/instrument")
public class InstrumentController extends BaseController{

	private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
    	super();
        this.instrumentService = instrumentService;
    }
    
    /**
     * @param request
     * @return Return all instruments
     */
    @SuppressWarnings("unchecked")
	@GetMapping
	public ResponseEntity<?> findInstruments(HttpServletRequest request) {
	  
    	log.debug("instrumentController:findAllInstruments");
    		     
    	Set<InstrumentEntity> list = instrumentService.findAll().orElse(null);
	    
    	Set<Instrument> listInstruments = Utils.convertFromTo(list, Instrument.class);
	    
        return ResponseEntity.ok(messageSuccess(listInstruments, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * @param request
     * @return Return all instruments actived
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/active")
	public ResponseEntity<?> findInstrumentActive(HttpServletRequest request) {
	  
    	log.debug("instrumentController:findInstrumentActive");
    		     
    	Set<InstrumentEntity> list = instrumentService.findActive();
	    
    	Set<Instrument> listInstruments = Utils.convertFromTo(list, Instrument.class);
	    
        return ResponseEntity.ok(messageSuccess(listInstruments, request, new String[] {ConstantsMessages.SUCCESS}, null));	    
    }
    
    /**
     * @param request
     * @return Return all instruments inactived
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/inactive")
	public ResponseEntity<?> findInstrumentInactive(HttpServletRequest request) {
	  
    	log.debug("instrumentController:findInstrumentInactive");
    		     
    	Set<InstrumentEntity> list = instrumentService.findInactive();
	    
    	Set<Instrument> listInstruments = Utils.convertFromTo(list, Instrument.class);
	    
        return ResponseEntity.ok(messageSuccess(listInstruments, request, new String[] {ConstantsMessages.SUCCESS}, null));	    
    }
    
    /**
     * @param name Instrument's name
     * @param request
     * @return Return the instrument required
     */
    @GetMapping("/{name}")
	public ResponseEntity<?> findInstrumentByName(@PathVariable String name, HttpServletRequest request) {
	  
    	log.debug("instrumentController:findInstrumentByName");
    		     
	   InstrumentEntity instrumentEntity = instrumentService.findOptionalByName(name);
	    
	   if(instrumentEntity != null) {
		   Instrument instrument = (Instrument) Utils.convertFromTo(instrumentEntity, Instrument.class);
		   return ResponseEntity.ok(messageSuccess(instrument, request, new String[] {ConstantsMessages.SUCCESS}, null));
	   }else {
		   return messageError(request, new String[] {MSG_ERROR_INSTRUMENT_NOTFOUND}, null);
	   }	    
    }
    
    /**
     * Save instrument
     * @param instrument Instrument that will be saved
     * @param result
     * @param request
     * @param errors
     * @return return instrument saved
     */
    @PostMapping
    public ResponseEntity<GenericResponse> createInstrument(@Valid @RequestBody Instrument instrument, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	Instrument instrumentSaved = null;
    	
    	if (!result.hasErrors()) {
    	
    		try {
    			InstrumentEntity instrumentEntity = (InstrumentEntity) Utils.convertFromTo(instrument, InstrumentEntity.class);
    			instrumentSaved = (Instrument) Utils.convertFromTo(instrumentService.save(instrumentEntity), Instrument.class);

    		} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
			return messageError(request, validateErrors(result), null);
		}

    	return ResponseEntity.ok(messageSuccess(instrumentSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * Update instrument
     * @param instrument Instrument that will be updated
     * @param result
     * @param request
     * @param errors
     * @return return instrument updated
     */
    @PutMapping
    public ResponseEntity<GenericResponse> updateinstrument(@Valid @RequestBody Instrument instrument, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	Instrument instrumentSaved = null;
    	
    	if (!result.hasErrors() && instrument.getId() != null) {
    	
    		try {
    			
    			InstrumentEntity instrumentEntity = (InstrumentEntity) Utils.convertFromTo(instrument, InstrumentEntity.class);    			
    			instrumentSaved = (Instrument) Utils.convertFromTo(instrumentService.save(instrumentEntity), Instrument.class);

			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
    		
    		if(result.hasErrors()) {
    			return messageError(request, validateErrors(result), null);
    		}else {
    			return messageError(request, new String[] {NOTBLANK_INSTRUMENT_ID}, null);
    		}
		} 

    	return ResponseEntity.ok(messageSuccess(instrumentSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * 
     * @param instrument Instrument that will be deleted
     * @param result
     * @param request
     * @param errors
     * @return 
     */
    @DeleteMapping
    public ResponseEntity<GenericResponse> deleteInstrument(@Valid @RequestBody Instrument instrument, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	if (!result.hasErrors() && instrument.getId() != null) {
    	
    		try {
    			
    			InstrumentEntity instrumentEntity = (InstrumentEntity) Utils.convertFromTo(instrument, InstrumentEntity.class);    			
    			instrumentService.delete(instrumentEntity);

			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
    		
    		if(result.hasErrors()) {
    			return messageError(request, validateErrors(result), null);
    		}else {
    			return messageError(request, new String[] {NOTBLANK_INSTRUMENT_ID}, null);
    		}
		} 

    	return ResponseEntity.ok(messageSuccess(null, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * 
     * @param listInstrument Instruments that will be deleted
     * @param request
     * @return
     */
    @DeleteMapping("/list")
    public ResponseEntity<GenericResponse> deleteAllInstrument(@RequestBody List<Instrument> listInstrument,
			HttpServletRequest request) {    	
    	    
    	for(Instrument instrument : listInstrument) {    		
    		if(instrument.getId() == null) {
    			return messageError(request, new String[] {NOTBLANK_INSTRUMENT_ID}, null);
    		}
    	}
    	
		try {
			
			InstrumentEntity instrumentEntity = null;
			
			for(Instrument instrument : listInstrument) {
				instrumentEntity = (InstrumentEntity) Utils.convertFromTo(instrument, InstrumentEntity.class);    			
				instrumentService.delete(instrumentEntity);
			}

		} catch (BusinessException e) {
			return messageError(request, new String[] {e.getMessage()}, null);
		}

    	return ResponseEntity.ok(messageSuccess(null, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * 
     * @param instrument Instrument that will be actived
     * @param request
     * @return return instrument actived
     */
    @PutMapping("/active")
    public ResponseEntity<GenericResponse> active(@RequestBody Instrument instrument, HttpServletRequest request) {
    	
    	Instrument instrumentSaved = null;
    	
    	if (instrument.getId() != null) {
    	
    		try {    			
    			InstrumentEntity instrumentEntity = (InstrumentEntity) Utils.convertFromTo(instrument, InstrumentEntity.class);    			
    			instrumentSaved = (Instrument) Utils.convertFromTo(instrumentService.active(instrumentEntity), Instrument.class);
			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
   			return messageError(request, new String[] {NOTBLANK_INSTRUMENT_ID}, null);
		} 

    	return ResponseEntity.ok(messageSuccess(instrumentSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
    
    /**
     * 
     * @param instrument Instrument that will be inactived
     * @param request
     * @return return instrument inactived
     */
    @PutMapping("/inactive")
    public ResponseEntity<GenericResponse> inactive(@RequestBody Instrument instrument, HttpServletRequest request) {
    	
    	Instrument instrumentSaved = null;
    	
    	if (instrument.getId() != null) {
    	
    		try {    			
    			InstrumentEntity instrumentEntity = (InstrumentEntity) Utils.convertFromTo(instrument, InstrumentEntity.class);    			
    			instrumentSaved = (Instrument) Utils.convertFromTo(instrumentService.inactive(instrumentEntity), Instrument.class);
			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
   			return messageError(request, new String[] {NOTBLANK_INSTRUMENT_ID}, null);
		} 

    	return ResponseEntity.ok(messageSuccess(instrumentSaved, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
}
