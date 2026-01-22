import greenfoot.*;

/**
 * Base class for all menu worlds in ClashMate.
 * Provides common functionality like background setup, music handling,and standard dimensions.
 * 
 * @author Saiful Shaik
 * @version
 */
public abstract class MenuWorld extends World
{
    // Standard menu dimensions
    protected static final int MENU_WIDTH = 600;
    protected static final int MENU_HEIGHT = 600;
    
    // Music state tracking
    private boolean musicStarted = false;
    
    /**
     * Create a MenuWorld
     */
    public MenuWorld()
    {
        super(MENU_WIDTH, MENU_HEIGHT, 1);
        setupBackground();
    }
    
    public MenuWorld(int width, int height)
    {
        super(width, height, 1);
        setupBackground();
    }
    
    /**
     * Setup the background image. Override to customize.
     */
    protected void setupBackground()
    {
        GreenfootImage bg = new GreenfootImage(getWidth(), getHeight());
        bg.setColor(new Color(20, 30, 60));
        bg.fill();
        
        try
        {
            GreenfootImage img = new GreenfootImage("Background.png");
            bg.drawImage(img, 0, 0);
        }
        catch (Exception e)
        {
            // Background image not found, use solid color
        }
        
        setBackground(bg);
    }
    
    /**
     * Start menu music if not already playing.
     * Call this in act() or when the world becomes active.
     */
    protected void startMusicIfNeeded()
    {
        if (!musicStarted)
        {
            SoundManager.getInstance().playMenuMusic();
            musicStarted = true;
        }
    }
    
    /**
     * Called when Greenfoot is paused/stopped
     */
    public void stopped()
    {
        SoundManager.getInstance().stopMenuMusic();
    }
    
    /**
     * Called when Greenfoot is started/resumed
     */
    public void started()
    {
        SoundManager.getInstance().playMenuMusic();
    }
    
    /**
     * Navigate to another world
     */
    protected void navigateTo(World world)
    {
        Greenfoot.setWorld(world);
    }
    
    /**
     * Navigate back to landing page
     */
    protected void goToLandingPage()
    {
        Greenfoot.setWorld(new LandingPage());
    }
    
    /**
     * Create a styled title label
     */
    protected Label createTitle(String text, int fontSize)
    {
        Label title = new Label(text, fontSize);
        title.setFillColor(Color.WHITE);
        title.setLineColor(new Color(100, 100, 100));
        return title;
    }
    
    /**
     * Create a standard back button
     */
    protected Button createBackButton()
    {
        return new Button("← Back", 100, 40,
            new Color(70, 70, 90), new Color(100, 100, 130), Color.WHITE, 18);
    }
}
