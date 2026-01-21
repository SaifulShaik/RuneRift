import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Description that shows up after a piece is clicked on the info world
 * Provides information on how a piece moves as well as its ability
 * 
 * @author Owen Lee
 * @version
 */
class DescriptionBox extends Actor
{
    private boolean isVisible = false;
    private String currentPiece = "";
    
    /**
     * Constructor for description box
     */
    public DescriptionBox()
    {
        createEmptyImage();
    }
    
    /**
     * The description box pops up for the specified character 
     * 
     * @param pieceName the name of the piece that should have its description visible
     */
    public void showPiece(String pieceName)
    {
        isVisible = true;
        currentPiece = pieceName;
        createDescriptionImage(pieceName);
    }
    
    /**
     * Hides description box
     */
    public void hide()
    {
        isVisible = false;
        currentPiece = "";
        createEmptyImage();
    }
    
    /**
     * Tracks user clicks and mouse movement to see whether or not the user clicked 
     * the X button to close the display
     */
    public void act()
    {
        if (isVisible && Greenfoot.mouseClicked(this))
        {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null)
            {
                // Check if clicked on X button area
                // Image is 400x400, so offset is 200 from center
                int relativeX = mouse.getX() - (getX() - 200);
                int relativeY = mouse.getY() - (getY() - 200);
                
                if (relativeX >= 350 && relativeX <= 380 && relativeY >= 10 && relativeY <= 40)
                {
                    SoundManager.getInstance().playClick();
                    hide();
                }
            }
        }
    }
    
    private void createEmptyImage()
    {
        setImage(new GreenfootImage(1, 1));
    }
    
    // creates the box the description is placed in
    private void createDescriptionImage(String pieceName)
    {
        GreenfootImage img = new GreenfootImage(400, 400);
        
        // Background
        img.setColor(new Color(240, 240, 240));
        img.fillRect(0, 0, 400, 400);
        
        // Border
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, 399, 399);
        img.drawRect(1, 1, 397, 397);
        
        // Title bar
        img.setColor(new Color(60, 60, 120));
        img.fillRect(0, 0, 400, 60);
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", true, false, 24));
        img.drawString(pieceName.replace("_", " "), 20, 40);
        
        // Close button (X)
        img.setColor(Color.RED);
        img.fillRect(350, 10, 30, 30);
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", true, false, 22));
        img.drawString("X", 357, 33);
        
        // Description text
        img.setColor(Color.BLACK);
        img.setFont(new Font("Arial", false, false, 14));
        
        String description = getDescription(pieceName);
        drawMultilineText(img, description, 20, 90, 360);
        
        setImage(img);
    }
    
    private String getDescription(String pieceName)
    {
        // The description for pieces
        switch(pieceName)
        {
            case "DARK_PRINCE":
                return "Chess: Rook\n" +
                       "━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                       "Movement:\n" +
                       "• Moves in straight lines\n" +
                       "Special Ability (5 Elixir):\n" +
                       "• Mighty Mace: Charges forward to\n" +
                       "a chosen piece. All the pieces adjacent\n" +
                       "in a + pattern will be taken\n\n" +
                       "Tips:\n" +
                       "Use the charge ability to break through\n" +
                       "enemy lines!";
                       
            case "KNIGHT":
                return "Chess: Knight\n" +
                       "━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                       "Movement:\n" +
                       "• Moves in L-shape\n" +
                       "• Can jump over other pieces\n\n" +
                       "Special Ability (3 Elixir):\n" +
                       "• Close Combat: Eliminate all enemies\n" +
                       "  three tiles in front\n" +
                       "Tips:\n" +
                       "Infiltrate the enemy and slash them down!";
                       
            case "MUSKETEER":
                return "Chess: Bishop \n" +
                       "━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                       "Movement:\n" +
                       "• Moves diagonaly \n" +
                       "Special Ability (2 Elixir):\n" +
                       "• Sneaky Snipe: Shoots a bullet down a\n" +
                       "  column, eliminating the first target hit\n" +
                       "• Needs line of sight\n\n" +
                       "Tips:\n" +
                       "Keep a open lane to threaten the opponents\n" +
                       "pieces!";
                       
            case "WITCH":
                return "Chess: Queen\n" +
                       "━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                       "Movement:\n" +
                       "• Moves diagonally and horizontally\n" +
                       "Special Ability (3 Elixir):\n" +
                       "• Mother Witch: Spawns 4 skeleton around the\n" +
                       "  Witch in a + pattern that acts like \n" +
                       "  pawns (no skeleton ability) \n" +
                       "Tips:\n" +
                       "Protect the Witch to build an army of\n" +
                       "skeletons!";
                       
            case "ROYAL_GIANT":
                return "Chess: King\n" +
                       "━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                       "Movement:\n" +
                       "• Moves 1 space in any direction\n" +
                       "Special Ability (8 Elixir):\n" +
                       "• Bring the boom: Launches a cannon \n" +
                       "  that lands in 2 rounds, destroying\n" +
                       "  all pieces on the tile you choose as\n" +
                       "  well as the 8 adjacent tiles\n" +
                       "Tips:\n" +
                       "Launch your cannon at clumped enemies,\n" +
                       "your opponent only has 2 turns to avoid\n"+ 
                       "your attack!!";
                       
            case "ROYAL_RECRUITS":
                return "Chess: Pawn\n" +
                       "━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                       "Movement:\n" +
                       "• Moves like the pawn\n" +
                       "• Moves 1 space forwards,take 2 steps if on starting spot\n" +
                       "• Takes pieces in front diagonally\n" +
                       "Special Ability (1 Elixir):\n" +
                       "• Break the Limits: Gain the ability\n" +
                       "  to take the piece in front\n" +
                       "Tips:\n" +
                       "Use your pawns to limit your opponents squares\n"+
                       "by pushing them back!";
                       
            case "SKELETON":
                return "Chess: Pawn\n" +
                       "━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                       "Movement:\n" +
                       "• Moves like the pawn\n" +
                       "• Moves 1 space forwards, move 2 space if on starting spot\n" +
                       "• Takes pieces in front diagonally\n" +
                       "No Special Ability\n" +
                       "Tips:\n" +
                       "Use your skeletons to protect your stronger pieces!";
                       
            
                       
            default:
                return "No description available.\n\n" +
                       "Add your description in the\n" +
                       "getDescription() method!";
        }
    }
    
    //allows the text to be split into multiple lines for formatting
    private void drawMultilineText(GreenfootImage img, String text, int x, int y, int maxWidth)
    {
        String[] lines = text.split("\n");
        int lineHeight = 18;
        int currentY = y;
        
        for (String line : lines)
        {
            img.drawString(line, x, currentY);
            currentY += lineHeight;
        }
    }
}