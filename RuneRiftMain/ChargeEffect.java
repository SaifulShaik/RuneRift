import greenfoot.*;

class ChargeEffect extends Effect
{
    private int centerX, centerY;
    
    public ChargeEffect(int x, int y)
    {
        this.centerX = x;
        this.centerY = y;
        this.lifetime = 25; 
    }
    
    @Override
    protected void updateEffect()
    {
        double progress = (double)frameCount / lifetime;
        
        // Canvas large enough to cover the target and neighboring blocks
        int canvasSize = 200; 
        GreenfootImage img = new GreenfootImage(canvasSize, canvasSize);
        int mid = canvasSize / 2;
        int lineLength = 60; // How far the + lines go
        
        int alpha = (int)(255 * (1 - progress));
        
        // Draw the + pattern
        img.setColor(new Color(255, 140, 0, alpha)); // Orange
        
        // Vertical line of the "+"
        img.fillRect(mid - 4, mid - lineLength, 8, lineLength * 2);
        
        // Horizontal line of the "+"
        img.fillRect(mid - lineLength, mid - 4, lineLength * 2, 8);
        
        // Inner white shine for the "+"
        img.setColor(new Color(255, 255, 255, alpha));
        img.fillRect(mid - 1, mid - lineLength, 2, lineLength * 2);
        img.fillRect(mid - lineLength, mid - 1, lineLength * 2, 2);
        
        // Center impact circle
        img.fillOval(mid - 15, mid - 15, 30, 30);
        
        setImage(img);
        setLocation(centerX, centerY);
    }
}