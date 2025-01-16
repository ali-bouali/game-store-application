package com.alibou.store.category;

import com.alibou.store.game.GameResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseFull {

    private String id;
    private String name;
    private String description;
    private List<GameResponse> games;
}
