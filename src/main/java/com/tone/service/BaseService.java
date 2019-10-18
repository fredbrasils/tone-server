package com.tone.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.tone.exception.BusinessException;
import com.tone.model.enumm.StatusEnum;

public interface BaseService <T, ID> {

	Optional<T> findById(ID id);
	
	T save(T entity) throws BusinessException;
	
	Optional<Set<T>> findAll();
	
	void delete(T entity) throws BusinessException;
	
	void deleteById(ID id) throws BusinessException;

	Optional<List<T>> findByName(String name);

	Set<T> findActive();

	Set<T> findInactive();

	T active(T entity) throws BusinessException;

	T inactive(T entity) throws BusinessException;

	T activeOrInactive(T entity, StatusEnum status) throws BusinessException;
}
