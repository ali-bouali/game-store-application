package com.alibou.store.game;

import com.alibou.store.category.Category;
import com.alibou.store.common.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

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

    public PageResponse<Game> pagedResult(final int page, final int size) {

        Pageable pageable = PageRequest.of(page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "title",
                        "createTime"
                )
        );

        Page<Game> pagedResult = gameRepository.findAllByCategoryName("anyCat", pageable);

        return PageResponse.<Game>builder()
                .content(pagedResult.getContent())
                .totalPages(pagedResult.getTotalPages())
                .totalElements(pagedResult.getNumberOfElements())
                .isFirst(pagedResult.isFirst())
                .isLast(pagedResult.isLast())
                .build();

    }

    public void queryByExampleCaseSensitive() {

        Game game = new Game();
        game.setTitle("The witcher III"); // iny my DB --> the Witcher iii
        game.setSupportedPlatforms(SupportedPlatforms.PS);

        Example<Game> example = Example.of(game);

        Optional<Game> myGame = gameRepository.findOne(example);
    }

    public void queryByExampleCaseInsensitive() {

        Game game = new Game();
        game.setTitle("The witcher III"); // iny my DB --> the Witcher iii
        game.setSupportedPlatforms(SupportedPlatforms.PS);

        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase();

        Example<Game> example = Example.of(game, matcher);

        Optional<Game> myGame = gameRepository.findOne(example);
    }

    public void queryByExampleCustomMatching() {

        Game game = new Game();
        game.setTitle("witcher"); // iny my DB --> the Witcher iii
        game.setSupportedPlatforms(SupportedPlatforms.PS);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("supportedPlatforms", ExampleMatcher.GenericPropertyMatchers.exact());

        Example<Game> example = Example.of(game, matcher);

        // the output query
        /*
            select * from game
            where  lower(title) like '%witcher%'
            and supportedPlatforms = 'PS'
         */

        List<Game> myGame = gameRepository.findAll(example);
    }

    public void queryByExampleIgnoringProperties() {
        Game game = new Game();
        game.setTitle("witcher"); // iny my DB --> the Witcher iii

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase())
                .withIgnorePaths("supportedPlatforms", "coverPicture");

        Example<Game> example = Example.of(game, matcher);
        List<Game> myGame = gameRepository.findAll(example);

    }


    /*
    QueryByExampleExecutor LIMITATIONS
    1- Nesting and grouping statements are not supported
        => select * from game where (title = ?0 and supportedPlatforms = ?1) OR coverPicture is not null

    2- String matching only includes exact, case-sensitive, starts, ends, contains and regex
    3- All types other than String are exact-match only
     */


    public void specificationExample1() {

        Specification<Game> spec = buildSpecificationWithAndOperator("witcher", SupportedPlatforms.PC);
        List<Game> games = gameRepository.findAll(spec);

    }

    public void specificationExample2() {

        Specification<Game> spec = buildSpecificationWithOrOperator("witcher", SupportedPlatforms.PC);
        List<Game> games = gameRepository.findAll(spec);

    }

    private Specification<Game> buildSpecificationWithOrOperator(String title, SupportedPlatforms platform) {
        Specification<Game> spec = Specification.where(null);

        if (StringUtils.hasLength(title)) {
            spec = spec.and(GameSpecifications.byGameTitle(title));

        }
        if (platform != null) {
           spec = spec.or(GameSpecifications.bySupportedPlatform(platform));

        }


        return spec;
    }

    private Specification<Game> buildSpecificationWithAndOperator(String title, SupportedPlatforms platform) {
        Specification<Game> spec = Specification.where(null);

        if (StringUtils.hasLength(title)) {
            spec = spec.and(GameSpecifications.byGameTitle(title));

        }
        if (platform != null) {
           spec = spec.and(GameSpecifications.bySupportedPlatform(platform));

        }


        return spec;
    }


}
