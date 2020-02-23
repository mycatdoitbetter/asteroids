package com.mycatdoitbetter.managers;

import com.mycatdoitbetter.gamestates.GameState;
import com.mycatdoitbetter.gamestates.MenuState;
import com.mycatdoitbetter.gamestates.PlayState;

public class GameStateManager {
	
	private GameState gameState;
	
	public static final int MENU = 0;
	public static final int PLAY = 8591;
	public static final int GAMEOVER = 8821;
	
	public GameStateManager() {
		setState(MENU);
	}
	
	public void setState(int state) {
		
		
		
		if(gameState != null) gameState.dispose();
		
		if(state == MENU) {
			gameState = new MenuState(this);
			
		}
		
		if(state == PLAY) {
			
			gameState = new PlayState(this);
		}
	}
	
	public void update(float dt) {
		gameState.update(dt);
	}
	
	public void draw() {gameState.draw();}

}
