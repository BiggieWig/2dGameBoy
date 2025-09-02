package object;

import entity.Entity;
import org.example.Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends Entity {
    GamePanel gp;
    public OBJ_Key(GamePanel gp){
        super(gp);
        this.gp = gp;
        type = type_consumable;
        stackable = true;
        name = "Key";
        down1 = setup("/objects/key",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nMay open a door";
        price = 50;
    }
    public boolean use(Entity entity){
        gp.gameState = gp.dialogueState;
        int objIndex = getDetected(entity,gp.obj,"Door");

        if(objIndex != 999){
            gp.ui.currentDialogue = "Used your " + name + " to open the door!";
            gp.playSE(3);
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        }
        else{
            gp.ui.currentDialogue = "Cannot use " + name + " here!";
            return false;
        }

    }
}
