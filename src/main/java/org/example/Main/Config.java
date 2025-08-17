package org.example.Main;

import java.io.*;

public class Config {
    GamePanel gp;
    public Config(GamePanel gp) {
        this.gp = gp;
    }
    public void saveConfig(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

            ///fullscreen
            if(gp.fullScreenOn == true){
                bw.write("On");
            }
            else{
                bw.write("Off");
            }
            bw.newLine();
            ///music volume
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();
            ///se volume
            bw.write(String.valueOf(gp.se.volumeScale));
            bw.newLine();

            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadConfig(){

        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));
            String s = br.readLine();

            ///fullscreen
            if(s.equals("On")){
                gp.fullScreenOn = true;
            }
            else{
                gp.fullScreenOn = false;
            }
            ///music volume
            s = br.readLine();
            gp.music.volumeScale = Integer.parseInt(s);
            //se volume
            s = br.readLine();
            gp.se.volumeScale = Integer.parseInt(s);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
