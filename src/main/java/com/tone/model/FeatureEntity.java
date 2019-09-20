package com.tone.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tone.model.enumm.FeatureTypeEnum;
import com.tone.model.enumm.StatusEnum;
import com.tone.model.jsonserializer.FeatureEntitySerializer;
import com.tone.utils.IgnoreField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feature")
public class FeatureEntity extends BaseEntity{

	@IgnoreField
	private static final long serialVersionUID = 1L;
	
	@Builder
	public FeatureEntity(Long id, String name, FeatureTypeEnum type, StatusEnum status, 
			FeatureEntity root, Set<FeatureEntity> features,Set<LuthierFeatureEntity> featuresLuthier) {
		super(id);
		this.name = name;
		this.type = type;
		this.status = status;
		this.root = root;
		this.features = features;
		this.featuresLuthier = featuresLuthier;
	}

	@EqualsAndHashCode.Exclude
	@JsonSerialize(using = FeatureEntitySerializer.class)
	@ManyToOne
	@JoinColumn(name = "feature_id")
	private FeatureEntity root;
		
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy="root")
	private Set<FeatureEntity> features;
	
	@NotBlank
	private String name;

	@Enumerated(EnumType.STRING)
	private FeatureTypeEnum type;	
	
	@Enumerated(EnumType.ORDINAL)
	private StatusEnum status;
	
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "feature")
	private Set<LuthierFeatureEntity> featuresLuthier;
}
