package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {
    public float movementSpeed;
    public int health;

    public Rectangle thisRectangle;

    Texture entityTexture, projectileTexture;

    float projectileWidth, projectileHeight;
    float projectileMovementSpeed;
    float rateOfFire;
    float timeSinceLastShot = 0;

    public Entity(float movementSpeed, int health, float xPosition, float yPosition,
                  float width, float height, Texture entityTexture,
                  float projectileWidth, float projectileHeight, float projectileMovementSpeed,
                  float rateOfFire, Texture projectileTexture) {
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.thisRectangle = new Rectangle(xPosition, yPosition, width, height);
        this.entityTexture = entityTexture;
        this.projectileTexture = projectileTexture;
        this.projectileWidth = projectileWidth;
        this.projectileHeight = projectileHeight;
        this.projectileMovementSpeed = projectileMovementSpeed;
        this.rateOfFire = rateOfFire;
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canFire() {
        return (timeSinceLastShot - rateOfFire >= 0);
    }

    public abstract Projectile[] shootOnPattern();

    public boolean intersects(Rectangle otherRectangle) {
        return thisRectangle.overlaps(otherRectangle);
    }

    public boolean checkHit(Projectile projectile) {
        if (health > 0) {
            health--;
            return false;
        }
        return true;
    }

    public void draw(Batch batch) {
        batch.draw(entityTexture, thisRectangle.x, thisRectangle.y,
                thisRectangle.width, thisRectangle.height);
    }

    public void translate(float xChange, float yChange) {
        thisRectangle.setPosition(thisRectangle.x + xChange, thisRectangle.y + yChange);
    }

}
