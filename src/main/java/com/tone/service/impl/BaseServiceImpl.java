package com.tone.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.tone.model.enumm.StatusEnum;
import com.tone.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.tone.exception.BusinessException;
import com.tone.service.BaseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID>{
	
	private final CrudRepository<T, ID> crudRepository;

	public BaseServiceImpl(CrudRepository<T, ID> crudRepository) {
		log.debug("Create Super BaseServiceImpl");
		this.crudRepository = crudRepository;
	}
	
	public Optional<T> findById(ID id) {
		log.debug("BaseServiceImpl::findById");
		return crudRepository.findById(id);		
	}

	public T save(T entity) throws BusinessException{
		log.debug("BaseServiceImpl::save");
		return crudRepository.save(entity);
	}

	public Optional<Set<T>> findAll() {
		log.debug("BaseServiceImpl::findAll");
		Set<T> entities = new HashSet<>();
		crudRepository.findAll().forEach(entities::add);
		return Optional.ofNullable(entities);		
	}

	public void delete(T entity) throws BusinessException{
		log.debug("BaseServiceImpl::delete");
		crudRepository.delete(entity);
	}

	public void deleteById(ID id) throws BusinessException{
		log.debug("BaseServiceImpl::deleteById");
		crudRepository.deleteById(id);
	}

	/**
	 * @param name Entity's name
	 * @return Entity searched
	 *
	 */
	@Override
	public Optional<List<T>> findByName(String name) {
		return ((BaseRepository)crudRepository).findOptionalByNameContainingIgnoreCase(name);
	}

	/**
	 * @return Feature inactived
	 */
	@Override
	public Set<T> findInactive() {
		return findActiveOrInactive(StatusEnum.INACTIVE);
	}

	/**
	 * @return Feature actived
	 */
	@Override
	public Set<T> findActive() {
		return findActiveOrInactive(StatusEnum.ACTIVE);
	}

	/**
	 *
	 * @param status ACTIVE or INACTIVE
	 * @return
	 */
	private Set<T> findActiveOrInactive(StatusEnum status) {
		Set<T> entities = new HashSet<>();
		Optional<List<T>>  list = ((BaseRepository)crudRepository).findAllOptionalByStatus(status);
		if(list.isPresent()){
			entities = new HashSet<T>(list.get());
		}
		return entities;
	}

	/**
	 * @param entity entity that will be actived
	 * @return entity actived
	 * @throws BusinessException
	 */
	public T active(T entity) throws BusinessException{
		return activeOrInactive(entity, StatusEnum.ACTIVE);
	}

	/**
	 * @param entity Entity that will be inactived
	 * @return Entity inactived
	 * @throws BusinessException
	 */
	public T inactive(T entity) throws BusinessException{
		return activeOrInactive(entity, StatusEnum.INACTIVE);
	}
}
