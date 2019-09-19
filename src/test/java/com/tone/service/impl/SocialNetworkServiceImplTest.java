package com.tone.service.impl;


import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_DELETE_BOUND_LUTHIER;
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
import com.tone.model.SocialNetworkEntity;
import com.tone.model.LuthierEntity;
import com.tone.model.enumm.StatusEnum;
import com.tone.service.SocialNetworkService;
import com.tone.service.LuthierService;
import com.tone.utils.ConstantsMessages;
import static com.tone.utils.ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_NOTFOUND;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SocialNetworkServiceImplTest {

	@Autowired
	SocialNetworkService socialNetworkService;

	@Autowired
	LuthierService luthierService;
	
	@BeforeEach
	void setUp() {
		
		//LuthierEntity luthier = LuthierEntity.builder().name("Luthier 1").email("luthier@contact.com").build();
		
		SocialNetworkEntity socialNetwork1 = SocialNetworkEntity.builder().name("Instagram").status(StatusEnum.ACTIVE).build();
		SocialNetworkEntity socialNetwork2 = SocialNetworkEntity.builder().name("Facebook").status(StatusEnum.ACTIVE).build();
		SocialNetworkEntity socialNetwork3 = SocialNetworkEntity.builder().name("Website").status(StatusEnum.INACTIVE).build();
		SocialNetworkEntity socialNetwork4 = SocialNetworkEntity.builder().name("Other").status(StatusEnum.INACTIVE).build();
		
		try {
			socialNetwork1 = socialNetworkService.save(socialNetwork1);
			socialNetwork2 = socialNetworkService.save(socialNetwork2);
			socialNetwork3 = socialNetworkService.save(socialNetwork3);
			socialNetwork4 = socialNetworkService.save(socialNetwork4);
			
			/*luthier.addSocialNetwork(socialNetwork3);
			luthierService.save(luthier);*/
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@AfterEach
	void close() {
		
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
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findByName("instagram");
		assertNotNull(socialNetwork);		
	}
	
	@DisplayName("Not Found socialNetwork by name")
	@Test
	void notfoundSocialNetworkByName() {
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findByName("myspace");
		assertNull(socialNetwork);		
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
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findByName("myspace");
		assertNotNull(socialNetwork);		
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
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findByName("instagram");
		socialNetwork.setName("myspace");
		try {
			socialNetworkService.save(socialNetwork);
		} catch (BusinessException e) {
			fail();
		}
		
		assertNull(socialNetworkService.findByName("instagram"));
		assertNotNull(socialNetworkService.findByName("myspace"));
	}
	
	@DisplayName("Cant update an socialNetwork with an exists name")
	@Test
	void shouldntUpdateWithExistsName() {		
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findByName("instagram");
		socialNetwork.setName("facebook");
		try {
			socialNetworkService.save(socialNetwork);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), ConstantsMessages.MSG_ERROR_SOCIAL_NETWORK_EXIST);
		}
	}
	
	@DisplayName("Update an socialNetwork with the same name")
	@Test
	void shouldUpdateWithSameName() {		
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findByName("instagram");
		socialNetwork.setName("instagram");
		try {
			socialNetworkService.save(socialNetwork);
		} catch (BusinessException e) {
			fail();
		}
		
		assertNotNull(socialNetworkService.findByName("instagram"));
	}
	
	@DisplayName("Active an socialNetwork")
	@Test
	void shouldActiveSocialNetwork() {		
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findByName("Website");
		
		try {
			socialNetworkService.active(socialNetwork);
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.ACTIVE, socialNetworkService.findByName("Website").getStatus());
	}
	
	@DisplayName("Inactive an socialNetwork")
	@Test
	void shouldInactiveSocialNetwork() {		
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findByName("facebook");
		
		try {
			socialNetworkService.inactive(socialNetwork);
		} catch (BusinessException e) {
			fail();
		}
		
		assertEquals(StatusEnum.INACTIVE, socialNetworkService.findByName("facebook").getStatus());
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
	/*
	@DisplayName("Delete an socialNetwork")
	@Test
	void shouldDeleteSocialNetwork() {		
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findOptionalByName("SocialNetwork 1");
		
		try {
			socialNetworkService.delete(socialNetwork);
		} catch (BusinessException e) {
			fail();
		}
		
		assertNull(socialNetworkService.findOptionalByName("SocialNetwork 1"));
	}
	
	@DisplayName("Cant delete an socialNetwork with luthier")
	@Test
	void shouldntDeleteSocialNetwork() {		
		
		SocialNetworkEntity socialNetwork = socialNetworkService.findOptionalByName("SocialNetwork 3");
		
		try {
			socialNetworkService.delete(socialNetwork);
			fail();
		} catch (BusinessException e) {
			assertEquals(e.getMessage(), MSG_ERROR_SOCIAL_NETWORK_DELETE_BOUND_LUTHIER);
		}
		
		assertNotNull(socialNetworkService.findOptionalByName("SocialNetwork 3"));
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
	*/
}
