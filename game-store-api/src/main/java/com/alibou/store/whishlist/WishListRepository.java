package com.alibou.store.whishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, String> {

    @Query("""
            SELECT COUNT(w)
            FROM WishList w
            JOIN w.games g
            WHERE g.id = :gameId
            """)
    long countByGameId(String gameId);

    // Find all wishlists with gameID
    List<WishList> findAllByGamesId(String gameId);

    @Modifying
    @Query(value="DELETE FROM game_wishlist WHERE game_id = :gameId",
            nativeQuery = true)
    void deleteGameInWishlists(@Param("gameId") String gameId);
}
