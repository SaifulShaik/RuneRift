import greenfoot.*;
import java.util.LinkedList;
import java.util.Queue;

public class TurnManager
{
    private Queue<String> turnQueue;
    private String currentPlayer;
    private ElixirBar whiteElixirBar;
    private ElixirBar blackElixirBar;
    private int elixirMultiplier;  // How much elixir to gain per turn (1, 2, or 3)
    
    public TurnManager(ElixirBar whiteBar, ElixirBar blackBar)
    {
        this(whiteBar, blackBar, 1); // Default to 1x multiplier
    }
    
    public TurnManager(ElixirBar whiteBar, ElixirBar blackBar, int multiplier)
    {
        turnQueue = new LinkedList<String>();
        // Add players to queue - White goes first
        turnQueue.add("WHITE");
        turnQueue.add("BLACK");
        currentPlayer = turnQueue.peek();
        
        // Store references to both elixir bars
        this.whiteElixirBar = whiteBar;
        this.blackElixirBar = blackBar;
        
        // Set elixir multiplier (clamped to 1-3)
        this.elixirMultiplier = Math.max(1, Math.min(3, multiplier));
    }
    
    /**
     * Get whose turn it is
     */
    public String getCurrentPlayer()
    {
        return currentPlayer;
    }
    
    /**
     * Check if it's a specific player's turn
     */
    public boolean isPlayerTurn(String player)
    {
        return currentPlayer.equals(player);
    }
    
    /**
     * Get the current elixir multiplier
     */
    public int getElixirMultiplier()
    {
        return elixirMultiplier;
    }
    
    /**
     * End current turn and move to next player.
     * Awards elixir based on the multiplier setting.
     */
    public void nextTurn()
    {
        String player = turnQueue.poll(); // Remove from front
        turnQueue.add(player); // Add to back
        currentPlayer = turnQueue.peek(); // Get new current player
        
        // Award elixir to the new current player based on multiplier
        if (currentPlayer.equals("WHITE") && whiteElixirBar != null)
        {
            if (!whiteElixirBar.isFull())
            {
                whiteElixirBar.addElixir(elixirMultiplier);
            }
        }
        else if (currentPlayer.equals("BLACK") && blackElixirBar != null)
        {
            if (!blackElixirBar.isFull())
            {
                blackElixirBar.addElixir(elixirMultiplier);
            }
        }
    }
}