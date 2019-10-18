package com.tone.service.impl;

import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_EXIST;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_NOTFOUND;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_SAVE;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_DELETE_BOUND_LUTHIER;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_DELETE;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tone.exception.BusinessException;
import com.tone.model.SocialNetworkEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.repository.LuthierSocialNetworkRepository;
import com.tone.repository.SocialNetworkRepository;
import com.tone.service.SocialNetworkService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SocialNetworkServiceImpl extends BaseServiceImpl<SocialNetworkEntity,Long> implements SocialNetworkService{
	
	private SocialNetworkRepository socialNetworkRepository;
	private LuthierSocialNetworkRepository luthierSocialNetworkRepository;
	
	public SocialNetworkServiceImpl(SocialNetworkRepository socialNetworkRepository, LuthierSocialNetworkRepository luthierSocialNetworkRepository) {
		super(socialNetworkRepository);
		this.socialNetworkRepository = socialNetworkRepository;
		this.luthierSocialNetworkRepository = luthierSocialNetworkRepository;
	}
	
	/**
	 * @param entity SocialNetwork that will be save
	 * @return SocialNetwork saved
	 * @throws BusinessException 
	 */
	@Override
	public SocialNetworkEntity save(SocialNetworkEntity entity) throws BusinessException {

		log.info("Save socialNetwork");
		Optional<List<SocialNetworkEntity>> instruments = this.findByName(entity.getName());

		if(instruments.isPresent()) {

			Predicate<SocialNetworkEntity> predicate = instrument -> !instrument.getId().equals(entity.getId());

			if(instruments.get().stream().findFirst().filter(predicate).isPresent()) {
				throw new BusinessException(MSG_ERROR_SOCIAL_NETWORK_EXIST);
			}
		}
		return super.save(entity);
	}
	
	/**
	 * @param name SocialNetwork's name
	 * @return SocialNetwork searched
	 * 
	 */
	@Override
	public Optional<List<SocialNetworkEntity>> findByName(String name) {
		return this.socialNetworkRepository.findOptionalByNameContainingIgnoreCase(name);
	}

	/**
	 * @return SocialNetwork inactived
	 */
	@Override
	public Set<SocialNetworkEntity> findInactive() {
		return this.socialNetworkRepository.findAllOptionalByStatus(StatusEnum.INACTIVE).orElse(null).stream().collect(Collectors.toSet());
	}
	
	/**
	 * @return SocialNetwork actived
	 */
	@Override
	public Set<SocialNetworkEntity> findActive() {
		return this.socialNetworkRepository.findAllOptionalByStatus(StatusEnum.ACTIVE).orElse(null).stream().collect(Collectors.toSet());
	}

	/**
	 * @param entity SocialNetwork that will be actived
	 * @return SocialNetwork actived
	 * @throws BusinessException 
	 */
	public SocialNetworkEntity active(SocialNetworkEntity entity) throws BusinessException{
		return activeOrInactive(entity, StatusEnum.ACTIVE);
	}
	
	/**
	 * @param entity SocialNetwork that will be inactived
	 * @return SocialNetwork inactived
	 * @throws BusinessException 
	 */
	public SocialNetworkEntity inactive(SocialNetworkEntity entity) throws BusinessException{
		return activeOrInactive(entity, StatusEnum.INACTIVE);
	}
	
	/**
	 * @param socialNetwork SocialNetwork that will be actived or inactived
	 * @param status The new SocialNetwork's status 
	 * @return SocialNetwork actived or inactived
	 * @throws BusinessException 
	 */
	public SocialNetworkEntity activeOrInactive(SocialNetworkEntity socialNetwork, StatusEnum status) throws BusinessException{
		
		Optional<SocialNetworkEntity> socialNetworkSaved = findById(socialNetwork.getId());
		SocialNetworkEntity instr = null;
		
		if(socialNetworkSaved.isPresent()) {
			instr = socialNetworkSaved.get();
			instr.setStatus(status);
			try {
				instr = save(instr);
			} catch (BusinessException e) {
				throw new BusinessException(MSG_ERROR_SOCIAL_NETWORK_SAVE);
			}
		}else {
			throw new BusinessException(MSG_ERROR_SOCIAL_NETWORK_NOTFOUND);
		}		
		
		return instr;
	}
	
	/**
	 * @param socialNetwork SocialNetwork that will be deleted
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public void delete(SocialNetworkEntity socialNetwork) throws BusinessException {
		
		Optional<SocialNetworkEntity> socialNetworkSaved = findById(socialNetwork.getId());
		
		if(socialNetworkSaved.isPresent()) {
		
			if(!luthierSocialNetworkRepository.findAllOptionalBySocialNetwork(socialNetwork).isEmpty()) {
				throw new BusinessException(MSG_ERROR_SOCIAL_NETWORK_DELETE_BOUND_LUTHIER);
			}
			
			try {
				super.delete(socialNetworkSaved.get());
			} catch (BusinessException e) {
				throw new BusinessException(MSG_ERROR_SOCIAL_NETWORK_DELETE);
			}
			
		}else {
			throw new BusinessException(MSG_ERROR_SOCIAL_NETWORK_NOTFOUND);
		}
		
	}
}
