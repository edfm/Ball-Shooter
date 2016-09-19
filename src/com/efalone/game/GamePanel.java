package com.efalone.game;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	private static final long serialVersionUID = 1L;
	
	// FIELDS
	public static int WIDTH = 640;
	public static int HEIGHT = 480;
	
	private Thread thread;
	
	private boolean running;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private Collision coll;
	
	public static Player player;
	public static ArrayList<Bullet> bullets;
	public static ArrayList<Enemy> enemies;
	
	private static EnemySpawner enemySpawner;
	
	private int FPS = 60;
	private double avFPS;
	
	// CONSTRUCTOR
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	// FUNCTIONS
	public void addNotify(){
		super.addNotify();
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
	
	public void run(){
		running = true;
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		player = new Player();
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		
		enemySpawner = new EnemySpawner();
		
		coll = new Collision();
		
		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;
		
		int frameCount = 0;
		int maxFrameCount = FPS;
		
		long targetTime = 1000 / FPS;
		
		// GAME LOOP
		while(running){
			
			startTime = System.nanoTime();
			
			gameUpdate();
			gameRender();
			gameDraw();
			
			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			
			waitTime = targetTime - URDTimeMillis;
			
			try{
				Thread.sleep(waitTime);
			} catch(Exception e){}
			
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if(frameCount == maxFrameCount){
				avFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
			}
		}
		System.exit(0);
	}
	
	private void gameUpdate(){
		
		if(!player.dead) player.update();
		
		enemySpawner.update();
		
		// enemy update
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
			
			Enemy e = enemies.get(i);
			
			int ex = e.getX();
			int ey = e.getY();
			int ew = e.getWidth();
			int eh = e.getHeight();
			
			int px = player.getX();
			int py = player.getY();
			int pw = player.getWidth();
			int ph = player.getHeight();
			
			
			//collision player-enemy
			if(coll.check(ex, ey, ew, eh, px, py, pw, ph)){
				player.lives--;
				e.hit();
				i--;
				break;
			}
			
			if(enemies.get(i).isOutOfScreen()){
				enemies.get(i).hit();
				i--;
				break;
			}
				
		}
		// bullet update
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
			if(bullets.get(i).canRemove()){
				bullets.remove(i);
				i--;
			}
		}
		// enemy-bullet collision update
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			int bx = b.getX();
			int by = b.getY();
			int bw = b.getWidth();
			int bh = b.getHeight();
			
			for (int j = 0; j < enemies.size(); j++) {
				Enemy e = enemies.get(j);
				
				int ex = e.getX();
				int ey = e.getY();
				int ew = e.getWidth();
				int eh = e.getHeight();
				
				if(coll.check(bx, by, bw, bh, ex, ey, ew, eh)) {
					player.score++;
					e.hit();
					bullets.remove(i);
					i--;
					break;
				}
			}
		}
		
		//check enemy dead
		for (int i = 0; i < enemies.size(); i++) {
			if(enemies.get(i).isDead()){
				enemies.remove(i);
			}
		}
	}
	
	private void gameRender(){
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//Display the background
		g.setColor(new Color(0,80,255));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Display objects
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		
		if(!player.dead){ 
			player.draw(g);
		} else {
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(3));
			g.drawString("Game Over", (WIDTH / 2) - 30, (HEIGHT / 2));
		}
		
		//Display a box
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, WIDTH, 28);
		//Display text
		g.setColor(Color.WHITE);
		g.drawString("FPS " + (int)avFPS, 10, 16);
		g.drawString("LIVES " + player.lives, 60, 16);
		g.drawString("BULLETS ON SCREEN " + bullets.size(), 115, 16);
		g.drawString("ENEMIES ON SCREEN " + enemies.size(), 265, 16);
		g.drawString("SCORE " + player.score, 413, 16);
	}
	
	private void gameDraw(){
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent keyEvent){}
	
	public void keyPressed(KeyEvent keyEvent){
		
		if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT){
			player.setLeft(true);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT){
			player.setRight(true);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_UP){
			player.setUp(true);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN){
			player.setDown(true);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_Z){
			player.setFiring(true);
		}
		
		if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
		if(keyEvent.getKeyCode() == KeyEvent.VK_R){
			restart();
		}	
	}
	
	public void keyReleased(KeyEvent keyEvent){
		if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT){
			player.setLeft(false);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT){
			player.setRight(false);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_UP){
			player.setUp(false);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN){
			player.setDown(false);
		}
		if(keyEvent.getKeyCode() == KeyEvent.VK_Z){
			player.setFiring(false);
		}
	}
	
	private void restart() {
		
	}
}