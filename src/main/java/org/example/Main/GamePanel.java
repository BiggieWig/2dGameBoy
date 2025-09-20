package org.example.Main;
import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManger;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {
    //Screen setting
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;
    ///fullscreen
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;

    ///FPS
    int FPS =60;
    ///System
    public TileManager tileM= new TileManager(this);
    public KeyHandler keyH=new KeyHandler(this);
    Sound music=new Sound();
    Sound se = new Sound();
    Config config = new Config(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    public PathFinder pFinder = new PathFinder(this);
    EnvironmentManger eManager = new EnvironmentManger(this);
    Map map = new Map(this);
    SaveLoad saveLoad = new SaveLoad(this);
    public EntityGenerator eGenerator = new EntityGenerator(this);
    public CutsceneManager csManager = new CutsceneManager(this);
    ///World settings
    public  int maxWorldCol;
    public  int maxWorldRow;
    public final int worldWidth = maxWorldCol * tileSize;
    public final int worldHeight = maxWorldRow * tileSize;
    public int currentMap = 0;
    public final int maxMap = 10;
    /// ENTITY AND OBJECTS
    public Player player = new Player(this,keyH);
    public Entity obj[][] = new Entity[maxMap][20];
    public Entity npc[][] = new Entity[maxMap][10];
    public Entity monster[][] = new Entity[maxMap][20];
    public InteractiveTile iTile[][] = new InteractiveTile[maxMap][50];
    ArrayList<Entity> entityList = new ArrayList<>();
    public Entity projectile[][] = new Entity[maxMap][20];
    public ArrayList<Entity> particleList = new ArrayList<>();

    ///GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int optionsState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int mapState = 9;
    public final int cutsceneState = 10;
    ///OTHERS
    public boolean bossBattleOn = false;
    ///AREA
    public int currentArea;
    public int nextArea;
    public final int outside = 50;
    public final int indoor = 51;
    public final int dungeon = 52;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        eManager.setup();

        gameState = titleState;
        currentArea = outside;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        if(fullScreenOn){
            setFullScreen();
        }

    }
    public void resetGame(boolean restart){

        stopMusic();
        currentArea = outside;
        removeTempEntity();
        bossBattleOn = false;
        player.setDefaultPositions();
        player.restoreStatus();
        aSetter.setMonster();
        player.resetCounter();

        if(restart) {
            player.setDefaultValues();
            aSetter.setObject();
            aSetter.setInteractiveTile();
            eManager.lighting.resetDay();
        }
    }
    public void setFullScreen(){
        ///get monitor screen info
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        ///get full screen width and height
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1) {
                ///1 UPDATE : update information such as character postion
                update();
                ///2 DRAW: draw the screen with updated info
                drawToTempScreen();
                drawToScreen();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000){
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update(){

       if(gameState == playState) {
           ///player
           player.update();
           //npc
           for(int i =0 ;i< npc[1].length;i++) {
               if(npc[currentMap][i] != null) {
                   npc[currentMap][i].update();
               }
           }
            ///monster
            for(int i =0 ;i < monster[1].length;i++) {
                if(monster[currentMap][i] != null) {
                    if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false){
                        monster[currentMap][i].update();
                    }
                   if(monster[currentMap][i].alive == false){
                       monster[currentMap][i].checkDrop();
                       monster[currentMap][i] = null;
                   }
                }
            }
            ///projectile
           for(int i =0 ;i < projectile[1].length; i++) {
               if(projectile[currentMap][i] != null) {
                   if(projectile[currentMap][i].alive == true){
                       projectile[currentMap][i].update();
                   }
                   if(projectile[currentMap][i].alive == false){
                       projectile[currentMap][i] = null;
                   }
               }
           }
           ///interactive tiles
           for(int i = 0; i < iTile[1].length; i++) {
               if(iTile[currentMap][i] != null) {
                   iTile[currentMap][i].update();
               }
           }
           ///particles
           for(int i =0 ;i < particleList.size(); i++) {
               if(particleList.get(i) != null) {
                   if(particleList.get(i).alive == true){
                       particleList.get(i).update();
                   }
                   if(particleList.get(i).alive == false){
                       particleList.remove(i);
                   }
               }
           }
           eManager.update();

       }
       if(gameState == pauseState) {
           ///blank
       }
    }
    public void drawToTempScreen(){
        ///debug
        long drawStartTime = 0;
        if(keyH.showDebugText == true){
            drawStartTime = System.nanoTime();
        }
        ///title screen
        if(gameState == titleState) {
            ui.draw(g2);
        }
        ///map screen
        else if(gameState == mapState) {
            map.drawFullMapScreen(g2);
        }
        ///others
        else{
            ///tiles
            tileM.draw(g2);
            ///interactive tiles
            for(int i = 0 ;i < iTile[1].length;i++) {
                if(iTile[currentMap][i] != null) {
                    iTile[currentMap][i].draw(g2);
                }
            }
            ///entities
            entityList.add(player);
            for(int i = 0; i < npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }
            for(int i = 0; i < obj[1].length; i++) {
                if(obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }
            for(int i =0 ;i < monster[1].length;i++) {
                if(monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }
            for(int i =0 ;i < projectile[1].length;i++) {
                if(projectile[currentMap][i] != null) {
                    entityList.add(projectile[currentMap][i]);
                }
            }
            for(int i =0 ;i < particleList.size();i++) {
                if(particleList.get(i) != null) {
                    entityList.add(particleList.get(i));
                }
            }
            ///sort
            Collections.sort(entityList, new Comparator<Entity>(){

                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });
            ///draw entities
            for(int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            ///empty list
            entityList.clear();
            ///environment
            eManager.draw(g2);
            ///cutscenes
            csManager.draw(g2);
            ///ui
            ui.draw(g2);
        }
        ///debug
        if(keyH.showDebugText == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStartTime;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y =400;
            int lineHeight =20;

            g2.drawString("WorldX" + player.worldX,x,y);
            y += lineHeight;
            g2.drawString("WorldY" + player.worldY,x,y);
            y += lineHeight;
            g2.drawString("Col" + (player.worldX + player.solidArea.x)/tileSize,x,y);
            y += lineHeight;
            g2.drawString("Row" + (player.worldY + player.solidArea.x)/tileSize,x,y);
            y += lineHeight;
            g2.drawString("God Mode:" + keyH.godModeOn,x,y);

        }
    }
    public void drawToScreen(){
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
    public void playMusic(int i){
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic(){
        music.stop();
    }
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
    public void changeArea(){
        if(nextArea != currentArea){
            stopMusic();
            if(nextArea == outside){
                playMusic(0);
            }
            if(nextArea == indoor){
                playMusic(17);
            }
            if(nextArea == dungeon){
                playMusic(18);
            }
            aSetter.setNPC();
        }
        currentArea = nextArea;
        aSetter.setMonster();
    }
    public void removeTempEntity(){
        for(int mapNum = 0; mapNum < maxMap; mapNum++){
            for(int i = 0; i < obj[1].length; i++) {
                if(obj[mapNum][i] != null && obj[mapNum][i].temp == true) {
                    obj[mapNum][i] = null;
                }
            }
        }
    }
}
