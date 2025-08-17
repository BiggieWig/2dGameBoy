package object;

import entity.Entity;
import org.example.Main.GamePanel;

public class OBJ_Axe extends Entity {
    public OBJ_Axe(GamePanel gp) {
        super(gp);
        type = type_axe;
        name = "Woodcutter's Axe";
        down1 = setup("/objects/axe",gp.tileSize,gp.tileSize);
        attackValue = 2;
        attackArea.width = 30;
        attackArea.height = 30;
        description = "[" + name + "]\nTree's number 1 enemy";
        price = 100;
    }
}
