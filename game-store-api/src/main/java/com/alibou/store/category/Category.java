package com.alibou.store.category;

import com.alibou.store.common.BaseEntity;
import com.alibou.store.game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedQueries({
    @NamedQuery(name = "Category.namedQueryFindByName",
            query = "SELECT c FROM Category c " +
                    "WHERE c.name LIKE lower(:catName) " +
                    "ORDER BY c.name ASC")
})
public class Category extends BaseEntity {

    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Game> games;
}
