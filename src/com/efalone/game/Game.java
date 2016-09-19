package com.efalone.game;

import javax.swing.JFrame;

public class Game{
	
	public static void main(String args[]){
		JFrame window = new JFrame("Game Practice");
//		window.setSize(640,480);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		window.setLocationRelativeTo(null);
		window.setResizable(false);
		
		window.setContentPane(new GamePanel());
		
		window.pack();
		window.setVisible(true);
	}
}