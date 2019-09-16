package com.tone.service.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tone.exception.BusinessException;
import com.tone.model.LuthierEntity;
import com.tone.service.LuthierService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LuthierServiceImplTest {

	@Autowired
	LuthierService luthierService;

	@BeforeEach
	void setUp() {
		LuthierEntity luthier1 = LuthierEntity.builder().name("Luthier 1").build();
		LuthierEntity luthier2 = LuthierEntity.builder().name("Luthier 2").build();
		try {
			luthierService.save(luthier1);
			luthierService.save(luthier2);
		} catch (BusinessException e) {
		}
	}
	
	@AfterEach
	void close() {
		
		Set<LuthierEntity> luthiers = luthierService.findAll().orElse(null);
		luthiers.stream().forEach(lut -> {
			try {
				luthierService.delete(lut);
			} catch (BusinessException e) {
			}
		});
	}
	
	@DisplayName("Find all luthier")
	@Test
	void findAll() {
		
		Set<LuthierEntity> luthiers = luthierService.findAll().orElse(null);

		assertNotNull(luthiers);
		assertEquals(2, luthiers.size());
	}

	@DisplayName("Find luthier by name")
	@Test
	void findByName() {
		
		LuthierEntity luthier = luthierService.findOptionalByName("Luthier 2");
		assertNotNull(luthier);		
	}
	
	@DisplayName("Save luthier")
	@Test
	void save() {
		
		LuthierEntity luthier3 = LuthierEntity.builder().name("Luthier 3").build();
		try {
			luthierService.save(luthier3);
		} catch (BusinessException e) {
		}
		LuthierEntity luthier = luthierService.findOptionalByName("Luthier 3");
		assertNotNull(luthier);		
	}
	
	@DisplayName("Update luthier")
	@Test
	void update() {		
		
		LuthierEntity luthier = luthierService.findOptionalByName("Luthier 1");
		luthier.setName("new luthier");
		try {
			luthierService.save(luthier);
		} catch (BusinessException e) {
		}
		
		assertNotNull(luthierService.findOptionalByName("new luthier"));		
	}
	
}
