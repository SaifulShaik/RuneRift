import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.List;

/**
 * A menu that appears when a Royal Recruit (pawn) reaches the promotion row.
 * Shows available pieces to promote to with their elixir costs.
 * 
 * @author Joe Zhuo
 * @version 1/20/2026
 */
public class PromotionMenu extends Actor
{
    // promotion costs
    public static final int KNIGHT_COST = 2;
    public static final int MUSKETEER_COST = 3;
    public static final int DARK_PRINCE_COST = 5;
    public static final int WITCH_COST = 6;
    public static final int ROYAL_GIANT_COST = 5;
    
    private Piece promotingPiece;
    private boolean isWhite;
    private int availableElixir;
    
    private List<Button> optionButtons;
    private List<Piece.PieceType> pieceTypes;
    private List<Integer> costs;
    private Button cancelButton;
    
    private GreenfootImage backgroundImage;
    
    public PromotionMenu(Piece piece, int elixir)
    {
        this.promotingPiece = piece;
        this.isWhite = piece.checkIsWhite();
        this.availableElixir = elixir;
        
        optionButtons = new ArrayList<>();
        pieceTypes = new ArrayList<>();
        costs = new ArrayList<>();
        
        pieceTypes.add(Piece.PieceType.KNIGHT);
        costs.add(KNIGHT_COST);
        
        pieceTypes.add(Piece.PieceType.MUSKETEER);
        costs.add(MUSKETEER_COST);
        
        pieceTypes.add(Piece.PieceType.WITCH);
        costs.add(WITCH_COST);
        
        pieceTypes.add(Piece.PieceType.DARK_PRINCE);
        costs.add(DARK_PRINCE_COST);
        
        pieceTypes.add(Piece.PieceType.ROYAL_GIANT);
        costs.add(ROYAL_GIANT_COST);
        
        createBackground();
    }
    
    private void createBackground()
    {
        int width = 300;
        int height = 400;
        
        backgroundImage = new GreenfootImage(width, height);
        
        backgroundImage.setColor(new Color(30, 30, 50, 230));
        backgroundImage.fillRect(0, 0, width, height);
        
        backgroundImage.setColor(new Color(100, 149, 237));
        backgroundImage.drawRect(0, 0, width - 1, height - 1);
        backgroundImage.drawRect(1, 1, width - 3, height - 3);
        
        GreenfootImage titleImg = new GreenfootImage("PROMOTE", 24, Color.WHITE, new Color(0, 0, 0, 0));
        backgroundImage.drawImage(titleImg, (width - titleImg.getWidth()) / 2, 15);
        
        String elixirText = "Elixir: " + availableElixir;
        GreenfootImage elixirImg = new GreenfootImage(elixirText, 18, new Color(255, 100, 255), new Color(0, 0, 0, 0));
        backgroundImage.drawImage(elixirImg, (width - elixirImg.getWidth()) / 2, 45);
        
        setImage(backgroundImage);
    }
    
    @Override
    protected void addedToWorld(World world)
    {
        int startY = getY() - 75;
        int spacing = 50;
        
        for (int i = 0; i < pieceTypes.size(); i++)
        {
            Piece.PieceType type = pieceTypes.get(i);
            int cost = costs.get(i);
            
            String typeName = formatTypeName(type);
            String buttonText = typeName + " (" + cost + ")";
            
            Color normalColor, hoverColor;
            if (cost <= availableElixir)
            {
                normalColor = new Color(50, 120, 50);  
                hoverColor = new Color(70, 160, 70);
            }
            else
            {
                normalColor = new Color(100, 50, 50);  
                hoverColor = new Color(120, 60, 60);
            }
            
            Button btn = new Button(buttonText, 200, 35, normalColor, hoverColor, Color.WHITE, 16);
            optionButtons.add(btn);
            world.addObject(btn, getX(), startY + i * spacing);
        }
        
        cancelButton = new Button("Cancel", 120, 30, new Color(100, 100, 100), new Color(130, 130, 130), Color.WHITE, 14);
        world.addObject(cancelButton, getX(), startY + pieceTypes.size() * spacing + 20);
    }
    
    public void act()
    {
        for (int i = 0; i < optionButtons.size(); i++)
        {
            Button btn = optionButtons.get(i);
            if (btn.wasClicked())
            {
                int cost = costs.get(i);
                if (cost <= availableElixir)
                {
                    promoteToType(pieceTypes.get(i), cost);
                    return;
                }
            }
        }
        
        if (cancelButton != null && cancelButton.wasClicked())
        {
            cancelPromotion();
        }
    }
    
    private void cancelPromotion()
    {
        promotingPiece.setWaitingForPromotion(true);
        
        GridWorld gw = (GridWorld) getWorld();
        
        closeMenu();
        
        if (gw != null) {
            gw.endTurn();
        }
    }
    
    private void promoteToType(Piece.PieceType newType, int cost)
    {
        GridWorld gw = (GridWorld) getWorld();
        
        gw.removeElixir(isWhite, cost);
        
        Block block = promotingPiece.getCurrentBlock();
        
        if (isWhite)
        {
            gw.getWhitePieces().remove(promotingPiece);
        }
        else
        {
            gw.getBlackPieces().remove(promotingPiece);
        }
        block.setPiece(null);
        gw.removeObject(promotingPiece);
        
        Piece newPiece = new Piece(newType, block, isWhite);
        gw.addObject(newPiece, block.getX(), block.getY());
        
        if (isWhite)
        {
            gw.getWhitePieces().add(newPiece);
        }
        else
        {
            gw.getBlackPieces().add(newPiece);
        }
        
        closeMenu();
        gw.endTurn();
    }
    
    private void closeMenu()
    {
        World world = getWorld();
        
        for (Button btn : optionButtons)
        {
            world.removeObject(btn);
        }
        if (cancelButton != null)
        {
            world.removeObject(cancelButton);
        }
        
        GridWorld gw = (GridWorld) world;
        gw.setPromotionMenuActive(false);
        
        world.removeObject(this);
    }
    
    private String formatTypeName(Piece.PieceType type)
    {
        switch (type)
        {
            case KNIGHT: return "Knight";
            case MUSKETEER: return "Musketeer";
            case DARK_PRINCE: return "Dark Prince";
            case WITCH: return "Witch";
            case ROYAL_GIANT: return "Royal Giant";
            default: return type.toString();
        }
    }
}
