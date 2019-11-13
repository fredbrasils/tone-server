package com.tone.model.payload;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.tone.model.enumm.StatusEnum;
import com.tone.utils.IgnoreField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Luthier implements Serializable{

	@IgnoreField
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank
	private String name;

	private String description;
	
	@NotBlank
	private String email;
	
	private String phone;
	
	private String address;

	private Set<LuthierSocialNetwork> socialNetworks;
	
	private Set<Instrument> instruments;
	
	private Set<LuthierFeature> features;

	private StatusEnum status;
	
}
