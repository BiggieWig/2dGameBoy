package environment;

import org.example.Main.GamePanel;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;
    int dayCounter;
    float filterAlpha = 0f;

    ///day state
    final int day = 0;
    final int dusk = 1;
    final int night = 2;
    final int dawn = 3;
    int dayState = day;

    public Lighting(GamePanel gp) {
       this.gp = gp;
       setLightSource();
    }
    public void setLightSource(){
        ///create buffered image
        darknessFilter = new BufferedImage(gp.screenWidth,gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();
        if(gp.player.currentLight == null){
            g2.setColor(new Color(0,0,0,0.90f));
        }
        else {
            ///get center X and Y of the circle
            int centerX = gp.player.screenX + (gp.tileSize) / 2;
            int centerY = gp.player.screenY + (gp.tileSize) / 2;
            ///create gradiation effect
            Color color[] = new Color[5];
            float fraction[] = new float[5];
            color[0] = new Color(0, 0, 0, 0f);
            color[1] = new Color(0, 0, 0, 0.25f);
            color[2] = new Color(0, 0, 0, 0.5f);
            color[3] = new Color(0, 0, 0, 0.75f);
            color[4] = new Color(0, 0, 0, 0.90f);

            fraction[0] = 0f;
            fraction[1] = 0.25f;
            fraction[2] = 0.5f;
            fraction[3] = 0.75f;
            fraction[4] = 1f;

            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, gp.player.currentLight.lightRadius, fraction, color);
            ///drawing
            g2.setPaint(gPaint);
        }
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.dispose();
    }
    public void resetDay(){
        dayState = day;
        filterAlpha = 0f;
    }
    public void update(){
        if(gp.player.lightUpdated == true){
            setLightSource();
            gp.player.lightUpdated = false;
        }
        ///check state
        if(dayState == day){
            dayCounter++;
            if(dayCounter > 600){
                dayState = dusk;
                dayCounter = 0;
            }
        }
        if(dayState == dusk){
            filterAlpha += 0.001f;

            if(filterAlpha > 1f){
                filterAlpha = 1f;
                dayState = night;
            }
        }
        if(dayState == night){
            dayCounter++;

            if(dayCounter > 600){
                dayState = dawn;
                dayCounter = 0;
            }
        }
        if(dayState == dawn){
            filterAlpha -= 0.001f;

            if(filterAlpha < 0f){
                filterAlpha = 0f;
                dayState = day;
            }
        }
    }
    public void draw(Graphics2D g2){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        g2.drawImage(darknessFilter,0,0,null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        ///debug
        String situation = "";

        switch(dayState){
            case day:situation = "Day";break;
            case dawn:situation = "Dawn";break;
            case night:situation = "Night";break;
            case dusk:situation = "Dusk";break;
        }
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.drawString(situation,800,500);
    }
}
