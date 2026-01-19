import greenfoot.*;

public class SummonEffect extends Effect
{
    private int centerX, centerY;
    
    public SummonEffect(int x, int y)
    {
        this.centerX = x;
        this.centerY = y;
        lifetime = 30;
    }
    
    @Override
    protected void updateEffect()
    {
        double progress = (double)frameCount / lifetime;
        int size = 60;
        
        GreenfootImage img = new GreenfootImage(size, size);
        
        // Purple magic circle
        img.setColor(new Color(138, 43, 226, 200));
        
        // Rotating circle effect
        int rotation = (int)(360 * progress);
        for (int i = 0; i < 6; i++)
        {
            double angle = Math.toRadians(rotation + i * 60);
            int x = (int)(size/2 + Math.cos(angle) * 20);
            int y = (int)(size/2 + Math.sin(angle) * 20);
            img.fillOval(x - 5, y - 5, 10, 10);
        }
        
        // Fade in then out
        int transparency = frameCount < lifetime/2 ? 
            (int)(255 * progress * 2) : 
            (int)(255 * (1 - progress) * 2);
        img.setTransparency(Math.max(0, Math.min(255, transparency)));
        
        setImage(img);
        setLocation(centerX, centerY);
    }
}