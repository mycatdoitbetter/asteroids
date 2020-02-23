package com.mycatdoitbetter.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mycatdoitbetter.entities.*;
import com.mycatdoitbetter.main.Game;
import com.mycatdoitbetter.managers.GameKeys;
import com.mycatdoitbetter.managers.GameStateManager;

public class PlayState extends GameState {
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	private BitmapFont font;
	
	private Player hudPlayer;
	
	
	
	private Player player;
	private ArrayList <Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;
	
	

	public PlayState(GameStateManager gsm) {
		super(gsm);
		
	}
	
	public void init() {
		
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		FreeTypeFontGenerator gen = 
				new FreeTypeFontGenerator(
						Gdx.files.internal("fonts/Hyperspace Bold.ttf")
						);
		
		font = gen.generateFont(30);
		
		bullets = new ArrayList <Bullet>();
		
		player = new Player(bullets);
		
		asteroids = new ArrayList<Asteroid>();
		
		asteroids.add(new Asteroid(100, 100, Asteroid.LARGE));
		asteroids.add(new Asteroid(200, 100, Asteroid.MEDIUM));
		asteroids.add(new Asteroid(300, 100, Asteroid.SMALL));
		
		level = 1;
				
		spawnAsteroids();
		
		hudPlayer = new Player(null);
		
	}
	
	private void splitAsteroids(Asteroid a) {
		numAsteroidsLeft--;
		if(a.getType() == Asteroid.LARGE) {
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.MEDIUM));
		}
		if(a.getType() == Asteroid.MEDIUM) {
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
			asteroids.add(new Asteroid(a.getx(), a.gety(), Asteroid.SMALL));
		}
	}
	
	private void spawnAsteroids() {
		
		asteroids.clear();
		
		int numToSpawn = 4 + level;
		totalAsteroids = numToSpawn * 7;
		numAsteroidsLeft = totalAsteroids;
		
		for(int i = 0; i < numToSpawn; i++ ) {
			float x = MathUtils.random(Game.WIDTH);
			float y = MathUtils.random(Game.HEIGHT);
			
			float dx = x - player.getx();
			float dy = y - player.gety();
			float dist = (float) Math.sqrt(dx * dx + dy * dy);
			
			while(dist < 100) {
				x = MathUtils.random(Game.WIDTH);
				y = MathUtils.random(Game.HEIGHT);
				dx = x - player.getx();
				dy = y - player.gety();
				dist = (float) Math.sqrt(dx*dx + dy*dy);
			}
			
			asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
		}
	}
	
	
	public void update(float dt) {
		
		// next level
		
		if(asteroids.size() == 0) {
			level++;
			spawnAsteroids();
			
		}
		
		
		// get the player input
		handleInput();
		
		// update player
		player.update(dt);
		if(player.isDead()) {
			if(player.getLives() == 0) {
				gsm.setState(GameStateManager.MENU);
			}
			player.reset();
			player.loseLife();
			return;
			
		}
		
		// update the bullets
		for(int i = 0; i< bullets.size(); i++) {
			bullets.get(i).update(dt);
			if(bullets.get(i).shouldRemove()) {
				bullets.remove(i);
				i--;
			}
		}
		
		// update asteroids
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(dt);
			if(asteroids.get(i).shouldRemove()) {
				asteroids.remove(i);
				i--;
			}
			
		}
		// check collison
		
		checkCollisions();
		
		
	
	}
	private void checkCollisions() {
		
		//player-astereoid
		for(int i = 0; i < asteroids.size(); i++) {
			Asteroid a = asteroids.get(i);
			if(a.intersects(player)) {
				player.hit();
				asteroids.remove(i);
				i--;
				splitAsteroids(a);
				break;
				
			}
		}
		
		
		
		
		// bullet-asteroid
		
		for(int i = 0; i< bullets.size(); i++) {
			
			Bullet b = bullets.get(i);
			for(int j = 0; j < asteroids.size(); j++) {
				Asteroid a = asteroids.get(j);
				if(a.contains(b.getx(), b.gety())) {
					bullets.remove(i);
					i--;
					asteroids.remove(j);
					j--;
					splitAsteroids(a);
					player.incrementScore(a.getScore());
					break;
					
				}
			}
			
		}
	}
	
	public void draw() {
		
		//draw player
		player.draw(sr);
		
		
		// draw bullets
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(sr);
		}
		
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		} 
		
		sb.setColor(1, 1, 1, 1);
		sb.begin();
		font.draw(sb, Long.toString(player.getScore()), 40, 480);
		sb.end();
		for(int i = 0; i < player.getLives(); i++) {
			hudPlayer.setPosition(30 + i * 15, 440);
			hudPlayer.draw(sr);
		}
		
	}
	
	
	

	public void handleInput() {
			if(!player.isHit()) {
			player.setLeft(GameKeys.isDown(GameKeys.LEFT));
			player.setRight(GameKeys.isDown(GameKeys.RIGHT));
			player.setUp(GameKeys.isDown(GameKeys.UP));
			if(GameKeys.isPressed(GameKeys.SPACE)) {
				player.shoot();}
			}
	}
	public void dispose() {}
		
	}


