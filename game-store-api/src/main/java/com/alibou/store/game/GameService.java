package com.alibou.store.game;

import com.alibou.store.category.CategoryRepository;
import com.alibou.store.common.PageResponse;
import com.alibou.store.file.FileStorageService;
import com.alibou.store.platform.Console;
import com.alibou.store.platform.Platform;
import com.alibou.store.platform.PlatformRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
// add the Transactional annotation
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;
    private final CategoryRepository categoryRepository;
    private final GameMapper gameMapper;
    private final FileStorageService fileStorageService;

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

    public void updateGame(final String gameId, final GameRequest gameRequest) {
        final Game savedGame = gameRepository.findById(gameId)
                .orElseThrow(
                        () -> new EntityNotFoundException(format("The game with ID:: %s was not found", gameId))
                );

        final Game updatedGame = gameMapper.toGame(gameRequest);
        updatedGame.setId(savedGame.getId());
        gameRepository.save(updatedGame);
    }

    // Todo upload just for now on the system and not the s3
    // I'm using a biFunction just to use new features of java

    BiFunction<MultipartFile, String, String> uploadGameImage = (file, gameId) -> {
        final Game game = gameRepository.findById(gameId)
                .orElseThrow(
                        () -> new EntityNotFoundException(format("The game with ID:: %s was not found", gameId))
                );
        final String savedFile = fileStorageService.saveFile(file, gameId, "games");
        game.setCoverPicture(savedFile);
        gameRepository.save(game);
        return savedFile;
    };

    public void uploadGameImage(final MultipartFile file, final String gameId) {
        uploadGameImage.apply(file, gameId);
    }

    public PageResponse<GameResponse> findAllGames(int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<Game> gamePage = gameRepository.findAll(pageRequest);

        List<GameResponse> gameResponses = gamePage
                .getContent()
                .stream()
                .map(gameMapper::toGameResponse)
                .toList();

        return PageResponse.<GameResponse>builder()
                .number(gamePage.getNumber())
                .size(gamePage.getSize())
                .content(gameResponses)
                .isFirst(gamePage.isFirst())
                .isLast(gamePage.isLast())
                .build();
    }

    public void deleteGame(final String gameId) {
        final Game game = gameRepository.findById(gameId)
                .orElseThrow(
                        () -> new EntityNotFoundException(format("The game with ID:: %s was not found", gameId))
                );
        gameRepository.delete(game);
    }

}
