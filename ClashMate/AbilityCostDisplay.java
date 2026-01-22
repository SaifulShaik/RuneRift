import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Shows the ability cost of a piece after it has been selected
 * indicates through a number on a elixir symbol next to the ability button
 * 
 * @author Owen Lee
 * @version
 */
public class AbilityCostDisplay extends Actor
{
    private int cost = 0;
    private boolean isVisible = false;
    
    /**
     * Constructor for ability cost display
     */
    public AbilityCostDisplay()
    {
        hide();
    }
    
    /**
     * Show the ability cost for a piece
     * 
     * @param abilityCost   the cost of the ability that is taken to be displayed
     */
    public void showCost(int abilityCost)
    {
        this.cost = abilityCost;
        this.isVisible = true;
        updateDisplay();
    }
    
    /**
     * Hide the display
     */
    public void hide()
    {
        this.isVisible = false;
        setImage(new GreenfootImage(1, 1)); // Empty image
    }
    
    /**
     * updates display with the proper visibility and elixir cost
     */
    private void updateDisplay()
    {
        if (!isVisible)
        {
            hide();
            return;
        }
        
        // Load and resize the elixir icon
        GreenfootImage elixirIcon = new GreenfootImage("Elixir.png"); // Change filename if different
        elixirIcon.scale(90, 60); // Make it bigger to fit number inside
        
        // Draw the cost number on top of the icon
        elixirIcon.setColor(Color.WHITE);
        elixirIcon.setFont(new Font("Arial", true, false, 24));
        
        String costStr = "" + cost;
        elixirIcon.drawString(costStr, 37, 40);
        
        setImage(elixirIcon);
    }
}