package com.tone.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import com.tone.model.enumm.StatusEnum;
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
@Table(name = "luthier")
public class LuthierEntity extends BaseEntity{

	@IgnoreField
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String name;
	
	private String description;
	
	@NotBlank
	private String email;
	
	private String phone;
	
	private String address;

	@Enumerated(EnumType.ORDINAL)
	private StatusEnum status = StatusEnum.ACTIVE;

	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "luthier")
	private Set<LuthierSocialNetworkEntity> socialNetworks;
	
	@EqualsAndHashCode.Exclude
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
	  name = "luthier_instruments", 
	  joinColumns = @JoinColumn(name = "luthier_id"), 
	  inverseJoinColumns = @JoinColumn(name = "instrument_id"))
	private Set<InstrumentEntity> instruments = new HashSet<>();

	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "luthier")
	private Set<LuthierFeatureEntity> features;
	
	/**
	 * 
	 * @param instrument Instrument will be bound to Luthier
	 */
	public void addInstrument(InstrumentEntity instrument) {
		
		if(this.instruments == null) {
			this.instruments = new HashSet<InstrumentEntity>();
		}
		
		this.instruments.add(instrument);
	}
	
	/**
	 * 
	 * @param socialNetwork SocialNetwork will be bound to Luthier
	 * @param link Link of social network
	 */
	public void addSocialNetwork(SocialNetworkEntity socialNetwork, String link) {
		
		if(this.socialNetworks == null) {
			this.socialNetworks = new HashSet<LuthierSocialNetworkEntity>();
		}
		
		LuthierSocialNetworkEntity ls = new LuthierSocialNetworkEntity(link, socialNetwork, this);		
		this.socialNetworks.add(ls);
	}
	
	/**
	 * 
	 * @param socialNetwork SocialNetwork will be bound to Luthier
	 */
	public void addSocialNetwork(LuthierSocialNetworkEntity socialNetwork) {
		
		if(this.socialNetworks == null) {
			this.socialNetworks = new HashSet<LuthierSocialNetworkEntity>();
		}
		
		this.socialNetworks.add(socialNetwork);
	}

	/**
	 * 
	 * @param feature Feature will be bound to Luthier
	 * @param value Feature's value
	 */
	public void addFeature(FeatureEntity feature, String value) {
		
		if(this.features == null) {
			this.features = new HashSet<LuthierFeatureEntity>();
		}
		
		LuthierFeatureEntity lf = new LuthierFeatureEntity(this,feature,value);
		this.features.add(lf);
	}

	/**
	 *
	 * @param feature Feature will be bound to Luthier
	 */
	public void addFeature(LuthierFeatureEntity feature) {

		if(this.features == null) {
			this.features = new HashSet<LuthierFeatureEntity>();
		}

		this.features.add(feature);
	}
}
