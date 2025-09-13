package com.springdemo.bangtori_be.dto;

import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MissionDTO {
    public String title;
    public Integer points;
    public String category;
}
