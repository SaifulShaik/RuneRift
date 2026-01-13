import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

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
    private boolean isSelected;
    
    private boolean canMove;
    
    private List<Block> highlightedBlocks = new ArrayList<>();
    
    public Piece(PieceType type, Block block, boolean isWhite) {
        this.type = type;
        this.currentBlock = block;
        this.isWhite = isWhite;
        
        setImage(type, isWhite);
        moveTo(currentBlock);
        showHitbox(Color.RED);
    }
    
    private boolean isMyTurn()
    {
        GridWorld world = (GridWorld) getWorld();
        TurnManager tm = world.getTurnManager();
    
        return isWhite ? tm.isPlayerTurn("WHITE") : tm.isPlayerTurn("BLACK");
    }
    
    private void moveTo(Block target) {
        setLocation(target.getX(), target.getY());
        currentBlock = target;
        clearHighlights();
        target.setPiece(this);
    }
    
    private boolean checkIfMoveIsValid(Block targetBlock) {
        Piece pieceOnTarget = targetBlock.currentPiece();
        
        if (pieceOnTarget != null) {
            if (pieceOnTarget.checkIsWhite() == this.isWhite) {
                return false; 
            }
        }

        
        int x = currentBlock.getBoardX();
        int y = currentBlock.getBoardY();
        
        int targetX = targetBlock.getBoardX();
        int targetY = targetBlock.getBoardY();
        
        int dy = targetY - y;
        int dx = targetX - x;
                
        int direction = isWhite ? -1 : 1;
        
        switch (type) {
            case PieceType.ROYAL_RECRUITS:
                if (dy != 0) return false;
                
                int startRow = isWhite ? 6 : 1;
                boolean isFirstMove = (x == startRow);
                
                if (dx == direction) return true;
                if (isFirstMove && dx == 2 * direction) return true;
                return false;
            case PieceType.DARK_PRINCE:
                return dy == 0 || dx == 0;
            case PieceType.WITCH:
                return Math.abs(dx) == Math.abs(dy) || dy == 0 || dx == 0;
            case PieceType.SKELETON:
                return dy == 0 && dx == direction;
            case PieceType.ROYAL_GIANT:
                return Math.abs(dy) < 2 && Math.abs(dx) < 2;
            case PieceType.KNIGHT:
                return (Math.abs(dx) == 2 && Math.abs(dy) == 1) || 
                       (Math.abs(dy) == 2 && Math.abs(dx) == 1);
            case PieceType.MUSKETEER:
                return Math.abs(dx) == Math.abs(dy);
        }
        return false;
    }
    
    private void move() {        
        if (!Greenfoot.mouseClicked(null)) return;
        if (!isMyTurn()) return;
        
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
        
        int mouseX = mouse.getX();
        int mouseY = mouse.getY();
        
        Block selectedBlock = ((GridWorld) getWorld()).worldToBlockPos(mouseX, mouseY);
        if (selectedBlock == null) return;
        
        if (selectedBlock == currentBlock) {
            isSelected = !isSelected;
            showHitbox(isSelected ? Color.GREEN : Color.RED);
            
            if (isSelected) showPossibleMoves();
            else clearHighlights();
        }
        else if (isSelected) {
            //System.out.println(checkIfMoveIsValid(selectedBlock));
            if (checkIfMoveIsValid(selectedBlock)) {
                moveTo(selectedBlock);
                
                ((GridWorld) getWorld()).endTurn();
                isSelected = false;
                
                showHitbox(Color.RED);
                clearHighlights();
            }
        }
    }
    
    private void showPossibleMoves() {
        GridWorld world = (GridWorld) getWorld();
        clearHighlights(); 
        for (int x = 0; x < GridWorld.CELLS_TALL; x++) {
            for (int y = 0; y < GridWorld.CELLS_WIDE; y++) {
                Block block = world.getBlock(x, y);
                if (block != null && checkIfMoveIsValid(block)) {
                    block.highlight(Color.GREEN); 
                    highlightedBlocks.add(block);
                }
            }
        }
    }
    
    private void clearHighlights() {
        for (Block block : highlightedBlocks) {
            block.clearHighlight(); 
        }
        highlightedBlocks.clear();
    }
    
    private void setImage(PieceType type, boolean isWhite) {
        int size = 60;
        switch (type) {
            case PieceType.DARK_PRINCE:
                if (isWhite) setImage("images/WDarkPrince.png");
                else setImage("images/BDarkPrince.png");
                break;
            case PieceType.KNIGHT:
                if (isWhite) setImage("images/WKnight.png");
                else setImage("images/BKnight.png");
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
        getImage().scale(size, size);
    }
    
    private void showHitbox(Color color) {
        GreenfootImage img = getImage();
        GreenfootImage hitboxImg = new GreenfootImage(img); // copy to avoid scaling issues
        hitboxImg.setColor(color);
        hitboxImg.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);
        setImage(hitboxImg);
    }
    
    public boolean checkIsWhite() {
        return isWhite;
    }
    
    public void act()
    {
        move();
    }
}
