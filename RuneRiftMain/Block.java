import greenfoot.*;

public class Block extends Actor
{
    private int xPos, yPos;
    private int worldX, worldY;
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

    public void act()
    {
        // nothing here — block does not react to clicks
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
    
    public Piece currentPiece() {
        return piece;
    }
    
    public void setPiece(Piece p) {
        piece = p;
    }
    
    public void removePiece(boolean removeFromWorld) {
        if (piece == null) return;
        
        Piece p = piece;
        piece = null;
        
        if (removeFromWorld) {
            getWorld().removeObject(p);
        }
        
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
