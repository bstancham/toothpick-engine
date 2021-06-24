package info.bstancham.toothpick.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;

public class TPSampledSound implements TPSound {

    private List<SoundClip> sfxClips = new ArrayList<>();

    public void addSfx(File f) {
        SoundClip sfx = new SoundClip(f);
        sfxClips.add(sfx);
    }

    @Override
    public void sfxExplode() {
        if (sfxClips.size() > 0) {
            SoundClip sc = sfxClips.get((int) (Math.random() * sfxClips.size()));
            sc.play();
        }

    }

    private class SoundClip implements LineListener {

        private File soundFile;
        private Clip clip = null;

        public SoundClip(File f) {
            soundFile = f;
            openClip();
        }

        private void openClip() {
            try {
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundFile);
                AudioFormat format = inputStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                clip = (Clip) AudioSystem.getLine(info);
                // clip.addLineListener(this);
                clip.open(inputStream);
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void play() {
            if (clip.isRunning()) {
                clip.stop();
            } else {
                // this seems to be where it stops working
                clip.close();
                openClip();
            }
            clip.setFramePosition(0);
            clip.start();
        }

        @Override
        public void update(LineEvent e) {
            System.out.println("LineEvent: " + e);
        }

    }

}
