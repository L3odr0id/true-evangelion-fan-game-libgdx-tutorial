package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Projectile {
    public float yMovementSpeed;
    public float xMovementSpeed;
    public Rectangle boundingBox;

    Texture projectileTexture;

    public Projectile(float yMovementSpeed, float xMovementSpeed, float xPosition, float yPosition, float width,
                      float height, Texture projectileTexture) {
        this.yMovementSpeed = yMovementSpeed;
        this.xMovementSpeed = xMovementSpeed;
        this.boundingBox = new Rectangle(xPosition, yPosition, width, height);
        this.projectileTexture = projectileTexture;
    }

    public void draw(Batch batch) {
        batch.draw(projectileTexture, boundingBox.x, boundingBox.y, boundingBox.width,
                boundingBox.height);
    }

    /*public Rectangle getBoundingBox(){
        return boundingBox;
    }

    public void moveProjectile(float value){
        this.boundingBox.y += value;
    }*/
}
