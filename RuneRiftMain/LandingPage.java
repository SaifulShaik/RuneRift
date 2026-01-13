import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Landing page for RuneRift game.
 * Features animated title and navigation buttons.
 * 
 * @author RuneRift Team
 * @version 1.0
 */
public class LandingPage extends World
{
    // Labels and Buttons
    private Button playButton;
    private Button creditsButton;
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
        // Create a new world with 600x600 cells with a cell size of 1x1 pixels.
        super(600, 600, 1); 
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
            // Fade in animation
            if (titleOpacity < 255)
            {
                titleOpacity += 5;
                if (titleOpacity > 255) titleOpacity = 255;
                
                titleLabel.setFillColor(new Color(255, 255, 255, titleOpacity));
                subtitleLabel.setFillColor(new Color(225, 225, 225, titleOpacity));
            }
            
            // Move up animation
            if (titleY > targetTitleY)
            {
                titleY -= 2;
                titleLabel.setLocation(300, titleY);
                subtitleLabel.setLocation(300, titleY + 70);
            }
            
            // Check if animation is complete
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
            else if (creditsButton.wasClicked())
            {
                showCredits();
            }
        }
    }
    
    /**
     * Display credits information
     */
    private void showCredits()
    {
        // Could navigate to a credits world or show a dialog
        Greenfoot.playSound("sounds/click.wav");
        // For now, just play a sound
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
        playButton = new Button("START GAME", 220, 65, 
                                new Color(34, 139, 34), 
                                new Color(50, 205, 50), 
                                Color.WHITE, 28);
        addObject(playButton, 300, 320);
        
        // Create editor button
        creditsButton = new Button("CREDITS", 220, 65,
                                   new Color(70, 130, 180),
                                   new Color(100, 149, 237),
                                   Color.WHITE, 28);
        addObject(creditsButton, 300, 410);
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
        GreenfootImage img = new GreenfootImage("Background.jpg");
        img.scale(600, 600);
        
        // Center the image on the background
        int x = 0;
        int y = 0;
        bg.drawImage(img, x, y);
        
        setBackground(bg);
    }
}
