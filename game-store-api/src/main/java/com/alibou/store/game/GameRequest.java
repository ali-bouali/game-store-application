package com.alibou.store.game;

import java.util.List;

public record GameRequest(
        String title, // perform a check, to not allow duplicates
        String categoryId, // we need to check that the cat exists (relationship with this entity / table)
        List<String> platforms // we need to check that the platforms exist (relationship with this entity / table)
) {
}
