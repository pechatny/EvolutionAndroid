package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class HighScores implements Serializable {

    private ArrayList<HighScore> scores;

    public HighScores() {
        FileHandle file = Gdx.files.local("scores.json");
        String scoreString = file.readString();
        Json json = new Json();
        scores = json.fromJson(ArrayList.class, scoreString);
        if (scores == null) {
            scores = new ArrayList<HighScore>();
        }
    }

    public ArrayList<HighScore> getScores() {
        return scores;
    }

    public void addScore(String name, Integer scores) {
        HighScore score = (new HighScore()).init(name, scores);
        this.scores.add(score);
    }

    public void saveScores() {

        Json json = new Json();
        sort(scores);
        String score = json.toJson(scores);

        FileHandle file = Gdx.files.local("scores.json");
        file.writeString(score, false);
    }

    private ArrayList<HighScore> sort(ArrayList<HighScore> scores) {
        Collections.sort(scores,
                (o1, o2) -> o2.score.compareTo(o1.score));

        return scores;
    }
}
