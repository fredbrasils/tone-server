package com.tone.service;

import java.util.Set;

import com.tone.exception.BusinessException;

public interface BaseService <T, ID> {

	T findById(ID id);
	
	T save(T entity) throws BusinessException;
	
	Set<T> findAll();
	
	void delete(T entity) throws BusinessException;
	
	void deleteById(ID id) throws BusinessException;
}
