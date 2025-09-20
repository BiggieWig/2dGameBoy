package org.example.Main;

import entity.PlayerDummy;
import monster.MON_SkeletonLord;
import object.OBJ_BlueHeart;
import object.OBJ_Door_Iron;

import java.awt.*;

public class CutsceneManager {
    GamePanel gp;
    Graphics2D g2;
    public int sceneNum;
    public int scenePhase;
    int counter = 0;
    float alpha = 0f;
    int y;
    String endCredits;

    ///Scene Number
    public final int NA = 0;
    public final int skeletonLord = 1;
    public final int ending = 2;

    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
        endCredits = "Daca ai ajuns pana aici \n"
                + "Felicitari! "
                + "\n\n\n\n\n\n\n\n"
                + "Respect Kendama!"
                + "\n\n\n\n\n\n\n"
                + "Multam ca ai jucat jocu asta de cct \n"
                + "\n\n\n\n\n\n\n"
                + "MULTA MULTA MULTA \n\n"
                + "MUIE";

    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;

        switch (sceneNum) {
            case skeletonLord:scene_skeletonLord();break;
            case ending:scene_ending();break;
        }
    }
    public void scene_skeletonLord(){
        if(scenePhase == 0){
            gp.bossBattleOn = true;
            ///shut the iron door
            for(int i = 0; i < gp.obj[1].length; i++){
                if(gp.obj[gp.currentMap][i] == null){
                    gp.obj[gp.currentMap][i] = new OBJ_Door_Iron(gp);
                    gp.obj[gp.currentMap][i].worldX = gp.tileSize*25;
                    gp.obj[gp.currentMap][i].worldY = gp.tileSize*28;
                    gp.obj[gp.currentMap][i].temp = true;
                    gp.playSE(20);
                    break;
                }
            }
            ///search for dummy slot
            for(int i = 0; i < gp.npc[1].length; i++){
                if(gp.npc[gp.currentMap][i] == null){
                    gp.npc[gp.currentMap][i] = new PlayerDummy(gp);
                    gp.npc[gp.currentMap][i].worldX = gp.player.worldX;
                    gp.npc[gp.currentMap][i].worldY = gp.player.worldY;
                    gp.npc[gp.currentMap][i].direction = gp.player.direction;
                    break;
                }
            }
            gp.player.drawing = false;

            scenePhase++;
        }
        if(scenePhase == 1){
            gp.player.worldY -= 2;

            if(gp.player.worldY < gp.tileSize*18){
                scenePhase++;
            }
        }
        if(scenePhase == 2){
            ///search boss
            for(int i = 0; i < gp.monster[1].length; i++){
                if(gp.monster[gp.currentMap][i] != null &&
                    gp.monster[gp.currentMap][i].name.equals(MON_SkeletonLord.monName)){
                    gp.monster[gp.currentMap][i].sleep = false;
                    gp.ui.npc = gp.monster[gp.currentMap][i];
                    scenePhase++;
                    break;
                }
            }
        }
        if(scenePhase == 3){
            gp.ui.drawDialogueScreen();
        }
        if(scenePhase == 4){
            ///return to player
            for(int i = 0; i < gp.npc[1].length; i++){
                if(gp.npc[gp.currentMap][i] != null && gp.npc[gp.currentMap][i].name.equals(PlayerDummy.npcName)){
                    System.out.println("Found dummy at index " + i + ", removing it");
                    ///reset position
                    gp.player.worldX = gp.npc[gp.currentMap][i].worldX;
                    gp.player.worldY = gp.npc[gp.currentMap][i].worldY;
                    ///delete dummy
                    gp.npc[gp.currentMap][i] = null;
                    break;
                }
            }
            ///draw again
            gp.player.drawing = true;
            scenePhase = 0;
            sceneNum = NA;
            gp.gameState = gp.playState;
            ///change music
            gp.stopMusic();
            gp.playMusic(21);
        }
    }
    public void scene_ending(){
        if(scenePhase == 0){
            gp.stopMusic();
            gp.ui.npc = new OBJ_BlueHeart(gp);
            scenePhase++;
        }
        if(scenePhase == 1){
            ///display dialogue
            gp.ui.drawDialogueScreen();
        }
        if(scenePhase == 2){
            ///play SE
            gp.playSE(4);
            scenePhase++;
        }
        if(scenePhase == 3){
            if(counterReached(300)){
                scenePhase++;
            }
        }
        if(scenePhase == 4){
            ///scene gets darker
            alpha += 0.005f;
            if(alpha > 1f){
                alpha = 1f;
            }
            drawBlackBackground(alpha);

            if(alpha == 1){
                alpha = 0;
                scenePhase++;
            }
        }
        if(scenePhase == 5){
            drawBlackBackground(1f);

            alpha += 0.005f;
            if(alpha > 1f){
                alpha = 1f;
            }

            String text = "After a fierce battle with the Skeleton Lord, \n"
                        + "the Blue Boy finally found the legendary treasure.\n"
                        + "But this is not the end of his journey.\n"
                        + "The Blue Boy's adventure has just begun.";
            drawString(alpha,38f,200, text, 70);
            if(counterReached(600)){
                gp.playMusic(0);
                scenePhase++;
            }
        }
        if(scenePhase == 6){
            drawBlackBackground(1f);
            drawString(1f,120,gp.screenHeight/2,"Blue Boy Adventure", 40);
            if(counterReached(480)){
                scenePhase++;
            }
        }
        if(scenePhase == 7){
            drawBlackBackground(1f);
            y = gp.screenHeight/2;
            drawString(1f,38f, y, endCredits, 40);
            if(counterReached(480)){
                scenePhase++;
            }
        }
        if(scenePhase == 8){
            drawBlackBackground(1f);
            ///scroll credits
            y--;
            drawString(1f,38f, y, endCredits, 40);
            int creditsHeight = endCredits.split("\n").length * 40;

            // When the bottom of the credits goes above the screen, wait for input
            if(y + creditsHeight < 0){
                scenePhase++;
            }
        }
        if(scenePhase == 9){
            drawBlackBackground(1f);

            // Check for any key press to return to title screen
            if(gp.keyH.enterPressed) {

                // Reset everything and go back to title screen
                scenePhase = 0;
                sceneNum = NA;
                counter = 0;
                alpha = 0f;
                y = 0;
                gp.gameState = gp.titleState;
                gp.ui.titleScreenState = 0;
                gp.ui.commandNumber = 0;
                gp.stopMusic();

                // Reset key states
                gp.keyH.enterPressed = false;
            }
        }
    }
    public boolean counterReached(int target){
        boolean counterReached = false;

        counter++;
        if(counter > target){
            counterReached = true;
            counter = 0;
        }
        return counterReached;
    }
    public void drawBlackBackground(float alpha){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.BLACK);
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
    public void drawString(float alpha, float fontSize, int y, String text, int lineHeight){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(fontSize));

        for(String line : text.split("\n")){
            int x = gp.ui.getXforCenteredText(line);
            g2.drawString(line, x, y);
            y += lineHeight;
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
