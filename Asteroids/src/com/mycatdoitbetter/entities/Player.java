package com.mycatdoitbetter.entities;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.mycatdoitbetter.main.Game;

public class Player extends SpaceObject {
	
	private final int MAX_BULLETS = 4;
	
	private ArrayList <Bullet> bullets;
		
	
	private boolean left;
	private boolean right;
	private boolean up;
	
	private float maxSpeed;
	private float acceleration;
	private float deceleration;
	
	private boolean hit;
	private boolean dead;
	
	private float hitTimer;
	private float hitTime;
	private Line2D.Float[] hitLines;
	private Point2D.Float[] hitLinerVector;
	
	private long score;
	private int extraLives;
	private long requiredScore;
	
	public Player(ArrayList<Bullet> bullets) {
		
		this.bullets = bullets;
		
		x = Game.WIDTH / 2;
		y = Game.WIDTH / 2;
		
		maxSpeed = 300;
		acceleration = 200;
		deceleration = 50;
		
		shapex = new float[4];
		shapey = new float[4];
		
		radians = 3.1415f / 2;
		rotationSpeed = 5;
		
		hit = false;
		hitTimer = 0;
		hitTime = 2;
		
		
		score = 0;
		extraLives = 3;
		requiredScore = 5000;
			
	}
	
	private void setShape() {
		
		shapex[0] = x + MathUtils.cos(radians) * 8;
		shapey[0] = y + MathUtils.sin(radians) * 8;
		
		shapex[1] = x + MathUtils.cos(radians - 4 * 3.1415f / 5) * 8;
		shapey[1] = y + MathUtils.sin(radians - 4 * 3.1415f / 5) * 8;
		
		shapex[2] = x + MathUtils.cos(radians + 3.1415f) * 5;
		shapey[2] = y + MathUtils.sin(radians + 3.1415f) * 5;
		
		shapex[3] = x + MathUtils.cos(radians + 4 * 3.1415f / 5) * 8;
		shapey[3] = y + MathUtils.sin(radians + 4 * 3.1415f / 5) * 8;
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) {right = b; }
	public void setUp(boolean b) { up = b; }
	
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		setShape();
	}
	
	public boolean isHit() {return hit;}
	public boolean isDead() {return dead;}
	public void reset() {
		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;
		setShape();
		hit = dead = false;
	}
	
	public long getScore() {return score;}
	public int getLives() {return extraLives;}
	
	public void loseLife() {extraLives--;}
	public void incrementScore(long l) {score += l;}
	
	
	public void shoot() {
		
		if(bullets.size() == MAX_BULLETS) return;
		bullets.add(new Bullet(x, y, radians));
	}
	
	public void hit() {
		
		if(hit) return;
		
		hit = true;
		dx = dy = 0;
		left = right = up = false;
		
		hitLines = new Line2D.Float[4];
		for(int i = 0, j = hitLines.length - 1; i < hitLines.length;
				j = i++) {
			hitLines[i] = new Line2D.Float(
					shapex[i], shapey[i], shapex[j], shapey[j]);
		}
		
		hitLinerVector = new Point2D.Float[4];
		hitLinerVector[0] = new Point2D.Float(
				MathUtils.cos(radians + 1.5f),
				MathUtils.sin(radians + 1.5f)
				);
		hitLinerVector[1] = new Point2D.Float(
				MathUtils.cos(radians - 1.5f),
				MathUtils.sin(radians - 1.5f)
				);
		hitLinerVector[2] = new Point2D.Float(
				MathUtils.cos(radians - 2.8f),
				MathUtils.sin(radians - 2.8f)
				);
		hitLinerVector[3] = new Point2D.Float(
				MathUtils.cos(radians + 2.8f),
				MathUtils.sin(radians + 2.8f)
				);
		
	}
	
	public void update(float dt) {
		// check the hit
		
		if(hit) {	
			hitTimer += dt;
			if(hitTimer > hitTime) {
				dead = true;
				hitTimer = 0;
			} 
			for(int i = 0; i < hitLines.length; i++) {
				hitLines[i].setLine(
						hitLines[i].x1 + hitLinerVector[i].x * 10 * dt,
						hitLines[i].y1 + hitLinerVector[i].y * 10 * dt,
						hitLines[i].x2 + hitLinerVector[i].x * 10 * dt,
						hitLines[i].y2 + hitLinerVector[i].y * 10 * dt
						
						
						);
				
			}
			return;
			
		}
		
		//check lives
		if(score >= requiredScore) {
			extraLives++;
			requiredScore += 5000;
		}
		
		
		if(left) {
			radians += rotationSpeed * dt;			
		}
		else if (right) {
			radians -= rotationSpeed * dt;
		}
		if(up) {
			dx += MathUtils.cos(radians) * acceleration * dt;
			dy += MathUtils.sin(radians) * acceleration * dt;
			
		}
		
		float vec = (float) Math.sqrt(dx * dx + dy * dy);
		if(vec > 0) {
			dx -= (dx / vec) * deceleration * dt;
			dy -= (dy / vec) * deceleration * dt;
			
		}
		if(vec > maxSpeed) {
			dx = (dx / vec) * maxSpeed;
			dy = (dy / vec) * maxSpeed;
		}
		
		x += dx * dt;
		y += dy * dt;
		
		setShape();
		
		wrap();
	}
	
	public void draw(ShapeRenderer sr) {
		
		sr.setColor(1, 1, 1, 1);
		
		sr.begin(ShapeType.Line);
		
		if(hit) {
			for(int i = 0; i < hitLines.length; i++ ) {
				sr.line(
						hitLines[i].x1,
						hitLines[i].y1,
						hitLines[i].x2,
						hitLines[i].y2	
						);
			}
			sr.end();
			return;
		}
		
		for(int i = 0, j = shapex.length - 1;
				i< shapex.length;
				j = i++) {
			
			sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
		}
		
		sr.end();		
		
	}

}
