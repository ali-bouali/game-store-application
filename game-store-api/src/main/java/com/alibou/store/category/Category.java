package com.alibou.store.category;

import com.alibou.store.common.BaseEntity;
import com.alibou.store.game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class Category extends BaseEntity {

    private String name;
    private String description;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private List<Game> games;
}
