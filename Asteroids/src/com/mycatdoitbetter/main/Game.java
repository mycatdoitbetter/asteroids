package com.mycatdoitbetter.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mycatdoitbetter.managers.GameInputProcessor;
import com.mycatdoitbetter.managers.GameKeys;
import com.mycatdoitbetter.managers.GameStateManager;

public class Game implements ApplicationListener {
	
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static OrthographicCamera cam;
	
	private GameStateManager gsm;
	
	
	public void create() {
		
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		
		// the camera must be on the same focus of the
		// game screen, but the cam have the middle on
		// the source of the cartesian plan
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH / 2, HEIGHT / 2);
		cam.update();
		
		Gdx.input.setInputProcessor( new GameInputProcessor() );
		gsm = new GameStateManager();
	}
	
	
	public void render() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1); // BLACK IN RGB
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();
		
		
		GameKeys.update();
		
		
	}	
	
	
	public void resize(int width, int height) {}
	public void pause() {}
	public void resume() {}
	public void dispose() {}
	
	

}
