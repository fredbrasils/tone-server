/**
 * 
 */
package com.tone.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.tone.model.enumm.OperationEnum;
import com.tone.utils.IgnoreField;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Fred Brasil
 *
 */

@EqualsAndHashCode
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity implements Serializable {

	@IgnoreField
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Long id;

	@EqualsAndHashCode.Exclude
	@IgnoreField
	@Enumerated
    private OperationEnum operation;
      
	@EqualsAndHashCode.Exclude
	@IgnoreField
    @Column
    private LocalDateTime createdTime;
    
	@EqualsAndHashCode.Exclude
	@IgnoreField
	@Column
    @CreatedBy
    private String createdBy;
 
	@EqualsAndHashCode.Exclude
	@IgnoreField
    @Column
    @LastModifiedBy
    private String modifiedBy;
    
    @PrePersist
    public void onPrePersist() {
        audit(OperationEnum.INSERT);
    }
      
    @PreUpdate
    public void onPreUpdate() {
        audit(OperationEnum.UPDATE);
    }
      
    @PreRemove
    public void onPreRemove() {
        audit(OperationEnum.DELETE);
    }
      
    private void audit(OperationEnum operation) {
        setOperation(operation);
        setCreatedTime(LocalDateTime.now());
    }
}
