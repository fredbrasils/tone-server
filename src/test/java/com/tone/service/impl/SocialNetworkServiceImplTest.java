package com.tone.service.impl;


import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_DELETE_BOUND_LUTHIER;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
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
import com.tone.model.SocialNetworkEntity;
import com.tone.model.LuthierEntity;
import com.tone.model.LuthierSocialNetworkEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.service.SocialNetworkService;
import com.tone.service.LuthierService;
import com.tone.utils.ConstantsMessages;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_NOTFOUND;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SocialNetworkServiceImplTest {

	@Autowired
	SocialNetworkService socialNetworkService;

	@Autowired
	LuthierService luthierService;
	
	@BeforeEach
	void setUp() {
		
		LuthierEntity luthier = LuthierEntity.builder().name("Luthier 1").email("luthier@contact.com").build();
		
		SocialNetworkEntity socialNetwork1 = SocialNetworkEntity.builder().name("Instagram").status(StatusEnum.ACTIVE).build();
		SocialNetworkEntity socialNetwork2 = SocialNetworkEntity.builder().name("Facebook").status(StatusEnum.ACTIVE).build();
		SocialNetworkEntity socialNetwork3 = SocialNetworkEntity.builder().name("Website").status(StatusEnum.INACTIVE).build();
		SocialNetworkEntity socialNetwork4 = SocialNetworkEntity.builder().name("Other").status(StatusEnum.INACTIVE).build();
		
		try {
			socialNetworkService.save(socialNetwork1);
			socialNetwork2 = socialNetworkService.save(socialNetwork2);
			socialNetworkService.save(socialNetwork3);
			socialNetworkService.save(socialNetwork4);
			
			luthier.addSocialNetwork(socialNetwork2, "facebook.com/luthier1");
			luthier = luthierService.save(luthier);		
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
		
		Set<SocialNetworkEntity> socialNetworks = socialNetworkService.findAll().orElse(null);
		socialNetworks.stream().forEach(lut -> {
			try {
				socialNetworkService.delete(lut);
			} catch (BusinessException e) {
			}
		});
	}
	
	@DisplayName("Find all socialNetwork")
	@Test
	void findAll() {
		
		Set<SocialNetworkEntity> socialNetworks = socialNetworkService.findAll().orElse(null);
		
		assertNotNull(socialNetworks);
		assertEquals(4, socialNetworks.size());
	}

	@DisplayName("Find socialNetwork by name")
	@Test
	void findByName() {
		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("instagram");
		assertTrue(socialNetwork.isPresent());
	}
	
	@DisplayName("Not Found socialNetwork by name")
	@Test
	void notfoundSocialNetworkByName() {
		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("myspace");
		assertTrue(socialNetwork.isEmpty());
	}
	
	@DisplayName("Find socialNetwork active")
	@Test
	void findAllSocialNetworkActive() {
		
		Set<SocialNetworkEntity> socialNetwork = socialNetworkService.findActive();
		assertEquals(2,socialNetwork.size());		
	}
	
	@DisplayName("Find socialNetwork inactive")
	@Test
	void findAllSocialNetworkInactive() {
		
		Set<SocialNetworkEntity> socialNetwork = socialNetworkService.findInactive();
		assertEquals(2,socialNetwork.size());		
	}
	
	@DisplayName("Save socialNetwork")
	@Test
	void save() {
		
		SocialNetworkEntity socialNetwork4 = SocialNetworkEntity.builder().name("myspace").build();
		try {
			socialNetworkService.save(socialNetwork4);			
		} catch (BusinessException e) {
			fail();
		}

		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("myspace");
		assertTrue(socialNetwork.isPresent());
	}
	
	@DisplayName("Cant save an socialNetwork with an exists name")
	@Test
	void cantSave() {
		
		SocialNetworkEntity socialNetwork2 = SocialNetworkEntity.builder().name("insTagram").build();
		try {
			socialNetworkService.save(socialNetwork2);
			fail("Fail: Saved an socialNetwork with an exists name");
		}catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_EXIST);
		}
	}
	
	@DisplayName("Update socialNetwork")
	@Test
	void update() {

		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("instagram");
		socialNetwork.get().get(0).setName("myspace");
		try {
			socialNetworkService.save(socialNetwork.get().get(0));
		} catch (BusinessException e) {
			fail();
		}
		
		assertTrue(socialNetworkService.findByName("instagram").isEmpty());
		assertTrue(socialNetworkService.findByName("myspace").isPresent());
	}
	
	@DisplayName("Cant update an socialNetwork with an exists name")
	@Test
	void shouldntUpdateWithExistsName() {

		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("instagram");
		socialNetwork.get().get(0).setName("facebook");
		try {
			socialNetworkService.save(socialNetwork.get().get(0));
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_EXIST);
		}
	}
	
	@DisplayName("Update an socialNetwork with the same name")
	@Test
	void shouldUpdateWithSameName() {

		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("instagram");
		socialNetwork.get().get(0).setName("instagram");
		try {
			socialNetworkService.save(socialNetwork.get().get(0));
		} catch (BusinessException e) {
			fail();
		}

		assertTrue(socialNetworkService.findByName("instagram").isPresent());
	}
	
	@DisplayName("Active an socialNetwork")
	@Test
	void shouldActiveSocialNetwork() {

		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("Website");
		
		try {
			socialNetworkService.active(socialNetwork.get().get(0));
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.ACTIVE, socialNetworkService.findByName("Website").get().get(0).getStatus());
	}
	
	@DisplayName("Inactive an socialNetwork")
	@Test
	void shouldInactiveSocialNetwork() {

		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("facebook");
		
		try {
			socialNetworkService.inactive(socialNetwork.get().get(0));
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.INACTIVE, socialNetworkService.findByName("facebook").get().get(0).getStatus());
	}
	
	@DisplayName("Dont actived an socialNetwork that doesn exist")
	@Test
	void shouldntActiveSocialNetworkThatDoesntExist() {		
		
		SocialNetworkEntity socialNetwork = SocialNetworkEntity.builder().id(99l).build();
		
		try {
			socialNetworkService.active(socialNetwork);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_NOTFOUND);
		}
	}
	
	@DisplayName("Dont inactived an socialNetwork that doesn exist")
	@Test
	void shouldntInactiveSocialNetworkThatDoesntExist() {		
		
		SocialNetworkEntity socialNetwork = SocialNetworkEntity.builder().id(99l).build();
		
		try {
			socialNetworkService.inactive(socialNetwork);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_NOTFOUND);
		}		
	}
	
	@DisplayName("Delete an socialNetwork")
	@Test
	void shouldDeleteSocialNetwork() {

		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("instagram");
		
		try {
			socialNetworkService.delete(socialNetwork.get().get(0));
		} catch (BusinessException e) {
			fail();
		}
		
		assertTrue(socialNetworkService.findByName("instagram").isEmpty());
	}
	
	@DisplayName("Cant delete an socialNetwork with luthier")
	@Test
	void shouldntDeleteSocialNetwork() {

		Optional<List<SocialNetworkEntity>> socialNetwork = socialNetworkService.findByName("facebook");
		
		try {
			socialNetworkService.delete(socialNetwork.get().get(0));
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), MSG_ERROR_SOCIAL_NETWORK_DELETE_BOUND_LUTHIER);
		}
		
		assertTrue(socialNetworkService.findByName("facebook").isPresent());
	}
	
	@DisplayName("Cant delete an socialNetwork without id")
	@Test
	void shouldntDeleteSocialNetworkWithoutId() {		
		
		SocialNetworkEntity socialNetwork = SocialNetworkEntity.builder().id(99l).build();
		
		try {
			socialNetworkService.delete(socialNetwork);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), MSG_ERROR_SOCIAL_NETWORK_NOTFOUND);
		}
	}
	
}
