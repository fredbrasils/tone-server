package com.tone.service.impl;


import static org.junit.Assert.fail;
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
import com.tone.model.InstrumentEntity;
import com.tone.service.InstrumentService;
import com.tone.utils.ConstantsMessages;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InstrumentServiceImplTest {

	@Autowired
	InstrumentService instrumentService;

	@BeforeEach
	void setUp() {
		InstrumentEntity instrument1 = InstrumentEntity.builder().name("Instrument 1").build();
		InstrumentEntity instrument2 = InstrumentEntity.builder().name("Instrument 2").build();
		try {
			instrumentService.save(instrument1);
			instrumentService.save(instrument2);			
		} catch (Exception e) {
		}
	}
	
	@AfterEach
	void close() {
		
		Set<InstrumentEntity> instruments = instrumentService.findAll();
		instruments.stream().forEach(lut -> {
			try {
				instrumentService.delete(lut);
			} catch (BusinessException e) {
			}
		});
	}
	
	/*
	@DisplayName("Find all instrument")
	@Test
	void findAll() {
		
		Set<InstrumentEntity> instruments = instrumentService.findAll();

		assertNotNull(instruments);
		assertEquals(2, instruments.size());
	}

	@DisplayName("Find instrument by name")
	@Test
	void findByName() {
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 2");
		assertNotNull(instrument);		
	}
	*/
	
	@DisplayName("Save instrument")
	@Test
	void save() {
		
		InstrumentEntity instrument3 = InstrumentEntity.builder().name("Instrument 3").build();
		try {
			instrumentService.save(instrument3);			
		} catch (Exception e) {
			fail();
		}
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 3");
		assertNotNull(instrument);		
	}
	
	@DisplayName("Cant save an instrument with an exists name")
	@Test
	void cantSave() {
		
		InstrumentEntity instrument2 = InstrumentEntity.builder().name("Instrument 2").build();
		try {
			instrumentService.save(instrument2);
			fail("Fail: Saved an instrument with an exists name");
		}catch (Exception e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_INSTRUMENT_EXIST);
		}
	}
	
	/*
	@DisplayName("Update instrument")
	@Test
	void update() {		
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 1");
		instrument.setName("new instrument");
		instrumentService.save(instrument);
		
		assertNotNull(instrumentService.findOptionalByName("new instrument"));		
	}
	*/
	
}
