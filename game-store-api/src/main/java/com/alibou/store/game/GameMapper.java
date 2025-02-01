package com.alibou.store.game;

import com.alibou.store.category.Category;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GameMapper {
    public Game toGame(GameRequest gameRequest) {
        return Game.builder()
                .title(gameRequest.title())
                .category(
                        Category.builder()
                                .id(gameRequest.categoryId())
                                .build()
                )
                .build();
    }

    public GameResponse toGameResponse(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .title(game.getTitle())
                // fixme set the CDN URL
                .imageUrl("FIX-ME")
                .platforms(
                        game.getPlatforms()
                                .stream()
                                .map(p -> p.getConsole().name())
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
