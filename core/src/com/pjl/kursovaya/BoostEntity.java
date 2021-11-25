package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public abstract class BoostEntity {
    public Rectangle thisRectangle;
    float movementSpeed;
    Texture boosterTexture, bubbleTexture;

    public BoostEntity(float movementSpeed, float xPosition, float yPosition, float width, float height,
                       Texture boosterTexture) {
        this.movementSpeed = movementSpeed;
        this.boosterTexture = boosterTexture;
        //this.bubbleTexture = bubbleTexture;
        this.thisRectangle = new Rectangle(xPosition, yPosition, width, height);
    }

    public abstract void boosterAction(Player player);

    public void move(float delta) {
        thisRectangle.setPosition(thisRectangle.x, thisRectangle.y - movementSpeed * delta);
    }

    public void draw(Batch batch) {
        batch.draw(boosterTexture, thisRectangle.x, thisRectangle.y,
                thisRectangle.width, thisRectangle.height);
    }
}
