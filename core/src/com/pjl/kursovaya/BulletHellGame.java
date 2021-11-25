package com.pjl.kursovaya;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;


public class BulletHellGame extends Game {

    public static float WINDOW_HEIGHT = 900;
    public static float WINDOW_WIDTH = 1600;
    public static Random random = new Random();
    public SpriteBatch batch;
    public GameCamera cam;
    Screen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        cam = GameCamera.INSTANCE;
        BulletHellGame game = this;
        gameScreen = new MainMenuScreen(game, WINDOW_WIDTH, WINDOW_HEIGHT);
        setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        gameScreen.resize(width, height);
    }
}


