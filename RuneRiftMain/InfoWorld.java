import greenfoot.*;

public class InfoWorld extends World
{
    private DescriptionBox descriptionBox;
    private Button backButton;
    
    public InfoWorld()
    {
        super(800, 600, 1);
        
        setBackground("Background.png");
        
        // Title
        getBackground().setColor(Color.WHITE);
        getBackground().setFont(new Font("Arial", true, false, 30));
        getBackground().drawString("Piece Guide", 330, 85);
        
        // Back button
        backButton = new Button("← Back", 100, 40,
            new Color(70, 70, 90), new Color(100, 100, 130), Color.WHITE, 18);
        addObject(backButton, 70, 30);
        
        setupPieceImages();
        
        // Create description box (initially hidden)
        descriptionBox = new DescriptionBox();
        addObject(descriptionBox, 500, 300);
        descriptionBox.hide();
    }
    
    public void act()
    {
        // Click back button to return to LandingPage
        if (Greenfoot.mouseClicked(backButton))
        {
            Greenfoot.setWorld(new LandingPage());
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