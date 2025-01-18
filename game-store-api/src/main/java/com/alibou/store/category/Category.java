package com.alibou.store.category;

import com.alibou.store.common.BaseEntity;
import com.alibou.store.game.Game;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@NamedQuery(
    name = "category.findByName",
    query = """
        SELECT cat FROM Category cat
        WHERE cat.name =:catName
        ORDER BY cat.name asc
    """
)
@NamedEntityGraph(
    name = "graph.category.games",
    attributeNodes = @NamedAttributeNode("games")
)
public class Category extends BaseEntity {

    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Game> games;

    public void addGame(Game game) {
        games.add(game);
        game.setCategory(this);
    }

    public void removeGame(Game game) {
        games.remove(game);
        game.setCategory(null);
    }
}
