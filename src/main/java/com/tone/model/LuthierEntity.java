package com.tone.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
			Set<SocialNetworkEntity> socialNetworks, Set<InstrumentEntity> instruments,
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

	private String name;
	
	private String description;
	
	private String email;
	
	private String phone;
	
	private String address;

	@OneToMany(mappedBy = "luthier")
	private Set<SocialNetworkEntity> socialNetworks;
	
	@ManyToMany
	@JoinTable(
	  name = "luthier_instruments", 
	  joinColumns = @JoinColumn(name = "luthier_id"), 
	  inverseJoinColumns = @JoinColumn(name = "instrument_id"))
	private Set<InstrumentEntity> instruments;
	
	@OneToMany(mappedBy = "luthier")
	private Set<LuthierFeatureEntity> features;
	
	
}
