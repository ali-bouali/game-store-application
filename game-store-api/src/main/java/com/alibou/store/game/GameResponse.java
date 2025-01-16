package com.alibou.store.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameResponse {

    private String id;
    private String name;
    private String title;
    private Set<String> platforms;
    private String imageUrl; // the CDN url
}
