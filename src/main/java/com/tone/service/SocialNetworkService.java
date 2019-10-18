package com.tone.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.tone.exception.BusinessException;
import com.tone.model.SocialNetworkEntity;

public interface SocialNetworkService extends BaseService<SocialNetworkEntity, Long>{

	Optional<List<SocialNetworkEntity>> findByName(String name);

	Set<SocialNetworkEntity> findActive();
	
	Set<SocialNetworkEntity> findInactive();
	
	SocialNetworkEntity active(SocialNetworkEntity entity) throws BusinessException; 
	
	SocialNetworkEntity inactive(SocialNetworkEntity entity) throws BusinessException;
	
}
