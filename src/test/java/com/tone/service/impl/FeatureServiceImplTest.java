package com.tone.service.impl;


import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_DELETE_BOUND_LUTHIER;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_DELETE_WITH_FEATURE_CHILD;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_FEATURE_NOTFOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;
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
import com.tone.model.LuthierFeatureEntity;
import com.tone.model.enumm.FeatureTypeEnum;
import com.tone.model.enumm.StatusEnum;
import com.tone.service.FeatureService;
import com.tone.service.LuthierFeatureService;
import com.tone.service.LuthierService;
import com.tone.utils.ConstantsMessages;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FeatureServiceImplTest {

	@Autowired
	FeatureService featureService;

	@Autowired
	LuthierService luthierService;
	
	@Autowired
	LuthierFeatureService luthierFeatureService;
	
	@BeforeEach
	void setUp() {
		
		LuthierEntity luthier = LuthierEntity.builder().name("Luthier 1").email("luthier@contact.com").build();
		
		FeatureEntity feature1 = FeatureEntity.builder().name("style").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.BOOLEAN).build();
		FeatureEntity feature2 = FeatureEntity.builder().name("body materials").status(StatusEnum.INACTIVE).type(FeatureTypeEnum.STRING).build();
		FeatureEntity feature3 = FeatureEntity.builder().name("price").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.NUMBER).build();
		FeatureEntity feature4 = FeatureEntity.builder().name("0-100").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.NUMBER).build();
		FeatureEntity feature5 = FeatureEntity.builder().name("101-200").status(StatusEnum.INACTIVE).type(FeatureTypeEnum.NUMBER).build();
		
		try {
			feature1 = featureService.save(feature1);
			feature2 = featureService.save(feature2);
			feature3 = featureService.save(feature3);

			feature4.setRoot(feature3);
			feature4 = featureService.save(feature4);
			
			feature5.setRoot(feature3);
			feature5 = featureService.save(feature5);			
			
			luthier = luthierService.save(luthier);		
			luthier.addFeature(feature4, "500");
			
			for(LuthierFeatureEntity lf : luthier.getFeatures()) {
				luthierFeatureService.save(lf);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@AfterEach
	void close() {
		
		Set<LuthierFeatureEntity> luthiersFeatures = luthierFeatureService.findAll().orElse(null);
		luthiersFeatures.stream().forEach(lutFeat -> {
			try {
				luthierFeatureService.delete(lutFeat);
			} catch (BusinessException e) {
			}
		});
		
		Set<LuthierEntity> luthiers = luthierService.findAll().orElse(null);
		luthiers.stream().forEach(lut -> {
			try {
				luthierService.delete(lut);
			} catch (BusinessException e) {
			}
		});
		
		
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
	
	@DisplayName("Find all feature")
	@Test
	void findAll() {
		
		Set<FeatureEntity> features = featureService.findAll().orElse(null);
		
		assertNotNull(features);
		assertEquals(3, features.size());
	}

	@DisplayName("Find feature by name")
	@Test
	void findByName() {
		
		Optional<List<FeatureEntity>> feature = featureService.findByName("style");
		assertNotNull(feature.get());		
	}
	
	@DisplayName("Not Found feature by name")
	@Test
	void notFoundFeatureByName() {
		
		Optional<List<FeatureEntity>>  feature = featureService.findByName("guitar");
		assertTrue(feature.isEmpty());
	}
	
	@DisplayName("Find feature active")
	@Test
	void findAllFeatureActive() {
		
		Set<FeatureEntity> feature = featureService.findActive();
		assertEquals(3,feature.size());		
	}
	
	@DisplayName("Find feature inactive")
	@Test
	void findAllFeatureInactive() {
		
		Set<FeatureEntity> feature = featureService.findInactive();
		assertEquals(2,feature.size());		
	}
	
	@DisplayName("Save feature root")
	@Test
	void saveFeatureRoot() {
		
		FeatureEntity feature4 = FeatureEntity.builder().name("finishes").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.BOOLEAN).build();
		
		try {
			featureService.save(feature4);			
		} catch (BusinessException e) {
			fail();
		}
		
		Optional<List<FeatureEntity>> feature = featureService.findByName("finishes");
		assertTrue(feature.isPresent());
		assertEquals(4,feature.get().get(0).getPosition().intValue());
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
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("finishes");
		assertTrue(featureRoot.isPresent());
		assertNotNull(featureRoot.get().get(0).getFeatures());
		
		Optional<List<FeatureEntity>> nitro = featureService.findByName("Nitro");		
		assertEquals(1,nitro.get().get(0).getPosition().intValue());
	}
	
	@DisplayName("Save feature with an existing group's name")
	@Test
	void saveFeatureWithSameNameOfAGroupe() {
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("style");
		FeatureEntity feature = FeatureEntity.builder().name("price").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.BOOLEAN).build();
		feature.setRoot(featureRoot.get().get(0));
		
		try {
			featureService.save(feature);			
		} catch (BusinessException e) {
			fail();
		}
		
		featureRoot = featureService.findByName("style");
		assertTrue(featureRoot.isPresent());
		assertNotNull(featureRoot.get().get(0).getFeatures());
	}
	
	@DisplayName("Save feature root with an existing feature's name child")
	@Test
	void saveFeatureRootWithSameNameOfAChild() {
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("style");
		FeatureEntity feature = FeatureEntity.builder().name("style").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.BOOLEAN).build();
		feature.setRoot(featureRoot.get().get(0));
		
		try {
			featureService.save(feature);			
		} catch (BusinessException e) {
			fail();
		}
		
		featureRoot = featureService.findByName("style");
		assertTrue(featureRoot.isPresent());
		assertNotNull(featureRoot.get().get(0).getFeatures());
	}
	
	@DisplayName("Cant save feature root with an existing name")
	@Test
	void shouldntSaveFeatureRootWithAnExistingName() {
		
		FeatureEntity feature4 = FeatureEntity.builder().name("style").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.BOOLEAN).build();
		
		try {
			featureService.save(feature4);			
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_FEATURE_EXIST);
		}
	}
	
	@DisplayName("Cant save feature with an existing name")
	@Test
	void shouldntSaveFeatureWithAnExistingName() {
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("price");		
		FeatureEntity feature = FeatureEntity.builder().name("0-100").status(StatusEnum.INACTIVE).type(FeatureTypeEnum.NUMBER).build();
		feature.setRoot(featureRoot.get().get(0));
		
		try {
			featureService.save(feature);			
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_FEATURE_EXIST);
		}
	}
	
	@DisplayName("Update root feature")
	@Test
	void updateRootFeature() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("style");
		FeatureEntity feature = featureRoot.get().get(0);
		feature.setName("nitro");
		
		try {
			featureService.save(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		featureRoot = featureService.findByName("nitro");
		
		assertTrue(featureRoot.isPresent());
	}
	
	@DisplayName("Update feature")
	@Test
	void updateFeature() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("0-100");
		FeatureEntity feature = featureRoot.get().get(0);
		feature.setName("style");
		
		try {
			featureService.save(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		featureRoot = featureService.findByName("style");
		
		assertTrue(featureRoot.isPresent());
		assertEquals(2, featureRoot.get().size());
	}
	
	@DisplayName("Cant update an feature root with an exists name")
	@Test
	void shouldntUpdateFeatureRootWithExistsName() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("price");
		FeatureEntity feature = featureRoot.get().get(0);
		feature.setName("style");
		
		try {
			featureService.save(feature);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_FEATURE_EXIST);
		}
	}
	
	@DisplayName("Cant update an feature with an exists name")
	@Test
	void shouldntUpdateFeatureWithExistsName() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("0-100");
		FeatureEntity feature = featureRoot.get().get(0);
		feature.setName("101-200");
		
		try {
			featureService.save(feature);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_FEATURE_EXIST);
		}
	}
	
	@DisplayName("Update an feature root with the same name")
	@Test
	void shouldUpdateFeatureRootWithSameName() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("style");
		FeatureEntity feature = featureRoot.get().get(0);
		feature.setName("style");
		
		try {
			featureService.save(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		assertTrue(featureService.findByName("style").isPresent());
	}
	
	@DisplayName("Update an feature with the same name")
	@Test
	void shouldUpdateFeatureWithSameName() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("0-100");
		FeatureEntity feature = featureRoot.get().get(0);
		feature.setName("0-100");
		
		try {
			featureService.save(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		assertTrue(featureService.findByName("0-100").isPresent());
	}
	
	@DisplayName("Active an feature")
	@Test
	void shouldActiveFeature() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("body materials");
		FeatureEntity feature = featureRoot.get().get(0);
		
		try {
			featureService.active(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		featureRoot = featureService.findByName("body materials");
		feature = featureRoot.get().get(0);
		assertEquals(StatusEnum.ACTIVE, feature.getStatus());
	}
	
	@DisplayName("Inactive an feature")
	@Test
	void shouldInactiveFeature() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("0-100");
		FeatureEntity feature = featureRoot.get().get(0);
		
		try {
			featureService.inactive(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		featureRoot = featureService.findByName("0-100");
		feature = featureRoot.get().get(0);
		assertEquals(StatusEnum.INACTIVE, feature.getStatus());
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
	
	@DisplayName("Active an feature root active feature child")
	@Test
	void shouldActiveFeatureChilds() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("price");
		FeatureEntity feature = featureRoot.get().get(0);
		
		try {
			featureService.active(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		featureRoot = featureService.findByName("price");
		feature = featureRoot.get().get(0);
		assertEquals(StatusEnum.ACTIVE, feature.getStatus());
		
		feature.getFeatures().forEach( fea -> {			
			assertEquals(StatusEnum.ACTIVE, fea.getStatus());			
		});
	}
	
	@DisplayName("Inactive an feature root inactive feature child")
	@Test
	void shouldInactiveFeatureChilds() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("price");
		FeatureEntity feature = featureRoot.get().get(0);
		
		try {
			featureService.inactive(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		featureRoot = featureService.findByName("price");
		feature = featureRoot.get().get(0);
		assertEquals(StatusEnum.INACTIVE, feature.getStatus());
		
		feature.getFeatures().forEach( fea -> {			
			assertEquals(StatusEnum.INACTIVE, fea.getStatus());			
		});
	}
	
	@DisplayName("Delete an feature")
	@Test
	void shouldDeleteFeature() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("101-200");
		FeatureEntity feature = featureRoot.get().get(0);
		
		try {
			featureService.delete(feature);
		} catch (BusinessException e) {
			fail();
		}
		
		featureRoot = featureService.findByName("101-200");
		
		assertTrue(featureRoot.isEmpty());
	}
	
	@DisplayName("Cant delete an feature with luthier")
	@Test
	void shouldntDeleteFeature() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("0-100");
		FeatureEntity feature = featureRoot.get().get(0);
		
		try {
			featureService.delete(feature);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), MSG_ERROR_FEATURE_DELETE_BOUND_LUTHIER);
		}
		
		featureRoot = featureService.findByName("0-100");
		
		assertTrue(featureRoot.isPresent()); 
		
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
	
	@DisplayName("Cant delete an feature with feature childs")
	@Test
	void shouldntDeleteFeatureWithFeatureChild() {		
		
		Optional<List<FeatureEntity>> featureRoot = featureService.findByName("price");
		FeatureEntity feature = featureRoot.get().get(0);		
		
		try {
			featureService.delete(feature);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), MSG_ERROR_FEATURE_DELETE_WITH_FEATURE_CHILD);
		}
		
		featureRoot = featureService.findByName("price");
		
		assertTrue(featureRoot.isPresent()); 
	}
	
}
