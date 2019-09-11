package com.tone.controller;

import static com.tone.utils.ConstantsMessages.*;

import java.util.ArrayList;
import java.util.List;

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
    
    @GetMapping
	public ResponseEntity<?> findInstruments(HttpServletRequest request) {
	  
    	log.debug("instrumentController:findInstruments");
    		     
	    List<InstrumentEntity> list = new ArrayList<InstrumentEntity>(instrumentService.findAll());
	    
	    List listInstruments = Utils.convertFromTo(list, Instrument.class);
	    
        return ResponseEntity.ok(listInstruments);
    
    }
    
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
    
    @DeleteMapping
    public ResponseEntity<GenericResponse> deleteinstrument(@Valid @RequestBody Instrument instrument, BindingResult result,
			HttpServletRequest request, Errors errors) {
    	
    	if (!result.hasErrors()) {
    		InstrumentEntity instrumentEntity = (InstrumentEntity) Utils.convertFromTo(instrument, InstrumentEntity.class);    			
			try {
				instrumentService.delete(instrumentEntity);
			} catch (BusinessException e) {
				return messageError(request, new String[] {e.getMessage()}, null);
			}
    	}else {
			return messageError(request, validateErrors(result), null);
		} 

    	return ResponseEntity.ok(messageSuccess(null, request, new String[] {ConstantsMessages.SUCCESS}, null));
    }
}
