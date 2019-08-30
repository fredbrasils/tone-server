package com.tone.model.payload;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.tone.model.enumm.SocialNetworkTypeEnum;
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
public class SocialNetwork implements Serializable{

	@IgnoreField
	private static final long serialVersionUID = 1L;

	@NotBlank
	private SocialNetworkTypeEnum type;
	
	@NotBlank
	private String link;
	
	@NotBlank
	private Luthier luthier;
	
}
