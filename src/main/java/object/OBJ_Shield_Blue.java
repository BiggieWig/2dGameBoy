package object;

import entity.Entity;
import org.example.Main.GamePanel;

public class OBJ_Shield_Blue extends Entity {
    public OBJ_Shield_Blue(GamePanel gp) {
        super(gp);
        type = type_shield;
        name="Blue Shield";
        down1 = setup("/objects/shield_blue",gp.tileSize,gp.tileSize);
        defenseValue = 2;
        description = "[" + name + "]\nMade of shiny blue metal";
        price = 150;
    }
}
