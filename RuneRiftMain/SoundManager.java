import greenfoot.*;

/**
 * SoundManager - Centralized sound management for RuneRift.
 * Handles all game sounds including UI clicks, piece movements, and game events.
 * 
 * @author RuneRift Team
 * @version 1.0
 */
public class SoundManager  
{
    // Singleton instance
    private static SoundManager instance;
    
    // Sound file names
    public static final String CLICK = "click.mp3";
    public static final String MOVE = "move.mp3";
    public static final String CAPTURE = "capture.mp3";
    public static final String SELECT = "select.mp3";
    public static final String ERROR = "error.mp3";
    public static final String WIN = "win.mp3";
    public static final String LOSE = "lose.mp3";
    public static final String START = "start.mp3";
    
    // Volume control
    private boolean soundEnabled = true;

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
     * Play a sound effect
     * @param soundFile the name of the sound file to play
     */
    public void play(String soundFile)
    {
        if (soundEnabled)
        {
            try
            {
                Greenfoot.playSound(soundFile);
            }
            catch (Exception e)
            {
                // Sound file not found, fail silently
            }
        }
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
        play(MOVE);
    }
    
    /**
     * Play the capture sound (when capturing a piece)
     */
    public void playCapture()
    {
        play(CAPTURE);
    }
    
    /**
     * Play the select sound (when selecting a piece)
     */
    public void playSelect()
    {
        play(SELECT);
    }
    
    /**
     * Play the error sound (for invalid actions)
     */
    public void playError()
    {
        play(ERROR);
    }
    
    /**
     * Play the win sound
     */
    public void playWin()
    {
        play(WIN);
    }
    
    /**
     * Play the lose sound
     */
    public void playLose()
    {
        play(LOSE);
    }
    
    /**
     * Play the game start sound
     */
    public void playStart()
    {
        play(START);
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
}
