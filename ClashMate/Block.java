import greenfoot.*;

/**
 * Block code from class, with new features
 * 
 * @author Jordan Cohen (edited by Joe Zhuo)
 * @version
 */
public class Block extends Actor
{
    private int xPos, yPos;
    private int worldX, worldY;

    // piece on this block
    private Piece piece;
    
    GreenfootImage originalImage;

    public Block(int x, int y, int worldX, int worldY)
    {
        xPos = x;
        yPos = y;
        this.worldX = worldX;
        this.worldY = worldY;
        
        drawCell();
        
        originalImage = getImage();
    }

    private void drawCell()
    {
        GreenfootImage image = new GreenfootImage(GridWorld.SIZE, GridWorld.SIZE);

        // Chessboard colour logic
        image.setColor((xPos + yPos) % 2 == 0 ? Color.WHITE : Color.BLACK);
        image.fill();

        // Optional: show coordinates
        image.setColor((xPos + yPos) % 2 == 0 ? Color.WHITE : Color.BLACK);
        image.drawString(xPos + ", " + yPos, 4, image.getHeight() - 6);

        setImage(image);
    }
    
    public void highlight(Color color) {
        GreenfootImage highlightImg = new GreenfootImage(originalImage);
        highlightImg.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100)); // semi-transparent
        highlightImg.fillRect(0, 0, getImage().getWidth(), getImage().getHeight());
        setImage(highlightImg);
    }
    
    /**
     * get the piece currently on this block
     * @return the piece on this block, or null if none
     */
    public Piece currentPiece() {
        return piece;
    }
    
    /** 
     * set the piece on this block
     * @param p the piece to place on this block
     */
    public void setPiece(Piece p) {
        piece = p;
    }
    
    /** 
     * remove the piece from this block
     * @param removeFromWorld true to also remove the piece from the world, false to just clear the block's reference
     */
    public void removePiece(boolean removeFromWorld) {
        if (piece == null) return;
        
        Piece p = piece;
        piece = null;
        
        if (removeFromWorld) {
            // remove from GridWorld's piece list before removing from world
            GridWorld gw = (GridWorld) getWorld();

            if (gw != null) gw.removePieceFromList(p);
            gw.removeObject(p);
        }
        
        // clear block reference in piece
        p.clearBlock();
    }
    
    public void clearHighlight() {
        setImage(new GreenfootImage(originalImage));
    }

    public int getBoardX() {
        return xPos;
    }
    
    public int getBoardY() {
        return yPos;
    }
}
