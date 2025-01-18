package com.alibou.store.game;

import com.alibou.store.category.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                .imageUrl(game.getCoverPicture())
                .build();
    }

    public List<GameResponse> toGameResponses(List<Game> games) {
        final List<GameResponse> gameResponses = new ArrayList<>();
        for (Game game : games) {
            gameResponses.add(toGameResponse(game));
        }
        return gameResponses;
    }
}
