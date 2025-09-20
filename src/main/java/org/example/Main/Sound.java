package org.example.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumeScale =3 ;
    float volume;

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/BlueBoyAdventure.wav");
        soundURL[1] = getClass().getResource("/sound/coin.wav");
        soundURL[2] = getClass().getResource("/sound/powerup.wav");
        soundURL[3] = getClass().getResource("/sound/unlock.wav");
        soundURL[4] = getClass().getResource("/sound/fanfare.wav");
        soundURL[5] = getClass().getResource("/sound/hitmonster.wav");
        soundURL[6] = getClass().getResource("/sound/receivedamage.wav");
        soundURL[7] = getClass().getResource("/sound/swingweapon.wav");
        soundURL[8] = getClass().getResource("/sound/levelup.wav");
        soundURL[9] = getClass().getResource("/sound/cursor.wav");
        soundURL[10] = getClass().getResource("/sound/burning.wav");
        soundURL[11] = getClass().getResource("/sound/cuttree.wav");
        soundURL[12] = getClass().getResource("/sound/gameover.wav");
        soundURL[13] = getClass().getResource("/sound/stairs.wav");
        soundURL[14] = getClass().getResource("/sound/blocked.wav");
        soundURL[15] = getClass().getResource("/sound/parry.wav");
        soundURL[16] = getClass().getResource("/sound/speak.wav");
        soundURL[17] = getClass().getResource("/sound/Merchant.wav");
        soundURL[18] = getClass().getResource("/sound/Dungeon.wav");
        soundURL[19] = getClass().getResource("/sound/chipwall.wav");
        soundURL[20] = getClass().getResource("/sound/dooropen.wav");
        soundURL[21] = getClass().getResource("/sound/FinalBattle.wav");
    }

    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

            // Check if MASTER_GAIN control is supported before trying to get it
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                fc = null; // Set to null if not supported
                System.err.println("Warning: MASTER_GAIN control not supported for sound: " + soundURL[i].getPath());
                // You might consider a fallback volume adjustment method here if MASTER_GAIN is crucial
            }
            checkVolume(); // Always call checkVolume to apply the current setting, even if fc is null
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException e){
            System.err.println("Error loading sound file: " + soundURL[i].getPath());
            e.printStackTrace();
        }
        catch(Exception e){ // Catch any other unexpected exceptions
            System.err.println("An unexpected error occurred while setting sound file: " + soundURL[i].getPath());
            e.printStackTrace();
        }
    }

    public void play(){
        if(clip != null) {
            clip.start();
        }
    }
    public void loop(){
        if(clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void stop(){
        if(clip != null) {
            clip.stop();
        }
    }

    public void checkVolume(){
        // Only attempt to set the volume if the FloatControl exists
        if (clip != null && fc != null) {
            switch (volumeScale) {
                case 0:
                    volume = -80f; // Mute
                    break;
                case 1:
                    volume = -20f;
                    break;
                case 2:
                    volume = -12f;
                    break;
                case 3:
                    volume = -5f;
                    break;
                case 4:
                    volume = -1f;
                    break;
                case 5:
                    volume = 0f; // Max
                    break;
                default:
                    volume = -5f; // Default if volumeScale is out of expected range
                    break;
            }
            // Ensure the volume is within the control's allowed range
            float minVolume = fc.getMinimum();
            float maxVolume = fc.getMaximum();
            if (volume < minVolume) volume = minVolume;
            if (volume > maxVolume) volume = maxVolume;

            fc.setValue(volume);
        }
        else{
            // This message is now more informative, as fc might legitimately be null
            // if MASTER_GAIN is not supported for a particular clip.
            System.out.println("Warning: Cannot adjust volume. FloatControl is unavailable or clip is null.");
            if (clip == null) {
                System.out.println("Audio clip is null. Audio may not have loaded properly.");
            }
        }
    }
}