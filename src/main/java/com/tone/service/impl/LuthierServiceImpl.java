package com.tone.service.impl;

import com.tone.exception.BusinessException;
import com.tone.model.LuthierEntity;
import com.tone.model.LuthierEntity;
import com.tone.model.LuthierFeatureEntity;
import com.tone.model.LuthierSocialNetworkEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.repository.LuthierFeatureRepository;
import com.tone.repository.LuthierRepository;
import com.tone.repository.LuthierSocialNetworkRepository;
import com.tone.service.LuthierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.tone.utils.ConstantsMessages.*;

@Slf4j
@Service
public class LuthierServiceImpl extends BaseServiceImpl<LuthierEntity,Long> implements LuthierService{
	
	private LuthierRepository luthierRepository;	
	private LuthierSocialNetworkRepository luthierSocialNetworkRepository;
	private LuthierFeatureRepository luthierFeatureRepository;
	
	public LuthierServiceImpl(LuthierRepository luthierRepository, LuthierSocialNetworkRepository luthierSocialNetworkRepository,
							  LuthierFeatureRepository luthierFeatureRepository) {
		super(luthierRepository);
		this.luthierRepository = luthierRepository;
		this.luthierSocialNetworkRepository = luthierSocialNetworkRepository;
		this.luthierFeatureRepository = luthierFeatureRepository;
	}

	@Override
	public LuthierEntity findOptionalByEmail(String email) {
		return this.luthierRepository.findOptionalByEmail(email).orElse(null);
	}

	/**
	 * Save a luthier
	 * @param entity Luthier will be save
	 * @return LUthier saved (with id)
	 * @throws BusinessException
	 */
	@Override
	public LuthierEntity save(LuthierEntity entity) throws BusinessException {

		Optional<LuthierEntity> luthier = this.luthierRepository.findOptionalByEmail(entity.getEmail());

		if(luthier.isPresent() && !luthier.get().getId().equals(entity.getId())) {
			throw new BusinessException(MSG_ERROR_LUTHIER_EXIST);
		}else if(luthier.isPresent()){

			if(luthier.get().getSocialNetworks() != null && !luthier.get().getSocialNetworks().isEmpty()){
				Optional<List<LuthierSocialNetworkEntity>> list = luthierSocialNetworkRepository.findAllOptionalByLuthier(luthier.get());
				list.ifPresent( listls -> listls.forEach(ls -> luthierSocialNetworkRepository.delete(ls)));
			}

			if(luthier.get().getFeatures() != null && !luthier.get().getFeatures().isEmpty()){
				Optional<List<LuthierFeatureEntity>> list = luthierFeatureRepository.findAllOptionalByLuthier(luthier.get());
				list.ifPresent( listlf -> listlf.forEach(lf -> luthierFeatureRepository.delete(lf)));
			}
		}

		Set<LuthierSocialNetworkEntity> listSocial = entity.getSocialNetworks();
		Set<LuthierFeatureEntity> features = entity.getFeatures();
		entity.setSocialNetworks(null);
		entity.setFeatures(null);

		try {
			entity = super.save(entity);
		} catch (BusinessException e) {
			throw new BusinessException(MSG_ERROR_LUTHIER_SAVE);
		}

		if(listSocial != null && !listSocial.isEmpty()) {
			for(LuthierSocialNetworkEntity social : listSocial) {
				social.setLuthier(entity);
				social = this.luthierSocialNetworkRepository.save(social);
				entity.addSocialNetwork(social);
			}
		}

		if(features != null && !features.isEmpty()) {
			for(LuthierFeatureEntity feature : features) {
				feature = this.luthierFeatureRepository.save(new LuthierFeatureEntity(entity, feature.getFeature(), feature.getValue()));
				entity.addFeature(feature);
			}
		}
		
		return entity;
	}
	
	@Override
	public void delete(LuthierEntity entity) throws BusinessException {

		Optional<LuthierEntity> luthier = this.findById(entity.getId());
		
		if(luthier.isPresent()) {

			if(luthier.get().getSocialNetworks() != null) {
				for (LuthierSocialNetworkEntity social : luthier.get().getSocialNetworks()) {
					this.luthierSocialNetworkRepository.delete(social);
				}
			}
		}
		
		super.delete(entity);
	}

	@Override
	public LuthierEntity activeOrInactive(LuthierEntity luthier, StatusEnum status) throws BusinessException {

		Optional<LuthierEntity> luthierSaved = findById(luthier.getId());
		LuthierEntity lut = null;

		if(luthierSaved.isPresent()) {
			lut = luthierSaved.get();
			lut.setStatus(status);
			try {
				lut = save(lut);
			} catch (BusinessException e) {
				throw new BusinessException(MSG_ERROR_LUTHIER_SAVE);
			}
		}else {
			throw new BusinessException(MSG_ERROR_LUTHIER_NOTFOUND);
		}

		return lut;
	}

}
