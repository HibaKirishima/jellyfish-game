package main;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

public class GamePanel extends JPanel implements Runnable, Serializable {
    private static final long serialVersionUID = 1L;

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile for characters, objects, etc.
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 40 x 40 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 760=48 x 16 pixels *changeable
    public final int screenHeight = tileSize * maxScreenRow; // 576=48 x 12 pixels *changeable

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    int FPS = 60;
    
    //System
    TileManager tileM = new TileManager(this);
    // Time
    KeyHandler keyH = new KeyHandler();
    Sound music=new Sound();
    Sound se=new Sound();//sound effect
    public CollisionChecker cChecker=new CollisionChecker(this);
    public AssetSetter aSetter=new AssetSetter(this);
    public UI ui=new UI(this);
    Thread gameThread;
    
    //Entity and Object
    public Player player = new Player(this, keyH);
    public SuperObject obj[]=new SuperObject[10];//we can create up to 10 objects in the game
    
     

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // set the size of this class(JPanel)
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // when set true, all drawings from this component will be done in an offscreen
                                       // painting buffer
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    public void setupGame() {
    	aSetter.setObject();
    	
    	playMusic(0);//according to soundURL[0]
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;// 0.0166666 SECONDS
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {

            update();

            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void update() {
        // Update game logic here aka player position
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw graphics here

        Graphics2D g2 = (Graphics2D) g;

        // Calculate camera position
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        int playerX = player.worldX + tileSize / 2;
        int playerY = player.worldY + tileSize / 2;

        int dx = centerX - playerX;
        int dy = centerY - playerY;

        // Draw graphics here
        tileM.draw(g2);
        
        //OBJECT
        for(int i=0; i<obj.length;i++) {
        	if(obj[i] !=null) {
        		obj[i].draw(g2, this);
        	}
        }

        // Adjust player position based on camera
        int playerScreenX = playerX + dx;
        int playerScreenY = playerY + dy;

        player.draw(g2, playerScreenX, playerScreenY);
        
        //UI
        ui.draw(g2);

        g2.dispose();
    }
    public void playMusic(int i) {
    	music.setFile(i);
    	music.play();
    	music.loop();
    }
    public void stopMusic() {
    	music.stop();
    }
    public void playSE(int i) {
    	se.setFile(i);
    	se.play();
    }
}
