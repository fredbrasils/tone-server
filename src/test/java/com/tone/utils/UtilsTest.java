package com.tone.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tone.model.LuthierEntity;
import com.tone.model.payload.Luthier;

class UtilsTest {

    @DisplayName(value="Convert luthier to entity.") 
    @Test
    void convertLuthierToEntity() throws Exception {
		
    	Luthier l = Luthier.builder().id(1l).name("Luthier 1").build();
    	
    	LuthierEntity entity = (LuthierEntity) Utils.convertFromTo(l, LuthierEntity.class);
    	
    	assertEquals(l.getId(), entity.getId());
    	assertEquals(l.getName(), entity.getName());
    }
    
    @DisplayName(value="Convert list of luthiers to entity.") 
    @Test
    void convertListOfLuthiersToEntity() throws Exception {
		
    	Luthier l1 = Luthier.builder().id(1l).name("Luthier 1").build();
    	Luthier l2 = Luthier.builder().id(2l).name("Luthier 2").build();
    	Luthier l3 = Luthier.builder().id(3l).name("Luthier 3").build();
    	
    	List list = new ArrayList<>();
    	list.add(l1);list.add(l2);list.add(l3);
    	
    	List lEntities = Utils.convertFromTo(list, LuthierEntity.class);
    	
    	lEntities.stream().forEach(lut -> {
    		assertNotNull( ((LuthierEntity)lut).getId());
    		assertNotNull( ((LuthierEntity)lut).getName());
    	});
    	
    }

}
