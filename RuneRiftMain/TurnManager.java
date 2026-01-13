import greenfoot.*;
import java.util.LinkedList;
import java.util.Queue;

public class TurnManager
{
    private Queue<String> turnQueue;
    private String currentPlayer;
    private ElixirBar whiteElixirBar;
    private ElixirBar blackElixirBar;
    
    public TurnManager(ElixirBar whiteBar, ElixirBar blackBar)
    {
        turnQueue = new LinkedList<String>();
        // Add players to queue - White goes first
        turnQueue.add("WHITE");
        turnQueue.add("BLACK");
        currentPlayer = turnQueue.peek();
        
        // Store references to both elixir bars
        this.whiteElixirBar = whiteBar;
        this.blackElixirBar = blackBar;
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
     * End current turn and move to next player
     */
    public void nextTurn()
    {
        String player = turnQueue.poll(); // Remove from front
        turnQueue.add(player); // Add to back
        currentPlayer = turnQueue.peek(); // Get new current player
        
        
        if (currentPlayer.equals("WHITE") && whiteElixirBar != null)
        {
            if (!whiteElixirBar.isFull())
            {
                whiteElixirBar.addElixir(1);
            }
        }
        else if (currentPlayer.equals("BLACK") && blackElixirBar != null)
        {
            if (!blackElixirBar.isFull())
            {
                blackElixirBar.addElixir(1);
            }
        }
    }
}