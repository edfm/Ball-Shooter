package com.efalone.game;

import java.awt.*;

public class Enemy implements Entity{

	// FIELDS
	private int x;
	private int y;
	private int r;
	
	private int width;
	private int height;
	
	private int dy;
	private int speed;
	
	private boolean dead;
	
	// CONSTRUCTOR
	public Enemy() {
		x = (int)((Math.random() * (GamePanel.WIDTH - 50) - 200) + 200);
		y = 0;
		
		r = 14;
		
		width = 2 * r;
		height = 2 * r;
		
		speed = 4;
		
		dy = speed;
		
		dead = false;
	}
	
	// FUNCTIONS
	public int getX() { return x; }
	public int getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public boolean isDead() { return dead; }
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public void hit() {
		dead = true;
	}
	
	public void update() {
		y += dy;
	}
	
	public boolean isOutOfScreen() {
		if(y > GamePanel.HEIGHT - r) {
			return true;
		}
		
		return false;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.RED.darker());
		g.fillOval(x - r, y - r, width, height);
		
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.RED);
		g.drawOval(x - r, y - r, width, height);
		g.setStroke(new BasicStroke(1));
	}
	
}
