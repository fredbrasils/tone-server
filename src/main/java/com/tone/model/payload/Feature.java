package com.tone.model.payload;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.tone.model.enumm.FeatureTypeEnum;
import com.tone.model.enumm.StatusEnum;
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
public class Feature implements Serializable{

	@IgnoreField
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank
	private String name;

	private FeatureTypeEnum type;	
	
	@EqualsAndHashCode.Exclude
	private StatusEnum status;
	
	@EqualsAndHashCode.Exclude
	private Set<LuthierFeature> features;
}
