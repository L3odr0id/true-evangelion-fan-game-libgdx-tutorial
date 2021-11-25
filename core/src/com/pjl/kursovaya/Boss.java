package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;

public class Boss extends Enemy {

    private float angle = 0f;
    private final float radius = 100f;
    private final float circleX;
    private final float circleY;

    public Boss(float movementSpeed, int health, float xPosition, float yPosition, float width,
                float height, Texture bossTexture, float projectileWidth, float projectileHeight,
                float projectileMovementSpeed, float rateOfFire, Texture projectileTexture) {
        super(movementSpeed, health, xPosition, yPosition, width, height, bossTexture, projectileWidth,
                projectileHeight, projectileMovementSpeed, rateOfFire, projectileTexture, "Boss");
        this.circleX = xPosition;
        this.circleY = yPosition;
        this.rateOfFire = rateOfFire;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        angle += deltaTime * 1.5;
        if (angle >= 360f) angle -= 360f;

        rateOfFire = 1.4f + ((float) Math.random() * 2.1f - 1.4f);
        System.out.println(rateOfFire);
    }


    @Override
    public void moveOnPattern(float delta) {
        thisRectangle.setPosition((float) (circleX + radius * Math.cos(angle)),
                (float) (circleY + radius * Math.sin(angle)));
    }


    @Override
    public Projectile[] shootOnPattern() {
        Projectile[] projectiles = new Projectile[3];
        projectiles[0] = new Projectile(projectileMovementSpeed, -projectileMovementSpeed / (1 + (float) (Math.random() * 2 - 1)),
                thisRectangle.x + thisRectangle.width / 2 - projectileWidth,
                thisRectangle.y, projectileWidth, projectileHeight,
                projectileTexture);
        projectiles[1] = new Projectile(projectileMovementSpeed, projectileMovementSpeed / (1 + (float) (Math.random() * 2 - 1)),
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
}
