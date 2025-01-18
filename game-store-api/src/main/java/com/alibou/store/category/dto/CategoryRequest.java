package com.alibou.store.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "name is mandatory")
    @Size(message = "The name must contain at least 5 characters", min = 5)
    private String name;
    private String description;
}
