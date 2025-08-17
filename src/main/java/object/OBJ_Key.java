package object;

import entity.Entity;
import org.example.Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends Entity {

    public OBJ_Key(GamePanel gp){
        super(gp);
        type = type_consumable;
        name = "Key";
        down1 = setup("/objects/key",gp.tileSize,gp.tileSize);
        description = "[" + name + "]\nMay open a door";
        price = 50;

    }
}
