package com.glanner.core.domain.base;

import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        modifiedDate = now;
    }

    @PreUpdate
    public void preUpdate(){
        modifiedDate = LocalDateTime.now();
    }
}
