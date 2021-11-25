package com.pjl.kursovaya;

import com.badlogic.gdx.graphics.Texture;

public class PowerUp extends BoostEntity {
    public PowerUp(float movementSpeed, float xPosition, float yPosition, float width, float height,
                   Texture boosterTexture) {
        super(movementSpeed, xPosition, yPosition, width, height, boosterTexture);
    }

    @Override
    public void boosterAction(Player player) {
        player.levelUp();
    }
}
