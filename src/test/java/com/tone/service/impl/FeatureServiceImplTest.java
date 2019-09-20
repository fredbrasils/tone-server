package com.tone.service.impl;


import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Comparator;
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
import com.tone.model.FeatureEntity;
import com.tone.model.LuthierEntity;
import com.tone.model.enumm.FeatureTypeEnum;
import com.tone.model.enumm.StatusEnum;
import com.tone.service.FeatureService;
import com.tone.service.LuthierService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FeatureServiceImplTest {

	@Autowired
	FeatureService featureService;

	@Autowired
	LuthierService luthierService;
	
	@BeforeEach
	void setUp() {
		
		LuthierEntity luthier = LuthierEntity.builder().name("Luthier 1").email("luthier@contact.com").build();
		
		FeatureEntity feature1 = FeatureEntity.builder().name("style").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.BOOLEAN).build();
		FeatureEntity feature2 = FeatureEntity.builder().name("body materials").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.STRING).build();
		FeatureEntity feature3 = FeatureEntity.builder().name("price").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.NUMBER).build();
		FeatureEntity feature4 = FeatureEntity.builder().name("0-100").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.NUMBER).build();
		
		try {
			feature1 = featureService.save(feature1);
			feature2 = featureService.save(feature2);
			feature3 = featureService.save(feature3);

			feature4.setRoot(feature3);
			feature4 = featureService.save(feature4);
			
			/*
			luthier.addFeature(feature2, "facebook.com/luthier1");
			luthier = luthierService.save(luthier);		
			*/
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@AfterEach
	void close() {
		/*
		Set<LuthierEntity> luthiers = luthierService.findAll().orElse(null);
		luthiers.stream().forEach(lut -> {
			try {
				luthierService.delete(lut);
			} catch (BusinessException e) {
			}
		});
		*/
		
		Set<FeatureEntity> features = featureService.findAll().orElse(null);		
		features.stream().forEach(fet -> {
			try {
				for(FeatureEntity f : fet.getFeatures()) {
					featureService.delete(f);
				}
				featureService.delete(fet);
			} catch (BusinessException e) {
			}
		});
	}
	/*
	@DisplayName("Find all feature")
	@Test
	void findAll() {
		
		Set<FeatureEntity> features = featureService.findAll().orElse(null);
		
		assertNotNull(features);
		assertEquals(4, features.size());
	}

	@DisplayName("Find feature by name")
	@Test
	void findByName() {
		
		FeatureEntity feature = featureService.findByName("instagram");
		assertNotNull(feature);		
	}
	
	@DisplayName("Not Found feature by name")
	@Test
	void notfoundFeatureByName() {
		
		FeatureEntity feature = featureService.findByName("myspace");
		assertNull(feature);		
	}
	
	@DisplayName("Find feature active")
	@Test
	void findAllFeatureActive() {
		
		Set<FeatureEntity> feature = featureService.findActive();
		assertEquals(2,feature.size());		
	}
	
	@DisplayName("Find feature inactive")
	@Test
	void findAllFeatureInactive() {
		
		Set<FeatureEntity> feature = featureService.findInactive();
		assertEquals(2,feature.size());		
	}
	*/
	@DisplayName("Save feature root")
	@Test
	void saveFeatureRoot() {
		
		FeatureEntity feature4 = FeatureEntity.builder().name("finishes").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.BOOLEAN).build();
		
		try {
			featureService.save(feature4);			
		} catch (BusinessException e) {
			fail();
		}
		
		FeatureEntity feature = featureService.findByName("finishes");
		assertNotNull(feature);		
	}
	
	@DisplayName("Save feature")
	@Test
	void saveFeature() {
		
		FeatureEntity root = FeatureEntity.builder().name("finishes").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.BOOLEAN).build();
		FeatureEntity feature = FeatureEntity.builder().name("Nitro").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.BOOLEAN).build();
		
		try {
			featureService.save(root);
			feature.setRoot(root);
			featureService.save(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		FeatureEntity featureRoot = featureService.findByName("finishes");
		assertNotNull(featureRoot);
		assertNotNull(featureRoot.getFeatures());
		
	}
	
	/*
	@DisplayName("Cant save an feature with an exists name")
	@Test
	void cantSave() {
		
		FeatureEntity feature2 = FeatureEntity.builder().name("insTagram").build();
		try {
			featureService.save(feature2);
			fail("Fail: Saved an feature with an exists name");
		}catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_FEATURE_EXIST);
		}
	}
	*/
	/*
	@DisplayName("Update feature")
	@Test
	void update() {		
		
		FeatureEntity feature = featureService.findByName("instagram");
		feature.setName("myspace");
		try {
			featureService.save(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		assertNull(featureService.findByName("instagram"));
		assertNotNull(featureService.findByName("myspace"));
	}
	
	@DisplayName("Cant update an feature with an exists name")
	@Test
	void shouldntUpdateWithExistsName() {		
		
		FeatureEntity feature = featureService.findByName("instagram");
		feature.setName("facebook");
		try {
			featureService.save(feature);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_FEATURE_EXIST);
		}
	}
	
	@DisplayName("Update an feature with the same name")
	@Test
	void shouldUpdateWithSameName() {		
		
		FeatureEntity feature = featureService.findByName("instagram");
		feature.setName("instagram");
		try {
			featureService.save(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		assertNotNull(featureService.findByName("instagram"));
	}
	
	@DisplayName("Active an feature")
	@Test
	void shouldActiveFeature() {		
		
		FeatureEntity feature = featureService.findByName("Website");
		
		try {
			featureService.active(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.ACTIVE, featureService.findByName("Website").getStatus());
	}
	
	@DisplayName("Inactive an feature")
	@Test
	void shouldInactiveFeature() {		
		
		FeatureEntity feature = featureService.findByName("facebook");
		
		try {
			featureService.inactive(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.INACTIVE, featureService.findByName("facebook").getStatus());
	}
	
	@DisplayName("Dont actived an feature that doesn exist")
	@Test
	void shouldntActiveFeatureThatDoesntExist() {		
		
		FeatureEntity feature = FeatureEntity.builder().id(99l).build();
		
		try {
			featureService.active(feature);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_FEATURE_NOTFOUND);
		}
	}
	
	@DisplayName("Dont inactived an feature that doesn exist")
	@Test
	void shouldntInactiveFeatureThatDoesntExist() {		
		
		FeatureEntity feature = FeatureEntity.builder().id(99l).build();
		
		try {
			featureService.inactive(feature);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_FEATURE_NOTFOUND);
		}		
	}
	
	@DisplayName("Delete an feature")
	@Test
	void shouldDeleteFeature() {		
		
		FeatureEntity feature = featureService.findByName("instagram");
		
		try {
			featureService.delete(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		assertNull(featureService.findByName("instagram"));
	}
	
	@DisplayName("Cant delete an feature with luthier")
	@Test
	void shouldntDeleteFeature() {		
		
		FeatureEntity feature = featureService.findByName("facebook");
		
		try {
			featureService.delete(feature);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), MSG_ERROR_FEATURE_DELETE_BOUND_LUTHIER);
		}
		
		assertNotNull(featureService.findByName("facebook"));
	}
	
	@DisplayName("Cant delete an feature without id")
	@Test
	void shouldntDeleteFeatureWithoutId() {		
		
		FeatureEntity feature = FeatureEntity.builder().id(99l).build();
		
		try {
			featureService.delete(feature);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), MSG_ERROR_FEATURE_NOTFOUND);
		}
	}
	*/
}
