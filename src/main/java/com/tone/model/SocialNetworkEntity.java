package com.tone.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.tone.model.enumm.SocialNetworkTypeEnum;
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
	public SocialNetworkEntity(Long id, SocialNetworkTypeEnum type, String link) {
		super(id);
		this.type = type;
		this.link = link;
	}

	@Enumerated(EnumType.STRING)
	private SocialNetworkTypeEnum type;
	
	private String link;
	
	@EqualsAndHashCode.Exclude
	@NotBlank
	@ManyToOne
	@JoinColumn(name = "luthier_id")
	private LuthierEntity luthier;	
	
}
