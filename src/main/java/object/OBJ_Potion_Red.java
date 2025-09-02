package object;

import entity.Entity;
import org.example.Main.GamePanel;

public class OBJ_Potion_Red extends Entity {
    GamePanel gp;

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_consumable;
        stackable = true;
        name = "Red Potion";
        value = 4;
        down1 = setup("/objects/potion_red",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nHeals you " + value + ".";
        price = 50;
    }
    public boolean use(Entity entity) {
        if(gp.player.life != gp.player.maxLife) {
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You drank the " + name + "!\n" + "Your life has been recovered by " + value + "!";
            entity.life += value;
            gp.playSE(2);
            return true;
        }
        else{
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are already full health!";
            return false;
        }
    }
}
