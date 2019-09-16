package com.tone.service.impl;

import static com.tone.utils.ConstantsMessages.MSG_ERROR_INSTRUMENT_EXIST;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_INSTRUMENT_NOTFOUND;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_INSTRUMENT_SAVE;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tone.exception.BusinessException;
import com.tone.model.InstrumentEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.repository.InstrumentRepository;
import com.tone.service.InstrumentService;

import lombok.extern.slf4j.Slf4j;

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
	 */
	@Override
	public InstrumentEntity save(InstrumentEntity entity) throws BusinessException {

		log.info("Save instrument");
		InstrumentEntity instrument = this.findOptionalByName(entity.getName());
		
		if(instrument != null && instrument.getId() != entity.getId()) {
			throw new BusinessException(MSG_ERROR_INSTRUMENT_EXIST);
		}
		
		return super.save(entity);
	}
	
	/**
	 * @param name Instrument's name
	 * @return Instrument searched
	 * 
	 */
	@Override
	public InstrumentEntity findOptionalByName(String name) {
		return this.instrumentRepository.findOptionalByNameIgnoreCase(name).orElse(null);
	}	

	/**
	 * @return Instrument inactived
	 */
	@Override
	public Set<InstrumentEntity> findInactive() {
		return this.instrumentRepository.findAllOptionalByStatus(StatusEnum.INACTIVE).orElse(null).stream().collect(Collectors.toSet());
	}
	
	/**
	 * @return Instrument actived
	 */
	@Override
	public Set<InstrumentEntity> findActive() {
		return this.instrumentRepository.findAllOptionalByStatus(StatusEnum.ACTIVE).orElse(null).stream().collect(Collectors.toSet());
	}

	/**
	 * @param entity Instrument that will be actived
	 * @return Instrument actived
	 */
	public InstrumentEntity active(InstrumentEntity entity) throws BusinessException{
		return activeOrInactive(entity, StatusEnum.ACTIVE);
	}
	
	/**
	 * @param entity Instrument that will be inactived
	 * @return Instrument inactived
	 */
	public InstrumentEntity inactive(InstrumentEntity entity) throws BusinessException{
		return activeOrInactive(entity, StatusEnum.INACTIVE);
	}
	
	/**
	 * @param entity Instrument that will be actived or inactived
	 * @param status The new Instrument's status 
	 * @return Instrument actived or inactived
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
}
