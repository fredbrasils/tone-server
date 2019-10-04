package com.tone.service.impl;

import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_DELETE;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_NOTFOUND;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_SAVE;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_EXIST;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_DELETE_BOUND_LUTHIER;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_DELETE_WITH_FEATURE_CHILD;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tone.exception.BusinessException;
import com.tone.model.FeatureEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.repository.FeatureRepository;
import com.tone.service.FeatureService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FeatureServiceImpl extends BaseServiceImpl<FeatureEntity,Long> implements FeatureService{
	
	private FeatureRepository featureRepository;
	
	public FeatureServiceImpl(FeatureRepository featureRepository) {
		super(featureRepository);
		this.featureRepository = featureRepository;
	}
	
	/**
	 * @param entity Feature that will be save
	 * @return Feature saved
	 * @throws BusinessException 
	 */
	@Override
	public FeatureEntity save(FeatureEntity entity) throws BusinessException {

		log.info("Save feature");
		Optional<List<FeatureEntity>> features = this.findByName(entity.getName());		
		
		if(features.isPresent()) {
			
			Predicate<FeatureEntity> predicate = f -> f.getId() != entity.getId() 
					&& ( (f.getRoot() != null && entity.getRoot() != null 
					&& f.getRoot().getId() == entity.getRoot().getId())
							|| f.getRoot() == entity.getRoot());		
			
			if(features.get().stream().findFirst().filter(predicate).isPresent()) {
				throw new BusinessException(MSG_ERROR_FEATURE_EXIST);				
			}
		}
		
		/*
		if(feature != null && feature.getId() != entity.getId() 
				&& ( (feature.getRoot() != null && entity.getRoot() != null 
					   && feature.getRoot().getId() == entity.getRoot().getId())
				   || feature.getRoot() == entity.getRoot())) {
			throw new BusinessException(MSG_ERROR_FEATURE_EXIST);
		}
		*/
		
		return super.save(entity);
	}
	
	/**
	 * @param name Feature's name
	 * @return Feature searched
	 * 
	 */
	@Override
	public Optional<List<FeatureEntity>> findByName(String name) {
		return this.featureRepository.findOptionalByNameContainingIgnoreCase(name);
	}	

	/**
	 * @return Feature inactived
	 */
	@Override
	public Set<FeatureEntity> findInactive() {
		return this.featureRepository.findAllOptionalByStatus(StatusEnum.INACTIVE).orElse(null).stream().collect(Collectors.toSet());
	}
	
	/**
	 * @return Feature actived
	 */
	@Override
	public Set<FeatureEntity> findActive() {
		return this.featureRepository.findAllOptionalByStatus(StatusEnum.ACTIVE).orElse(null).stream().collect(Collectors.toSet());
	}

	/**
	 * @param entity Feature that will be actived
	 * @return Feature actived
	 * @throws BusinessException 
	 */
	public FeatureEntity active(FeatureEntity entity) throws BusinessException{
		return activeOrInactive(entity, StatusEnum.ACTIVE);
	}
	
	/**
	 * @param entity Feature that will be inactived
	 * @return Feature inactived
	 * @throws BusinessException 
	 */
	public FeatureEntity inactive(FeatureEntity entity) throws BusinessException{
		return activeOrInactive(entity, StatusEnum.INACTIVE);
	}
	
	/**
	 * @param entity Feature that will be actived or inactived
	 * @param status The new Feature's status 
	 * @return Feature actived or inactived
	 * @throws BusinessException 
	 */
	public FeatureEntity activeOrInactive(FeatureEntity feature, StatusEnum status) throws BusinessException{
		
		Optional<FeatureEntity> featureSaved = findById(feature.getId());
		FeatureEntity instr = null;
		
		if(featureSaved.isPresent()) {
			instr = featureSaved.get();
			instr.setStatus(status);
			try {
				instr = save(instr);
				
				if(instr.getFeatures() != null && !instr.getFeatures().isEmpty()) {
					for(FeatureEntity feat : instr.getFeatures()) {
						feat.setStatus(status);
						feat = save(feat);
					}
				}
				
			} catch (BusinessException e) {
				throw new BusinessException(MSG_ERROR_FEATURE_SAVE);
			}
		}else {
			throw new BusinessException(MSG_ERROR_FEATURE_NOTFOUND);
		}		
		
		return instr;
	}
	
	/**
	 * @param entity Feature that will be deleted
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public void delete(FeatureEntity feature) throws BusinessException {
		
		Optional<FeatureEntity> featureSaved = findById(feature.getId());
		
		if(featureSaved.isPresent()) {
			
			if(featureSaved.get().getFeatures() != null && !featureSaved.get().getFeatures().isEmpty()) {
				throw new BusinessException(MSG_ERROR_FEATURE_DELETE_WITH_FEATURE_CHILD);
			}
			
			if(featureSaved.get().getLuthiers() != null && !featureSaved.get().getLuthiers().isEmpty()) {
				throw new BusinessException(MSG_ERROR_FEATURE_DELETE_BOUND_LUTHIER);
			}
			
			try {
				super.delete(featureSaved.get());
			} catch (BusinessException e) {
				throw new BusinessException(MSG_ERROR_FEATURE_DELETE);
			}
			
		}else {
			throw new BusinessException(MSG_ERROR_FEATURE_NOTFOUND);
		}
		
	}
	
	@Override
	public Optional<Set<FeatureEntity>> findAll() {
		Set<FeatureEntity> entities = new HashSet<>();
		this.featureRepository.findAll().forEach(entities::add);
		return Optional.ofNullable(entities);		
		
	}
}
