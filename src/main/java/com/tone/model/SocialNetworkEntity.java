package com.tone.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.tone.model.enumm.StatusEnum;
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
@Table(name = "social_network")
public class SocialNetworkEntity extends BaseEntity{

	@IgnoreField
	private static final long serialVersionUID = 1L;
	
	@Builder
	public SocialNetworkEntity(Long id, StatusEnum status, String name) {
		super(id);
		this.status = status;
		this.name = name;
	}

	@Enumerated(EnumType.ORDINAL)
	private StatusEnum status;
	
	@NotBlank
	private String name;
	
}
