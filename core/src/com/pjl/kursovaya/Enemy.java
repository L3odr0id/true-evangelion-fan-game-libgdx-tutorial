package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

abstract public class Enemy extends Entity {

    public String name;
    Vector2 directionVector;

    public Enemy(float movementSpeed, int health, float xPosition, float yPosition,
                 float width, float height, Texture enemyTexture,
                 float projectileWidth, float projectileHeight, float projectileMovementSpeed,
                 float rateOfFire, Texture projectileTexture, String name) {
        super(movementSpeed, health, xPosition, yPosition, width, height, enemyTexture,
                projectileWidth, projectileHeight, projectileMovementSpeed, rateOfFire,
                projectileTexture);
        directionVector = new Vector2(0, -1);
        this.name = name;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    abstract public void moveOnPattern(float delta);

}
