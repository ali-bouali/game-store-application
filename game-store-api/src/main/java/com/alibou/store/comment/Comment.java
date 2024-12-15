package com.alibou.store.comment;


import com.alibou.store.common.BaseEntity;
import com.alibou.store.game.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment extends BaseEntity {

    private String content;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
}
