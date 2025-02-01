package com.alibou.store.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, String>, JpaSpecificationExecutor<Game> {

    boolean existsByTitle(String title);

}
