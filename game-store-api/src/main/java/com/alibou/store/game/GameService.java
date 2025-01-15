package com.alibou.store.game;

import com.alibou.store.category.CategoryRepository;
import com.alibou.store.common.PageResponse;
import com.alibou.store.platform.Console;
import com.alibou.store.platform.Platform;
import com.alibou.store.platform.PlatformRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;
    private final CategoryRepository categoryRepository;
    private final GameMapper gameMapper;

    public String saveGame(final GameRequest gameRequest) {

        if (gameRepository.existsByTitle(gameRequest.title())) {
            log.info("Game already exists: {}", gameRequest.title());
            // todo create a dedicated exp
            throw new RuntimeException("Game already exists");
        }

        final List<Console> selectedConsoles =  gameRequest.platforms()
                .stream()
                .map(Console::valueOf)
                .toList();

        final List<Platform> platforms = platformRepository.findAllByConsoleIn(selectedConsoles);

        if (platforms.size() != selectedConsoles.size()) {
            log.warn("Received a non supported platforms. Received: {} - Stored: {}", selectedConsoles, platforms);
            // todo dedicated exception
            throw new RuntimeException("One or more platforms are not supported");
        }

        if (!categoryRepository.existsById(gameRequest.categoryId())) {
            log.warn("Received a category that does not exist: {}", gameRequest.categoryId());
            // todo dedicated exception
            throw new RuntimeException("Category does not exist");
        }

        final Game game = gameMapper.toGame(gameRequest);
        game.setPlatforms(platforms);
        final Game savedGame = gameRepository.save(game);
        // todo do we need to assign the game to the selectedPlatforms!?
        return savedGame.getId();
    }

    public void updateGame(String gameId, GameRequest gameRequest) {}

    public String uploadGameImage(MultipartFile file, String gameId) {return null;}

    public PageResponse<GameResponse> findAllGames(int page, int size) {return null;}

    public void deleteGame(String gameId) {}

}
