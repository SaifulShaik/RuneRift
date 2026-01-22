import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Used in the InfoWorld to establish the appearance of each piece
 * Checks for clicks to provide the information for the piece clicked
 * 
 * @author Owen Lee
 * @version
 */

class PieceImage extends Actor
{
    private String pieceName;
    private String imagePath;
    
    /**
     * Creates PieceImage that has a name and image
     * 
     * @param pieceName the name of the piece
     * @param imagePath the path of the image that is to be added onto pieceImage
     */
    public PieceImage(String pieceName, String imagePath)
    {
        this.pieceName = pieceName;
        this.imagePath = imagePath;
        loadImage();
    }
    
    /**
     * Preforms set of actions when it notices it has been clicked
     */
    public void act()
    {
        if (Greenfoot.mouseClicked(this))
        {
            SoundManager.getInstance().playClick();
            InfoWorld world = (InfoWorld)getWorld();
            world.showDescription(pieceName);
        }
    }
    
    //Create the display for the image
    private void loadImage()
    {
        GreenfootImage img = new GreenfootImage(imagePath);
        
        // Resize to fit in a nice display size
        img.scale(100, 100);
        
        // Create a frame around it
        GreenfootImage framed = new GreenfootImage(120, 140);
        framed.setColor(new Color(100, 100, 150));
        framed.fillRect(0, 0, 120, 140);
        framed.setColor(Color.WHITE);
        framed.drawRect(0, 0, 119, 139);
        
        // Draw the piece image
        framed.drawImage(img, 10, 10);
        
        // Draw piece name below
        framed.setColor(Color.WHITE);
        framed.setFont(new Font("Arial", true, false, 12));
        String displayName = pieceName.replace("_", " ");
        framed.drawString(displayName, 10, 130);
        
        setImage(framed);
    }
}