package com.pjl.kursovaya.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pjl.kursovaya.BoostEntity;
import com.pjl.kursovaya.Boss;
import com.pjl.kursovaya.BulletHellGame;
import com.pjl.kursovaya.Enemy;
import com.pjl.kursovaya.EyeEnemy;
import com.pjl.kursovaya.FlowerEnemy;
import com.pjl.kursovaya.HealthUp;
import com.pjl.kursovaya.Player;
import com.pjl.kursovaya.PowerUp;
import com.pjl.kursovaya.Projectile;
import com.pjl.kursovaya.SlimeEnemy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

public class GameScreen implements Screen {

    private final float WORLD_WIDTH = BulletHellGame.WINDOW_WIDTH;
    private final float WORLD_HEIGHT = BulletHellGame.WINDOW_HEIGHT;
    private final float TOUCH_THRESHOLD = 0.5f;
    BulletHellGame game;
    BitmapFont font;
    float hudVerticalMargin, hudLeftX, hudRightX, hudCenterX, hudRow1Y, hudRow2Y, hudSectionWidth;
    //screen
    private final Camera camera;
    private final Viewport viewport;
    //graphics
    private final SpriteBatch batch;
    //private Texture background;
    private final Texture[] backgrounds;
    private final Texture playerTexture;
    private final Texture enemySlimeTexture;
    private final Texture enemyEyeTexture;
    private final Texture enemyFlowerTexture;
    private final Texture playerProjectileTexture;
    private final Texture enemyProjectileTexture;
    private final Texture healthPickUpTexture;
    private final Texture powerUpPickUpTexture;
    private final Texture bossTexture;
    //timing
    //private int backgroundOffset;
    private final float[] backgroundOffsets = {0, 0, 0, 0};
    private final float bgMaxScrollingSpeed;
    private final float timeBetweenPowerUpSpawns = 15f;
    private float powerUpSpawnTimer = 0f;
    private final float timeBetweenHealthUpSpawns = 4f;
    private float healthUpSpawnTimer = 0f;
    private final Player player;
    private final LinkedList<Enemy> enemies;
    private final LinkedList<Projectile> playerProjectileList;
    private final LinkedList<Projectile> enemyProjectileList;
    private final LinkedList<BoostEntity> pickUps;
    private int score;
    private final int waveNumber;
    private FileReader fileReader;
    private final BufferedReader reader;

    public GameScreen(BulletHellGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        backgrounds = new Texture[4];
        backgrounds[0] = new Texture("Starscape00.png");
        backgrounds[1] = new Texture("Starscape01.png");
        backgrounds[2] = new Texture("Starscape02.png");
        backgrounds[3] = new Texture("Starscape03.png");

        bgMaxScrollingSpeed = (float) (WORLD_HEIGHT) / 4;

        playerTexture = new Texture("aaa_wunder.png");
        enemySlimeTexture = new Texture("Shamshel.png");
        enemyEyeTexture = new Texture("Sahaquieldesign.png");
        enemyFlowerTexture = new Texture("Lelieldesign.png");
        bossTexture = new Texture("Ramieldesign.png");
        playerProjectileTexture = new Texture("player_projectile.png");
        enemyProjectileTexture = new Texture("enemy_projectile.png");
        healthPickUpTexture = new Texture("heart_icon.png");
        powerUpPickUpTexture = new Texture("power_icon.png");

        player = new Player(400f, 7, WORLD_WIDTH / 2,
                WORLD_HEIGHT / 4, 159, 204, playerTexture,
                20f, 16f, 500f, 0.3f,
                playerProjectileTexture);
        enemies = new LinkedList<>();

        playerProjectileList = new LinkedList<>();
        enemyProjectileList = new LinkedList<>();
        pickUps = new LinkedList<>();

        waveNumber = 1;

        batch = new SpriteBatch();

        prepareHUD();

        try {
            fileReader = new FileReader("android/assets/Wave Data/wave_" + waveNumber + "_data.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        reader = new BufferedReader(fileReader);

        try {
            nextWave();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void prepareHUD() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);

        font = fontGenerator.generateFont(fontParameter);

        font.getData().setScale(0.4f);

        hudVerticalMargin = font.getCapHeight() / 2;
        hudLeftX = hudVerticalMargin;
        hudRightX = WORLD_WIDTH * 2 / 3 - hudLeftX;
        hudCenterX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    @Override
    public void render(float delta) {
        batch.begin();

        if (player.health <= 0) {
            this.dispose();
            game.setScreen(new GameOverScreen(game, score, WORLD_WIDTH, WORLD_HEIGHT));
            return;
        }

        renderBackground(delta);

        detectInput(delta);

        player.update(delta);

        //draw pick ups
        ListIterator<BoostEntity> pickUpsIterator = pickUps.listIterator();
        while (pickUpsIterator.hasNext()) {
            BoostEntity pickUp = pickUpsIterator.next();
            pickUp.move(delta);
            pickUp.draw(batch);
            if (pickUp.thisRectangle.y + pickUp.thisRectangle.height < 0) pickUpsIterator.remove();
        }

        ListIterator<Enemy> iterator = enemies.listIterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.moveOnPattern(delta);
            enemy.update(delta);
            enemy.draw(batch);
        }

        player.draw(batch);

        renderProjectiles(delta);

        spawnPickUps(delta);

        detectCollisions();

        updateAndRenderHUD();

        batch.end();
    }

    private void updateAndRenderHUD() {
        font.draw(batch, "Score", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false);
        font.draw(batch, "Health", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), hudRightX, hudRow2Y,
                hudSectionWidth, Align.right, false);
        font.draw(batch, String.format(Locale.getDefault(), "%06d", player.health), hudLeftX, hudRow2Y,
                hudSectionWidth, Align.left, false);
    }

    /*private void spawnEnemy(float delta){
        enemySpawnTimer+=delta;
        if (enemySpawnTimer > timeBetweenEnemySpawns) {
            enemies.add(new SlimeEnemy(120f, 10, WORLD_WIDTH / 2,
                    WORLD_HEIGHT * 3 / 5, 135, 176, enemySlimeTexture,
                    20f, 60f, 450f, 1.5f,
                    enemyProjectileTexture));
            enemySpawnTimer-=timeBetweenEnemySpawns;
        }
    }*/

    private void spawnPickUps(float delta) {
        powerUpSpawnTimer += delta;
        healthUpSpawnTimer += delta;
        if (powerUpSpawnTimer > timeBetweenPowerUpSpawns) {
            pickUps.add(new PowerUp(90f, 100 + BulletHellGame.random.nextFloat() * WORLD_WIDTH % (WORLD_WIDTH - 100),
                    WORLD_HEIGHT + 120f, 60, 60, powerUpPickUpTexture));
            powerUpSpawnTimer -= timeBetweenPowerUpSpawns;
        }
        if (healthUpSpawnTimer > timeBetweenHealthUpSpawns) {
            pickUps.add(new HealthUp(80f, 100 + BulletHellGame.random.nextFloat() * WORLD_WIDTH % (WORLD_WIDTH - 100),
                    WORLD_HEIGHT + 120f, 60, 60, healthPickUpTexture));
            healthUpSpawnTimer -= timeBetweenHealthUpSpawns;
        }
    }

    private void nextWave() throws IOException {
        reader.readLine();
        int quantity = Integer.parseInt(reader.readLine());
        String enemyName = reader.readLine();
        String newName;
        float xPosition = 0, yPosition = 0;
        for (int i = 0; i < quantity; i++) {
            newName = reader.readLine();
            if (newName.equals("Next wave")) return;
            else if (newName.equals("Slm") || newName.equals("Eye") || newName.equals("Flw")) {
                enemyName = newName;
                xPosition = Integer.parseInt(reader.readLine());
            } else xPosition = Integer.parseInt(newName);
            yPosition = Integer.parseInt(reader.readLine());
            if (enemyName.equals("Slm")) {
                enemies.add(new SlimeEnemy(120f, 5, xPosition,
                        yPosition, 156, 436, enemySlimeTexture,
                        20f, 60f, 450f, 1.5f,
                        enemyProjectileTexture));
            } else if (enemyName.equals("Eye")) {
                enemies.add(new EyeEnemy(80f, 10, xPosition,
                        yPosition, 500, 188, enemyEyeTexture,
                        20f, 60f, 450f, 1.5f,
                        enemyProjectileTexture));
            } else if (enemyName.equals("Flw")) {
                enemies.add(new FlowerEnemy(60f, 15, xPosition,
                        yPosition, 200, 200, enemyFlowerTexture,
                        20f, 60f, 450f, 1.5f,
                        enemyProjectileTexture));
            } else if (enemyName.equals("Boss")) {
                enemies.add(new Boss(60f, 25, xPosition,
                        yPosition, 228, 228, bossTexture,
                        20f, 60f, 450f, 1.5f,
                        enemyProjectileTexture));
            }
        }
    }

    private void detectInput(float delta) {
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -player.thisRectangle.x;
        downLimit = -player.thisRectangle.y;
        rightLimit = WORLD_WIDTH - player.thisRectangle.x - player.thisRectangle.width;
        upLimit = WORLD_HEIGHT - player.thisRectangle.y - player.thisRectangle.height;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            player.translate(Math.min(player.movementSpeed * delta, rightLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            player.translate(0f, Math.min(player.movementSpeed * delta, upLimit));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {
            player.translate(Math.max(-player.movementSpeed * delta, leftLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {
            player.translate(0f, Math.max(-player.movementSpeed * delta, downLimit));
        }

        if (Gdx.input.isTouched()) {
            float xTouchPosition = Gdx.input.getX();
            float yTouchPosition = Gdx.input.getY();

            Vector2 touchPoint = new Vector2(xTouchPosition, yTouchPosition);
            touchPoint = viewport.unproject(touchPoint);

            Vector2 playerCenter = new Vector2(
                    player.thisRectangle.x + player.thisRectangle.width / 2,
                    player.thisRectangle.y + player.thisRectangle.height / 2);

            float touchDistance = touchPoint.dst(playerCenter);
            if (touchDistance > TOUCH_THRESHOLD) {
                float xTouchDiff = touchPoint.x - playerCenter.x;
                float yTouchDiff = touchPoint.y - playerCenter.y;

                float xMove = xTouchDiff / touchDistance * player.movementSpeed * delta;
                float yMove = yTouchDiff / touchDistance * player.movementSpeed * delta;

                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);
                if (yMove > 0) yMove = Math.min(yMove, upLimit);
                else yMove = Math.max(yMove, downLimit);

                player.translate(xMove, yMove);
            }
        }
    }

    /*private void moveEnemies(Enemy enemy, float delta){
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -enemy.thisRectangle.x;
        downLimit = -enemy.thisRectangle.y;
        rightLimit = WORLD_WIDTH - enemy.thisRectangle.x - enemy.thisRectangle.width;
        upLimit = WORLD_HEIGHT - enemy.thisRectangle.y - enemy.thisRectangle.height;

        float xMove = enemy.getDirectionVector().x * enemy.movementSpeed * delta;
        float yMove = enemy.getDirectionVector().y * enemy.movementSpeed * delta;

        if (xMove>0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);
        if (yMove>0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        enemy.translate(xMove,yMove);
    }*/

    private void detectCollisions() {
        ListIterator<Projectile> iterator = playerProjectileList.listIterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            ListIterator<Enemy> enemyListIterator = enemies.listIterator();
            while (enemyListIterator.hasNext()) {
                Enemy enemy = enemyListIterator.next();
                if (enemy.intersects(projectile.boundingBox)) {
                    if (enemy.checkHit(projectile)) {
                        enemyListIterator.remove();
                        switch (enemy.name) {
                            case "Slm":
                                score += 69;
                                break;
                            case "Eye":
                                score += 228;
                                break;
                            case "Flw":
                                score += 1337;
                                break;
                            case "Boss":
                                score += 1488;
                                this.dispose();
                                game.setScreen(new GameCompletedScreen(game, score, WORLD_WIDTH, WORLD_HEIGHT));
                                return;
                        }

                        if (enemies.isEmpty()) {
                            try {
                                nextWave();
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                    iterator.remove();
                    break;
                }
            }
        }

        iterator = enemyProjectileList.listIterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            if (player.intersects(projectile.boundingBox)) {
                final int before = player.health;
                player.checkHit(projectile);
                final int after = player.health;
                if (before > after)
                    score -= 15;
                iterator.remove();
            }
        }

        ListIterator<BoostEntity> pickUpsIterator = pickUps.listIterator();
        while (pickUpsIterator.hasNext()) {
            BoostEntity pickUp = pickUpsIterator.next();
            if (player.intersects(pickUp.thisRectangle)) {
                pickUp.boosterAction(player);
                pickUpsIterator.remove();
            }
        }
    }

    private void renderBackground(float deltaTime) {
        backgroundOffsets[0] += deltaTime * bgMaxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * bgMaxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * bgMaxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * bgMaxScrollingSpeed;

        for (int layer = 0; layer < backgroundOffsets.length; layer++) {
            if (backgroundOffsets[layer] > WORLD_HEIGHT)
                backgroundOffsets[layer] = 0;
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer],
                    WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer] + WORLD_HEIGHT,
                    WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    private void renderProjectiles(float delta) {
        if (player.canFire()) {
            Projectile[] projectiles = player.shootOnPattern();
            playerProjectileList.addAll(Arrays.asList(projectiles));
        }

        ListIterator<Enemy> enemyListIterator = enemies.listIterator();
        while (enemyListIterator.hasNext()) {
            Enemy enemy = enemyListIterator.next();
            if (enemy.canFire()) {
                Projectile[] projectiles = enemy.shootOnPattern();
                enemyProjectileList.addAll(Arrays.asList(projectiles));
            }
        }

        ListIterator<Projectile> iterator = playerProjectileList.listIterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.draw(batch);
            projectile.boundingBox.y += projectile.yMovementSpeed * delta;
            //if (projectile.boundingBox.y > WORLD_HEIGHT)
            //iterator.remove();
        }

        iterator = enemyProjectileList.listIterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.draw(batch);
            projectile.boundingBox.y -= projectile.yMovementSpeed * delta;
            projectile.boundingBox.x += projectile.xMovementSpeed * delta;
            // if (projectile.boundingBox.y + projectile.boundingBox.height < 0)
            //iterator.remove();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
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
    public void show() {

    }

    @Override
    public void dispose() {

    }
}
