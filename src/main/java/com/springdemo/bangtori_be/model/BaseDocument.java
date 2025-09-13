package com.springdemo.bangtori_be.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter @Setter
public abstract class BaseDocument {
    @Id
    private String id;

    /** Unix epoch seconds (초 단위) */
    private long createdAt;
}