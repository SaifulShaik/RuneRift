import greenfoot.*;

// ===== KNIGHT SLASH EFFECT =====
public class KnightSlashEffect extends Effect
{
    private int centerX, centerY;
    
    public KnightSlashEffect(int x, int y)
    {
        this.centerX = x;
        this.centerY = y;
        lifetime = 25;
    }
    
    @Override
    protected void updateEffect()
    {
        double progress = (double)frameCount / lifetime;
        
        int size = 150;  // Smaller canvas
        GreenfootImage img = new GreenfootImage(size, size);
        
        // Sword slashes from LEFT to RIGHT across the three blocks
        double slashProgress = (progress * 2) - 1; // -1 to 1
        
        // Horizontal offset for the slash
        int xOffset = (int)(slashProgress * 50); // Smaller movement
        
        // Create smaller sword image (VERTICAL, pointing UP/forward)
        GreenfootImage sword = new GreenfootImage(20, 80);  // Much smaller
        
        // Draw blade (bright silver) - VERTICAL
        sword.setColor(new Color(240, 240, 240, 255));
        sword.fillRect(4, 10, 12, 70);
        
        // Blade shine
        sword.setColor(new Color(255, 255, 255, 255));
        sword.fillRect(6, 10, 3, 70);
        
        // Blade tip (pointed UP/forward)
        sword.setColor(new Color(255, 255, 255, 255));
        int[] xPoints = {6, 10, 14};
        int[] yPoints = {10, 0, 10};
        sword.fillPolygon(xPoints, yPoints, 3);
        
        // Golden slash trail
        sword.setColor(new Color(255, 215, 0, (int)(200 * (1 - progress * 0.3))));
        sword.fillRect(0, 10, 20, 70);
        
        // White energy trail
        sword.setColor(new Color(255, 255, 255, (int)(180 * (1 - progress * 0.5))));
        sword.fillRect(3, 15, 14, 60);
        
        // Tilt the sword slightly during the slash
        int tilt = (int)(slashProgress * 20);
        sword.rotate(tilt);
        
        // Draw sword at the slash position
        img.drawImage(sword, size/2 - sword.getWidth()/2 + xOffset, size/2 - sword.getHeight()/2 - 20);
        
        // Keep visible longer
        int transparency = frameCount < lifetime * 0.7 ? 255 : (int)(255 * (1 - progress) * 3);
        img.setTransparency(Math.max(0, transparency));
        
        setImage(img);
        setLocation(centerX, centerY);
    }
}