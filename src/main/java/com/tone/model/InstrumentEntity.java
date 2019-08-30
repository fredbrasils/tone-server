package com.tone.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
@Table(name = "instrument")
public class InstrumentEntity extends BaseEntity{

	@IgnoreField
	private static final long serialVersionUID = 1L;
	
	@Builder
	public InstrumentEntity(Long id, String name) {
		super(id);
		this.name = name;
	}

	private String name;
	
	@ManyToMany(mappedBy = "instruments")
	private LuthierEntity luthier;
}
