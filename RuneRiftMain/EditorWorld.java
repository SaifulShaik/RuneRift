import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Editor World for configuring game settings before starting.
 * Allows adjustment of Time, Elixir, and Side selection.
 * 
 * @author RuneRift Team
 * @version 1.0
 */
public class EditorWorld extends World
{
    // Game settings
    private int timeMinutes = 10;       // Default 10 minutes (range 1-30)
    private int elixirMultiplier = 1;   // Default 1x elixir (options: 1, 2, 3)
    private boolean isWhiteSide = true; // Default white side
    
    // UI Elements
    private Label titleLabel;
    private Label timeLabel;
    private Label timeValueLabel;
    private Label elixirLabel;
    private Label elixirValueLabel;
    private Label sideLabel;
    private Label sideValueLabel;
    
    // Buttons
    private Button timeUpButton;
    private Button timeDownButton;
    private Button elixir1xButton;
    private Button elixir2xButton;
    private Button elixir3xButton;
    private Button whiteSideButton;
    private Button blackSideButton;
    private Button startButton;
    
    // Visual elements
    private GreenfootImage backgroundImage;
    
    /**
     * Constructor for objects of class EditorWorld.
     */
    public EditorWorld()
    {    
        super(600, 600, 1); 
        loadSavedSettings();
        createBackground();
        setupUI();
    }
    
    /**
     * Load previously saved settings from file
     */
    private void loadSavedSettings()
    {
        GameSettings settings = GameSettings.getInstance();
        timeMinutes = settings.getTimeMinutes();
        elixirMultiplier = settings.getElixirMultiplier();
        isWhiteSide = settings.isWhiteSide();
    }
    
    /**
     * Create a stylish background for the editor
     */
    private void createBackground()
    {
        backgroundImage = new GreenfootImage(600, 600);
        
        // Gradient background (dark blue to darker blue)
        for (int y = 0; y < 600; y++)
        {
            int r = 20 + (y * 15 / 600);
            int g = 30 + (y * 20 / 600);
            int b = 60 + (y * 40 / 600);
            backgroundImage.setColor(new Color(r, g, b));
            backgroundImage.drawLine(0, y, 600, y);
        }
        
        // Add decorative border
        backgroundImage.setColor(new Color(100, 150, 200, 100));
        backgroundImage.drawRect(20, 20, 560, 560);
        backgroundImage.drawRect(22, 22, 556, 556);
        
        setBackground(backgroundImage);
    }
    
    /**
     * Setup all UI elements
     */
    private void setupUI()
    {
        // === VERTICAL OFFSET ===
        // Change this single value to move the entire UI up (negative) or down (positive)
        int yOffset = 0;
        
        // Title
        titleLabel = new Label("GAME SETUP", 42);
        titleLabel.setFillColor(new Color(255, 215, 0)); // Gold
        titleLabel.setLineColor(new Color(180, 140, 0));
        addObject(titleLabel, 300, 65 + yOffset);
        
        // Decorative line under title
        addDecorativeLine(300, 90 + yOffset, 300);
        
        // === TIME SECTION ===
        createSectionPanel(300, 150 + yOffset, 400, 80, "TIME");
        
        timeLabel = new Label("Game Time:", 24);
        timeLabel.setFillColor(Color.WHITE);
        timeLabel.setLineColor(new Color(100, 100, 100));
        addObject(timeLabel, 180, 150 + yOffset);
        
        timeValueLabel = new Label(timeMinutes + " min", 28);
        timeValueLabel.setFillColor(new Color(100, 200, 255));
        timeValueLabel.setLineColor(new Color(50, 100, 150));
        addObject(timeValueLabel, 300, 150 + yOffset);
        
        timeDownButton = new Button("-", 50, 40, 
            new Color(180, 80, 80), new Color(220, 100, 100), Color.WHITE, 28);
        addObject(timeDownButton, 390, 150 + yOffset);
        
        timeUpButton = new Button("+", 50, 40, 
            new Color(80, 180, 80), new Color(100, 220, 100), Color.WHITE, 28);
        addObject(timeUpButton, 450, 150 + yOffset);
        
        // === ELIXIR MULTIPLIER SECTION ===
        createSectionPanel(300, 265 + yOffset, 400, 120, "ELIXIR");
        
        elixirLabel = new Label("Elixir Multiplier: ", 24);
        elixirLabel.setFillColor(Color.WHITE);
        elixirLabel.setLineColor(new Color(100, 100, 100));
        addObject(elixirLabel, 280, 230 + yOffset);
        
        elixirValueLabel = new Label(elixirMultiplier + "x", 28);
        elixirValueLabel.setFillColor(new Color(200, 100, 255));
        elixirValueLabel.setLineColor(new Color(100, 50, 150));
        addObject(elixirValueLabel, 380, 230 + yOffset);
        
        // Three mutually exclusive multiplier buttons
        elixir1xButton = new Button("1x", 70, 40, 
            getMultiplierButtonColor(1, false), getMultiplierButtonColor(1, true), Color.WHITE, 22);
        addObject(elixir1xButton, 220, 290 + yOffset);
        
        elixir2xButton = new Button("2x", 70, 40, 
            getMultiplierButtonColor(2, false), getMultiplierButtonColor(2, true), Color.WHITE, 22);
        addObject(elixir2xButton, 300, 290 + yOffset);
        
        elixir3xButton = new Button("3x", 70, 40, 
            getMultiplierButtonColor(3, false), getMultiplierButtonColor(3, true), Color.WHITE, 22);
        addObject(elixir3xButton, 380, 290 + yOffset);
        
        updateElixirButtonHighlights();
        
        // === SIDE SELECTION SECTION ===
        createSectionPanel(300, 420 + yOffset, 400, 160, "CHOOSE YOUR SIDE");
        
        sideLabel = new Label("Playing as:", 24);
        sideLabel.setFillColor(Color.WHITE);
        sideLabel.setLineColor(new Color(100, 100, 100));
        addObject(sideLabel, 300, 370 + yOffset);
        
        whiteSideButton = new Button("WHITE", 140, 50, 
            new Color(240, 240, 240), new Color(255, 255, 255), Color.BLACK, 22);
        addObject(whiteSideButton, 200, 420 + yOffset);
        
        blackSideButton = new Button("BLACK", 140, 50, 
            new Color(50, 50, 50), new Color(80, 80, 80), Color.WHITE, 22);
        addObject(blackSideButton, 400, 420 + yOffset);
        
        sideValueLabel = new Label("► WHITE ◄", 26);
        sideValueLabel.setFillColor(new Color(255, 255, 200));
        sideValueLabel.setLineColor(new Color(150, 150, 100));
        addObject(sideValueLabel, 300, 470 + yOffset);
        
        // === START BUTTON ===
        startButton = new Button("START GAME", 200, 60, 
            new Color(50, 150, 50), new Color(70, 200, 70), Color.WHITE, 26);
        addObject(startButton, 300, 540 + yOffset);
    }
    
    /**
     * Create a decorative section panel background
     */
    private void createSectionPanel(int x, int y, int width, int height, String title)
    {
        GreenfootImage panel = new GreenfootImage(width, height);
        
        // Semi-transparent background
        panel.setColor(new Color(40, 50, 70, 180));
        panel.fillRect(0, 0, width, height);
        
        // Border
        panel.setColor(new Color(100, 130, 180));
        panel.drawRect(0, 0, width - 1, height - 1);
        panel.drawRect(1, 1, width - 3, height - 3);
        
        // Corner decorations
        panel.setColor(new Color(150, 180, 220));
        panel.fillRect(0, 0, 10, 3);
        panel.fillRect(0, 0, 3, 10);
        panel.fillRect(width - 10, 0, 10, 3);
        panel.fillRect(width - 3, 0, 3, 10);
        panel.fillRect(0, height - 3, 10, 3);
        panel.fillRect(0, height - 10, 3, 10);
        panel.fillRect(width - 10, height - 3, 10, 3);
        panel.fillRect(width - 3, height - 10, 3, 10);
        
        Actor panelActor = new Actor() {};
        panelActor.setImage(panel);
        addObject(panelActor, x, y);
    }
    
    /**
     * Add a decorative horizontal line
     */
    private void addDecorativeLine(int x, int y, int width)
    {
        GreenfootImage line = new GreenfootImage(width, 5);
        
        // Gradient line effect
        for (int i = 0; i < width; i++)
        {
            int alpha = 255 - Math.abs(i - width/2) * 255 / (width/2);
            line.setColor(new Color(200, 180, 100, alpha));
            line.drawLine(i, 2, i, 2);
            line.setColor(new Color(255, 215, 0, alpha / 2));
            line.drawLine(i, 1, i, 1);
            line.drawLine(i, 3, i, 3);
        }
        
        Actor lineActor = new Actor() {};
        lineActor.setImage(line);
        addObject(lineActor, x, y);
    }
    
    /**
     * Act method - handle button clicks
     */
    public void act()
    {
        handleButtonClicks();
    }
    
    /**
     * Handle all button click events
     */
    private void handleButtonClicks()
    {
        if (Greenfoot.mouseClicked(timeUpButton))
        {
            timeMinutes = Math.min(timeMinutes + 1, 30); // Max 30 minutes
            updateTimeDisplay();
            SoundManager.getInstance().playClick();
        }
        else if (Greenfoot.mouseClicked(timeDownButton))
        {
            timeMinutes = Math.max(timeMinutes - 1, 1); // Min 1 minute
            updateTimeDisplay();
            SoundManager.getInstance().playClick();
        }
        else if (Greenfoot.mouseClicked(elixir1xButton))
        {
            elixirMultiplier = 1;
            updateElixirDisplay();
            SoundManager.getInstance().playClick();
        }
        else if (Greenfoot.mouseClicked(elixir2xButton))
        {
            elixirMultiplier = 2;
            updateElixirDisplay();
            SoundManager.getInstance().playClick();
        }
        else if (Greenfoot.mouseClicked(elixir3xButton))
        {
            elixirMultiplier = 3;
            updateElixirDisplay();
            SoundManager.getInstance().playClick();
        }
        else if (Greenfoot.mouseClicked(whiteSideButton))
        {
            isWhiteSide = true;
            updateSideDisplay();
            SoundManager.getInstance().playClick();
        }
        else if (Greenfoot.mouseClicked(blackSideButton))
        {
            isWhiteSide = false;
            updateSideDisplay();
            SoundManager.getInstance().playClick();
        }
        else if (Greenfoot.mouseClicked(startButton))
        {
            startGame();
        }
    }
    
    /**
     * Update the time display label
     */
    private void updateTimeDisplay()
    {
        timeValueLabel.setValue(timeMinutes + " min");
    }
    
    /**
     * Update the elixir display label and button highlights
     */
    private void updateElixirDisplay()
    {
        elixirValueLabel.setValue(elixirMultiplier + "x");
        updateElixirButtonHighlights();
    }
    
    /**
     * Get the color for a multiplier button based on selection state
     */
    private Color getMultiplierButtonColor(int multiplier, boolean isHover)
    {
        if (multiplier == elixirMultiplier)
        {
            // Selected - bright purple
            return isHover ? new Color(180, 80, 255) : new Color(140, 60, 200);
        }
        else
        {
            // Not selected - gray
            return isHover ? new Color(100, 100, 100) : new Color(70, 70, 70);
        }
    }
    
    /**
     * Update button colors to show which multiplier is selected
     */
    private void updateElixirButtonHighlights()
    {
        // Recreate buttons with updated colors to show selection
        if (elixir1xButton != null)
        {
            elixir1xButton.updateColors(
                getMultiplierButtonColor(1, false), 
                getMultiplierButtonColor(1, true));
        }
        if (elixir2xButton != null)
        {
            elixir2xButton.updateColors(
                getMultiplierButtonColor(2, false), 
                getMultiplierButtonColor(2, true));
        }
        if (elixir3xButton != null)
        {
            elixir3xButton.updateColors(
                getMultiplierButtonColor(3, false), 
                getMultiplierButtonColor(3, true));
        }
    }
    
    /**
     * Update the side selection display
     */
    private void updateSideDisplay()
    {
        if (isWhiteSide)
        {
            sideValueLabel.setValue("► WHITE ◄");
            sideValueLabel.setFillColor(new Color(255, 255, 200));
        }
        else
        {
            sideValueLabel.setValue("► BLACK ◄");
            sideValueLabel.setFillColor(new Color(150, 150, 150));
        }
    }
    
    /**
     * Start the game with selected settings
     */
    private void startGame()
    {
        // Save settings to file
        saveSettings();
        
        SoundManager.getInstance().playStart();
        // GridWorld will load settings from GameSettings
        Greenfoot.setWorld(new GridWorld());
    }
    
    /**
     * Save current settings to configuration file
     */
    private void saveSettings()
    {
        GameSettings settings = GameSettings.getInstance();
        settings.setElixirMultiplier(elixirMultiplier);
        settings.setTimeMinutes(timeMinutes);
        settings.setWhiteSide(isWhiteSide);
        settings.save();
    }
    
    // Getter methods for settings
    public int getTimeMinutes() { return timeMinutes; }
    public int getElixirMultiplier() { return elixirMultiplier; }
    public boolean isWhiteSide() { return isWhiteSide; }
}
