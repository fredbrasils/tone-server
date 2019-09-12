package com.tone.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
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
	public InstrumentEntity(Long id, String name, StatusEnum status) {
		super(id);
		this.name = name;
		this.status = status;
	}

	@NotBlank
	private String name;
	
	@Enumerated(EnumType.ORDINAL)
	private StatusEnum status = StatusEnum.ACTIVE;
	
	@ManyToMany(mappedBy = "instruments")	
	private Set<LuthierEntity> luthiers;
}
