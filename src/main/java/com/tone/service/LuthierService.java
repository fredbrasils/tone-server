package com.tone.service;

import org.springframework.stereotype.Service;

import com.tone.model.LuthierEntity;

public interface LuthierService extends BaseService<LuthierEntity, Long>{

	LuthierEntity findOptionalByName(String name);
}
