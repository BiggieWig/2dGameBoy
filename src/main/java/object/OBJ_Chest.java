package object;

import entity.Entity;
import org.example.Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Chest extends Entity {
    GamePanel gp;
    public static final String objName = "Chest";
    public OBJ_Chest(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = type_obstacle;
        name = objName;
        image = setup("/objects/chest",gp.tileSize,gp.tileSize);
        image2 = setup("/objects/chest_opened",gp.tileSize,gp.tileSize);
        down1 = image;
        collision = true;

        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    public void setLoot(Entity loot) {
        this.loot = loot;
        setDialogue();
    }
    public void setDialogue(){

        if(loot != null){
        dialogues[0][0] = "Opened the chest and found " + loot.name + "!" + "\n...But you cannot carry more!";
        dialogues[1][0] = "Opened the chest and found " + loot.name + "!";
        }
        dialogues[2][0] = "Chest is empty!";
    }
    public void interact(){

        if(opened == false){
            gp.playSE(3);

            if(loot != null && gp.player.canObtainItem(loot) == false){
               startDialogue(this,0);
            }
            else{
                startDialogue(this,1);
                down1 = image2;
                opened = true;
            }
        }
        else{
            startDialogue(this,2);
        }
    }
}
