package com.tone.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

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
@Entity
public class LuthierFeatureEntity implements Serializable{ 

	@IgnoreField
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private LuthierFeatureKey id;
 
    @ManyToOne
    @MapsId("luthier_id")
    @JoinColumn(name = "luthier_id")
    private LuthierEntity luthier;
 
    @ManyToOne
    @MapsId("feature_id")
    @JoinColumn(name = "feature_id")
    private FeatureEntity feature;
	
	private String value;
}
