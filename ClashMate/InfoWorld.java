import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * World that provides information on all the pieces as well as their abilities
 * 
 * @author Owen Lee & Saiful Shaik
 * @version
 */
public class InfoWorld extends MenuWorld
{
    private DescriptionBox descriptionBox;
    private Button backButton;
    
    /**
     * Constructor for objects of class InfoWorld.
     */
    public InfoWorld()
    {
        super(800, 600);
        
        // Title
        getBackground().setColor(Color.WHITE);
        getBackground().setFont(new Font("Arial", true, false, 30));
        getBackground().drawString("Piece Guide", 330, 85);
        
        // Back button using inherited helper
        backButton = createBackButton();
        addObject(backButton, 70, 30);
        
        setupPieceImages();
        
        // Create description box (initially hidden)
        descriptionBox = new DescriptionBox();
        addObject(descriptionBox, getWidth() / 2 + 10, getHeight() / 2 + 30);
        descriptionBox.hide();
    }
    
    /**
     * Override to handle wider background for InfoWorld
     */
    @Override
    protected void setupBackground()
    {
        GreenfootImage bg = new GreenfootImage(getWidth(), getHeight());
        bg.setColor(new Color(20, 30, 60));
        bg.fill();
        
        try
        {
            GreenfootImage img = new GreenfootImage("Background.png");
            img.scale(getWidth(), getHeight());
            bg.drawImage(img, 0, 0);
        }
        catch (Exception e) {}
        
        setBackground(bg);
    }
    
    /**
     * Constantly checks for back button to see if user wants to return to landing page
     */
    public void act()
    {
        // Click back button to return to LandingPage
        if (backButton.wasClicked())
        {
            goToLandingPage();
        }
    }
    
    /**
     * Creates the description boxes in InfoWorld that can be clicked to display information
     */
    private void setupPieceImages()
    {
        // Row 1 - Top pieces
        addObject(new PieceImage("ROYAL_RECRUITS", "BRoyalRecruits.png"), 270, 205);
        addObject(new PieceImage("MUSKETEER", "BMusketeer.png"), 410, 205);
        addObject(new PieceImage("KNIGHT", "BKnight.png"), 550, 205);
        
        // Row 2 - Bottom pieces 
        addObject(new PieceImage("DARK_PRINCE", "BDarkPrince.png"), 200, 405);
        addObject(new PieceImage("WITCH", "BWitch.png"), 340, 405);
        addObject(new PieceImage("ROYAL_GIANT", "BRoyalGiant.png"), 480, 405);
        addObject(new PieceImage("SKELETON", "BSkeleton.png"), 620, 405);
    }
    
    /**
     * Description box pops out and shows
     * 
     * @param pieceName tells the method which box was clicked and which display to pop out
     */
    public void showDescription(String pieceName)
    {
        descriptionBox.showPiece(pieceName);
    }
    
    /**
     * Hides the description box
     */
    public void hideDescription()
    {
        descriptionBox.hide();
    }
}