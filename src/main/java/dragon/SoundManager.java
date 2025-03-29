package dragon;

import javafx.scene.media.AudioClip;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * SoundManager stores and plays sounds and music
 *
 * <br><br>
 * Sounds can be played using {@link #playSound} and {@link #setSoundVolume}
 * Songs can be played using {@link #playSong} and {@link #setSongVolume}
 * <br><br>
 *
 * <b>Example use:</b>
 * <pre>
 *    {@code
 *      SoundManager sm = new SoundManager();
 *      sm.playSound("menu_select");
 *      sm.playSong("ingame_0");
 *      sm.setSoundVolume(0.5);
 *    }
 * </pre>
 *
 * @version 1.0.0
 * @author Cole Vale
 */
public class SoundManager {

    //Note that the Audioclip class cannot swap clips. It stores one sound, and it cannot change
    //Thus I keep an array of clips, and play them as requested

    /**These maps store all the songs and sounds*/
    private Map<String, AudioClip> songs;
    private Map<String, AudioClip> sounds;

    private AudioClip curSong; //this stores the currently playing song, so I can stop it later
    private double songVolume, soundVolume = 1.0;

    private boolean isDragon = false;


    /**
     * SoundManager constructor, makes a SoundManager object
     */
    public SoundManager()
    {
        //retrieve all the songs and sounds
        songs = retrieveAudio("src/main/resources/audio/songs");
        sounds = retrieveAudio("src/main/resources/audio/sounds");

        songVolume = 1.0;
        soundVolume = 1.0;
    }

    //get all the songs and put it into the hashmap

    /**
     * Collects all the audio files in a directory and returns it as a Map object
     *
     * @param location the directory storing audio files
     * @return a Map storing all the audio clips in the requested directory
     */
    private Map<String, AudioClip> retrieveAudio(String location) {

        //make the variables
        String soundName = "menu_select.wav"; //stores the current file being used
        String extension; //the file extension
        File[] soundDirectory;
        Map<String, AudioClip> audioClips = new HashMap<String, AudioClip>(); //stores all the new sounds


        //get the directory with the sound files
        try {
            soundDirectory = new File(location).listFiles(); //the directory with all the audiofiles
        } catch (NullPointerException e) { //________________________________________________________________________________________________________
            System.out.println("Audio file directory invalid:" + location);
            return null;
        }


        //for each file in the directory
        for (int i = 0; i < soundDirectory.length; i++)
        {
            soundName = soundDirectory[i].getName(); //get the name, without the extention
            extension = soundName.substring(soundName.length()-4, soundName.length());



            //only add the file if it is an audio file
            if (!extension.equals(".wav") && !extension.equals(".mp3") && !extension.equals(".ogg"))
                continue;

            AudioClip sound = new AudioClip(soundDirectory[i].toURI().toString()); //create the audioclip

            //Add it to the list of sounds. The key is the name, the object is the clip
            audioClips.put(soundName.substring(0, soundName.length()-4), sound);
        }

        return audioClips; //return the set of sounds
    }

    /**
     * Play the requested song. To play sounds, use {@link #playSound}
     * To stop a song that is playing use {@link #stopSong}
     *
     * @param name the name of the song to be played
     * @see #playSound
     * @see #stopSong
     */
    public void playSong(String name) {

        if (curSong != null)
            curSong.stop();



        curSong = songs.get(name); //get the new song
        curSong.play(songVolume); //play the song
    }

    /**
     * Play random song in ingame_* list, use{@link #playSong}
     *
     * @see #playSong
     */
    public void playRandomIngame(){
        Random random = new Random();
        int songIndex = random.nextInt(4);
        String randomSong = "ingame_" + songIndex;
        playSong(randomSong);
        isDragon = false;
    }

    /**
     * Play random dragon_*.mp3 after player select dragon
     */
    public void playRandomDragon(){
        Random random = new Random();
        int songIndex = random.nextInt(3);
        String randomSong = "dragon_" + songIndex;
        playSong(randomSong);
        isDragon = true;
    }

    /**
     * Prevent logic chaos after quit mini game
     */
    public void playRandomSong(){
        if(isDragon){
            playRandomDragon();
        }else{
            playRandomIngame();
        }

    }


    /**
     * Stop the currently playing song. To play songs, use {@link #playSong}
     *
     * @see #playSong
     */
    public void stopSong() {

        if (curSong != null)
            curSong.stop();
    }

    /**
     * Play the requested sound. To play sounds, use {@link #playSong}
     *
     * @param name the name of the sound to be played
     * @see #playSong
     */
    public void playSound(String name) {
        sounds.get(name).play(soundVolume); //play the requested sound
    }

    /**
     * Change the volume for songs to be played at. To change sound volume, use {@link #setSoundVolume}
     *
     * @param volume the new volume for songs
     * @see #setSoundVolume
     */
    public void setSongVolume(double volume)
    {
        songVolume = volume; //store the new volume

        if (curSong != null && curSong.isPlaying()) //if there is a song playing, reset it with new volume
        {
            curSong.stop(); //cannot just set volume for whatever reason. Function does not work
            curSong.play(songVolume); //A song may be playing already, change that volume (it must reset)
        }

    }

    /**
     * Change the volume for sounds to be played at. To change song volume, use {@link #setSongVolume}
     *
     * @param volume the new volume for sounds
     * @see #setSongVolume
     */
    public void setSoundVolume(double volume)
    {
        soundVolume = volume; //Sounds aren't continuously playing, no need to set anything now
    }


//    //DELETE LATER. THIS IS JUST FOR TESTING _____________________________________________________________
//    public static void main(String[] args) {
//        SoundManager sm = new SoundManager();
//
//
//        sm.playSong("ingame_2");
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        sm.setSongVolume(0.2);
//
//
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        sm.stopSong();
//
//        sm.playSound("menu_select");
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

}


/*
Songlist:
Ingame ones by Haywyre:
    Double Edged
    Transition
    Sedative Shapes
    Midnight Mist
 */