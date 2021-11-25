package com.pjl.kursovaya.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pjl.kursovaya.BulletHellGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 900;
		config.width = 1600;
		new LwjglApplication(new BulletHellGame(), config);
	}
}
