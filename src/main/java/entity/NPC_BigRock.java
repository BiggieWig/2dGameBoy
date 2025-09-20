package entity;

import object.OBJ_Door_Iron;
import org.example.Main.GamePanel;
import tile_interactive.IT_MetalPlate;
import tile_interactive.InteractiveTile;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class NPC_BigRock extends Entity {

    public static final String npcName = "Big Rock";

    public NPC_BigRock(GamePanel gp) {
        super(gp);

        name = npcName;
        direction = "down";
        speed = 2;

        solidArea = new Rectangle();
        solidArea.x = 2;
        solidArea.y = 16;
        solidArea.width = 44;
        solidArea.height = 35;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;


        dialogueSet = -1;

        getImage();
        setDialogue();
    }
    public void getImage(){

        up1=setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        up2=setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        down1=setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        down2=setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        left1=setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        left2=setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        right1=setup("/npc/bigrock",gp.tileSize,gp.tileSize);
        right2=setup("/npc/bigrock",gp.tileSize,gp.tileSize);

    }
    public void setDialogue(){
        dialogues[0][0] = "It's a giant rock";

    }
    public void setAction(){}
    public void update(){}
    public void speak(){
        facePlayer();
        startDialogue(this,dialogueSet);

        dialogueSet++;

        if(dialogues[dialogueSet][0] == null){
            dialogueSet--;
        }
    }
    public void move(String d){
        this.direction = d;

        checkCollision();

        if(collisionOn == false){
            switch(direction){
                case "up":worldY -= speed;break;
                case "down":worldY += speed;break;
                case "left":worldX -= speed;break;
                case "right":worldX += speed;break;
            }
        }
        detectPlate();
    }
    public void detectPlate(){
        ArrayList<InteractiveTile> plateList = new ArrayList<>();
        ArrayList<Entity> rockList = new ArrayList<>();

        ///fill plate list
        for(int i = 0 ; i < gp.iTile[1].length; i++){
            if(gp.iTile[gp.currentMap][i] != null &&
                    gp.iTile[gp.currentMap][i].name != null &&
                    gp.iTile[gp.currentMap][i].name.equals(IT_MetalPlate.itName)){
                plateList.add(gp.iTile[gp.currentMap][i]);
            }
        }
        ///fill rock list
        for(int i = 0 ; i < gp.npc[1].length; i++){
            if(gp.npc[gp.currentMap][i] != null &&
                    gp.npc[gp.currentMap][i].name.equals(NPC_BigRock.npcName)){
                rockList.add(gp.npc[gp.currentMap][i]);
            }
        }
        ///scan plate list
        for(int i = 0; i < plateList.size(); i++){

            int xDistance = Math.abs(worldX - plateList.get(i).worldX);
            int yDistance = Math.abs(worldY - plateList.get(i).worldY);
            int distance = (int)Math.sqrt(xDistance * xDistance + yDistance * yDistance);

            if(distance < 24){
                if(linkedEntity == null){
                    linkedEntity = plateList.get(i);
                    gp.playSE(3);
                }
            }
            else{
                if(linkedEntity == plateList.get(i)) {
                    linkedEntity = null;
                }
            }
        }
        ///scan rock list
        int count = 0;
        for(int i = 0; i < rockList.size(); i++){
            ///count rocks on plate
            if(rockList.get(i).linkedEntity != null){
                count++;
            }
        }

        // After counting rocks
        System.out.println("Rocks on plates: " + count + " / Total rocks: " + rockList.size());
        System.out.println("Current map: " + gp.currentMap);

        // In the door opening section
        if(count == rockList.size()){
            System.out.println("All rocks on plates! Opening door...");
            // ... door opening code
        }
        ///open door if all plates pushed
        if(count == rockList.size()){

            for(int i = 0; i < gp.obj[1].length; i++){
                if(gp.obj[gp.currentMap][i] != null && gp.obj[gp.currentMap][i].name.equals(OBJ_Door_Iron.objName)){
                    gp.obj[gp.currentMap][i] = null;
                    gp.playSE(20);
                }
            }
        }
    }
}
