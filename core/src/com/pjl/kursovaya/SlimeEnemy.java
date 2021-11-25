package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;

public class SlimeEnemy extends Enemy {

    private float angle = 0f;
    private final float radius = 100f;
    private final float circleX;
    private final float circleY;

    public SlimeEnemy(float movementSpeed, int health, float xPosition, float yPosition, float width,
                      float height, Texture enemyTexture, float projectileWidth, float projectileHeight,
                      float projectileMovementSpeed, float rateOfFire, Texture projectileTexture) {
        super(movementSpeed, health, xPosition, yPosition, width, height, enemyTexture, projectileWidth,
                projectileHeight, projectileMovementSpeed, rateOfFire, projectileTexture, "Slm");
        this.circleX = xPosition;
        this.circleY = yPosition;
        this.rateOfFire = 1f;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        angle += deltaTime * 1.5;
        if (angle >= 360f) angle -= 360f;
    }

    //moving in a circle pattern;

    @Override
    public void moveOnPattern(float delta) {
        thisRectangle.setPosition((float) (circleX + radius * 1.15 * Math.cos(angle)),
                (float) (circleY + radius / 3 * Math.sin(angle)));
    }

    @Override
    public Projectile[] shootOnPattern() {
        Projectile[] projectiles = new Projectile[1];
        projectiles[0] = new Projectile(projectileMovementSpeed, 0,
                thisRectangle.x + thisRectangle.width / 2,
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        timeSinceLastShot = 0;

        return projectiles;
    }
}
