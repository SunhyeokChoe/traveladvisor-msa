package com.traveladvisor.common.datasource.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter @Setter @ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseZonedDateTimeEntity {

    @CreatedDate
    @Column(name="created_at", updatable = false)
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at", insertable = false)
    private ZonedDateTime updatedAt;

}
