package com.alibou.store.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("""
        SELECT COUNT(c) > 0 FROM Category c
        WHERE upper(c.name) = upper(:name)
    """)
    boolean existByName(String name);


}
