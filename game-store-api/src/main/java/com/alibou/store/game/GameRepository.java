package com.alibou.store.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, String>, JpaSpecificationExecutor<Game> {

    boolean existsByTitle(String title);

    @Query("""
        SELECT COUNT(g) FROM Game g
        WHERE g.category.id = :categoryId
    """
    ) long countByCategoryId(final String categoryId);
}
