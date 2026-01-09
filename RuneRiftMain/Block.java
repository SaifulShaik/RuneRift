import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Block here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Block extends Actor
{
    private GreenfootImage image;
    private int xx, yy;

    public Block (int x, int y){
        xx = x;
        yy = y;
        drawCell (xx, yy);
    }

    /**
     * Act - do whatever the Block wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (Greenfoot.mouseClicked(this)){
            drawCell (xx, yy);
        }
    }

    private void drawCell (int x, int y){
        image = new GreenfootImage (GridWorld.SIZE, GridWorld.SIZE);
        int red = Greenfoot.getRandomNumber(255);
        int green = Greenfoot.getRandomNumber(255);
        int blue = Greenfoot.getRandomNumber(255);
        image.setColor (new Color (red, green, blue));
        image.fill();
        setImage(image);
        image.setColor (new Color (255-red, 255-green, 255-blue));
        image.drawString (x + ", " + y, 4, getImage().getHeight()-6);
        
        setImage(image);

    }
}
