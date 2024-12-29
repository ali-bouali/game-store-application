package com.alibou.store.game;

import com.alibou.store.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public void something(String categoryId) {

        var games = gameRepository.findAllByCategoryId(categoryId);
    }

    public void somethingElse() {
        List<Game> games = gameRepository.findAll();
        for (Game game : games) {
            game.setTitle(game.getTitle().toUpperCase());
        }
        gameRepository.saveAll(games);

        // in one line of code
        gameRepository.transformGamesTitleToUpperCase();
    }
}
