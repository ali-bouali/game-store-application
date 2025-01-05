package com.alibou.store.game;

import com.alibou.store.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, String>, JpaSpecificationExecutor<Game> {

    // find all games by category (v1)
    List<Game> findAllByCategory(Category category);

    // find all games by category (v2)
    List<Game> findAllByCategoryId(String categoryId);


    // find all the games where the name equals 'Action'
    /*
        select g.* from category c
        inner join game g on g.category_id = c.id
        where c.name = 'Action'
     */
    List<Game> findAllByCategoryName(String categoryName);


    Page<Game> findAllByCategoryName(String categoryName, Pageable pageable);


    // JPQL
    /*@Query("""
            SELECT g FROM Game g
            INNER JOIN Category c ON g.category.id = c.id
            WHERE c.name LIKE :catName
            """)*/
    @Query("""
            SELECT g FROM Game g
            INNER JOIN g.category c
            WHERE c.name LIKE :catName
            """)
    List<Game> findAllByCat(@Param("catName") String catName);


    @Query("""
            UPDATE Game SET title = upper(title)
            """)
    @Modifying
    void transformGamesTitleToUpperCase();

    @Query("""
            select g.id as gameId,
                   g.title AS gameTitle
            from Game g
            """)
    List<GameRepresentation1> findAllGames();

    @Query("""
            select g.title AS gameTitle,
                   g.category.name AS categoryName
            from Game g
            """)
    List<GameRepresentation2> finAllGames2();
}
