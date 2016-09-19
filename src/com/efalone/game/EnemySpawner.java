package com.efalone.game;

public class EnemySpawner {
	
	// FIELDS
	
	int spawnerDelay;
	long spawnerTimer;
	// CONSTRUCTOR
	
	public EnemySpawner() {
		
		spawnerDelay = 800;
		spawnerTimer = System.nanoTime();
	}
	
	// FUNCTIONS

	public void update() {
		long elapsed = (System.nanoTime() - spawnerTimer) / 1000000;
		if(elapsed > spawnerDelay){
			GamePanel.enemies.add(new Enemy());
			spawnerTimer = System.nanoTime();
		}
	}
}
