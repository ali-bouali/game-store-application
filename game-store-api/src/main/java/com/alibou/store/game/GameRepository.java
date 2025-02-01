package com.alibou.store.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GameRepository extends JpaRepository<Game, String>, JpaSpecificationExecutor<Game> {

    long countByCategoryId(String categoryId);

    boolean existsByTitle(String title);
}
