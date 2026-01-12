import greenfoot.*;

public class Block extends Actor
{
    private int xPos, yPos;
    private int worldX, worldY;

    public Block(int x, int y, int worldX, int worldY)
    {
        xPos = x;
        yPos = y;
        this.worldX = worldX;
        this.worldY = worldY;
        
        drawCell();
    }

    public void act()
    {
        // nothing here — block does not react to clicks
    }

    private void drawCell()
    {
        GreenfootImage image = new GreenfootImage(GridWorld.SIZE, GridWorld.SIZE);

        // Chessboard colour logic
        if ((xPos + yPos) % 2 == 0)
        {
            image.setColor(Color.BLACK);
        }
        else
        {
            image.setColor(Color.WHITE);
        }

        image.fill();

        // Optional: show coordinates
        image.setColor((xPos + yPos) % 2 == 0 ? Color.WHITE : Color.BLACK);
        image.drawString(xPos + ", " + yPos, 4, image.getHeight() - 6);

        setImage(image);
    }
    
    public int getBoardX() {
        return xPos;
    }
    
    public int getBoardY() {
        return yPos;
    }
    
    public int getX() {
        return worldX;
    }
    
    public int getY() {
        return worldY;
    }
}
