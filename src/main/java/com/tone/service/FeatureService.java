package com.tone.service;

import java.util.Set;

import com.tone.exception.BusinessException;
import com.tone.model.FeatureEntity;

public interface FeatureService extends BaseService<FeatureEntity, Long>{

	FeatureEntity findByName(String name);

	Set<FeatureEntity> findActive();
	
	Set<FeatureEntity> findInactive();
	
	FeatureEntity active(FeatureEntity entity) throws BusinessException; 
	
	FeatureEntity inactive(FeatureEntity entity) throws BusinessException;
	
}
