import java.io.*;

/**
 * GameSettings manages persistent game configuration.
 * Handles saving/loading settings to/from a file.
 * 
 * @author Saiful Shaik
 */
public class GameSettings
{
    private static final String SETTINGS_FILE = "game_settings.txt";
    
    // Settings with defaults
    private int elixirMultiplier = 1;
    private int timeMinutes = 10;
    private boolean isWhiteSide = true;
    
    // Volume settings (0-100)
    private int masterVolume = 100;
    private int musicVolume = 80;
    private int sfxVolume = 100;
    
    // Singleton instance
    private static GameSettings instance;
    
    /**
     * Private constructor for singleton pattern
     */
    private GameSettings()
    {
        load();
    }
    
    /**
     * Get the singleton instance
     */
    public static GameSettings getInstance()
    {
        if (instance == null)
        {
            instance = new GameSettings();
        }
        return instance;
    }
    
    /**
     * Reset the singleton (useful for testing)
     */
    public static void resetInstance()
    {
        instance = null;
    }
    
    // === GETTERS ===
    
    /**
     * Get the elixir multiplier (1, 2, or 3)
     */
    public int getElixirMultiplier()
    {
        return elixirMultiplier;
    }
    
    /**
     * Get the time limit in minutes (1-30)
     */
    public int getTimeMinutes()
    {
        return timeMinutes;
    }
    
    /**
     * Get the time limit in seconds
     */
    public int getTimeSeconds()
    {
        return timeMinutes * 60;
    }
    
    /**
     * Check if player chose white side
     */
    public boolean isWhiteSide()
    {
        return isWhiteSide;
    }
    
    /**
     * Get master volume (0-100)
     */
    public int getMasterVolume()
    {
        return masterVolume;
    }
    
    /**
     * Get music volume (0-100)
     */
    public int getMusicVolume()
    {
        return musicVolume;
    }
    
    /**
     * Get SFX volume (0-100)
     */
    public int getSfxVolume()
    {
        return sfxVolume;
    }
    
    // === SETTERS WITH VALIDATION ===
    
    /**
     * Set the elixir multiplier (clamped to 1, 2, or 3)
     */
    public void setElixirMultiplier(int multiplier)
    {
        this.elixirMultiplier = Math.max(1, Math.min(3, multiplier));
    }
    
    /**
     * Set the time limit in minutes (clamped to 1-30)
     */
    public void setTimeMinutes(int minutes)
    {
        this.timeMinutes = Math.max(1, Math.min(30, minutes));
    }
    
    /**
     * Set the side selection
     */
    public void setWhiteSide(boolean isWhite)
    {
        this.isWhiteSide = isWhite;
    }
    
    /**
     * Set master volume (clamped to 0-100)
     */
    public void setMasterVolume(int volume)
    {
        this.masterVolume = Math.max(0, Math.min(100, volume));
    }
    
    /**
     * Set music volume (clamped to 0-100)
     */
    public void setMusicVolume(int volume)
    {
        this.musicVolume = Math.max(0, Math.min(100, volume));
    }
    
    /**
     * Set SFX volume (clamped to 0-100)
     */
    public void setSfxVolume(int volume)
    {
        this.sfxVolume = Math.max(0, Math.min(100, volume));
    }
    
    // === PERSISTENCE ===
    
    /**
     * Save settings to file
     * @return true if save was successful
     */
    public boolean save()
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SETTINGS_FILE)))
        {
            writer.println("elixirMultiplier=" + elixirMultiplier);
            writer.println("timeMinutes=" + timeMinutes);
            writer.println("isWhiteSide=" + isWhiteSide);
            writer.println("masterVolume=" + masterVolume);
            writer.println("musicVolume=" + musicVolume);
            writer.println("sfxVolume=" + sfxVolume);
            return true;
        }
        catch (IOException e)
        {
            System.err.println("Error saving settings: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load settings from file
     * Falls back to defaults if file is missing or corrupted
     * @return true if load was successful
     */
    public boolean load()
    {
        File file = new File(SETTINGS_FILE);
        if (!file.exists())
        {
            // Use defaults - this is not an error
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split("=");
                if (parts.length == 2)
                {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    
                    switch (key)
                    {
                        case "elixirMultiplier":
                            setElixirMultiplier(Integer.parseInt(value));
                            break;
                        case "timeMinutes":
                            setTimeMinutes(Integer.parseInt(value));
                            break;
                        case "isWhiteSide":
                            setWhiteSide(Boolean.parseBoolean(value));
                            break;
                        case "masterVolume":
                            setMasterVolume(Integer.parseInt(value));
                            break;
                        case "musicVolume":
                            setMusicVolume(Integer.parseInt(value));
                            break;
                        case "sfxVolume":
                            setSfxVolume(Integer.parseInt(value));
                            break;
                    }
                }
            }
            return true;
        }
        catch (IOException | NumberFormatException e)
        {
            System.err.println("Error loading settings: " + e.getMessage());
            // Fall back to defaults
            setElixirMultiplier(1);
            setTimeMinutes(10);
            setWhiteSide(true);
            return false;
        }
    }
    
    /**
     * Get a formatted string describing current settings
     */
    @Override
    public String toString()
    {
        return String.format("GameSettings[elixir=%dx, time=%dmin, side=%s]",
            elixirMultiplier, timeMinutes, isWhiteSide ? "WHITE" : "BLACK");
    }
}
