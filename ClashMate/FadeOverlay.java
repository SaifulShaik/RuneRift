import greenfoot.*;

/**
 * Creates a fade-in effect for the game board.
 * Shows a loading animation, then fades out to reveal the board.
 * 
 * @author Saiful Shaik
 */
public class FadeOverlay extends Actor
{
    private int alpha;
    private double fadeSpeed;
    private boolean fadeComplete;
    private int delayFrames;
    private int animationFrame;
    private int width;
    private int height;
    private static final int FRAMES_PER_SECOND = 30;
    private static final int DELAY_SECONDS = 1;
    private static final int NUM_DOTS = 8;
    private static final int DOT_RADIUS = 8;
    private static final int CIRCLE_RADIUS = 40;
    
    /**
     * Create a fade overlay that covers the entire screen
     */
    public FadeOverlay(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.alpha = 255; // Start fully opaque (black)
        this.fadeSpeed = 1; // How fast to fade (higher = faster)
        this.fadeComplete = false;
        this.delayFrames = FRAMES_PER_SECOND * DELAY_SECONDS; // 3 second delay
        this.animationFrame = 0;
        updateLoadingImage();
    }
    
    /**
     * Update the overlay image with loading animation
     */
    private void updateLoadingImage()
    {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(0, 0, 0, alpha));
        img.fill();
        
        // Draw loading spinner
        int centerX = width / 2;
        int centerY = height / 2;
        
        for (int i = 0; i < NUM_DOTS; i++)
        {
            double angle = (2 * Math.PI * i / NUM_DOTS) - (Math.PI / 2);
            int dotX = centerX + (int)(Math.cos(angle) * CIRCLE_RADIUS);
            int dotY = centerY + (int)(Math.sin(angle) * CIRCLE_RADIUS);
            
            // Calculate alpha for each dot based on animation frame
            int dotIndex = (animationFrame / 5) % NUM_DOTS;
            int distance = (i - dotIndex + NUM_DOTS) % NUM_DOTS;
            int dotAlpha = 255 - (distance * 30);
            if (dotAlpha < 50) dotAlpha = 50;
            
            img.setColor(new Color(255, 215, 0, dotAlpha)); // Gold color
            img.fillOval(dotX - DOT_RADIUS, dotY - DOT_RADIUS, DOT_RADIUS * 2, DOT_RADIUS * 2);
        }
        
        // Draw "Loading..." text
        img.setColor(new Color(255, 255, 255, 200));
        img.setFont(new Font("Arial", true, false, 24));
        String loadingText = "Loading";
        int dots = (animationFrame / 20) % 4;
        for (int i = 0; i < dots; i++) loadingText += ".";
        
        // Center the text
        int textWidth = loadingText.length() * 12;
        img.drawString(loadingText, centerX - textWidth / 2, centerY + CIRCLE_RADIUS + 50);
        
        setImage(img);
    }
    
    /**
     * Update the overlay image during fade
     */
    private void updateFadeImage()
    {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setColor(new Color(0, 0, 0, alpha));
        img.fill();
        setImage(img);
    }
    
    /**
     * Act - show loading animation, then fade out over time
     */
    public void act()
    {
        // Loading animation during delay
        if (delayFrames > 0)
        {
            animationFrame++;
            updateLoadingImage();
            delayFrames--;
            return;
        }
        
        if (!fadeComplete)
        {
            alpha -= fadeSpeed;
            if (alpha <= 0)
            {
                alpha = 0;
                fadeComplete = true;
                getWorld().removeObject(this);
                return;
            }
            updateFadeImage();
        }
    }
    
    /**
     * Check if the fade animation is complete
     */
    public boolean isFadeComplete()
    {
        return fadeComplete;
    }
}
