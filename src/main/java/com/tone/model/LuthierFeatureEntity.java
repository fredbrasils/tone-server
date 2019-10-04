package com.tone.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "luthier_feature")
public class LuthierFeatureEntity implements Serializable{ 

	@IgnoreField
	private static final long serialVersionUID = 1L;
	
	@Builder
	public LuthierFeatureEntity(LuthierEntity luthier, FeatureEntity feature, String value) {
		super();
		this.luthier = luthier;
		this.feature = feature;
		this.value = value;
		this.id = LuthierFeatureKey.builder().luthierId(luthier.getId()).featureId(feature.getId()).build();
	}	
	
	@EmbeddedId
	private LuthierFeatureKey id;
 
	@EqualsAndHashCode.Exclude
    @ManyToOne
    @MapsId("luthier_id")
    @JoinColumn(name = "luthier_id")
    private LuthierEntity luthier;
 
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @MapsId("feature_id")
    @JoinColumn(name = "feature_id")
    private FeatureEntity feature;
	
	private String value;

}
