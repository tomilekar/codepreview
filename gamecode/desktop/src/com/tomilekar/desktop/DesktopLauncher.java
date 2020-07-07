package com.tomilekar.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.tomilekar.MainScreen;
import com.tomilekar.helpers.GameInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = GameInfo.GAME_HEIGHT;
		config.width = GameInfo.GAME_WIDTH;
		new LwjglApplication(new MainScreen(), config);
	}
}
