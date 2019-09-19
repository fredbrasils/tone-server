package com.tone.model.payload;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.tone.utils.IgnoreField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LuthierSocialNetwork implements Serializable{

	@IgnoreField
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotBlank
	private String link;

	@EqualsAndHashCode.Exclude
	@NotBlank
	private SocialNetwork socialNetwork;
	
	@EqualsAndHashCode.Exclude
	@NotBlank
	private Luthier luthier;	
	
}
