import greenfoot.*;

/**
 * SoundManager - Centralized sound management for RuneRift.
 * Handles all game sounds including UI clicks, piece movements, and game events.
 * 
 * @author Saiful Shaik
 * @version
 */
public class SoundManager  
{
    // Singleton instance
    private static SoundManager instance;
    
    // Sound file names
    public static final String CLICK = "click.mp3";
    public static final String MOVE = "move.mp3";
    public static final String SELECT = "select.mp3";
    public static final String LOSE = "draw.mp3";
    public static final String START = "start.mp3";
    public static final String MUSIC_MENU1 = "menu_music1.mp3";
    public static final String MUSIC_MENU2 = "menu_music2.mp3";
    // public static final String MUSIC_MENU3 = "menu_music3.mp3"; // Disabled - corrupted file
    public static final String DARK_PRINCE = "darkPrince.mp3";
    public static final String KNIGHT = "knight.mp3";
    public static final String MUSKETEER = "musketeer.mp3";
    public static final String ROYAL_RECRUIT = "royalRecruit.mp3";
    public static final String WITCH = "witch.mp3";
    public static final String BOMB = "bomb.mp3";
    
    
    // Array of menu music files for random selection (menu_music3 excluded due to corruption)
    private static final String[] MENU_MUSIC_FILES = {MUSIC_MENU1, MUSIC_MENU2};
    
    // Volume control
    private boolean soundEnabled = true;
    
    // Background music
    private GreenfootSound menuMusic;

    /**
     * Private constructor for singleton pattern
     */
    private SoundManager()
    {
        
    }
    
    /**
     * Get the singleton instance of SoundManager
     */
    public static SoundManager getInstance()
    {
        if (instance == null)
        {
            instance = new SoundManager();
        }
        return instance;
    }
    
    /**
     * Play a sound effect with volume from settings
     * @param soundFile the name of the sound file to play
     */
    public void play(String soundFile)
    {
        if (soundEnabled)
        {
            try
            {
                GreenfootSound sound = new GreenfootSound(soundFile);
                sound.setVolume(getEffectiveSfxVolume());
                sound.play();
            }
            catch (Exception e)
            {
                // Sound file not found, fail silently
            }
        }
    }
    
    /**
     * Get effective SFX volume (master * sfx / 100)
     */
    public int getEffectiveSfxVolume()
    {
        GameSettings settings = GameSettings.getInstance();
        return (settings.getMasterVolume() * settings.getSfxVolume()) / 100;
    }
    
    /**
     * Get effective music volume (master * music / 100)
     */
    public int getEffectiveMusicVolume()
    {
        GameSettings settings = GameSettings.getInstance();
        return (settings.getMasterVolume() * settings.getMusicVolume()) / 100;
    }
    
    /**
     * Play the click sound (for UI buttons)
     */
    public void playClick()
    {
        play(CLICK);
    }
    
    /**
     * Play the move sound (for piece movement)
     */
    public void playMove()
    {
        //if() {}
        //play(MOVE);
    }
    
    /**
     * Play the select sound (when selecting a piece)
     */
    public void playSelect()
    {
        play(SELECT);
    }
    
    /**
     * Play the game start sound
     */
    public void playStart()
    {
        play(START);
    }
    
    /**
     * Play the lose/draw sound (when game ends)
     */
    public void playLose()
    {
        play(LOSE);
    }
    
    /**
     * Enable or disable all sounds
     * @param enabled true to enable sounds, false to disable
     */
    public void setSoundEnabled(boolean enabled)
    {
        this.soundEnabled = enabled;
    }
    
    /**
     * Check if sounds are enabled
     * @return true if sounds are enabled
     */
    public boolean isSoundEnabled()
    {
        return soundEnabled;
    }
    
    /**
     * Toggle sound on/off
     */
    public void toggleSound()
    {
        soundEnabled = !soundEnabled;
    }
    
    /**
     * Play menu background music (loops)
     * Randomly selects one of the three menu music tracks
     */
    public void playMenuMusic()
    {
        try
        {
            if (menuMusic == null)
            {
                // Randomly select one of the menu music files
                int randomIndex = Greenfoot.getRandomNumber(MENU_MUSIC_FILES.length);
                String selectedMusic = MENU_MUSIC_FILES[randomIndex];
                menuMusic = new GreenfootSound(selectedMusic);
            }
            menuMusic.setVolume(getEffectiveMusicVolume());
            menuMusic.playLoop();
        }
        catch (Exception e)
        {
            // Music file not found, fail silently
        }
    }
    
    /**
     * Stop menu music
     */
    public void stopMenuMusic()
    {
        if (menuMusic != null)
        {
            menuMusic.stop();
        }
    }
    
    /**
     * Update music volume in real-time (call when slider changes)
     */
    public void updateMusicVolume()
    {
        if (menuMusic != null)
        {
            menuMusic.setVolume(getEffectiveMusicVolume());
        }
    }
}
