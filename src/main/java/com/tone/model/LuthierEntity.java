package com.tone.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
@Table(name = "luthier")
public class LuthierEntity extends BaseEntity{

	@IgnoreField
	private static final long serialVersionUID = 1L;
	
	@Builder
	public LuthierEntity(Long id, String name, String email, String phone, String address, 
			Set<LuthierSocialNetworkEntity> socialNetworks,Set<InstrumentEntity> instruments,
			Set<LuthierFeatureEntity> features) {
		super(id);
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.socialNetworks = socialNetworks;
		this.instruments = instruments;
		this.features = features;
	}

	@NotBlank
	private String name;
	
	private String description;
	
	@NotBlank
	private String email;
	
	private String phone;
	
	private String address;

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
	
	
	public void addInstrument(InstrumentEntity instrument) {
		
		if(this.instruments == null) {
			this.instruments = new HashSet<InstrumentEntity>();
		}
		
		this.instruments.add(instrument);
	}
	
	public void addSocialNetwork(SocialNetworkEntity socialNetwork, String link) {
		
		if(this.socialNetworks == null) {
			this.socialNetworks = new HashSet<LuthierSocialNetworkEntity>();
		}
		
		LuthierSocialNetworkEntity ls = new LuthierSocialNetworkEntity(link, socialNetwork, this);		
		this.socialNetworks.add(ls);
	}
	
	public void addSocialNetwork(LuthierSocialNetworkEntity socialNetwork) {
		
		if(this.socialNetworks == null) {
			this.socialNetworks = new HashSet<LuthierSocialNetworkEntity>();
		}
		
		this.socialNetworks.add(socialNetwork);
	}

}
