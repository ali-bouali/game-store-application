package com.alibou.store.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, String> {

    long countByGameId(String gameId);


    @Modifying
    @Query(value = "DELETE FROM comment WHERE game_id = :gamedId",
            nativeQuery = true)
    void deleteByGameId(@Param("gameId") String gameId);
}
