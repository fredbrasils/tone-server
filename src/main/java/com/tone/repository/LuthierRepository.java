package com.tone.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.tone.model.LuthierEntity;

public interface LuthierRepository extends PagingAndSortingRepository<LuthierEntity, Long> {

	Optional<LuthierEntity> findOptionalByName(String name);
}
