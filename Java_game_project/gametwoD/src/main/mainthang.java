package main;

import javax.swing.JFrame;

public class mainthang {

	public static void main(String[]args) {
			
	JFrame window=new JFrame(); 
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//this lets the window properly close when user clicks the close button X
	window.setResizable(false);//so we can not resize this window
	window.setTitle("2D adventure!");
			
	GamePanel gamePanel=new GamePanel();
	window.add(gamePanel);
			
	window.pack();//causes window to be sized to fit the preferred size and layouts of its subcomponents(GamePanel)
			
			
	window.setLocationRelativeTo(null);
			//the window will be displayed at the center of the screen
	window.setVisible(true);//so we can see the window
	
	    gamePanel.setupGame();{
	    	
	    }
	    gamePanel.startGameThread();
	
	
	
	
		}

	}



