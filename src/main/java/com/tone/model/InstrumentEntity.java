package com.tone.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.tone.utils.IgnoreField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author fredbrasil
 *
 */
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

	@NotBlank
	private String name;
	
	@ManyToMany(mappedBy = "instruments")	
	private Set<LuthierEntity> luthiers;
}
