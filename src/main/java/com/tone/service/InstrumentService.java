package com.tone.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.tone.exception.BusinessException;
import com.tone.model.FeatureEntity;
import com.tone.model.InstrumentEntity;

public interface InstrumentService extends BaseService<InstrumentEntity, Long>{

	Optional<List<InstrumentEntity>> findByName(String name);

	Set<InstrumentEntity> findActive();
	
	Set<InstrumentEntity> findInactive();
	
	InstrumentEntity active(InstrumentEntity entity) throws BusinessException; 
	
	InstrumentEntity inactive(InstrumentEntity entity) throws BusinessException;
	
}
