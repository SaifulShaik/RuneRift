import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LandingPage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LandingPage extends World
{
    // Labels and Buttons
    private Button playButton;
    private Button settingsButton;
    private Button creditsButton;
    private Label titleLabel;
    private Label subtitleLabel;
    
    // Animation variables
    private int titleOpacity;
    private int titleY;
    private int targetTitleY;
    private int frameCount;
    private boolean titleAnimationComplete;
    
    public LandingPage()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 600, 1); 
        titleOpacity = 0;
        titleY = 200;
        targetTitleY = 150;
        frameCount = 0;
        titleAnimationComplete = false;
        prepare();
        
    }
    
    private void prepare() {
        createBackground();
        
        titleLabel = new Label("CLASHMATE", 80);
        titleLabel.setLineColor(new Color(0, 0, 0, 0));
        titleLabel.setFillColor(new Color(255, 255, 255, titleOpacity));
        subtitleLabel = new Label("Clash Royale Chess.", 40);
        subtitleLabel.setLineColor(new Color(0, 0, 0, 0));
        subtitleLabel.setFillColor(new Color(225, 225, 225, titleOpacity));
        addObject(titleLabel, 600, titleY);
        addObject(subtitleLabel, 600, 200);
        playButton = new Button("START GAME", 220, 65, 
                                new Color(34, 200, 170), 
                                new Color(50, 205, 50), 
                                Color.WHITE, 30);
        addObject(playButton, 900, 300);
        
        creditsButton = new Button("CREDITS", 180, 55,
                                   new Color(50, 50, 50),
                                   new Color(80, 80, 80),
                                   Color.WHITE, 24);
        //addObject(creditsButton, 600, 520);
        addObject(creditsButton, 900, 400);
    }

    private void createBackground()
    {
        setBackground(new GreenfootImage(""));
    }
}
