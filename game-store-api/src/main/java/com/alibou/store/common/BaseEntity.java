package com.alibou.store.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
public class BaseEntity {

    @Id
    // UUID ==> Universal Unique ID
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // auditing attributes
    // private String createdBy;
    // private String lastModifiedBy;
    // private String createdDate;
    // private String lastModifiedDate;
}
