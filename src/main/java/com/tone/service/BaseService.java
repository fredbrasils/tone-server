package com.tone.service;

import java.util.Optional;
import java.util.Set;

import com.tone.exception.BusinessException;

public interface BaseService <T, ID> {

	Optional<T> findById(ID id);
	
	T save(T entity) throws BusinessException;
	
	Optional<Set<T>> findAll();
	
	void delete(T entity) throws BusinessException;
	
	void deleteById(ID id) throws BusinessException;
}
