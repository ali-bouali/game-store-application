package com.alibou.store.game;

import lombok.Data;

import java.util.List;


public class GameException extends RuntimeException{
    public GameException(String message) {
        super(message);
    }
    public GameException(List<String> warnings) {
        super(String.join("\n", warnings));
    }

}
