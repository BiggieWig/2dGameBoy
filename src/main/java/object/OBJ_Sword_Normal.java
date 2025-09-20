package object;

import entity.Entity;
import org.example.Main.GamePanel;

public class OBJ_Sword_Normal extends Entity {
    public static final String objName = "Normal Sword";

    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);
        type = type_sword;
        name = objName;
        down1 = setup("/objects/sword_normal",gp.tileSize,gp.tileSize);
        attackArea.width = 36;
        attackArea.height = 36;
        attackValue = 1;
        description = "[" + name + "]\nAn old sword";
        price = 25;
        knockBackPower = 3;
        motion1_duration =5;
        motion2_duration =20;
    }
}
