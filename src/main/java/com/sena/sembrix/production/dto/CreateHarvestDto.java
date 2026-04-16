package com.sena.sembrix.production.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateHarvestDto {
    @NotBlank
    private String batchName;
    @NotNull
    private Long productId;
    private LocalDateTime startDate;
}
