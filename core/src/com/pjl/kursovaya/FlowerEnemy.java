package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;

public class FlowerEnemy extends Enemy {
    private float yDirection = -1;
    private final float dirChangeFrequency = 2f;
    private float timeSinceLastChange = 0f;

    public FlowerEnemy(float movementSpeed, int health, float xPosition, float yPosition, float width,
                       float height, Texture enemyTexture, float projectileWidth, float projectileHeight,
                       float projectileMovementSpeed, float rateOfFire, Texture projectileTexture) {
        super(movementSpeed, health, xPosition, yPosition, width, height, enemyTexture, projectileWidth,
                projectileHeight, projectileMovementSpeed, rateOfFire, projectileTexture, "Flw");
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceLastChange += deltaTime;
        if (timeSinceLastChange > dirChangeFrequency) {
            yDirection *= -1;
            timeSinceLastChange -= dirChangeFrequency;
        }
    }

    @Override
    public Projectile[] shootOnPattern() {
        Projectile[] projectiles = new Projectile[3];
        projectiles[0] = new Projectile(projectileMovementSpeed, -projectileMovementSpeed / 2,
                thisRectangle.x + thisRectangle.width / 2 - projectileWidth,
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        projectiles[1] = new Projectile(projectileMovementSpeed, projectileMovementSpeed / 2,
                thisRectangle.x + thisRectangle.width / 2 + projectileWidth,
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        timeSinceLastShot = 0;
        projectiles[2] = new Projectile(projectileMovementSpeed, 0,
                thisRectangle.x + thisRectangle.width / 2,// - Math.round(thisRectangle.width*0.25) ,//+ projectileWidth,
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);

        return projectiles;
    }

    @Override
    public void moveOnPattern(float delta) {
        translate(0, movementSpeed * yDirection * delta);
    }
}
