import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Elixir bar used in the game to track how much elixir each player has
 * Indicates elixir through a bar filling up
 * 
 * @author Owen Lee
 * @version
 */

public class ElixirBar extends Actor
{
    private int maxElixir = 10;
    private int currentElixir = 0;
    private int barWidth = 200;
    private int barHeight = 30;
    
    public ElixirBar()
    {
        updateImage();
    }
    
    /**
     * Set the current elixir amount (0-10)
     * 
     * @param amount the amount of elixir the bar will have
     */
    public void setElixir(int amount)
    {
        currentElixir = Math.max(0, Math.min(amount, maxElixir));
        updateImage();
    }
    
    /**
     * Add elixir to the bar
     * 
     * @param amount the amount of elixir added to the bar, total must be < 10
     */
    public void addElixir(int amount)
    {
        setElixir(currentElixir + amount);
    }
    
    /**
     * Remove elixir from the bar
     * 
     * @param amount the amount of elixir removed from the bar
     */
    public void removeElixir(int amount)
    {
        setElixir(currentElixir - amount);
    }
    
    /**
     * Get the current elixir amount
     * 
     * @return int amount of elixir
     */
    public int getElixir()
    {
        return currentElixir;
    }
    
    /**
     * Check if bar is full
     * 
     * @return boolean True if the bar is at max elixir
     */
    public boolean isFull()
    {
        return currentElixir >= maxElixir;
    }
    
    /**
     * Check if bar is empty
     * 
     * @return boolean True if the bar has 0 elixir
     */
    public boolean isEmpty()
    {
        return currentElixir <= 0;
    }
    
    //create the bar and fill it up based on elixir amount
    private void updateImage()
    {
        // Add space for the icon on the left
        int iconSize = 50;
        int iconPadding = 1;
        int totalWidth = iconSize + iconPadding + barWidth + 4;
        
        GreenfootImage img = new GreenfootImage(totalWidth, barHeight + 4);
        
        // Draw elixir icon on the left
        GreenfootImage elixirIcon = new GreenfootImage("Elixir.png");
        elixirIcon.scale(iconSize + 30, iconSize);
        img.drawImage(elixirIcon, 0, (barHeight + 4 - iconSize) / 2); // Vertically center icon
        
        // Offset for the bar (after the icon)
        int barStartX = iconSize + iconPadding;
        
        // Draw dark background for bar
        img.setColor(new Color(50, 50, 50));
        img.fillRect(barStartX, 0, barWidth + 4, barHeight + 4);
        
        // Draw purple fill
        int fillWidth = (barWidth * currentElixir) / maxElixir;
        img.setColor(new Color(138, 43, 226)); // Purple
        img.fillRect(barStartX + 2, 2, fillWidth, barHeight);
        
        // Draw tick marks (vertical lines)
        img.setColor(Color.WHITE);
        int tickSpacing = barWidth / maxElixir;
        for (int i = 0; i <= maxElixir; i++)
        {
            int x = barStartX + 2 + (i * tickSpacing);
            img.drawLine(x, 2, x, barHeight + 2);
        }
        
        // Draw white border
        img.drawRect(barStartX + 1, 1, barWidth + 2, barHeight + 2);
        
        setImage(img);
    }
}