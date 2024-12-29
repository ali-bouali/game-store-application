package com.alibou.store.game;

import com.alibou.store.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, String> {

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
}
