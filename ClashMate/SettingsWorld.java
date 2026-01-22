import greenfoot.*;

/**
 * Settings world for adjusting game audio settings.
 * Features animated sliders for Master, Music, and SFX volume.
 * Settings are automatically saved and persist across sessions.
 * Extends MenuWorld for common menu functionality.
 * 
 * @author Saiful Shaik
 * @version
 */
public class SettingsWorld extends MenuWorld
{
    // UI Components
    private Button backButton;
    private Label titleLabel;
    private VolumeSlider masterSlider;
    private VolumeSlider musicSlider;
    private VolumeSlider sfxSlider;
    
    // Animation variables
    private int fadeInAlpha;
    private int slideOffset;
    private boolean animatingIn;
    private int frameCount;
    
    // Panel animation
    private int panelY;
    private int targetPanelY;
    
    // Exit animation
    private boolean isExiting = false;
    private int exitAlpha = 0;
    
    public SettingsWorld()
    {
        super();
        
        // Initialize animation variables
        fadeInAlpha = 0;
        slideOffset = 100;
        animatingIn = true;
        frameCount = 0;
        panelY = 700;
        targetPanelY = 300;
        
        prepare();
    }
    
    public void act()
    {
        frameCount++;
        
        if (isExiting)
        {
            animateExit();
        }
        else
        {
            animateEntrance();
            checkButtonClicks();
            updateSettingsOnChange();
        }
    }
    
    /**
     * Animate the exit transition
     */
    private void animateExit()
    {
        exitAlpha += 15;
        
        // Slide panel down
        if (panelY < 700)
        {
            panelY += 30;
            repositionElements();
        }
        
        if (exitAlpha >= 255)
        {
            goToLandingPage();
            return;
        }
        
        // Update background with fade
        updateBackground();
        
        // Draw fade overlay
        GreenfootImage overlay = getBackground();
        overlay.setColor(new Color(0, 0, 0, exitAlpha));
        overlay.fillRect(0, 0, getWidth(), getHeight());
    }
    
    /**
     * Animate elements sliding in
     */
    private void animateEntrance()
    {
        if (animatingIn)
        {
            // Fade in background
            if (fadeInAlpha < 255)
            {
                fadeInAlpha = Math.min(255, fadeInAlpha + 15);
                updateBackground();
            }
            
            // Slide in panel
            if (panelY > targetPanelY)
            {
                panelY = Math.max(targetPanelY, panelY - 25);
                repositionElements();
            }
            
            // Slide offset for elements
            if (slideOffset > 0)
            {
                slideOffset = Math.max(0, slideOffset - 8);
            }
            
            // Animation complete
            if (fadeInAlpha >= 255 && slideOffset <= 0 && panelY <= targetPanelY)
            {
                animatingIn = false;
            }
        }
    }
    
    /**
     * Reposition elements during animation
     */
    private void repositionElements()
    {
        if (titleLabel != null)
        {
            titleLabel.setLocation(300, panelY - 180);
        }
        if (masterSlider != null)
        {
            masterSlider.setLocation(300, panelY - 70);
        }
        if (musicSlider != null)
        {
            musicSlider.setLocation(300, panelY + 20);
        }
        if (sfxSlider != null)
        {
            sfxSlider.setLocation(300, panelY + 110);
        }
        if (backButton != null)
        {
            backButton.setLocation(300, panelY + 200);
        }
    }
    
    /**
     * Check for button clicks
     */
    private void checkButtonClicks()
    {
        if (backButton != null && backButton.wasClicked() && !isExiting)
        {
            // Save settings before leaving
            saveSettings();
            isExiting = true;
        }
    }
    
    /**
     * Update settings in real-time as sliders change
     */
    private void updateSettingsOnChange()
    {
        GameSettings settings = GameSettings.getInstance();
        
        if (masterSlider != null)
        {
            settings.setMasterVolume(masterSlider.getValue());
        }
        if (musicSlider != null)
        {
            settings.setMusicVolume(musicSlider.getValue());
        }
        if (sfxSlider != null)
        {
            settings.setSfxVolume(sfxSlider.getValue());
        }
        
        // Update music volume in real-time
        SoundManager.getInstance().updateMusicVolume();
        
        // Save settings
        settings.save();
    }
    
    /**
     * Save all settings to file
     */
    private void saveSettings()
    {
        GameSettings.getInstance().save();
    }
    
    /**
     * Prepare all UI elements
     */
    private void prepare()
    {
        updateBackground();
        
        GameSettings settings = GameSettings.getInstance();
        
        // Create title with animation offset
        titleLabel = new Label("SETTINGS", 48);
        titleLabel.setLineColor(new Color(0, 0, 0, 0));
        titleLabel.setFillColor(Color.WHITE);
        addObject(titleLabel, 300, panelY - 180);
        
        // Create volume sliders
        masterSlider = new VolumeSlider("Master Volume", settings.getMasterVolume());
        addObject(masterSlider, 300, panelY - 70);
        
        musicSlider = new VolumeSlider("Music Volume", settings.getMusicVolume());
        addObject(musicSlider, 300, panelY + 20);
        
        sfxSlider = new VolumeSlider("SFX Volume", settings.getSfxVolume());
        addObject(sfxSlider, 300, panelY + 110);
        
        // Create back button
        backButton = new Button("← BACK", 180, 55,
                               new Color(80, 80, 100),
                               new Color(100, 120, 160),
                               Color.WHITE, 24);
        addObject(backButton, 300, panelY + 200);
    }
    
    /**
     * Update the background with animated fade
     */
    private void updateBackground()
    {
        GreenfootImage bg = new GreenfootImage(getWidth(), getHeight());
        
        // Draw gradient background
        for (int y = 0; y < getHeight(); y++)
        {
            float ratio = (float) y / getHeight();
            int r = (int) (20 + ratio * 15);
            int g = (int) (25 + ratio * 20);
            int b = (int) (50 + ratio * 30);
            
            // Apply fade
            int alpha = Math.min(fadeInAlpha, 255);
            bg.setColor(new Color(r, g, b, alpha));
            bg.drawLine(0, y, getWidth(), y);
        }
        
        // Draw decorative panel background
        if (fadeInAlpha > 100)
        {
            int panelAlpha = Math.min((fadeInAlpha - 100) * 2, 80);
            bg.setColor(new Color(40, 45, 70, panelAlpha));
            int panelTop = panelY - 220;
            int panelHeight = 450;
            bg.fillRect(50, Math.max(0, panelTop), 500, panelHeight);
            
            // Panel border
            bg.setColor(new Color(100, 120, 160, panelAlpha));
            bg.drawRect(50, Math.max(0, panelTop), 500, panelHeight);
        }
        
        // Draw subtle grid pattern
        if (fadeInAlpha > 150)
        {
            int gridAlpha = Math.min((fadeInAlpha - 150), 20);
            bg.setColor(new Color(255, 255, 255, gridAlpha));
            for (int x = 0; x < getWidth(); x += 30)
            {
                bg.drawLine(x, 0, x, getHeight());
            }
            for (int y = 0; y < getHeight(); y += 30)
            {
                bg.drawLine(0, y, getWidth(), y);
            }
        }
        
        setBackground(bg);
    }
}
