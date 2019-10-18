package com.tone.service.impl;

import com.tone.exception.BusinessException;
import com.tone.model.FeatureEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.repository.FeatureRepository;
import com.tone.service.FeatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tone.utils.ConstantsMessages.*;

@Slf4j
@Service
public class FeatureServiceImpl extends BaseServiceImpl<FeatureEntity,Long> implements FeatureService{
	
	private static final Integer ONE = 1;
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
		Optional<List<FeatureEntity>> features = findByName(entity.getName());		
		
		if(features.isPresent()) {
			
			Predicate<FeatureEntity> predicate = f -> !f.getId().equals(entity.getId())
					&& ( (f.getRoot() != null && entity.getRoot() != null 
					&& f.getRoot().getId().equals(entity.getRoot().getId()))
							|| ( f.getRoot() == null && entity.getRoot() == null));
			
			if(features.get().stream().findFirst().filter(predicate).isPresent()) {
				throw new BusinessException(MSG_ERROR_FEATURE_EXIST);				
			}
		}
		
		if(entity.getPosition() == null) {
			entity.setPosition(getOrder(entity));
		}
		
		return super.save(entity);
	}
	
	/**
	 * Define Feature's order
	 * @param entity 
	 */
	private Integer getOrder(FeatureEntity entity) throws BusinessException{
		
		log.info("Define feature's order");
		Integer order = ONE;
		
		// if it is a feature root
		if(entity.getRoot() == null) {
			
			Optional<Set<FeatureEntity>> features = findAll();
			return features.isPresent() ? features.get().size() + order : order;
			
		}else { // if it is a feature child
			
			Optional<FeatureEntity> features = findById(entity.getRoot().getId());		
			
			if(features.isEmpty()) {
				throw new BusinessException(MSG_ERROR_FEATURE_ROOT_NOTFOUND);
			}
			
			return features.get().getFeatures() != null ? features.get().getFeatures().size() + order : order;			
		}
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
	 * @param feature Feature that will be actived or inactived
	 * @param status The new Feature's status 
	 * @return Feature actived or inactived
	 * @throws BusinessException 
	 */
	private FeatureEntity activeOrInactive(FeatureEntity feature, StatusEnum status) throws BusinessException{
		
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
	 * @param feature Feature that will be deleted
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

	/**
	 *
	 * @return all features
	 */
	@Override
	public Optional<Set<FeatureEntity>> findAll() {
		Set<FeatureEntity> entities = new HashSet<>();
		this.featureRepository.findAll().forEach(entities::add);
		return Optional.ofNullable(entities);
	}

	/**
	 * @param feature Feature that will be update order
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public void changeOrder(FeatureEntity feature) throws BusinessException{

		Optional<FeatureEntity> featureSaved = findById(feature.getId());

		if(featureSaved.isPresent()) {

			List<FeatureEntity> listFeatures;
			Integer oldPosition = featureSaved.get().getPosition();
			Integer newPosition = feature.getPosition();

			if(featureSaved.get().getRoot() == null){
				listFeatures = new ArrayList<>(findAll().get());
			}else{
				listFeatures = new ArrayList<>(featureSaved.get().getFeatures());;
			}

			// down
			if(oldPosition.compareTo(newPosition) < 0){

				listFeatures = listFeatures.stream()
						.sorted(Comparator.comparingInt(FeatureEntity::getPosition).reversed())
						.collect(Collectors.toList());

			}else{ // up

				listFeatures = listFeatures.stream()
						.sorted(Comparator.comparingInt(FeatureEntity::getPosition))
						.collect(Collectors.toList());
			}

			final boolean[] found = new boolean[1];

			int bound = listFeatures.size();
			for (int idx = 0; idx < bound; idx++) {
				if ((listFeatures.get(idx).getPosition().equals(newPosition)) || (found[0] && !listFeatures.get(idx).getPosition().equals(oldPosition))) {
					found[0] = true;
					listFeatures.get(idx).setPosition(listFeatures.get(idx + 1).getPosition());
					try {
						save(listFeatures.get(idx));
					} catch (BusinessException e) {
						throw new BusinessException(MSG_ERROR_FEATURE_SAVE);
					}
				} else if (listFeatures.get(idx).getPosition().equals(oldPosition)) {
					found[0] = false;
					listFeatures.get(idx).setPosition(newPosition);
					try {
						save(listFeatures.get(idx));
					} catch (BusinessException e) {
						throw new BusinessException(MSG_ERROR_FEATURE_SAVE);
					}
				}
			}

		}else {
			throw new BusinessException(MSG_ERROR_FEATURE_NOTFOUND);
		}

	}

}
