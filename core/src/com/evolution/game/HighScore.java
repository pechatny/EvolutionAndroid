package com.evolution.game;

public class HighScore {
    public String name;
    public Integer score;

    public HighScore init(String name, Integer score) {
        this.name = name;
        this.score = score;

        return this;
    }
}

