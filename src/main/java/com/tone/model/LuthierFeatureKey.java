package com.tone.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.tone.utils.IgnoreField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Embeddable
public class LuthierFeatureKey implements Serializable{

	@IgnoreField
	private static final long serialVersionUID = 1L;

	@Column(name = "luthier_id")
    Long luthierId;
 
    @Column(name = "feature_id")
    Long featureId;
}
