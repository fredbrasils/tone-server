package com.tone.service.impl;


import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.tone.model.FeatureEntity;
import com.tone.model.LuthierFeatureEntity;
import com.tone.model.SocialNetworkEntity;
import com.tone.model.enumm.FeatureTypeEnum;
import com.tone.model.enumm.StatusEnum;
import com.tone.service.FeatureService;
import com.tone.service.LuthierFeatureService;
import com.tone.service.SocialNetworkService;
import com.tone.utils.ConstantsMessages;
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

import javax.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LuthierServiceImplTest {

	@Autowired
	LuthierService luthierService;

	@Autowired
	SocialNetworkService socialNetworkService;

	@Autowired
	FeatureService featureService;

	@Autowired
	LuthierFeatureService luthierFeatureService;

	@BeforeEach
	void setUp() {

		// luthiers
		LuthierEntity luthier1 = LuthierEntity.builder().name("Luthier 1").email("luthier1@tone.com").status(StatusEnum.ACTIVE).build();
		LuthierEntity luthier2 = LuthierEntity.builder().name("Luthier 2").email("luthier2@tone.com").status(StatusEnum.INACTIVE).build();

		// social networks
		SocialNetworkEntity socialNetwork1 = SocialNetworkEntity.builder().name("Instagram").status(StatusEnum.ACTIVE).build();
		SocialNetworkEntity socialNetwork2 = SocialNetworkEntity.builder().name("Facebook").status(StatusEnum.ACTIVE).build();

		//features
		FeatureEntity feature3 = FeatureEntity.builder().name("price").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.NUMBER).build();
		FeatureEntity feature31 = FeatureEntity.builder().name("0-100").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.NUMBER).build();
		FeatureEntity feature32 = FeatureEntity.builder().name("101-200").status(StatusEnum.ACTIVE).type(FeatureTypeEnum.NUMBER).build();
		FeatureEntity feature33 = FeatureEntity.builder().name("201-300").status(StatusEnum.INACTIVE).type(FeatureTypeEnum.NUMBER).build();

		try {

			//save social networks
			socialNetworkService.save(socialNetwork1);
			socialNetworkService.save(socialNetwork2);

			// save luthiers
			luthierService.save(luthier1);
			luthierService.save(luthier2);

			// save features
			feature3 = featureService.save(feature3);

			feature31.setRoot(feature3);
			featureService.save(feature31);

			feature32.setRoot(feature3);
			featureService.save(feature32);

			feature33.setRoot(feature3);
			featureService.save(feature33);

		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}
	
	@AfterEach
	void close() {

		// remove all features and luthiers bounds
		Set<LuthierFeatureEntity> luthiersFeatures = luthierFeatureService.findAll().orElse(null);
		luthiersFeatures.stream().forEach(lutFeat -> {
			try {
				luthierFeatureService.delete(lutFeat);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});

		// remove all luthiers
		Set<LuthierEntity> luthiers = luthierService.findAll().orElse(null);
		luthiers.stream().forEach(lut -> {
			try {
				luthierService.delete(lut);
			} catch (BusinessException e) {
				System.out.println(e.getMessage());
			}
		});

		// remove all features
		Set<FeatureEntity> features = featureService.findAll().orElse(null);
		features.stream().forEach(fet -> {
			try {
				for(FeatureEntity f : fet.getFeatures()) {
					featureService.delete(f);
				}
				featureService.delete(fet);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});

		// remove all social network
		Set<SocialNetworkEntity> socialNetworks = socialNetworkService.findAll().orElse(null);
		socialNetworks.stream().forEach(lut -> {
			try {
				socialNetworkService.delete(lut);
			} catch (BusinessException e) {
				System.out.println(e.getMessage());
			}
		});
	}

	@DisplayName("Save luthier")
	@Test
	void save() {

		LuthierEntity luthier3 = LuthierEntity.builder().name("Luthier 3").email("luthier3@tone.com").build();
		try {
			luthierService.save(luthier3);
		} catch (BusinessException e) {
		}
		LuthierEntity luthier = luthierService.findOptionalByEmail("luthier3@tone.com");
		assertNotNull(luthier);
	}

	@DisplayName("Dont save luthier with an email existing")
	@Test
	void dontSave() {

		LuthierEntity luthier3 = LuthierEntity.builder().name("Luthier new").email("luthier1@tone.com").build();
		try {
			luthierService.save(luthier3);
			fail("Fail: Saved a luthier with an exists email");
		}catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_LUTHIER_EXIST);
		}

		Optional<List<LuthierEntity>> luthier = luthierService.findByName("Luthier new");
		assertNotNull(luthier.isEmpty());
	}

	@Transactional
	@DisplayName("Save a new luthier with social network")
	@Test
	void saveAnNewLuthierWithSocialNetwork() {

		LuthierEntity luthier4 = LuthierEntity.builder().name("Luthier 4").email("luthier4@tone.com").build();
		luthier4.addSocialNetwork(socialNetworkService.findByName("Instagram").get().get(0),"instagram/luthier4");

		try {
			luthierService.save(luthier4);
		} catch (BusinessException e) {
		}

		LuthierEntity luthier = luthierService.findOptionalByEmail("luthier4@tone.com");
		assertNotNull(luthier);
		assertEquals(1,luthier.getSocialNetworks().size());
	}

	@Transactional
	@DisplayName("Add social network to a luthier")
	@Test
	void saveLuthierWithSocialNetwork() {

		LuthierEntity luthier1 = luthierService.findOptionalByEmail("luthier1@tone.com");
		luthier1.addSocialNetwork(socialNetworkService.findByName("Facebook").get().get(0),"facebook/luthier");

		try {
			luthierService.save(luthier1);
		} catch (BusinessException e) {
		}

		LuthierEntity luthier = luthierService.findOptionalByEmail("luthier1@tone.com");

		assertNotNull(luthier);
		assertEquals(1,luthier.getSocialNetworks().size());
	}

	@Transactional
	@DisplayName("Save luthier with features")
	@Test
	void saveAnNewLuthierWithFeatures() {

		LuthierEntity luthier5 = LuthierEntity.builder().name("Luthier 5").email("luthier5@tone.com").build();
		luthier5.addFeature(featureService.findByName("101-200").get().get(0), "180");

		try {
			luthierService.save(luthier5);
		} catch (BusinessException e) {
		}

		LuthierEntity luthier = luthierService.findOptionalByEmail("luthier5@tone.com");
		assertNotNull(luthier);
		assertEquals(1,luthier.getFeatures().size());
	}

	@Transactional
	@DisplayName("Add features to a luthier")
	@Test
	void saveLuthierWithFeatures() {

		LuthierEntity luthier1 = luthierService.findOptionalByEmail("luthier1@tone.com");
		luthier1.addFeature(featureService.findByName("101-200").get().get(0), "200");

		try {
			luthierService.save(luthier1);
		} catch (BusinessException e) {
		}

		LuthierEntity luthier = luthierService.findOptionalByEmail("luthier1@tone.com");
		assertNotNull(luthier);
		assertEquals(1,luthier.getFeatures().size());
	}

	@Transactional
	@DisplayName("Save luthier with features and social network")
	@Test
	void saveAnNewLuthierWithFeaturesAndSocialNetwork() {

		LuthierEntity luthier6 = LuthierEntity.builder().name("Luthier 6").email("luthier6@tone.com").build();
		luthier6.addFeature(featureService.findByName("101-200").get().get(0), "180");
		luthier6.addFeature(featureService.findByName("0-100").get().get(0), "50");

		luthier6.addSocialNetwork(socialNetworkService.findByName("Instagram").get().get(0),"instagram/luthier6");
		luthier6.addSocialNetwork(socialNetworkService.findByName("Facebook").get().get(0),"facebook/luthier6");

		try {
			luthierService.save(luthier6);
		} catch (BusinessException e) {
		}

		LuthierEntity luthier = luthierService.findOptionalByEmail("luthier6@tone.com");
		assertNotNull(luthier);
		assertEquals(2,luthier.getFeatures().size());
		assertEquals(2,luthier.getSocialNetworks().size());
	}

	@Transactional
	@DisplayName("Add features and social network to luthier")
	@Test
	void saveLuthierWithFeaturesAndSocialNetwork() {

		LuthierEntity luthier1 = luthierService.findOptionalByEmail("luthier1@tone.com");
		luthier1.addFeature(featureService.findByName("101-200").get().get(0), "180");
		luthier1.addFeature(featureService.findByName("0-100").get().get(0), "50");

		luthier1.addSocialNetwork(socialNetworkService.findByName("Instagram").get().get(0),"instagram/luthier6");

		try {
			luthierService.save(luthier1);
		} catch (BusinessException e) {
		}

		LuthierEntity luthier = luthierService.findOptionalByEmail("luthier1@tone.com");
		assertNotNull(luthier);
		assertEquals(2,luthier.getFeatures().size());
		assertEquals(1,luthier.getSocialNetworks().size());
	}

	/*
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
	*/
}
