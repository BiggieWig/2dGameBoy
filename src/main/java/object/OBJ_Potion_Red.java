package object;

import entity.Entity;
import org.example.Main.GamePanel;

public class OBJ_Potion_Red extends Entity {
    GamePanel gp;
    public static final String objName = "Red Potion";

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_consumable;
        stackable = true;
        name = objName;
        value = 4;
        down1 = setup("/objects/potion_red",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nHeals you " + value + ".";
        price = 50;
        setDialogue();
    }
    public void setDialogue(){
        dialogues[0][0] = "You drank the " + name + "!\n" + "Your life has been recovered by " + value + "!";
        dialogues[1][0] = "You are already full health!";
    }
    public boolean use(Entity entity) {
        if(gp.player.life != gp.player.maxLife) {
            startDialogue(this,0);
            entity.life += value;
            gp.playSE(2);
            return true;
        }
        else{
            startDialogue(this,1);
            return false;
        }
    }
}
