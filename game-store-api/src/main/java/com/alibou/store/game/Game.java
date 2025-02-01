package com.alibou.store.game;

import com.alibou.store.category.Category;
import com.alibou.store.comment.Comment;
import com.alibou.store.common.BaseEntity;
import com.alibou.store.platform.Platform;
import com.alibou.store.whishlist.WishList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class Game extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String title;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Platform> platforms;
    private String coverPicture;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "game", orphanRemoval = true)
    private List<Comment> comments;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "game_wishlist",
            joinColumns = {
                    @JoinColumn(name = "game_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "wishlist_id")
            }
    )
    private List<WishList> wishlists;

    public void addWishlist(WishList wishlist) {
        this.wishlists.add(wishlist);
        wishlist.getGames().add(this);
    }

    public void removeWishlist(WishList wishlist) {
        this.wishlists.remove(wishlist);
        wishlist.getGames().remove(this);
    }

    public void addPlatform(Platform platform) {
        this.platforms.add(platform);
        platform.getGames().add(this);
    }

    public void removePlatform(Platform platform) {
        this.platforms.remove(platform);
        platform.getGames().remove(this);
    }
}
