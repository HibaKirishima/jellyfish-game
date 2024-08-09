package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    
    public final int screenX; // Indicates where we draw player in the screen
    public final int screenY;
    public int hasKey=0;//indicates how many keys the player actually has

    public Player(GamePanel gp, KeyHandler keyH) {

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
        // Making square solid area of the character to be the only collision with other
        // objects
        solidArea = new Rectangle(8, 16, 32, 32);// x,y,width,height
        solidAreaDefaultX=solidArea.x;
        solidAreaDefaultY=solidArea.y;

        setDefaultValues();
        getPlayerImage();

    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23; // Player position in the map, like in the middle
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/player/New Piskel-9.png.png"));

            up2 = ImageIO.read(getClass().getResourceAsStream("/player/New Piskel-10.png.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/player/New Piskel-3.png.png"));

            down2 = ImageIO.read(getClass().getResourceAsStream("/player/New Piskel-4.png.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/player/New Piskel-1.png.png"));

            left2 = ImageIO.read(getClass().getResourceAsStream("/player/New Piskel-7.png.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/player/New Piskel-2.png.png"));

            right2 = ImageIO.read(getClass().getResourceAsStream("/player/New Piskel-8.png.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (keyH.upPressed) {
            direction = "up";
        } else if (keyH.downPressed) {
            direction = "down";
        } else if (keyH.leftPressed) {
            direction = "left";
        } else if (keyH.rightPressed) {
            direction = "right";
        }else {
        	// If no movement keys are pressed, do not update the player's position
        	return;
        }
        // CHECK TILE COLLISION
        collisionOn = false;
        gp.cChecker.checkTile(this);
        
        //CHECK OBJECT COLLISION
        int objIndex=gp.cChecker.checkObject(this,true);
        pickUpObject(objIndex);
        

        // IF COLLISION IS FALSE, PLAYER CAN MOVE
        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 14) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
    }
    public void pickUpObject(int i) {
    	
    	if(i!=999) {//any number is fine as long as its not used by the object array's index
    		
    		//gp.obj[i]=null;//delete the object that we just touched
    		String objectName=gp.obj[i].name;
    		
    		
    		switch(objectName) {
    		case "Key":
    			gp.playSE(1);
    			hasKey++;
    			gp.obj[i]=null;
    			gp.ui.showMessage("You got a key!");
    			
    			break;
    		case "Door":
    			if(hasKey>0) {
    				gp.playSE(3);
    				gp.obj[i]=null;
    				hasKey--;
    				gp.ui.showMessage("You opened the door!");
    			}
    			else {
    				gp.ui.showMessage("You need a key!");
    			}
    			System.out.println("Key"+hasKey);
    			break;
    		case"Boots"://increase player movement speed
    			gp.playSE(2);
    			speed+=2;
    			gp.obj[i]=null;
    			gp.ui.showMessage("Speed up");
    			break;
    		case"Chest":
    			gp.ui.gameFinished=true;
    			gp.stopMusic();
    			gp.playSE(4);
    			break;
    			
    		}
    	}
    }

    public void draw(Graphics2D g2, int playerScreenX, int playerScreenY) {

        // g2.setColor(Color.pink);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;

            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;

            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }

                if (spriteNum == 2) {
                    image = left2;
                }
                break;

            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }

                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, playerScreenX, playerScreenY, gp.tileSize, gp.tileSize, null);

    }
}
