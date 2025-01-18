package com.alibou.store.category.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private String id;
    private String name;
    private String description;
}
