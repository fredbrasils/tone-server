package com.tone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.tone.model.LuthierSocialNetworkEntity;
import com.tone.model.SocialNetworkEntity;

public interface LuthierSocialNetworkRepository extends PagingAndSortingRepository<LuthierSocialNetworkEntity, Long> {

	Optional<List<LuthierSocialNetworkEntity>> findAllOptionalBySocialNetwork(SocialNetworkEntity socialNetwork);
	
}
