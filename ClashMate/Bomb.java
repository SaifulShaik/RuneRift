import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Royal giant bomb class
 * 
 * @author Joe Zhuo
 * @version (a version number or a date)
 */
public class Bomb extends Actor
{
    // instance variables
    private Block location;
    private int turnCount;
    private boolean isWhite;
    private Label turnsLabel;
    
    /**
     * constructor for the Bomb class
     * @param location the Block where the bomb is placed
     * @param isWhite true if the bomb belongs to the white player, false if black
     */
    public Bomb(Block location, boolean isWhite) {
        this.location = location;
        this.isWhite = isWhite;
        this.turnCount = 4;
        
        // add label to show turns remaining
        turnsLabel = new Label(turnCount, 36);
    }
    
    /**
     * addedToWorld method to add the turns label to the world
     * @param world the World object the bomb is added to
     */
    protected void addedToWorld(World world)
    {
        world.addObject(turnsLabel, getX(), getY());
        SoundManager.getInstance().play(SoundManager.BOMB_PLANTED);
    }
    
    /**
     * act method for the Bomb class
     * explodes when turn count reaches 0
     */
    public void act()
    {
        turnsLabel.setLocation(getX(), getY());

        if (turnCount == 0) explode();
        
    }
    
    /**
     * progress the explosion countdown by 1 turn
     */
    public void progressExplosion() {
        turnCount--;
        turnsLabel.setValue(turnCount);
    }
    
    /**
     * explode method to remove pieces in adjacent blocks
     */
    private void explode() {
        GridWorld gw = (GridWorld) getWorld();

        int cx = location.getBoardX();
        int cy = location.getBoardY();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Block b = gw.getBlock(cx + dx, cy + dy);
                if (b != null && b.currentPiece() != null) {
                    if (b.currentPiece().checkIsWhite() != isWhite) {
                        b.removePiece(true);
                    }
                }
            }
        }
        
        SoundManager.getInstance().play(SoundManager.BOMB);
        
        // remove label and this object from the world
        gw.removeObject(turnsLabel);
        gw.removeObject(this);
    }
}
