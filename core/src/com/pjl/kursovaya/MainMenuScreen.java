package com.pjl.kursovaya;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.pjl.kursovaya.screens.GameScreen;


public class MainMenuScreen implements Screen {

    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int EXIT_BUTTON_Y = 350;
    private static final int PLAY_BUTTON_Y = 480;
    private static final int LOGO_WIDTH = 400;
    private static final int LOGO_HEIGHT = 250;
    private static final int LOGO_Y = 450;

    final BulletHellGame game;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture logo;
    Texture bg;

    private final float WIDTH;
    private final float HEIGHT;

    public MainMenuScreen(final BulletHellGame game, float width, float height) {
        this.game = game;
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        logo = new Texture("logo.png");
        bg = new Texture("menu_bg.png");

        WIDTH = width;
        HEIGHT = height;

//        game.scrollingBackground.setSpeedFixed(true);
//        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        final MainMenuScreen mainMenuScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //Exit button
                int x = Math.round(WIDTH / 2 - EXIT_BUTTON_WIDTH / 2);
                if (game.cam.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y) {
                    mainMenuScreen.dispose();
                    Gdx.app.exit();
                }

                //Play game button
                x = Math.round(WIDTH / 2 - PLAY_BUTTON_WIDTH / 2);
                if (game.cam.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && HEIGHT - game.cam.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && HEIGHT - game.cam.getInputInGameWorld().y > PLAY_BUTTON_Y) {
                    mainMenuScreen.dispose();
                    game.setScreen(new GameScreen(game));
                }

                return super.touchUp(screenX, screenY, pointer, button);
            }

        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.batch.draw(bg, 0, 0, 1600, 900);
//        game.scrollingBackground.updateAndRender(delta, game.batch);

        int x = Math.round(WIDTH / 2 - EXIT_BUTTON_WIDTH / 2);
        if (game.cam.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && HEIGHT - game.cam.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && HEIGHT - game.cam.getInputInGameWorld().y > EXIT_BUTTON_Y) {
            game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        } else {
            game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        x = Math.round(WIDTH / 2 - PLAY_BUTTON_WIDTH / 2);
        if (game.cam.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH && game.cam.getInputInGameWorld().x > x && HEIGHT - game.cam.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && HEIGHT - game.cam.getInputInGameWorld().y > PLAY_BUTTON_Y) {
            game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        } else {
            game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

//        game.batch.draw(logo, WIDTH / 2 - LOGO_WIDTH / 2, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        Gdx.input.setInputProcessor(null);
    }

}