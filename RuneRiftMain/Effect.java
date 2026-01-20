import greenfoot.*;

/**
 * Abstract effect superclass that provides functionality for all effects
 * 
 * @author Owen
 */
public abstract class Effect extends Actor
{
    protected int lifetime = 30; // How many frames the effect lasts
    protected int frameCount = 0;

    /**
     * updates the framecount for the effect
     * checks whether or not the effect should be removed 
     * based on frameCount compared with lifetime
     */
    public void act()
    {
        frameCount++;
        updateEffect();
        
        if (frameCount >= lifetime)
        {
            getWorld().removeObject(this);
        }
    }
    
    //necessary method for all effects, it is to be overided
    protected abstract void updateEffect();
}