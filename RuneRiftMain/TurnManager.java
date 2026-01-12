import greenfoot.*;
import java.util.LinkedList;
import java.util.Queue;

public class TurnManager
{
    private Queue<String> turnQueue;
    private String currentPlayer;
    
    public TurnManager()
    {
        turnQueue = new LinkedList<String>();
        // Add players to queue - White goes first
        turnQueue.add("WHITE");
        turnQueue.add("BLACK");
        currentPlayer = turnQueue.peek();
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
    }
}