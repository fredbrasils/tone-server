package com.tone.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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
@Table(name = "luthier_social_network")
public class LuthierSocialNetworkEntity extends BaseEntity{

	@IgnoreField
	private static final long serialVersionUID = 1L;
	
	@Builder
	public LuthierSocialNetworkEntity(Long id, SocialNetworkEntity socialNetwork, LuthierEntity luthier, String link) {
		super(id);
		this.socialNetwork = socialNetwork;
		this.link = link;
		this.luthier = luthier;
	}
	
	@NotBlank
	private String link;

	@EqualsAndHashCode.Exclude
	@NotBlank
	@ManyToOne
	@JoinColumn(name = "social_network_id")
	private SocialNetworkEntity socialNetwork;
	
	@EqualsAndHashCode.Exclude
	@NotBlank
	@ManyToOne
	@JoinColumn(name = "luthier_id")
	private LuthierEntity luthier;	
	
}
