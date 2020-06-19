package com.fastcode.demo.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter @Setter
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;
    
}