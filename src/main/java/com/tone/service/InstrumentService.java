package com.tone.service;

import java.util.Set;

import com.tone.model.InstrumentEntity;

public interface InstrumentService extends BaseService<InstrumentEntity, Long>{

	InstrumentEntity findOptionalByName(String name);

	Set<InstrumentEntity> findActive();
	
	Set<InstrumentEntity> findInactive();
}
