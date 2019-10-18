package com.tone.service.impl;


import static com.tone.utils.ConstantsMessages.MSG_ERROR_INSTRUMENT_DELETE_BOUND_LUTHIER;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.tone.model.FeatureEntity;
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
import static com.tone.utils.ConstantsMessages.MSG_ERROR_INSTRUMENT_NOTFOUND;

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
		InstrumentEntity instrument3 = InstrumentEntity.builder().name("Instrument 3").status(StatusEnum.ACTIVE).build();
		
		try {
			instrument1 = instrumentService.save(instrument1);
			instrument2 = instrumentService.save(instrument2);
			instrument3 = instrumentService.save(instrument3);
			
			luthier.addInstrument(instrument3);
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
		assertEquals(3, instruments.size());
	}

	@DisplayName("Find instrument by name")
	@Test
	void findByName() {
		Optional<List<InstrumentEntity>> instrument = instrumentService.findByName("Instrument 2");
		assertNotNull(instrument.get());
	}
	
	@DisplayName("Not Found instrument by name")
	@Test
	void notfoundInstrumentByName() {
		Optional<List<InstrumentEntity>>  instrument = instrumentService.findByName("guitar");
		assertTrue(instrument.isEmpty());
	}
	
	@DisplayName("Find instrument active")
	@Test
	void findAllInstrumentActive() {
		
		Set<InstrumentEntity> instrument = instrumentService.findActive();
		assertEquals(2,instrument.size());		
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
		
		InstrumentEntity instrument4 = InstrumentEntity.builder().name("Instrument 4").build();
		try {
			instrumentService.save(instrument4);			
		} catch (BusinessException e) {
			fail();
		}

		Optional<List<InstrumentEntity>>  instrument = instrumentService.findByName("Instrument 4");
		assertTrue(instrument.isPresent());
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

		Optional<List<InstrumentEntity>> instrument = instrumentService.findByName("Instrument 1");
		instrument.get().get(0).setName("new instrument");
		try {
			instrumentService.save(instrument.get().get(0));
		} catch (BusinessException e) {
			fail();
		}

		instrument = instrumentService.findByName("new instrument");
		assertTrue(instrument.isPresent());

	}
	
	@DisplayName("Cant update an instrument with an exists name")
	@Test
	void shouldntUpdateWithExistsName() {

		Optional<List<InstrumentEntity>>  instrument = instrumentService.findByName("Instrument 1");
		instrument.get().get(0).setName("Instrument 2");
		try {
			instrumentService.save(instrument.get().get(0));
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_INSTRUMENT_EXIST);
		}
	}
	
	@DisplayName("Update an instrument with the same name")
	@Test
	void shouldUpdateWithSameName() {

		Optional<List<InstrumentEntity>>  instrument = instrumentService.findByName("Instrument 1");
		instrument.get().get(0).setName("Instrument 1");
		try {
			instrumentService.save(instrument.get().get(0));
		} catch (BusinessException e) {
			fail();
		}
		
		assertTrue(instrumentService.findByName("Instrument 1").isPresent());
	}
	
	@DisplayName("Active an instrument")
	@Test
	void shouldActiveInstrument() {

		Optional<List<InstrumentEntity>>  instrument = instrumentService.findByName("Instrument 2");
		
		try {
			instrumentService.active(instrument.get().get(0));
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.ACTIVE, instrumentService.findByName("Instrument 2").get().get(0).getStatus());
	}
	
	@DisplayName("Inactive an instrument")
	@Test
	void shouldInactiveInstrument() {

		Optional<List<InstrumentEntity>>  instrument = instrumentService.findByName("Instrument 1");
		
		try {
			instrumentService.inactive(instrument.get().get(0));
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.INACTIVE, instrumentService.findByName("Instrument 1").get().get(0).getStatus());
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

		Optional<List<InstrumentEntity>> instrument = instrumentService.findByName("Instrument 1");
		
		try {
			instrumentService.delete(instrument.get().get(0));
		} catch (BusinessException e) {
			fail();
		}
		
		assertTrue(instrumentService.findByName("Instrument 1").isEmpty());
	}
	
	@DisplayName("Cant delete an instrument with luthier")
	@Test
	void shouldntDeleteInstrument() {

		Optional<List<InstrumentEntity>> instrument = instrumentService.findByName("Instrument 3");
		
		try {
			instrumentService.delete(instrument.get().get(0));
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), MSG_ERROR_INSTRUMENT_DELETE_BOUND_LUTHIER);
		}
		
		assertTrue(instrumentService.findByName("Instrument 3").isPresent());
	}
	
	@DisplayName("Cant delete an instrument without id")
	@Test
	void shouldntDeleteInstrumentWithoutId() {		
		
		InstrumentEntity instrument = InstrumentEntity.builder().id(99l).build();
		
		try {
			instrumentService.delete(instrument);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), MSG_ERROR_INSTRUMENT_NOTFOUND);
		}
	}
}
