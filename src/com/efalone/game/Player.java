package com.efalone.game;

import java.awt.*;

public class Player implements Entity{
	//	FIELDS
	private int x;
	private int y;
	private int r;
	
	private int width;
	private int height;
	
	private int dx;
	private int dy;
	private int speed;
	
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	private boolean firing;
	private long firingTimer;
	private long firingDelay;
	
	private Color color;
	private Color color2;
	
	public int lives;
	public int score;
	public boolean dead;
	
	//	CONSTRUCTOR
	public Player(){
		
		x = GamePanel.WIDTH / 2;
		y = GamePanel.HEIGHT / 2;
		r = 16;
		
		width = height = r * 2;
		
		dx = 0;
		dy = 0;
		speed = 5;
		
		color = Color.WHITE;
		color2 = color.darker();
		
		lives = 3;
		score = 0;
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 200;
	}
	
	//	FUNCTIONS
	public void setLeft(boolean b){ left = b; }
	public void setRight(boolean b){ right = b; }
	public void setUp(boolean b){ up = b; }
	public void setDown(boolean b){ down = b; }
	public void setFiring(boolean b){ firing = b; }
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public void update(){
		if(left){
			dx = -speed;
		}
		if(right){
			dx = speed;
		}
		if(up){
			dy = -speed;
		}
		if(down){
			dy = speed;
		}
		
		x += dx;
		y += dy;
		
		if(x < r) x = r;
		if(y < 28 + r) y = 28 + r;
		if(x > GamePanel.WIDTH - r) x = GamePanel.WIDTH - r;
		if(y > GamePanel.HEIGHT - r) y = GamePanel.HEIGHT - r;
		
		dx = 0;
		dy = 0;
		
		if(firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if(elapsed > firingDelay) {
				GamePanel.bullets.add(new Bullet(x, y + r));
				firingTimer = System.nanoTime();
			}
		}
		
		if(lives <= 0) {
			dead = true;
		}
	}
	
	public void draw(Graphics2D g){
		g.setColor(color2);
		g.fillOval(x - r, y - r, width, height);
		
		g.setStroke(new BasicStroke(3));
		g.setColor(color);
		g.drawOval(x - r, y - r, width, height);
		g.setStroke(new BasicStroke(1));
	}
}