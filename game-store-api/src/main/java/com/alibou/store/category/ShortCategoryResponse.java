package com.alibou.store.category;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortCategoryResponse {
    private String id;
    private String name;
    private String description;
}
