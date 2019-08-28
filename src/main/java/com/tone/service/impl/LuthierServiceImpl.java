package com.tone.service.impl;

import org.springframework.stereotype.Service;

import com.tone.model.LuthierEntity;
import com.tone.repository.LuthierRepository;
import com.tone.service.LuthierService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LuthierServiceImpl extends BaseServiceImpl<LuthierEntity,Long> implements LuthierService{
	
	private LuthierRepository luthierRepository;	
	
	public LuthierServiceImpl(LuthierRepository luthierRepository) {
		super(luthierRepository);
		this.luthierRepository = luthierRepository;
	}

	@Override
	public LuthierEntity findOptionalByName(String name) {
		return this.luthierRepository.findOptionalByName(name).orElse(null);
	}	

	
}
