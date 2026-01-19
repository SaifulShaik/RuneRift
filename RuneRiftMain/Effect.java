import greenfoot.*;

public abstract class Effect extends Actor
{
    protected int lifetime = 30; // How many frames the effect lasts
    protected int frameCount = 0;
    
    public void act()
    {
        frameCount++;
        updateEffect();
        
        if (frameCount >= lifetime)
        {
            getWorld().removeObject(this);
        }
    }
    
    /**
     * Override this to create custom animation for each effect
     */
    protected abstract void updateEffect();
}