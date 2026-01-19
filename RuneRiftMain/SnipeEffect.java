import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class SnipeEffect extends Effect
{
    private int startX, startY;
    private int endX, endY;
    
    public SnipeEffect(int startX, int startY, int endX, int endY)
    {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        lifetime = 10; // Very quick
    }
    
    @Override
    protected void updateEffect()
    {
        double progress = (double)frameCount / lifetime;
        
        // Calculate bullet position
        int currentX = (int)(startX + (endX - startX) * progress);
        int currentY = (int)(startY + (endY - startY) * progress);
        
        GreenfootImage img = new GreenfootImage(20, 20);
        
        // Draw bullet trail
        img.setColor(new Color(255, 255, 0, 200)); // Yellow
        img.fillOval(0, 0, 15, 15);
        
        // Bright center
        img.setColor(new Color(255, 255, 255));
        img.fillOval(5, 5, 5, 5);
        
        setImage(img);
        setLocation(currentX, currentY);
    }
}

