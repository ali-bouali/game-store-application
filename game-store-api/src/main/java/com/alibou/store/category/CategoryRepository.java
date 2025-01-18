package com.alibou.store.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("""
        SELECT COUNT(c) > 0 FROM Category c
        WHERE upper(c.name) = upper(:name)
    """)
    boolean existByName(String name);

    @Query(name = "category.findByName" )
    Category findByName(String catName);

    @EntityGraph(value = "graph.category.games", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
        SELECT c FROM Category c
    """)
    Page<Category> findAllWithGames(Pageable pageable);
}
