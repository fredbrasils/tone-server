package com.tone.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tone.model.enumm.FeatureTypeEnum;
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
	public FeatureEntity(Long id, String name, FeatureTypeEnum type) {
		super(id);
		this.name = name;
		this.type = type;
	}

	private String name;

	@Enumerated(EnumType.STRING)
	private FeatureTypeEnum type;	
	
	@OneToMany(mappedBy = "feature")
	private Set<LuthierFeatureEntity> features;
}
