package com.alibou.store.game;

import com.alibou.store.category.CategoryRepository;
import com.alibou.store.comment.CommentRepository;
import com.alibou.store.common.PageResponse;
import com.alibou.store.file.FileStorageService;
import com.alibou.store.platform.Console;
import com.alibou.store.platform.Platform;
import com.alibou.store.platform.PlatformRepository;
import com.alibou.store.whishlist.WishList;
import com.alibou.store.whishlist.WishListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final PlatformRepository platformRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final WishListRepository wishListRepository;
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

    public void updateGame(String gameId, GameRequest gameRequest) {
        final Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (!game.getTitle().equals(gameRequest.title()) && gameRepository.existsByTitle(gameRequest.title())) {
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

        final List<String> platformIds = platforms.stream()
                .map(Platform::getId)
                .collect(Collectors.toList());

        List<Platform> currentPlatforms = game.getPlatforms();

        List<Platform> newPlatforms = platformRepository.findAllById(platformIds);

        List<Platform> platformsToAdd = new ArrayList<>(newPlatforms);
        platformsToAdd.removeAll(currentPlatforms);

        List<Platform> platformsToRemove = new ArrayList<>(currentPlatforms);
        platformsToRemove.removeAll(newPlatforms);

        for(Platform platform: platformsToAdd) {
            game.addPlatform(platform);
        }

        for (Platform platform : platformsToRemove) {
            game.removePlatform(platform);
        }

        game.setTitle(gameRequest.title());
        gameRepository.save(game);
    }

    // Todo upload just for now on the system and not the s3

    public void uploadGameImage(final MultipartFile file, final String gameId) {
        final Game game = gameRepository.findById(gameId)
                .orElseThrow(
                        () -> new EntityNotFoundException(format("The game with ID:: %s was not found", gameId))
                );
        final String savedFile = fileStorageService.saveFile(file, gameId, "games");
        game.setCoverPicture(savedFile);
        gameRepository.save(game);
    }

    public PageResponse<GameResponse> findAllGames(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Game> gamesPage = gameRepository.findAll(pageable);
        List<GameResponse> gameResponses = gamesPage.stream()
                .map(this.gameMapper::toGameResponse)
                .toList();

        return PageResponse.<GameResponse>builder()
                .content(gameResponses)
                .pageNumber(gamesPage.getNumber())
                .size(gamesPage.getSize())
                .totalElements(gamesPage.getTotalElements())
                .totalPages(gamesPage.getTotalPages())
                .isFirst(gamesPage.isFirst())
                .isLast(gamesPage.isLast())
                .build();
    }

    @Transactional
    public void deleteGame(String gameId, boolean confirm) {
        // todo check the comments
        long commentsCount = commentRepository.countByGameId(gameId);
        long wishListCount = wishListRepository.countByGameId(gameId);


        final List<String> warnings = new ArrayList<>();

        if (commentsCount > 0) {
            warnings.add("Comment count is greater than 0");
            System.out.println("The current game has comments: " + commentsCount);
        }
        if (wishListCount > 0) {
            warnings.add("Wish list count is greater than 0");
            System.out.println("The current game has wishlist: " + wishListCount);
        }

        if (!warnings.isEmpty() && !confirm) {
            // todo add a custom exp
            throw new GameException("One or more warnings");
        }

        gameRepository.deleteById(gameId);

        // todo I would like you to show me how you would remove the game from the wishlists
        // Get the wishlists by game id
        // List<WishList> wishLists = wishListRepository.findAllByGamesId(gameId);
        // for each list delete the game
//        wishLists.forEach(wishlist -> {
//            List<Game> games = wishlist.getGames()
//                    .stream()
//                    .filter(game -> !Objects.equals(game.getId(), gameId))
//                    .toList();
//            wishlist.setGames(games);
//            wishListRepository.save(wishlist);
//        });

        // Delete the comments for that game
        commentRepository.deleteByGameId(gameId);
        // Or we can just execute the native query
        wishListRepository.deleteGameInWishlists(gameId);
    }

}
