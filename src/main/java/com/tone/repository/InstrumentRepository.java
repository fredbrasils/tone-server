package com.tone.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.tone.model.InstrumentEntity;

public interface InstrumentRepository extends PagingAndSortingRepository<InstrumentEntity, Long> {

	Optional<InstrumentEntity> findOptionalByNameIgnoreCase(String name);
}
