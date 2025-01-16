package com.alibou.store.category;

import com.alibou.store.game.Game;
import com.alibou.store.game.GameResponse;
import com.alibou.store.platform.Platform;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryMapper {

    public Category toCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .name(categoryRequest.name())
                .description(categoryRequest.description())
                .build();
    }

    public CategoryResponseShort toCategoryResponseShort(Category category) {
        return CategoryResponseShort.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public CategoryResponseFull toCategoryResponseFull(Category category) {
        return CategoryResponseFull.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .games(mapGamesToResponse(category.getGames()))
                .build();
    }

    private List<GameResponse> mapGamesToResponse(List<Game> games) {
        return games.stream()
                .map(this::toGameResponse)
                .collect(Collectors.toList());
    }

    private GameResponse toGameResponse(Game game) {
        return GameResponse.builder()
                .id(game.getId())
                .title(game.getTitle())
                .platforms(mapPlatformsToConsoleNames(game.getPlatforms()))
                .imageUrl(game.getCoverPicture())
                .build();
    }

    private Set<String> mapPlatformsToConsoleNames(List<Platform> platforms) {
        return platforms.stream()
                .map(platform -> platform.getConsole().name())
                .collect(Collectors.toSet());
    }
}
