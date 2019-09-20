package com.tone.service.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.tone.exception.BusinessException;
import com.tone.model.LuthierEntity;
import com.tone.model.LuthierSocialNetworkEntity;
import com.tone.repository.LuthierRepository;
import com.tone.repository.LuthierSocialNetworkRepository;
import com.tone.service.LuthierService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LuthierServiceImpl extends BaseServiceImpl<LuthierEntity,Long> implements LuthierService{
	
	private LuthierRepository luthierRepository;	
	private LuthierSocialNetworkRepository luthierSocialNetworkRepository;
	
	public LuthierServiceImpl(LuthierRepository luthierRepository, LuthierSocialNetworkRepository luthierSocialNetworkRepository) {
		super(luthierRepository);
		this.luthierRepository = luthierRepository;
		this.luthierSocialNetworkRepository = luthierSocialNetworkRepository;
	}

	@Override
	public LuthierEntity findOptionalByName(String name) {
		return this.luthierRepository.findOptionalByName(name).orElse(null);
	}	

	@Override
	public LuthierEntity save(LuthierEntity entity) throws BusinessException {

		Set<LuthierSocialNetworkEntity> listSocial = entity.getSocialNetworks();
		entity.setSocialNetworks(null);

		entity = super.save(entity);
		
		if(listSocial != null && !listSocial.isEmpty()) {
			for(LuthierSocialNetworkEntity social : listSocial) {
				social.setLuthier(entity);
				social = this.luthierSocialNetworkRepository.save(social);
				entity.addSocialNetwork(social);
			}
		}
		
		return entity;
	}
	
	@Override
	public void delete(LuthierEntity entity) throws BusinessException {

		Optional<LuthierEntity> luthier = this.findById(entity.getId());
		
		if(luthier.isPresent()) {
			
			for(LuthierSocialNetworkEntity social : luthier.get().getSocialNetworks()) {
				this.luthierSocialNetworkRepository.delete(social);				
			}
		}
		
		super.delete(entity);
	}
}
