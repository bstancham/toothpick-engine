package info.bschambers.toothpick.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class TPSampledSound implements TPSound {

    private List<File> soundFiles = new ArrayList<>();

    public void addSoundFile(File f) {
        soundFiles.add(f);
    }

    @Override
    public void sfxExplode() {
        if (soundFiles.size() > 0) {
            File f = soundFiles.get((int) (Math.random() * soundFiles.size()));
            try {
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(f);
                AudioFormat format = inputStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(inputStream);
                clip.start();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
