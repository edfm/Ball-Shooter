package com.efalone.game;

import java.awt.Graphics2D;

public interface Entity {
	
	int x=0;
	int y=0;
	
	int width=0;
	int height=0;
	
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public void update();
	public void draw(Graphics2D g);
	
}
