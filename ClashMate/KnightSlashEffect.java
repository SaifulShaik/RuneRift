import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The slash effect that appears when the knight uses its ability
 * It rotates to indicate the tiles that are removed because of the sword
 * 
 * @author Owen
 */

public class KnightSlashEffect extends Effect
{
    private int centerX, centerY;
    private boolean isWhite;
    
    /**
     * Creates the slash effect based on the x and y coordinates as well as whether or not it is a white piece
     * 
     * @param x the x coordinate of the effect
     * @param y the y coordinate of the effect
     * @param isWhite used to determine the direction of the sword
     */
    
    public KnightSlashEffect(int x, int y, boolean isWhite)
    {
        this.centerX = x;
        this.centerY = y;
        this.isWhite = isWhite;
        lifetime = 25;
        
        // Play knight sound once when effect is created
        SoundManager.getInstance().play(SoundManager.KNIGHT);
    }
    
    @Override
    protected void updateEffect()
    {
        double progress = (double)frameCount / lifetime;
        
        int size = 150;
        GreenfootImage img = new GreenfootImage(size, size);
        
        // Sword slashes from LEFT to RIGHT
        double slashProgress = (progress * 2) - 1; // -1 to 1
        int xOffset = (int)(slashProgress * 50);
        
        // Create sword image
        GreenfootImage sword = new GreenfootImage(24, 100);
        
        //CREATE THE HANDLE
        // Handle grip (brown leather)
        sword.setColor(new Color(101, 67, 33));
        sword.fillRect(8, 75, 8, 20);
        
        // Handle detail lines
        sword.setColor(new Color(139, 90, 43));
        sword.drawLine(8, 80, 16, 80);
        sword.drawLine(8, 85, 16, 85);
        sword.drawLine(8, 90, 16, 90);
        
        sword.setColor(new Color(255, 215, 0)); // Gold
        sword.fillOval(6, 93, 12, 7);
        
        sword.setColor(new Color(255, 215, 0)); // Gold
        sword.fillRect(0, 70, 24, 6);
        
        sword.setColor(new Color(218, 165, 32));
        sword.fillRect(2, 71, 20, 2);
        
        // CREATE THE BLADE
        // Main blade (silver)
        sword.setColor(new Color(230, 230, 240));
        sword.fillRect(7, 10, 10, 62);
        
        // Blade edges (darker for depth)
        sword.setColor(new Color(180, 180, 190));
        sword.fillRect(7, 10, 2, 62);
        sword.fillRect(15, 10, 2, 62);
        
        // Central groove
        sword.setColor(new Color(200, 200, 210));
        sword.fillRect(10, 15, 4, 57);
        
        // Blade shine (bright highlight)
        sword.setColor(new Color(255, 255, 255, 220));
        sword.fillRect(9, 12, 2, 58);
        
        //Blade tip
        sword.setColor(new Color(240, 240, 250));
        int[] xPoints = {9, 12, 15};
        int[] yPoints = {10, 3, 10};
        sword.fillPolygon(xPoints, yPoints, 3);
        
        // Tip shine
        sword.setColor(new Color(255, 255, 255));
        int[] xPoints2 = {10, 12, 13};
        int[] yPoints2 = {9, 4, 9};
        sword.fillPolygon(xPoints2, yPoints2, 3);
        
        // Golden slash trail
        sword.setColor(new Color(255, 215, 0, (int)(180 * (1 - progress * 0.4))));
        sword.fillRect(4, 10, 16, 65);
        
        // White energy trail
        sword.setColor(new Color(255, 255, 255, (int)(150 * (1 - progress * 0.6))));
        sword.fillRect(7, 15, 10, 55);
        
        // Tilt during slash FIRST
        int tilt = (int)(slashProgress * 20);
        sword.rotate(tilt);
        
        // Flip for black piece
        if (!isWhite)
        {
            sword.rotate(180);
        }
        
        // Adjust Y position based on color (black needs to be lower)
        int yOffset = isWhite ? -20 : 20;
        
        // Draw sword
        img.drawImage(sword, size/2 - sword.getWidth()/2 + xOffset, size/2 - sword.getHeight()/2 + yOffset);
        
        // Fade out
        int transparency = frameCount < lifetime * 0.7 ? 255 : (int)(255 * (1 - progress) * 3);
        img.setTransparency(Math.max(0, transparency));
        
        setImage(img);
        setLocation(centerX, centerY);
    }
}