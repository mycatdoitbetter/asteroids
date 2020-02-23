package com.mycatdoitbetter.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	
	public static void main(String[] args) {
		
		LwjglApplicationConfiguration cfg = 
				new LwjglApplicationConfiguration();
		
		cfg.title = "Asteroids";
		cfg.width = 600;
		cfg.height = 500;
		cfg.useGL20 = false;
		cfg.resizable = false;
		
		new LwjglApplication(new Game(), cfg);
		
		
		
	}

}
