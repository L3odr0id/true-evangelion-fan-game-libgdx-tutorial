package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;

public class EyeEnemy extends Enemy {

    private float angle = 0f;
    private final float radius = 100f;
    private final float circleX;
    private final float circleY;

    public EyeEnemy(float movementSpeed, int health, float xPosition, float yPosition, float width,
                    float height, Texture enemyTexture, float projectileWidth, float projectileHeight,
                    float projectileMovementSpeed, float rateOfFire, Texture projectileTexture) {
        super(movementSpeed, health, xPosition, yPosition, width, height, enemyTexture, projectileWidth,
                projectileHeight, projectileMovementSpeed, rateOfFire, projectileTexture, "Eye");
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

    @Override
    public void moveOnPattern(float delta) {
        thisRectangle.setPosition((float) (circleX + radius * Math.cos(angle)),
                (float) (circleY + radius * Math.sin(angle)));
    }

    @Override
    public Projectile[] shootOnPattern() {
        Projectile[] projectiles = new Projectile[3];
        projectiles[0] = new Projectile(projectileMovementSpeed, 0,
                thisRectangle.x + Math.round(thisRectangle.width * 0.25),//- thisRectangle.width + thisRectangle.width/10,
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        projectiles[1] = new Projectile(projectileMovementSpeed, 0,
                thisRectangle.x + thisRectangle.width - Math.round(thisRectangle.width * 0.25),//+ projectileWidth,
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        projectiles[2] = new Projectile(projectileMovementSpeed, 0,
                thisRectangle.x + thisRectangle.width / 2,// - Math.round(thisRectangle.width*0.25) ,//+ projectileWidth,
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        timeSinceLastShot = 0;

        return projectiles;
    }
}
