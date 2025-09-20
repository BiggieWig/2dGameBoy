package object;

import entity.Entity;
import org.example.Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends Entity {
    GamePanel gp;
    public static final String objName = "Key";
    public OBJ_Key(GamePanel gp){
        super(gp);
        this.gp = gp;
        type = type_consumable;
        stackable = true;
        name = objName;
        down1 = setup("/objects/key",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nMay open a door";
        price = 50;
        setDialogue();
    }
    public void setDialogue(){
        dialogues[0][0] =  "Used your " + name + " to open the door!";
        dialogues[1][0] = "Cannot use " + name + " here!";
    }
    public boolean use(Entity entity){
        int objIndex = getDetected(entity,gp.obj,"Door");

        if(objIndex != 999){
            startDialogue(this,0);
            gp.playSE(3);
            gp.obj[gp.currentMap][objIndex] = null;
            return true;
        }
        else{
            startDialogue(this,1);
            return false;
        }

    }
}
