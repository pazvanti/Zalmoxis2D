package com.zalmoxis2d.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zalmoxis2d.Zalmoxis2D;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		config.fullscreen = false;
		config.title = "Zalmoxis2D Demo and Test";
		config.resizable = true;
		new LwjglApplication(new Zalmoxis2D(), config);
	}
}
