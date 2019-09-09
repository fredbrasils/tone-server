package com.tone.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.tone.utils.IgnoreField;

@Embeddable
public class LuthierFeatureKey implements Serializable{

	/**
	 * 
	 */
	@IgnoreField
	private static final long serialVersionUID = 1L;

	@Column(name = "luthier_id")
    Long luthierId;
 
    @Column(name = "feature_id")
    Long featureId;
}
