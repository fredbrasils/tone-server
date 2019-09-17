package com.tone.service.impl;


import static org.junit.Assert.assertNull;
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
import com.tone.model.LuthierEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.service.InstrumentService;
import com.tone.service.LuthierService;
import com.tone.utils.ConstantsMessages;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InstrumentServiceImplTest {

	@Autowired
	InstrumentService instrumentService;

	@Autowired
	LuthierService luthierService;
	
	@BeforeEach
	void setUp() {
		
		LuthierEntity luthier = LuthierEntity.builder().name("Luthier 1").email("luthier@contact.com").build();
		
		InstrumentEntity instrument1 = InstrumentEntity.builder().name("Instrument 1").status(StatusEnum.ACTIVE).build();
		InstrumentEntity instrument2 = InstrumentEntity.builder().name("Instrument 2").status(StatusEnum.INACTIVE).build();
		
		try {
			instrument1 = instrumentService.save(instrument1);
			instrument2 = instrumentService.save(instrument2);
			
			luthier.addInstrument(instrument1);
			luthierService.save(luthier);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@AfterEach
	void close() {
		
		Set<InstrumentEntity> instruments = instrumentService.findAll().orElse(null);
		instruments.stream().forEach(lut -> {
			try {
				instrumentService.delete(lut);
			} catch (BusinessException e) {
			}
		});
	}
	
	@DisplayName("Find all instrument")
	@Test
	void findAll() {
		
		Set<InstrumentEntity> instruments = instrumentService.findAll().orElse(null);
		
		assertNotNull(instruments);
		assertEquals(2, instruments.size());
	}

	@DisplayName("Find instrument by name")
	@Test
	void findByName() {
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 2");
		assertNotNull(instrument);		
	}
	
	@DisplayName("Not Found instrument by name")
	@Test
	void notfoundInstrumentByName() {
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("guitar");
		assertNull(instrument);		
	}
	
	@DisplayName("Find instrument active")
	@Test
	void findAllInstrumentActive() {
		
		Set<InstrumentEntity> instrument = instrumentService.findActive();
		assertEquals(1,instrument.size());		
	}
	
	@DisplayName("Find instrument inactive")
	@Test
	void findAllInstrumentInactive() {
		
		Set<InstrumentEntity> instrument = instrumentService.findInactive();
		assertEquals(1,instrument.size());		
	}
	
	@DisplayName("Save instrument")
	@Test
	void save() {
		
		InstrumentEntity instrument3 = InstrumentEntity.builder().name("Instrument 3").build();
		try {
			instrumentService.save(instrument3);			
		} catch (BusinessException e) {
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
		}catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_INSTRUMENT_EXIST);
		}
	}
	
	@DisplayName("Update instrument")
	@Test
	void update() {		
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 1");
		instrument.setName("new instrument");
		try {
			instrumentService.save(instrument);
		} catch (BusinessException e) {
			fail();
		}
		
		assertNotNull(instrumentService.findOptionalByName("new instrument"));		
	}
	
	@DisplayName("Cant update an instrument with an exists name")
	@Test
	void shouldntUpdateWithExistsName() {		
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 1");
		instrument.setName("Instrument 2");
		try {
			instrumentService.save(instrument);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_INSTRUMENT_EXIST);
		}
	}
	
	@DisplayName("Update an instrument with the same name")
	@Test
	void shouldUpdateWithSameName() {		
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 1");
		instrument.setName("Instrument 1");
		try {
			instrumentService.save(instrument);
		} catch (BusinessException e) {
			fail();
		}
		
		assertNotNull(instrumentService.findOptionalByName("Instrument 1"));
	}
	
	@DisplayName("Active an instrument")
	@Test
	void shouldActiveInstrument() {		
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 2");
		
		try {
			instrumentService.active(instrument);
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.ACTIVE, instrumentService.findOptionalByName("Instrument 2").getStatus());
	}
	
	@DisplayName("Inactive an instrument")
	@Test
	void shouldInactiveInstrument() {		
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 1");
		
		try {
			instrumentService.inactive(instrument);
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.INACTIVE, instrumentService.findOptionalByName("Instrument 1").getStatus());
	}
	
	@DisplayName("Dont actived an instrument that doesn exist")
	@Test
	void shouldntActiveInstrumentThatDoesntExist() {		
		
		InstrumentEntity instrument = InstrumentEntity.builder().id(99l).build();
		
		try {
			instrumentService.active(instrument);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_INSTRUMENT_NOTFOUND);
		}
	}
	
	@DisplayName("Dont inactived an instrument that doesn exist")
	@Test
	void shouldntInactiveInstrumentThatDoesntExist() {		
		
		InstrumentEntity instrument = InstrumentEntity.builder().id(99l).build();
		
		try {
			instrumentService.inactive(instrument);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_INSTRUMENT_NOTFOUND);
		}		
	}
	
	@DisplayName("Delete an instrument")
	@Test
	void shouldDeleteInstrument() {		
		
		InstrumentEntity instrument = instrumentService.findOptionalByName("Instrument 2");
		
		try {
			instrumentService.delete(instrument);
		} catch (BusinessException e) {
			fail();
		}
		
		assertNull(instrumentService.findOptionalByName("Instrument 2"));
	}
}
