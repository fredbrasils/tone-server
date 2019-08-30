package com.tone.model.payload;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

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
public class LuthierFeature implements Serializable{ 

	@IgnoreField
	private static final long serialVersionUID = 1L;
	
	@NotBlank
    private Luthier luthier;
	
	@NotBlank
    private Feature feature;
	
	@NotBlank
	private String value;
}
