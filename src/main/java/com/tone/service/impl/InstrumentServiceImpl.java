package com.tone.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.tone.model.FeatureEntity;
import org.springframework.stereotype.Service;

import com.tone.exception.BusinessException;
import com.tone.model.InstrumentEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.repository.InstrumentRepository;
import com.tone.service.InstrumentService;

import lombok.extern.slf4j.Slf4j;

import static com.tone.utils.ConstantsMessages.*;

@Slf4j
@Service
public class InstrumentServiceImpl extends BaseServiceImpl<InstrumentEntity,Long> implements InstrumentService{
	
	private InstrumentRepository instrumentRepository;	
	
	public InstrumentServiceImpl(InstrumentRepository instrumentRepository) {
		super(instrumentRepository);
		this.instrumentRepository = instrumentRepository;
	}

	/**
	 * @param entity Instrument that will be save
	 * @return Instrument saved
	 * @throws BusinessException 
	 */
	@Override
	public InstrumentEntity save(InstrumentEntity entity) throws BusinessException {

		log.info("Save instrument");
		Optional<List<InstrumentEntity>> instruments = this.findByName(entity.getName());

		if(instruments.isPresent()) {

			Predicate<InstrumentEntity> predicate = instrument -> !instrument.getId().equals(entity.getId());

			if(instruments.get().stream().findFirst().filter(predicate).isPresent()) {
				throw new BusinessException(MSG_ERROR_INSTRUMENT_EXIST);
			}
		}

		return super.save(entity);
	}

	/**
	 * @param instrument Instrument that will be actived or inactived
	 * @param status The new Instrument's status 
	 * @return Instrument actived or inactived
	 * @throws BusinessException 
	 */
	public InstrumentEntity activeOrInactive(InstrumentEntity instrument, StatusEnum status) throws BusinessException{
		
		Optional<InstrumentEntity> instrumentSaved = findById(instrument.getId());
		InstrumentEntity instr = null;
		
		if(instrumentSaved.isPresent()) {
			instr = instrumentSaved.get();
			instr.setStatus(status);
			try {
				instr = save(instr);
			} catch (BusinessException e) {
				throw new BusinessException(MSG_ERROR_INSTRUMENT_SAVE);
			}
		}else {
			throw new BusinessException(MSG_ERROR_INSTRUMENT_NOTFOUND);
		}		
		
		return instr;
	}
	
	/**
	 * @param instrument Instrument that will be deleted
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public void delete(InstrumentEntity instrument) throws BusinessException {
		
		Optional<InstrumentEntity> instrumentSaved = findById(instrument.getId());
		
		if(instrumentSaved.isPresent()) {
			
			if(!instrumentSaved.get().getLuthiers().isEmpty()) {
				throw new BusinessException(MSG_ERROR_INSTRUMENT_DELETE_BOUND_LUTHIER);
			}
			
			try {
				super.delete(instrumentSaved.get());
			} catch (BusinessException e) {
				throw new BusinessException(MSG_ERROR_INSTRUMENT_DELETE);
			}
			
		}else {
			throw new BusinessException(MSG_ERROR_INSTRUMENT_NOTFOUND);
		}
		
	}
}
