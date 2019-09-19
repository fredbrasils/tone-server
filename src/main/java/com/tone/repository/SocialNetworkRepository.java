package com.tone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.tone.model.SocialNetworkEntity;
import com.tone.model.enumm.StatusEnum;

public interface SocialNetworkRepository extends PagingAndSortingRepository<SocialNetworkEntity, Long> {

	Optional<SocialNetworkEntity> findOptionalByNameIgnoreCase(String name);
	
	Optional<List<SocialNetworkEntity>> findAllOptionalByStatus(StatusEnum status);
}
