import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Landing page for ClashMate game.
 * Play, info, settings, and credits button added.
 * 
 * @author Saiful Shaik
 * @version
 */
public class LandingPage extends MenuWorld
{
    // Labels and Buttons
    private Button playButton;
    private Button settingsButton;
    private Button infoButton;
    private CreditsButton creditsButton;
    private Label titleLabel;
    private Label subtitleLabel;
    
    // Animation variables
    private int titleOpacity;
    private int titleY;
    private int targetTitleY;
    private int frameCount;
    private boolean titleAnimationComplete;
    private boolean buttonsVisible;
    
    public LandingPage()
    {    
        super();
        titleOpacity = 0;
        titleY = 250;
        targetTitleY = 150;
        frameCount = 0;
        titleAnimationComplete = false;
        buttonsVisible = false;
        prepare();
    }
    
    public void act()
    {
        // Start music on first act
        startMusicIfNeeded();
        
        frameCount++;
        animateTitle();
        checkButtonClicks();
    }
    
    /**
     * Animate the title fading in and moving up
     */
    private void animateTitle()
    {
        if (!titleAnimationComplete)
        {
            if (titleOpacity < 255)
            {
                titleOpacity += 5;
                if (titleOpacity > 255) titleOpacity = 255;
                
                titleLabel.setFillColor(new Color(255, 255, 255, titleOpacity));
                subtitleLabel.setFillColor(new Color(225, 225, 225, titleOpacity));
            }
            
            if (titleY > targetTitleY)
            {
                titleY -= 2;
                titleLabel.setLocation(300, titleY);
                subtitleLabel.setLocation(300, titleY + 70);
            }
            
            if (titleOpacity >= 255 && titleY <= targetTitleY)
            {
                titleAnimationComplete = true;
                showButtons();
            }
        }
    }
    
    /**
     * Show the buttons with a fade-in effect
     */
    private void showButtons()
    {
        if (!buttonsVisible)
        {
            buttonsVisible = true;
        }
    }
    
    /**
     * Check if any buttons were clicked and navigate accordingly
     */
    private void checkButtonClicks()
    {
        if (titleAnimationComplete)
        {
            if (playButton.wasClicked())
            {
                Greenfoot.setWorld(new EditorWorld());
            }
            else if (settingsButton.wasClicked())
            {
                Greenfoot.setWorld(new SettingsWorld());
            }
            else if (infoButton.wasClicked())
            {
                Greenfoot.setWorld(new InfoWorld());
            }
            else if (creditsButton.wasClicked())
            {
                Greenfoot.setWorld(new CreditsWorld());
            }
        }
    }
    
    /**
     * Display credits information
     */
    private void showCredits()
    {
        Greenfoot.playSound("sounds/click.mp3");
    }
    
    /**
     * Prepare the world with all UI elements
     */
    private void prepare() 
    {
        createBackground();
        
        // Create title label
        titleLabel = new Label("CLASHMATE", 80);
        titleLabel.setLineColor(new Color(0, 0, 0, 0));
        titleLabel.setFillColor(new Color(255, 255, 255, titleOpacity));
        addObject(titleLabel, 300, titleY);
        
        // Create subtitle label
        subtitleLabel = new Label("Clash Royale Chess", 36);
        subtitleLabel.setLineColor(new Color(0, 0, 0, 0));
        subtitleLabel.setFillColor(new Color(225, 225, 225, titleOpacity));
        addObject(subtitleLabel, 300, titleY + 70);
        
        // Create play button
        playButton = new Button("PLAY", 220, 65, 
                                new Color(34, 139, 34), 
                                new Color(50, 205, 50), 
                                Color.WHITE, 28);
        addObject(playButton, 300, 320);
        
        infoButton = new Button("INFO", 220, 65,
                                   new Color(70, 130, 180),
                                   new Color(100, 149, 237),
                                   Color.WHITE, 28);
                                   
        addObject(infoButton, 300, 410);
        
        // Create settings button
        settingsButton = new Button("SETTINGS", 220, 65,
                                   new Color(90, 90, 120),
                                   new Color(120, 120, 160),
                                   Color.WHITE, 28);
        addObject(settingsButton, 300, 500);
        
        // Create small credits button in bottom right corner
        creditsButton = new CreditsButton();
        addObject(creditsButton, 555, 580);
    }

    /**
     * Create a gradient background for the landing page
     */
    private void createBackground()
    {
        // Create a solid color background to fill the frame
        GreenfootImage bg = new GreenfootImage(getWidth(), getHeight());
        bg.setColor(new Color(20, 30, 60));
        bg.fill();
        
        // Load and scale the image smaller
        GreenfootImage img = new GreenfootImage("Background.png");
        int x = 0;
        int y = 0;
        bg.drawImage(img, x, y);
        
        setBackground(bg);
    }
}
