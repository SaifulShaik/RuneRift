import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Allows user to instantly end a turn, used for development purposes
 * 
 * @author Owen Lee
 * @version
 */

public class EndTurnButton extends Actor
{
    public EndTurnButton()
    {
        createImage();
    }
    
    /**
     * Checks for button being clicked
     */
    public void act()
    {
        if (Greenfoot.mouseClicked(this))
        {
            // Call end turn on the world
            GridWorld world = (GridWorld)getWorld();
            world.endTurn();
        }
    }
    
    //creates the button
    private void createImage()
    {
        GreenfootImage img = new GreenfootImage(150, 50);
        
        // Button background
        img.setColor(new Color(200, 50, 50));
        img.fillRect(0, 0, 150, 50);
        
        // Button border
        img.setColor(Color.WHITE);
        img.drawRect(0, 0, 149, 49);
        
        // Button text
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", true, false, 18));
        img.drawString("END TURN", 25, 32);
        
        setImage(img);
    }
}