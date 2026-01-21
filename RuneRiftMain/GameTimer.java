import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Timer that counts down to establish a time limit for the game
 * Both white and black have their own timer
 * When a player plays a move, their turn ends and the next players automatically starts
 * 
 * @author Owen Lee
 */

public class GameTimer extends Actor
{
    private int timeLeft;
    private int frameCount = 0;
    private String player; // white or black
    private boolean isActive = false;
    
    /**
     * Creates the timer
     * 
     * @param player white or black player
     * @param startingTime amount of time each player has in the game
     */
    public GameTimer(String player, int startingTime)
    {
        this.player = player;
        this.timeLeft = startingTime;
        updateDisplay();
    }
    
    /**
     * Counts down the timer based on frames that passed
     */
    public void act()
    {
        if (!isActive)
        {
            return; // Don't count down if clock not active, this means its the other players turn
        }
        
        frameCount++;
        
        // Every 60 frames = 1 second
        if (frameCount >= 60)
        {
            frameCount = 0;
            
            if (timeLeft > 0)
            {
                timeLeft--;
                updateDisplay();
                
                // Check for time out
                if (timeLeft <= 0)
                {
                    checkTimeOut();
                }
            }
        }
    }
    
    /**
     * Set whether this timer is active (start count down)
     * 
     * @param active true if the timer should count down, false when it should be paused
     */
    public void setActive(boolean active)
    {
        this.isActive = active;
        updateDisplay();
    }
    
    /**
     * Check if this timer is for a specific player
     * 
     * @param playerName the player name checked (white or black)
     * 
     * @return boolean true if the timer belongs to the specified player, false otherwise
     */
    public boolean isPlayer(String playerName)
    {
        return this.player.equals(playerName);
    }
    
    private void updateDisplay()
    {
        GreenfootImage img = new GreenfootImage(140, 40);
        
        // Background
        img.setColor(new Color(40, 40, 40));
        img.fillRect(0, 0, 200, 40);
        
        // Timer text - GREEN if active, WHITE if not
        img.setColor(isActive ? Color.GREEN : Color.WHITE);
        img.setFont(new Font("Arial", true, false, 20));
        img.drawString(player + ": " + formatTime(timeLeft), 10, 28);
        
        setImage(img);
    }
    
    private String formatTime(int seconds)
    {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }
    
    /**
     * Get the time remaining on this timer
     * @return time left in seconds
     */
    public int getTimeLeft()
    {
        return timeLeft;
    }
    
    /**
     * Get the player this timer belongs to
     * @return "WHITE" or "BLACK"
     */
    public String getPlayer()
    {
        return player;
    }
    
    private void checkTimeOut()
    {
        SoundManager.getInstance().playLose();
        
        // Notify GridWorld to handle game end
        GridWorld world = (GridWorld) getWorld();
        if (world != null) {
            world.endGameByTimeout(player);
        }
    }
}