package entity;

import object.*;
import org.example.Main.GamePanel;

public class NPC_Merchant extends Entity{
    public NPC_Merchant(GamePanel gp) {
        super(gp);
        solidArea.x = 0;
        solidArea.y = 24;
        solidArea.width = 48;
        solidArea.height = 24;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        direction = "down";
        speed = 0;
        getImage();
        setDialogue();
        setItems();
    }
    public void getImage(){

        up1=setup("/npc/merchant_down_1",gp.tileSize,gp.tileSize);
        up2=setup("/npc/merchant_down_2",gp.tileSize,gp.tileSize);
        down1=setup("/npc/merchant_down_1",gp.tileSize,gp.tileSize);
        down2=setup("/npc/merchant_down_2",gp.tileSize,gp.tileSize);
        left1=setup("/npc/merchant_down_1",gp.tileSize,gp.tileSize);
        left2=setup("/npc/merchant_down_2",gp.tileSize,gp.tileSize);
        right1=setup("/npc/merchant_down_1",gp.tileSize,gp.tileSize);
        right2=setup("/npc/merchant_down_2",gp.tileSize,gp.tileSize);

    }
    public void setDialogue(){
        dialogues[0][0] = "He he, so you found me.\nI have some good stuff.\nDo you want to trade?";
        dialogues[1][0] = "Come again, hehe!";
        dialogues[2][0] = "Get your broke ass out of here\nGo find some coins if you want to buy!";
        dialogues[3][0] = "Your inventory is full!";
        dialogues[4][0] = "You cant't sell an equiped item dummy!";
    }
    public void setItems(){
        inventory.add(new OBJ_Potion_Red(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Sword_Normal(gp));
        inventory.add(new OBJ_Shield_Blue(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Lantern(gp));

    }
    public void speak(){
        facePlayer();

        gp.gameState = gp.tradeState;
        gp.ui.npc = this;
    }
}
