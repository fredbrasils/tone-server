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
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "luthier_social_network")
public class LuthierSocialNetworkEntity extends BaseEntity{

	@IgnoreField
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String link;

	@EqualsAndHashCode.Exclude
	@ManyToOne
	@JoinColumn(name = "social_network_id")
	private SocialNetworkEntity socialNetwork;
	
	@EqualsAndHashCode.Exclude
	@ManyToOne
	@JoinColumn(name = "luthier_id")
	private LuthierEntity luthier;	
	
}
