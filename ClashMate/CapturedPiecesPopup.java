import greenfoot.*;
import java.util.List;

/**
 * A popup window showing captured pieces.
 * Can be closed by clicking anywhere or the close button.
 * 
 * @author Saiful Shaik
 * @version
 */
public class CapturedPiecesPopup extends Actor
{
    private String player;
    private List<Piece.PieceType> capturedPieces;
    private EndGameWorld parentWorld;
    private static final int POPUP_WIDTH = 400;
    private static final int POPUP_HEIGHT = 350;
    
    /**
     * Constructor for CapturedPiecesPopup
     * 
     * @param player The player whose captures are being shown
     * @param capturedPieces List of captured piece types
     * @param parentWorld Reference to parent world to close popup
     */
    public CapturedPiecesPopup(String player, List<Piece.PieceType> capturedPieces, EndGameWorld parentWorld)
    {
        this.player = player;
        this.capturedPieces = capturedPieces;
        this.parentWorld = parentWorld;
        createImage();
    }
    
    /**
     * Create the popup image
     */
    private void createImage()
    {
        GreenfootImage img = new GreenfootImage(POPUP_WIDTH, POPUP_HEIGHT);
        
        // Semi-transparent dark background
        img.setColor(new Color(20, 25, 40, 240));
        img.fillRect(0, 0, POPUP_WIDTH, POPUP_HEIGHT);
        
        // Border
        Color borderColor = player.equals("WHITE") ? new Color(200, 200, 200) : new Color(100, 100, 100);
        img.setColor(borderColor);
        img.drawRect(0, 0, POPUP_WIDTH - 1, POPUP_HEIGHT - 1);
        img.drawRect(2, 2, POPUP_WIDTH - 5, POPUP_HEIGHT - 5);
        
        // Title
        img.setColor(new Color(255, 215, 0)); // Gold
        img.setFont(new Font("Arial", true, false, 24));
        String title = player + "'S CAPTURED PIECES";
        int titleWidth = title.length() * 12;
        img.drawString(title, (POPUP_WIDTH - titleWidth) / 2, 35);
        
        // Subtitle
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", false, false, 16));
        String subtitle = "Total: " + capturedPieces.size() + " pieces";
        img.drawString(subtitle, (POPUP_WIDTH - subtitle.length() * 8) / 2, 60);
        
        // Draw captured pieces
        if (capturedPieces.isEmpty()) {
            img.setColor(new Color(150, 150, 150));
            img.setFont(new Font("Arial", true, false, 18));
            img.drawString("No pieces captured", POPUP_WIDTH / 2 - 80, POPUP_HEIGHT / 2);
        } else {
            drawCapturedPieces(img);
        }
        
        // Close instruction
        img.setColor(new Color(150, 150, 150));
        img.setFont(new Font("Arial", false, false, 14));
        img.drawString("Click anywhere to close", POPUP_WIDTH / 2 - 80, POPUP_HEIGHT - 20);
        
        // Close button (X) in top right
        img.setColor(new Color(200, 50, 50));
        img.fillRect(POPUP_WIDTH - 35, 5, 30, 25);
        img.setColor(Color.WHITE);
        img.setFont(new Font("Arial", true, false, 18));
        img.drawString("X", POPUP_WIDTH - 25, 24);
        
        setImage(img);
    }
    
    /**
     * Draw the captured pieces in a grid
     */
    private void drawCapturedPieces(GreenfootImage img)
    {
        int startX = 30;
        int startY = 80;
        int pieceSize = 50;
        int padding = 10;
        int piecesPerRow = 6;
        
        // Count each piece type
        int[] counts = new int[Piece.PieceType.values().length];
        for (Piece.PieceType type : capturedPieces) {
            counts[type.ordinal()]++;
        }
        
        int col = 0;
        int row = 0;
        
        // Determine which color the captured pieces are (opposite of player)
        boolean capturedAreWhite = !player.equals("WHITE");
        
        for (Piece.PieceType type : Piece.PieceType.values()) {
            if (counts[type.ordinal()] > 0) {
                int x = startX + col * (pieceSize + padding);
                int y = startY + row * (pieceSize + padding + 15);
                
                // Draw piece image
                try {
                    String imageName = getPieceImageName(type, capturedAreWhite);
                    GreenfootImage pieceImg = new GreenfootImage(imageName);
                    pieceImg.scale(pieceSize, pieceSize);
                    img.drawImage(pieceImg, x, y);
                } catch (Exception e) {
                    // Fallback: draw colored rectangle
                    img.setColor(capturedAreWhite ? Color.WHITE : Color.DARK_GRAY);
                    img.fillRect(x, y, pieceSize, pieceSize);
                }
                
                // Draw count below
                img.setColor(Color.WHITE);
                img.setFont(new Font("Arial", true, false, 14));
                String countText = "x" + counts[type.ordinal()];
                img.drawString(countText, x + pieceSize/2 - 10, y + pieceSize + 15);
                
                col++;
                if (col >= piecesPerRow) {
                    col = 0;
                    row++;
                }
            }
        }
    }
    
    /**
     * Get the image file name for a piece type
     */
    private String getPieceImageName(Piece.PieceType type, boolean isWhite)
    {
        String prefix = isWhite ? "W" : "B";
        switch (type) {
            case DARK_PRINCE: return "images/" + prefix + "DarkPrince.png";
            case KNIGHT: return "images/" + prefix + "Knight.png";
            case MUSKETEER: return "images/" + prefix + "Musketeer.png";
            case ROYAL_GIANT: return "images/" + prefix + "RoyalGiant.png";
            case SKELETON: return "images/" + prefix + "Skeleton.png";
            case WITCH: return "images/" + prefix + "Witch.png";
            case ROYAL_RECRUITS: return "images/" + prefix + "RoyalRecruits.png";
            default: return "images/" + prefix + "Skeleton.png";
        }
    }
    
    /**
     * Act - popup closing is now handled by EndGameWorld
     */
    public void act()
    {
        // Closing is handled by EndGameWorld to prevent immediate close
        // on the same click that opened the popup
    }
}
