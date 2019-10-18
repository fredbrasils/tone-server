package com.tone.service.impl;

import com.tone.model.enumm.StatusEnum;
import org.springframework.stereotype.Service;

import com.tone.exception.BusinessException;
import com.tone.model.LuthierFeatureEntity;
import com.tone.repository.LuthierFeatureRepository;
import com.tone.service.LuthierFeatureService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LuthierFeatureServiceImpl extends BaseServiceImpl<LuthierFeatureEntity,Long> implements LuthierFeatureService{
	
	private LuthierFeatureRepository luthierFeatureRepository;
	
	public LuthierFeatureServiceImpl(LuthierFeatureRepository luthierFeatureRepository) {
		super(luthierFeatureRepository);
	}
	
	/**
	 * @param entity LuthierFeature that will be save
	 * @return LuthierFeature saved
	 * @throws BusinessException 
	 */
	@Override
	public LuthierFeatureEntity save(LuthierFeatureEntity entity) throws BusinessException {
		log.info("Save luthierFeature");
		return super.save(entity);			
	}

	@Override
	public LuthierFeatureEntity activeOrInactive(LuthierFeatureEntity entity, StatusEnum status) throws BusinessException {
		return null;
	}

}
