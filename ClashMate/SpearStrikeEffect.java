import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Spear effect appears when the pawn uses its abiltiy to take the piece in front of it
 * 
 * @author Owen
 */

public class SpearStrikeEffect extends Effect
{
    private boolean isWhite;
    private int startX, startY;
    private int targetX, targetY;
    
    /**
     * Create the spear effect
     * 
     * @param startX initial x position
     * @param startY initial y position
     * @param isWhite determines which way the spear should face
     */
    
    public SpearStrikeEffect(int startX, int startY, boolean isWhite)
    {
        this.startX = startX;
        this.startY = startY;
        this.isWhite = isWhite;
        
        // Calculate target position (one space forward)
        int direction = isWhite ? -1 : 1;
        this.targetX = startX;
        this.targetY = startY + (direction * GridWorld.SIZE);
        
        lifetime = 20; // Quick strike animation
        createSpearImage();
        
        SoundManager.getInstance().play(SoundManager.ROYAL_RECRUIT);
    }
    
    private void createSpearImage()
    {
        GreenfootImage img = new GreenfootImage(10, 40);
        
        // Spear shaft
        img.setColor(new Color(139, 69, 19));
        img.fillRect(3, 5, 4, 30);
        
        // Spear tip 
        img.setColor(new Color(192, 192, 192));
        int[] xPoints = {5, 0, 10};
        int[] yPoints = {5, 0, 0};
        img.fillPolygon(xPoints, yPoints, 3);
        
        // Rotate if moving down
        if (!isWhite)
        {
            img.rotate(180);
        }
        
        setImage(img);
    }
    
    @Override
    protected void updateEffect()
    {
        // Animate spear moving forward
        double progress = (double)frameCount / lifetime;
        int currentX = (int)(startX + (targetX - startX) * progress);
        int currentY = (int)(startY + (targetY - startY) * progress);
        
        setLocation(currentX, currentY);
        
        // Fade out near the end
        if (frameCount > lifetime * 0.7)
        {
            GreenfootImage img = getImage();
            int transparency = (int)(255 * (1 - progress));
            img.setTransparency(Math.max(0, transparency));
        }
    }
}
