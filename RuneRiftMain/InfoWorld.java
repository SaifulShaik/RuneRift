import greenfoot.*;

/**
 * Info world showing piece guide and descriptions.
 * Extends MenuWorld for common menu functionality.
 * 
 * @author Saiful Shaik
 * @version 1.0
 */
public class InfoWorld extends MenuWorld
{
    private DescriptionBox descriptionBox;
    private Button backButton;
    
    public InfoWorld()
    {
        super(800, 600); // Custom width for piece display
        
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
            // Scale to fit width while maintaining aspect ratio, or tile/center
            img.scale(getWidth(), getHeight());
            bg.drawImage(img, 0, 0);
        }
        catch (Exception e)
        {
            // Background image not found, use solid color
        }
        
        setBackground(bg);
    }
    
    public void act()
    {
        // Click back button to return to LandingPage
        if (backButton.wasClicked())
        {
            goToLandingPage();
        }
    }
    
    private void setupPieceImages()
    {
        // Row 1 - Top pieces
        
        addObject(new PieceImage("ROYAL_RECRUITS", "BRoyalRecruits.png"), 270, 205);
        addObject(new PieceImage("MUSKETEER", "BMusketeer.png"), 410, 205);
        addObject(new PieceImage("KNIGHT", "BKnight.png"), 550, 205);
        
        
        
        // Row 2 - Bottom pieces 
        addObject(new PieceImage("DARK_PRINCE", "BDarkPrince.png"), 270, 405);
        addObject(new PieceImage("WITCH", "BWitch.png"), 410, 405);
        addObject(new PieceImage("ROYAL_GIANT", "BRoyalGiant.png"), 550, 405);
        
       
        
    }
    
    public void showDescription(String pieceName)
    {
        descriptionBox.showPiece(pieceName);
    }
    
    public void hideDescription()
    {
        descriptionBox.hide();
    }
}