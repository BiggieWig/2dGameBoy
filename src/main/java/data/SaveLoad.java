package data;

import org.example.Main.GamePanel;

import javax.xml.crypto.Data;
import java.io.*;

public class SaveLoad {
    GamePanel gp;
    public SaveLoad(GamePanel gp) {
        this.gp = gp;
    }
    public void save() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));

            DataStorage ds = new DataStorage();
            ds.level = gp.player.level;
            ds.maxLife = gp.player.maxLife;
            ds.life = gp.player.life;
            ds.maxMana = gp.player.maxMana;
            ds.mana = gp.player.mana;
            ds.strength = gp.player.strength;
            ds.dexterity = gp.player.dexterity;
            ds.exp = gp.player.exp;
            ds.nextLevelExp = gp.player.nextLevelExp;
            ds.coin = gp.player.coin;

            ///write the object
            oos.writeObject(ds);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void load() {
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));
            ///read
            DataStorage ds = (DataStorage) ois.readObject();
            gp.player.level = ds.level;
            gp.player.maxLife = ds.maxLife;
            gp.player.life = ds.life;
            gp.player.maxMana = ds.maxMana;
            gp.player.mana = ds.mana;
            gp.player.strength = ds.strength;
            gp.player.dexterity = ds.dexterity;
            gp.player.exp = ds.exp;
            gp.player.coin = ds.coin;
            gp.player.nextLevelExp = ds.nextLevelExp;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
