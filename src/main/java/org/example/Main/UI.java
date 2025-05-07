package org.example.Main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
    GamePanel gp;
    Font font,font2,arial;
    Color color;
    BufferedImage keyImage;
    private boolean messageOn = false;
    private String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;

    public UI(GamePanel gp) {
        this.gp = gp;
        font = new Font("Courier New", Font.BOLD, 40);
        font2 = new Font("Courier New", Font.BOLD, 80);
        arial = new Font("Arial", Font.PLAIN, 40);
        OBJ_Key key = new OBJ_Key();
        keyImage = key.image;
        color= new Color(255,140,0);
    }
    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }
    public void draw(Graphics g2) {
        if(gameFinished == true){
            String text;
            int textLength;
            int x,y;

            g2.setColor(Color.white);
            g2.setFont(arial);
            g2.setFont(g2.getFont().deriveFont(30f));
            text = "You found the Treasure!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
            x=gp.screenWidth/2 - textLength/2;
            y=gp.screenHeight/2 - (gp.tileSize*3);
            g2.drawString(text,x,y);

            g2.setFont(font2);
            g2.setColor(color);
            text = "Congratulations!";
            textLength = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
            x=gp.screenWidth/2 - textLength/2;
            y=gp.screenHeight/2 + (gp.tileSize*2);
            g2.drawString(text,x,y);

            gp.gameThread = null;
        }
        else {
            g2.setFont(font);
            g2.setColor(color);
            g2.drawImage(keyImage, gp.tileSize / 2, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
            g2.drawString("x" + gp.player.hasKey, 74, 65);

            if (messageOn == true) {
                g2.setFont(g2.getFont().deriveFont(20f));
                g2.drawString(message, gp.tileSize / 3, gp.tileSize * 2);

                messageCounter++;
                if (messageCounter > 120) {
                    messageCounter = 0;
                    messageOn = false;
                }
            }
        }
    }
}
