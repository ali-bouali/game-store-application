package com.alibou.store.whishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishListRepository extends JpaRepository<WishList, String> {

    @Query("""
            SELECT COUNT(w)
            FROM WishList w
            JOIN w.games g
            WHERE g.id = :gameId
            """)
    long countByGameId(String gameId);
}
