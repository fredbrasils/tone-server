package com.tone.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
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
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedEntityGraph(name = "FeatureEntity.detail",
	attributeNodes = @NamedAttributeNode("features"))
@Table(name = "feature")
public class FeatureEntity extends BaseEntity{

	@IgnoreField
	private static final long serialVersionUID = 1L;

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
	
	@EqualsAndHashCode.Exclude
	@Enumerated(EnumType.ORDINAL)
	private StatusEnum status;
	
	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "feature")
	private Set<LuthierFeatureEntity> luthiers;
	
	private Integer position;	
}
