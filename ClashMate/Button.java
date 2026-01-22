import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author Saiful Shaik
 * @version 
 */
public class Button extends Actor
{
    private String text;
    private int width;
    private int height;
    private Color normalColor;
    private Color hoverColor;
    private Color textColor;
    private int fontSize;
    private boolean isHovered;
    private int currentScale;
    private int targetScale;
    private static final int SCALE_SPEED = 2;
    
    public Button(String text, int width, int height)
    {
        this(text, width, height, new Color(70, 130, 180), new Color(100, 149, 237), Color.WHITE, 24);
    }
    
    // Method Overloading
    public Button(String text, int width, int height, Color normalColor, Color hoverColor, Color textColor, int fontSize)
    {
        this.text = text;
        this.width = width;
        this.height = height;
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.textColor = textColor;
        this.fontSize = fontSize;
        this.isHovered = false;
        this.currentScale = 100;
        this.targetScale = 100;
        updateImage();
    }
    
    public void act()
    {
        checkHover();
        animateScale();
    }
    
    /**
     * Check if mouse is hovering over the button
     */
    private void checkHover()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        boolean currentlyOver = isMouseOver();
        
        if (currentlyOver && !isHovered)
        {
            // Mouse just entered the button
            isHovered = true;
            targetScale = 110; // Scale up to 110%
        }
        else if (!currentlyOver && isHovered)
        {
            // Mouse just left the button
            isHovered = false;
            targetScale = 100; // Scale back to 100%
        }
    }
    
    /**
     * Check if mouse is currently over the button
     */
    private boolean isMouseOver()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null)
        {
            int mouseX = mouse.getX();
            int mouseY = mouse.getY();
            int myX = getX();
            int myY = getY();
            
            // Calculate scaled dimensions
            int scaledWidth = (width * currentScale) / 100;
            int scaledHeight = (height * currentScale) / 100;
            
            // Check if mouse is within button bounds
            return mouseX >= myX - scaledWidth / 2 && 
                   mouseX <= myX + scaledWidth / 2 &&
                   mouseY >= myY - scaledHeight / 2 && 
                   mouseY <= myY + scaledHeight / 2;
        }
        return false;
    }
    
    /**
     * Animate smooth scaling effect
     */
    private void animateScale()
    {
        if (currentScale < targetScale)
        {
            currentScale = Math.min(currentScale + SCALE_SPEED, targetScale);
            updateImage();
        }
        else if (currentScale > targetScale)
        {
            currentScale = Math.max(currentScale - SCALE_SPEED, targetScale);
            updateImage();
        }
    }
    
    public boolean wasClicked()
    {
        if (Greenfoot.mouseClicked(this))
        {
            SoundManager.getInstance().playClick();
            return true;
        }
        return false;
    }

    /**
     * Check whether a given point (usually mouse coordinates) lies within
     * this button's current drawn bounds. This is public so worlds can
     * perform click checks independent of Greenfoot's actor click events.
     */
    public boolean containsPoint(int px, int py)
    {
        int myX = getX();
        int myY = getY();

        int scaledWidth = (width * currentScale) / 100;
        int scaledHeight = (height * currentScale) / 100;

        return px >= myX - scaledWidth / 2 &&
               px <= myX + scaledWidth / 2 &&
               py >= myY - scaledHeight / 2 &&
               py <= myY + scaledHeight / 2;
    }
    
    /**
     * Update the button's visual appearance
     */
    private void updateImage()
    {
        Color bgColor = isHovered ? hoverColor : normalColor;
        
        int scaledWidth = (width * currentScale) / 100;
        int scaledHeight = (height * currentScale) / 100;
        
        int glowSize = isHovered ? 8 : 0;
        int totalWidth = scaledWidth + glowSize * 2;
        int totalHeight = scaledHeight + glowSize * 2;
        
        GreenfootImage img = new GreenfootImage(totalWidth, totalHeight);
        
        if (isHovered)
        {
            for (int i = glowSize; i > 0; i--)
            {
                int alpha = (int) (30 * ((double) i / glowSize));
                Color glowColor = new Color(
                    hoverColor.getRed(),
                    hoverColor.getGreen(),
                    hoverColor.getBlue(),
                    alpha
                );
                img.setColor(glowColor);
                img.fillRect(glowSize - i, glowSize - i, 
                           scaledWidth + i * 2, scaledHeight + i * 2);
            }
        }
        
        img.setColor(bgColor);
        img.fillRect(glowSize, glowSize, scaledWidth, scaledHeight);
        
        Color borderColor = isHovered ? 
            new Color(255, 255, 255, 150) : 
            new Color(0, 0, 0, 100);
        img.setColor(borderColor);
        img.drawRect(glowSize, glowSize, scaledWidth - 1, scaledHeight - 1);
        
        int scaledFontSize = (fontSize * currentScale) / 100;
        GreenfootImage textImg = new GreenfootImage(text, scaledFontSize, textColor, new Color(0, 0, 0, 0));
        int x = glowSize + (scaledWidth - textImg.getWidth()) / 2;
        int y = glowSize + (scaledHeight - textImg.getHeight()) / 2;
        img.drawImage(textImg, x, y);
        
        setImage(img);
    }
    
    public void setText(String newText)
    {
        this.text = newText;
        updateImage();
    }
    
    public String getText()
    {
        return text;
    }
    
    /**
     * Update the button's colors (useful for selection highlighting)
     */
    public void updateColors(Color newNormalColor, Color newHoverColor)
    {
        this.normalColor = newNormalColor;
        this.hoverColor = newHoverColor;
        updateImage();
    }
}
