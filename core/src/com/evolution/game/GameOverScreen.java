package com.evolution.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.evolution.game.units.Hero;

import java.util.ArrayList;

public class GameOverScreen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font32;
    private BitmapFont font96;
    private Stage stage;
    private Skin skin;
    public HighScores scores;
    private boolean showScores;

    public GameOverScreen(SpriteBatch batch) {
        this.batch = batch;
        showScores = false;
    }

    @Override
    public void show() {
        scores = new HighScores();
        font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);
        font96 = Assets.getInstance().getAssetManager().get("gomarice96.ttf", BitmapFont.class);
        scores = new HighScores();
        createInput();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font96.draw(batch, "Game Over", 0, 660, 1280, 1, false);

        int scoresSize = scores.getScores().size();
        if(showScores &&  scoresSize > 0){
            int padding = 600;
            int size = scoresSize > 10 ? 11 : scoresSize;
            for(int i = 0; i < size; i++){
                padding -= 33;
                ArrayList<HighScore> scoresList = scores.getScores();
                HighScore score = scoresList.get(i);
                font32.draw(batch, score.name + " " + score.score, 0, padding, 1280, 1, false);
            }
        }

        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void createInput(){
        skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        skin.add("font32", font32);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = skin.getFont("font32");
        skin.add("simpleButtonSkin", textButtonStyle);

        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        final TextField txtUsername = new TextField("", setupSkin());
        txtUsername.setWidth(320);
        txtUsername.setHeight(70);
        txtUsername.setPosition(640 -160, 210);
        stage.addActor(txtUsername);
        stage.setKeyboardFocus(txtUsername);

        final Button btnSubmit = new TextButton("Save", skin, "simpleButtonSkin");
        btnSubmit.setPosition(640 - 160, 110);
        stage.addActor(btnSubmit);

        final Button btnExitGame = new TextButton("Exit Game", skin, "simpleButtonSkin");
        btnExitGame.setPosition(640 - 160, 10);
        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        stage.addActor(btnExitGame);

        btnSubmit.addListener(new ChangeListener() {
                                @Override
                                public void changed(ChangeEvent event, Actor actor) {
                                    saveScores(txtUsername.getText(), Hero.score);
                                    btnSubmit.remove();
                                    txtUsername.remove();
                                    createGUI();
                                    showScores = true;
                                }
                            });

    }

    protected void saveScores(String name, Integer score){
        scores.addScore(name, score);
        scores.saveScores();
    }

    public void createGUI() {
        final Button btnMenu = new TextButton("Menu", skin, "simpleButtonSkin");
        btnMenu.setPosition(640 - 160, 110);
        stage.addActor(btnMenu);

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Setups a simple skin without any ressource
     *
     * @return A simple Skin
     */
    public Skin setupSkin() {
        Skin skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        skin.add("default", new BitmapFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = font32;
        skin.add("default", textButtonStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font96;
        labelStyle.fontColor = Color.BLACK;
        skin.add("default", labelStyle);

        Label.LabelStyle redLabelStyle = new Label.LabelStyle();
        redLabelStyle.font = font96;
        redLabelStyle.fontColor = Color.RED;
        skin.add("red", redLabelStyle);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font32;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.background = skin.newDrawable("white", Color.LIGHT_GRAY);
        skin.add("default", textFieldStyle);
        return skin;
    }
}
