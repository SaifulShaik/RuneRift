import greenfoot.*;

/**
 * A small credits button for the bottom right corner.
 * Shows credits information when clicked.
 * 
 * @author Saiful Shaik
 * @version
 */
public class CreditsButton extends Actor
{
    private int width;
    private int height;
    private boolean isHovered;
    private int hoverAlpha;
    
    public CreditsButton()
    {
        this.width = 80;
        this.height = 30;
        this.isHovered = false;
        this.hoverAlpha = 0;
        updateImage();
    }
    
    public void act()
    {
        checkHover();
        animateHover();
        updateImage();
    }
    
    /**
     * Check if mouse is hovering over button
     */
    private void checkHover()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null)
        {
            int mouseX = mouse.getX();
            int mouseY = mouse.getY();
            int myX = getX();
            int myY = getY();
            
            isHovered = mouseX >= myX - width/2 && mouseX <= myX + width/2 &&
                       mouseY >= myY - height/2 && mouseY <= myY + height/2;
        }
    }
    
    /**
     * Animate hover effect
     */
    private void animateHover()
    {
        if (isHovered)
        {
            hoverAlpha = Math.min(hoverAlpha + 15, 100);
        }
        else
        {
            hoverAlpha = Math.max(hoverAlpha - 10, 0);
        }
    }
    
    /**
     * Update visual appearance
     */
    private void updateImage()
    {
        GreenfootImage img = new GreenfootImage(width, height);
        
        // Background
        int bgAlpha = 100 + hoverAlpha;
        img.setColor(new Color(50, 50, 70, bgAlpha));
        img.fillRect(0, 0, width, height);
        
        // Border
        int borderAlpha = 80 + hoverAlpha;
        img.setColor(new Color(150, 150, 170, borderAlpha));
        img.drawRect(0, 0, width - 1, height - 1);
        
        // Text
        int textAlpha = 150 + hoverAlpha;
        GreenfootImage textImg = new GreenfootImage("Credits", 14, 
            new Color(200, 200, 220, textAlpha), new Color(0, 0, 0, 0));
        int textX = (width - textImg.getWidth()) / 2;
        int textY = (height - textImg.getHeight()) / 2;
        img.drawImage(textImg, textX, textY);
        
        setImage(img);
    }
    
    /**
     * Check if button was clicked
     */
    public boolean wasClicked()
    {
        boolean clicked = Greenfoot.mouseClicked(this);
        if (clicked)
        {
            SoundManager.getInstance().playClick();
        }
        return clicked;
    }
}
