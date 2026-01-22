import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Editor World for configuring game settings before starting.
 * Allows adjustment of Time, Elixir, and Side selection.
 * Extends MenuWorld for common menu functionality.
 * 
 * @author Saiful Shaik
 * @version
 */
public class EditorWorld extends MenuWorld
{
    // Game settings
    private int timeMinutes = 10;
    private int elixirMultiplier = 1;
    
    // UI Elements
    private Label titleLabel;
    private Label timeLabel;
    private Label timeValueLabel;
    private Label elixirLabel;
    private Label elixirValueLabel;
    
    // Buttons
    private Button timeUpButton;
    private Button timeDownButton;
    private Button elixir1xButton;
    private Button elixir2xButton;
    private Button elixir3xButton;
    private Button backButton;
    private Button startButton;
    
    // Visual elements
    private GreenfootImage backgroundImage;
    
    /**
     * Constructor for objects of class EditorWorld.
     */
    public EditorWorld()
    {    
        super();
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
    }
    
    /**
     * Create a stylish background for the editor
     */
    private void createBackground()
    {
        backgroundImage = new GreenfootImage(600, 600);
        
        // Gradient background
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
        int yOffset = 40;
        // Title
        titleLabel = new Label("GAME SETUP", 42);
        titleLabel.setFillColor(new Color(255, 215, 0)); // Gold
        titleLabel.setLineColor(new Color(180, 140, 0));
        addObject(titleLabel, 300, 70 + yOffset);
        
        // Decorative line under title
        addDecorativeLine(300, 90 + yOffset, 300);
        
        // === TIME SECTION (centered vertically) ===
        createSectionPanel(300, 220, 400, 80, "TIME");
        
        timeLabel = new Label("Game Time:", 24);
        timeLabel.setFillColor(Color.WHITE);
        timeLabel.setLineColor(new Color(100, 100, 100));
        addObject(timeLabel, 180, 220);
        
        timeValueLabel = new Label(timeMinutes + " min", 28);
        timeValueLabel.setFillColor(new Color(100, 200, 255));
        timeValueLabel.setLineColor(new Color(50, 100, 150));
        addObject(timeValueLabel, 300, 220);
        
        timeDownButton = new Button("-", 50, 40, 
            new Color(180, 80, 80), new Color(220, 100, 100), Color.WHITE, 28);
        addObject(timeDownButton, 390, 220);
        
        timeUpButton = new Button("+", 50, 40, 
            new Color(80, 180, 80), new Color(100, 220, 100), Color.WHITE, 28);
        addObject(timeUpButton, 450, 220);
        
        // === ELIXIR MULTIPLIER SECTION (centered vertically) ===
        createSectionPanel(300, 350, 400, 120, "ELIXIR");
        
        elixirLabel = new Label("Elixir Multiplier: ", 24);
        elixirLabel.setFillColor(Color.WHITE);
        elixirLabel.setLineColor(new Color(100, 100, 100));
        addObject(elixirLabel, 280, 315);
        
        elixirValueLabel = new Label(elixirMultiplier + "x", 28);
        elixirValueLabel.setFillColor(new Color(200, 100, 255));
        elixirValueLabel.setLineColor(new Color(100, 50, 150));
        addObject(elixirValueLabel, 380, 315);
        
        // Three mutually exclusive multiplier buttons
        elixir1xButton = new Button("1x", 70, 40, 
            getMultiplierButtonColor(1, false), getMultiplierButtonColor(1, true), Color.WHITE, 22);
        addObject(elixir1xButton, 220, 375);
        
        elixir2xButton = new Button("2x", 70, 40, 
            getMultiplierButtonColor(2, false), getMultiplierButtonColor(2, true), Color.WHITE, 22);
        addObject(elixir2xButton, 300, 375);
        
        elixir3xButton = new Button("3x", 70, 40, 
            getMultiplierButtonColor(3, false), getMultiplierButtonColor(3, true), Color.WHITE, 22);
        addObject(elixir3xButton, 380, 375);
        
        updateElixirButtonHighlights();
        
        // === BACK AND START BUTTONS (at bottom) ===
        backButton = new Button("← BACK", 140, 60,
            new Color(70, 70, 90), new Color(100, 100, 130), Color.WHITE, 24);
        addObject(backButton, 170, 540-yOffset);
        
        startButton = new Button("START GAME", 200, 60, 
            new Color(50, 150, 50), new Color(70, 200, 70), Color.WHITE, 26);
        addObject(startButton, 380, 540-yOffset);
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
        // Shortcut: Press 'E' to open EndGameWorld for testing
        if (Greenfoot.isKeyDown("e")) {
            // Dummy data for quick access
            java.util.List<Piece.PieceType> dummyWhite = new java.util.ArrayList<>();
            java.util.List<Piece.PieceType> dummyBlack = new java.util.ArrayList<>();
            for (int i = 0; i < 3; i++) dummyWhite.add(Piece.PieceType.ROYAL_RECRUITS);
            for (int i = 0; i < 2; i++) dummyBlack.add(Piece.PieceType.KNIGHT);
            Greenfoot.setWorld(new EndGameWorld(
                "WHITE", "Tested via shortcut", 120, 90, 600, 42, dummyWhite, dummyBlack));
        }
    }
    
    /**
     * Handle all button click events
     */
    private void handleButtonClicks()
    {
        if (timeUpButton.wasClicked())
        {
            timeMinutes = Math.min(timeMinutes + 1, 30); // Max 30 minutes
            updateTimeDisplay();
            saveSettings();
        }
        else if (timeDownButton.wasClicked())
        {
            timeMinutes = Math.max(timeMinutes - 1, 1); // Min 1 minute
            updateTimeDisplay();
            saveSettings();
        }
        else if (elixir1xButton.wasClicked())
        {
            elixirMultiplier = 1;
            updateElixirDisplay();
            saveSettings();
        }
        else if (elixir2xButton.wasClicked())
        {
            elixirMultiplier = 2;
            updateElixirDisplay();
            saveSettings();
        }
        else if (elixir3xButton.wasClicked())
        {
            elixirMultiplier = 3;
            updateElixirDisplay();
            saveSettings();
        }
        else if (backButton.wasClicked())
        {
            goToLandingPage();
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
            // Selected: bright purple
            return isHover ? new Color(180, 80, 255) : new Color(140, 60, 200);
        }
        else
        {
            // Not selected: gray
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
     * Start the game with selected settings
     */
    private void startGame()
    {
        // Save settings to file
        saveSettings();
        
        // Stop menu music before transitioning
        SoundManager.getInstance().stopMenuMusic();
        
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
        settings.save();
    }
    
    // Getter methods for settings
    public int getTimeMinutes() { return timeMinutes; }
    public int getElixirMultiplier() { return elixirMultiplier; }
}
