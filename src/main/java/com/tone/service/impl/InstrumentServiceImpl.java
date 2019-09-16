package com.tone.service.impl;

import static com.tone.utils.ConstantsMessages.MSG_ERROR_INSTRUMENT_EXIST;

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

	@Override
	public InstrumentEntity save(InstrumentEntity entity) throws BusinessException {

		log.info("Save instrument");
		InstrumentEntity instrument = this.findOptionalByName(entity.getName());
		
		if(instrument != null && instrument.getId() != entity.getId()) {
			throw new BusinessException(MSG_ERROR_INSTRUMENT_EXIST);
		}
		
		return super.save(entity);
	}
	
	@Override
	public InstrumentEntity findOptionalByName(String name) {
		return this.instrumentRepository.findOptionalByNameIgnoreCase(name).orElse(null);
	}	

	@Override
	public Set<InstrumentEntity> findInactive() {
		return this.instrumentRepository.findAllOptionalByStatus(StatusEnum.INACTIVE).orElse(null).stream().collect(Collectors.toSet());
	}
	
	@Override
	public Set<InstrumentEntity> findActive() {
		return this.instrumentRepository.findAllOptionalByStatus(StatusEnum.ACTIVE).orElse(null).stream().collect(Collectors.toSet());
	}
}
