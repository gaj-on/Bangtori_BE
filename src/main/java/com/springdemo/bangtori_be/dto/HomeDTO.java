package com.springdemo.bangtori_be.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HomeDTO {
    private String userName;
    private String userTitle;
    private Boolean notificationsEnabled;
}
