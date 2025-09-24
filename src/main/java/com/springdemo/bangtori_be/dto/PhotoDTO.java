package com.springdemo.bangtori_be.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PhotoDTO {
    @Min(1) @Max(2000) private int width;
    @Min(1) @Max(2000) private int height;
    @Min(1) @Max(3)    private int channels; // 1 or 3
    @NotNull private int[] data;            // 0..255 values
}
