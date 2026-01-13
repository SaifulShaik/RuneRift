import greenfoot.*;

public class GameTimer extends Actor
{
    private int timeLeft;
    private int frameCount = 0;
    private String player; // "WHITE" or "BLACK"
    private boolean isActive = false;
    
    public GameTimer(String player, int startingTime)
    {
        this.player = player;
        this.timeLeft = startingTime;
        updateDisplay();
    }
    
    public void act()
    {
        if (!isActive)
        {
            return; // Don't count down if not active
        }
        
        // Count frames (Greenfoot runs at ~60 fps)
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
     * Set whether this timer is active (counting down)
     */
    public void setActive(boolean active)
    {
        this.isActive = active;
        updateDisplay();
    }
    
    /**
     * Check if this timer is for a specific player
     */
    public boolean isPlayer(String playerName)
    {
        return this.player.equals(playerName);
    }
    
    private void updateDisplay()
    {
        GreenfootImage img = new GreenfootImage(200, 40);
        
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
    
    private void checkTimeOut()
    {
        Greenfoot.stop();
        
        // Show game over message
        GreenfootImage img = new GreenfootImage(250, 60);
        img.setColor(Color.RED);
        img.fillRect(0, 0, 250, 60);
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", true, false, 20));
        
        String winner = player.equals("WHITE") ? "BLACK" : "WHITE";
        img.drawString(winner + " WINS!", 50, 25);
        img.drawString("Time Out!", 70, 45);
        setImage(img);
    }
}