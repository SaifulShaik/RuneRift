import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Piece here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Piece extends Actor
{
    public static enum PieceType { DARK_PRINCE, KNIGHT, MUSKETEER, ROYAL_GIANT, SKELETON, WITCH, ROYAL_RECRUITS }
    
    private PieceType type;
    private boolean isWhite;
    private Block currentBlock;
    
    public Piece(PieceType type, Block block, boolean isWhite) {
        this.type = type;
        this.currentBlock = block;
        this.isWhite = isWhite;
        
        setImage(type, isWhite);
        move(currentBlock);
    }
    
    private void move(Block target) {
        setLocation(target.getX(), target.getY());
    }
    
    private void setImage(PieceType type, boolean isWhite) {
        switch (type) {
            case PieceType.DARK_PRINCE:
                if (isWhite) setImage("images/WDarkPrince.png");
                else setImage("images/BDarkPrince.png");
                break;
            case PieceType.KNIGHT:
                if (isWhite) setImage("images/WKnight.png");
                //else setImage("images/BKnight.png");
                break;
            case PieceType.MUSKETEER:
                if (isWhite) setImage("images/WMusketeer.png");
                else setImage("images/BMusketeer.png");
                break;
            case PieceType.ROYAL_GIANT:
                if (isWhite) setImage("images/WRoyalGiant.png");
                else setImage("images/BRoyalGiant.png");
                break;
            case PieceType.SKELETON:
                if (isWhite) setImage("images/WSkeleton.png");
                else setImage("images/BSkeleton.png");
                break;
            case PieceType.WITCH:
                if (isWhite) setImage("images/WWitch.png");
                else setImage("images/BWitch.png");
                break;
            case PieceType.ROYAL_RECRUITS:
                if (isWhite) setImage("images/WRoyalRecruits.png");
                else setImage("images/BRoyalRecruits.png");
                break;
        }
        getImage().scale(125, 125);
    }
    
    public void act()
    {
        // Add your action code here.
    }
    
    
}
