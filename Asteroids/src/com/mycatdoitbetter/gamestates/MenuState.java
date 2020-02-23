package com.mycatdoitbetter.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mycatdoitbetter.entities.Asteroid;
import com.mycatdoitbetter.main.Game;
import com.mycatdoitbetter.managers.GameKeys;
import com.mycatdoitbetter.managers.GameStateManager;

public class MenuState extends GameState {
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private BitmapFont titleFont;
	private BitmapFont font;
	
	private final String title = "Asteroids";
	
	private int currentItem;
	private String[] menuItems;
	
	private ArrayList<Asteroid> asteroids;
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
			Gdx.files.internal("fonts/Hyperspace Bold.ttf")
		);
		
		titleFont = gen.generateFont(56);
		titleFont.setColor(Color.WHITE);
		
		font = gen.generateFont(20);
		
		menuItems = new String[] {
			"Play",
			"Quit"
		};
		
		asteroids = new ArrayList<Asteroid>();
		for(int i = 0; i < 6; i++) {
			asteroids.add(
				new Asteroid(
					MathUtils.random(Game.WIDTH),
					MathUtils.random(Game.HEIGHT),
					Asteroid.LARGE
				)
			);
		}
		
		
	}
	
	public void update(float dt) {
		
		handleInput();
		
		for(int i = 0 ; i < asteroids.size(); i++) {
			asteroids.get(i).update(dt);
		}
		
	}
	
	public void draw() {
		
		sb.setProjectionMatrix(Game.cam.combined);
		
		
		// draw asteroids
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		}
		
		sb.begin();
		
		// draw title
		float width = titleFont.getBounds(title).width;
		titleFont.draw(
			sb,
			title,
			(Game.WIDTH - width) / 2,
			350
		);
		
		// draw menu
		for(int i = 0; i < menuItems.length; i++) {
			width = font.getBounds(menuItems[i]).width;
			if(currentItem == i) font.setColor(Color.RED);
			else font.setColor(Color.WHITE);
			font.draw(
				sb,
				menuItems[i],
				(Game.WIDTH - width) / 2,
				180 - 35 * i
			);
		}
		
		sb.end();
		
	}
	
	public void handleInput() {
		
		if(GameKeys.isPressed(GameKeys.UP)) {
			if(currentItem > 0) {
				currentItem--;
			}
		}
		if(GameKeys.isPressed(GameKeys.DOWN)) {
			if(currentItem < menuItems.length - 1) {
				currentItem++;
			}
		}
		if(GameKeys.isPressed(GameKeys.ENTER)) {
			select();
		}
		
	}
	
	private void select() {
		// play
		if(currentItem == 0) {
			gsm.setState(GameStateManager.PLAY);
		}
		else if(currentItem == 1) {
			Gdx.app.exit();
		}
	}
	
	public void dispose() {
		sb.dispose();
		sr.dispose();
		titleFont.dispose();
		font.dispose();
	}

}










