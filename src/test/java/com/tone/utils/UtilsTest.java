package com.tone.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tone.model.InstrumentEntity;
import com.tone.model.LuthierEntity;
import com.tone.model.payload.Instrument;
import com.tone.model.payload.Luthier;

class UtilsTest {

	
    @DisplayName(value="Convert luthier to entity without validate entities object.") 
    @Test
    void convertLuthierToEntityWithoutEntityObjects() throws Exception {
		
    	Luthier l = Luthier.builder().id(1l).name("Luthier 1").build();
    	
    	LuthierEntity entity = (LuthierEntity) Utils.convertFromTo(l, LuthierEntity.class);
    	
    	assertEquals(l.getId(), entity.getId());
    	assertEquals(l.getName(), entity.getName());
    }
    
 
    @DisplayName(value="Convert luthier to entity") 
    @Test
    void convertLuthierToEntity() throws Exception {
		
    	Instrument in1 = Instrument.builder().name("guitar").build();
    	Instrument in2 = Instrument.builder().name("drum").build();
    	
    	Set<Instrument> instruments = new HashSet<Instrument>();
    	instruments.add(in1);
    	instruments.add(in2);
    	
    	Luthier l = Luthier.builder().id(1l).name("Luthier 1").instruments(instruments).build();
    	in1.setLuthier(l);
    	
    	LuthierEntity entity = (LuthierEntity) Utils.convertFromTo(l, LuthierEntity.class);  	
    	
    	assertEquals(l.getId(), entity.getId());
    	assertEquals(l.getName(), entity.getName());
    	assertEquals(entity.getInstruments().size(), 2);
    	
    	
    	Predicate<InstrumentEntity> predicate = i -> i.getLuthier() != null;
    	
    	assertNotNull(entity.getInstruments().stream().findFirst().filter(predicate).get());
    	
    }
    
    
    @DisplayName(value="Convert list of luthiers to entity.") 
    @Test
    void convertListOfLuthiersToEntity() throws Exception {
		
    	Luthier l1 = Luthier.builder().id(1l).name("Luthier 1").build();
    	Luthier l2 = Luthier.builder().id(2l).name("Luthier 2").build();
    	Luthier l3 = Luthier.builder().id(3l).name("Luthier 3").build();
    	
    	Set list = new HashSet<>();
    	list.add(l1);list.add(l2);list.add(l3);
    	
    	Set lEntities = Utils.convertFromTo(list, LuthierEntity.class);
    	
    	lEntities.stream().forEach(lut -> {
    		assertNotNull( ((LuthierEntity)lut).getId());
    		assertNotNull( ((LuthierEntity)lut).getName());
    	});
    	
    }
    

	/*
	@DisplayName(value="Convert list of luthiers to entity.") 
    @Test
    void convertListOfLuthiersToEntity() throws Exception {
		
    	Utils.test();
    	
    	assertTrue(true);
    	
    }
	*/
	
}
