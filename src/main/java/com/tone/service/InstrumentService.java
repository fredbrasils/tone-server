package com.tone.service;

import com.tone.model.InstrumentEntity;

public interface InstrumentService extends BaseService<InstrumentEntity, Long>{

	InstrumentEntity findOptionalByName(String name);
	
}
