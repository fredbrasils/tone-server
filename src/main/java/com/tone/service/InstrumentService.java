package com.tone.service;

import java.util.Set;

import com.tone.exception.BusinessException;
import com.tone.model.InstrumentEntity;

public interface InstrumentService extends BaseService<InstrumentEntity, Long>{

	InstrumentEntity findOptionalByName(String name);

	Set<InstrumentEntity> findActive();
	
	Set<InstrumentEntity> findInactive();
	
	InstrumentEntity active(InstrumentEntity entity) throws BusinessException; 
	
	InstrumentEntity inactive(InstrumentEntity entity) throws BusinessException;
	
}
