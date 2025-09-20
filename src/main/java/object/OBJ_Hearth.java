package object;

import entity.Entity;
import org.example.Main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Hearth extends Entity {
    GamePanel gp;
    public static final String objName = "Heart";
    public OBJ_Hearth(GamePanel gp){
        super(gp);
        this.gp = gp;
        name = objName;
        type = type_pickupOnly;
        value = 2;
        down1 = setup("/objects/heart_full",gp.tileSize,gp.tileSize);
        image = setup("/objects/heart_full",gp.tileSize,gp.tileSize);
        image2 = setup("/objects/heart_half",gp.tileSize,gp.tileSize);
        image3 = setup("/objects/heart_blank",gp.tileSize,gp.tileSize);

    }
    public boolean use(Entity entity){
        gp.playSE(2);
        gp.ui.addMessage("Life + " + value);
        entity.life += value;
        return true;
    }
}
