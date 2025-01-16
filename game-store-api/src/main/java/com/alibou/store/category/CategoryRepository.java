package com.alibou.store.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    // To avoid N+1 queries problem, we fetch the games and platforms of the category
    @Query("""
            SELECT c FROM Category c
            JOIN FETCH c.games g
            JOIN FETCH g.platforms p
            WHERE c.id = :categoryId
            """
    )
    Optional<Category> findByIdWithGamesAndPlatforms(@Param("categoryId") String categoryId);


    // To avoid N+1 queries problem, we fetch the games and platforms of the category
    @Query("SELECT c FROM Category c " +
            "JOIN FETCH c.games g " +
            "JOIN FETCH g.platforms p")
    Page<Category> findAllWithGamesAndPlatforms(Pageable pageable);

    boolean existsByName(String name);
}
