package com.alibou.store.category.dto;

import com.alibou.store.game.GameResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseFull {

    private String id;
    private String name;
    private String description;
    private List<GameResponse> games;
}
