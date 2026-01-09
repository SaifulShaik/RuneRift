import greenfoot.*;

public class Block extends Actor
{
    private int xPos, yPos;
    private boolean isBlack;

    public Block(int x, int y)
    {
        xPos = x;
        yPos = y;

        // Chessboard logic
        isBlack = (xPos + yPos) % 2 == 0;

        drawCell();
    }

    public void act()
    {
        if (Greenfoot.mouseClicked(this))
        {
            isBlack = !isBlack;   // toggle colour
            drawCell();
        }
    }

    private void drawCell()
    {
        GreenfootImage image = new GreenfootImage(GridWorld.SIZE, GridWorld.SIZE);

        if (isBlack)
        {
            image.setColor(Color.BLACK);
        }
        else
        {
            image.setColor(Color.WHITE);
        }

        image.fill();

        // Optional: coordinates text
        image.setColor(isBlack ? Color.WHITE : Color.BLACK);
        image.drawString(xPos + ", " + yPos, 4, image.getHeight() - 6);

        setImage(image);
    }
}
