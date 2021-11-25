package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {

    Rectangle hitBox;
    Vector2[] projectilesPosition;
    int powerLevel;
    int maxProjectiles;

    public Player(float movementSpeed, int health, float xPosition, float yPosition,
                  float width, float height, Texture entityTexture,
                  float projectileWidth, float projectileHeight, float projectileMovementSpeed,
                  float rateOfFire, Texture projectileTexture) {
        super(movementSpeed, health, xPosition, yPosition, width, height, entityTexture,
                projectileWidth, projectileHeight, projectileMovementSpeed, rateOfFire,
                projectileTexture);
        hitBox = new Rectangle(xPosition + width * 2 / 5, yPosition + height * 2 / 5, width / 2,
                height / 5);
        projectilesPosition = new Vector2[5];
        projectilesPosition[0] = new Vector2(0, 0);
        projectilesPosition[0].set(thisRectangle.x + thisRectangle.width / 2,
                thisRectangle.y + thisRectangle.height);
        powerLevel = 1;
        maxProjectiles = 1;
    }

    @Override
    public Projectile[] shootOnPattern() {
        Projectile[] projectiles = new Projectile[maxProjectiles];
        for (int i = 0; i < maxProjectiles; i++) {
            projectiles[i] = new Projectile(projectileMovementSpeed, 0,
                    projectilesPosition[i].x, projectilesPosition[i].y, projectileWidth,
                    projectileHeight, projectileTexture);
        }
        timeSinceLastShot = 0;

        return projectiles;
    }

    public void levelUp() {
        powerLevel++;
        switch (powerLevel) {
            case 2: {
                maxProjectiles = 2;
                projectilesPosition[0].set(hitBox.x, thisRectangle.y + thisRectangle.height);
                projectilesPosition[1] = new Vector2(hitBox.x + hitBox.width,
                        thisRectangle.y + thisRectangle.height);
                break;
            }
            case 4: {
                maxProjectiles = 3;
                projectilesPosition[2] = new Vector2(thisRectangle.x + thisRectangle.width / 2,
                        thisRectangle.y + thisRectangle.height);
                break;
            }
            case 6: {
                maxProjectiles = 4;
                projectilesPosition[2].set(hitBox.x - hitBox.width, thisRectangle.y + thisRectangle.height);
                projectilesPosition[3] = new Vector2(hitBox.x + hitBox.width * 2,
                        thisRectangle.y + thisRectangle.height);
                break;
            }
            case 9: {
                maxProjectiles = 5;
                projectilesPosition[4] = new Vector2(thisRectangle.x + thisRectangle.width / 2,
                        thisRectangle.y + thisRectangle.height);
                break;
            }
        }
    }

    @Override
    public boolean intersects(Rectangle otherRectangle) {
        return hitBox.overlaps(otherRectangle);
    }

    @Override
    public void translate(float xChange, float yChange) {
        super.translate(xChange, yChange);
        hitBox.setPosition(hitBox.x + xChange, hitBox.y + yChange);
        for (int i = 0; i < maxProjectiles; i++) {
            projectilesPosition[i].add(xChange, yChange);
        }
    }
}
