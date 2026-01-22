import greenfoot.*;

/**
 * A custom volume slider component with drag functionality.
 * Allows users to click and drag to adjust volume levels.
 * 
 * @author Saiful Shaik
 */
public class VolumeSlider extends Actor
{
    private String label;
    private int value; // 0-100
    private int sliderWidth;
    private int sliderHeight;
    private int knobRadius;
    private boolean isDragging;
    private int animatedKnobX;
    private int targetKnobX;
    
    // Colors
    private Color trackColor;
    private Color filledColor;
    private Color knobColor;
    private Color knobHoverColor;
    private Color textColor;
    private boolean isHovering;
    
    // Animation
    private int glowPulse;
    private boolean glowIncreasing;
    
    /**
     * Create a volume slider
     * @param label The label to display
     * @param initialValue The initial value (0-100)
     */
    public VolumeSlider(String label, int initialValue)
    {
        this.label = label;
        this.value = Math.max(0, Math.min(100, initialValue));
        this.sliderWidth = 250;
        this.sliderHeight = 12;
        this.knobRadius = 14;
        this.isDragging = false;
        this.isHovering = false;
        this.glowPulse = 0;
        this.glowIncreasing = true;
        
        // Initialize colors
        trackColor = new Color(60, 60, 80);
        filledColor = new Color(100, 180, 255);
        knobColor = new Color(255, 255, 255);
        knobHoverColor = new Color(100, 200, 255);
        textColor = Color.WHITE;
        
        // Calculate initial knob position
        animatedKnobX = valueToKnobX(value);
        targetKnobX = animatedKnobX;
        
        updateImage();
    }
    
    public void act()
    {
        handleMouse();
        animateKnob();
        animateGlow();
        updateImage();
    }
    
    /**
     * Handle mouse interaction for dragging
     */
    private void handleMouse()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        if (mouse != null)
        {
            int localX = mouse.getX() - getX() + getImage().getWidth() / 2;
            int localY = mouse.getY() - getY() + getImage().getHeight() / 2;
            
            // Check if hovering over knob or track
            int trackStartX = 20;
            int trackEndX = trackStartX + sliderWidth;
            int trackY = 50;
            int knobX = trackStartX + (value * sliderWidth / 100);
            
            boolean overTrack = localX >= trackStartX - 5 && localX <= trackEndX + 5 &&
                               localY >= trackY - knobRadius && localY <= trackY + knobRadius;
            boolean overKnob = Math.abs(localX - knobX) <= knobRadius + 3 &&
                              Math.abs(localY - trackY) <= knobRadius + 3;
            
            isHovering = overTrack || overKnob;
            
            // Start dragging
            if (Greenfoot.mousePressed(null) && (overTrack || overKnob))
            {
                isDragging = true;
            }
            
            // While dragging, update value
            if (isDragging)
            {
                int newX = localX - trackStartX;
                int newValue = (newX * 100) / sliderWidth;
                newValue = Math.max(0, Math.min(100, newValue));
                
                if (newValue != value)
                {
                    value = newValue;
                    targetKnobX = valueToKnobX(value);
                    // Play subtle tick sound for feedback
                }
            }
        }
        
        // Stop dragging when mouse released
        if (Greenfoot.mouseClicked(null) || Greenfoot.mouseDragEnded(null))
        {
            isDragging = false;
        }
    }
    
    /**
     * Convert value to knob X position
     */
    private int valueToKnobX(int val)
    {
        return 20 + (val * sliderWidth / 100);
    }
    
    /**
     * Smoothly animate knob to target position
     */
    private void animateKnob()
    {
        if (animatedKnobX != targetKnobX)
        {
            int diff = targetKnobX - animatedKnobX;
            int step = Math.max(1, Math.abs(diff) / 4);
            
            if (diff > 0)
            {
                animatedKnobX = Math.min(animatedKnobX + step, targetKnobX);
            }
            else
            {
                animatedKnobX = Math.max(animatedKnobX - step, targetKnobX);
            }
        }
    }
    
    /**
     * Animate glow effect when hovering
     */
    private void animateGlow()
    {
        if (isHovering || isDragging)
        {
            if (glowIncreasing)
            {
                glowPulse += 3;
                if (glowPulse >= 30) glowIncreasing = false;
            }
            else
            {
                glowPulse -= 3;
                if (glowPulse <= 0) glowIncreasing = true;
            }
        }
        else
        {
            glowPulse = Math.max(0, glowPulse - 5);
        }
    }
    
    /**
     * Update the slider's visual appearance
     */
    private void updateImage()
    {
        int imgWidth = sliderWidth + 60;
        int imgHeight = 80;
        
        GreenfootImage img = new GreenfootImage(imgWidth, imgHeight);
        
        // Draw label
        GreenfootImage labelImg = new GreenfootImage(label, 22, textColor, new Color(0, 0, 0, 0));
        img.drawImage(labelImg, 20, 5);
        
        // Draw value percentage
        String valueText = value + "%";
        GreenfootImage valueImg = new GreenfootImage(valueText, 20, 
            new Color(180, 180, 180), new Color(0, 0, 0, 0));
        img.drawImage(valueImg, imgWidth - 50, 8);
        
        int trackY = 50;
        int trackStartX = 20;
        
        // Draw track background
        img.setColor(trackColor);
        img.fillRect(trackStartX, trackY - sliderHeight/2, sliderWidth, sliderHeight);
        
        // Draw filled portion
        int filledWidth = (animatedKnobX - trackStartX);
        if (filledWidth > 0)
        {
            // Create gradient effect for filled portion
            for (int i = 0; i < filledWidth; i++)
            {
                float ratio = (float) i / sliderWidth;
                int r = (int) (80 + ratio * 50);
                int g = (int) (150 + ratio * 50);
                int b = 255;
                img.setColor(new Color(r, g, b));
                img.drawLine(trackStartX + i, trackY - sliderHeight/2 + 2, 
                            trackStartX + i, trackY + sliderHeight/2 - 2);
            }
        }
        
        // Draw knob glow when hovering/dragging
        if (glowPulse > 0)
        {
            for (int i = 3; i >= 1; i--)
            {
                int alpha = (glowPulse * i) / 3;
                Color glowColor = new Color(100, 200, 255, Math.min(alpha, 100));
                img.setColor(glowColor);
                img.fillOval(animatedKnobX - knobRadius - i * 2, trackY - knobRadius - i * 2,
                            (knobRadius + i * 2) * 2, (knobRadius + i * 2) * 2);
            }
        }
        
        // Draw knob
        Color currentKnobColor = (isHovering || isDragging) ? knobHoverColor : knobColor;
        img.setColor(currentKnobColor);
        img.fillOval(animatedKnobX - knobRadius, trackY - knobRadius, 
                    knobRadius * 2, knobRadius * 2);
        
        // Draw knob border
        img.setColor(new Color(200, 200, 200));
        img.drawOval(animatedKnobX - knobRadius, trackY - knobRadius, 
                    knobRadius * 2, knobRadius * 2);
        
        // Draw inner knob highlight
        img.setColor(new Color(255, 255, 255, 100));
        img.fillOval(animatedKnobX - knobRadius + 4, trackY - knobRadius + 4, 
                    knobRadius - 2, knobRadius - 2);
        
        setImage(img);
    }
    
    /**
     * Get the current volume value
     * @return value 0-100
     */
    public int getValue()
    {
        return value;
    }
    
    /**
     * Set the volume value
     * @param val 0-100
     */
    public void setValue(int val)
    {
        this.value = Math.max(0, Math.min(100, val));
        this.targetKnobX = valueToKnobX(value);
        this.animatedKnobX = targetKnobX;
    }
    
    /**
     * Check if user is currently dragging
     */
    public boolean isDragging()
    {
        return isDragging;
    }
}
