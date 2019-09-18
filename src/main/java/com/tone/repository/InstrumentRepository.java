package com.tone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.tone.model.InstrumentEntity;
import com.tone.model.enumm.StatusEnum;

public interface InstrumentRepository extends PagingAndSortingRepository<InstrumentEntity, Long> {

	Optional<InstrumentEntity> findOptionalByNameIgnoreCase(String name);
	
	Optional<List<InstrumentEntity>> findAllOptionalByStatus(StatusEnum status);
}
