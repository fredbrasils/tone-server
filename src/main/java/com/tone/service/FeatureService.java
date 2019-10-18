package com.tone.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.tone.exception.BusinessException;
import com.tone.model.FeatureEntity;

public interface FeatureService extends BaseService<FeatureEntity, Long>{

	Optional<List<FeatureEntity>> findByName(String name);

	void changeOrder(FeatureEntity feature) throws BusinessException;
}
