import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class Piece here.
 * 
 * @author Joe Zhuo 
 * @version 1/13/2026
 */
public class Piece extends Actor
{
    public static enum PieceType { DARK_PRINCE, KNIGHT, MUSKETEER, ROYAL_GIANT, SKELETON, WITCH, ROYAL_RECRUITS }
    
    private PieceType type;
    private boolean isWhite;
    private Block currentBlock;
    private boolean isSelected;
    
    private boolean canMove;
    
    private boolean abilityUsed;
    private int abilityState;
    private int abilityCost;
    
    private List<Block> highlightedBlocks = new ArrayList<>();
    
    public Piece(PieceType type, Block block, boolean isWhite) {
        this.type = type;
        this.currentBlock = block;
        this.isWhite = isWhite;
        
        switch(type) {
            case DARK_PRINCE: abilityCost = 5; break;
            case KNIGHT: abilityCost = 3; break;
            case MUSKETEER: abilityCost = 2; break;
            case ROYAL_GIANT: abilityCost = 7; break;
            case WITCH: abilityCost = 6; break;
            case ROYAL_RECRUITS: abilityCost = 1; break;
        }
        
        setImage(type, isWhite);
        moveTo(currentBlock);
        updateHitbox();
    }
    
    private boolean isMyTurn()
    {
        GridWorld world = (GridWorld) getWorld();
        TurnManager tm = world.getTurnManager();
    
        return isWhite ? tm.isPlayerTurn("WHITE") : tm.isPlayerTurn("BLACK");
    }
    
    private void moveTo(Block target) {
        target.removePiece(true);
        
        if (currentBlock != null) currentBlock.setPiece(null);

        setLocation(target.getX(), target.getY());
        
        currentBlock = target;
        
        target.setPiece(this);
        
        if (abilityState == 1 && type == PieceType.DARK_PRINCE) {
            dealSplashDamage();
        }
        
        clearHighlights();
    }
    
    private void dealSplashDamage() {
        GridWorld gw = (GridWorld) getWorld();
            
        int x = currentBlock.getBoardX();
        int y = currentBlock.getBoardY();
        
        Block block1 = gw.getBlock(x+1, y);
        if (block1 != null) {
            block1.removePiece(true);
        }
        Block block2 = gw.getBlock(x-1, y);
        if (block2 != null) {
            block2.removePiece(true);
        }
        Block block3 = gw.getBlock(x, y+1);
        if (block3 != null) {
            block3.removePiece(true);
        }
        Block block4 = gw.getBlock(x, y-1);
        if (block4 != null) {
            block4.removePiece(true);
        }
        abilityState = 0;
    }
    
    private void spawnSkeletons() {
        GridWorld gw = (GridWorld) getWorld();
            
        int x = currentBlock.getBoardX();
        int y = currentBlock.getBoardY();
        
        Block block1 = gw.getBlock(x+1, y);
        if (block1 != null && block1.currentPiece() == null) {
            Piece skeleton = new Piece(PieceType.SKELETON, block1, this.isWhite);
            gw.addObject(skeleton, block1.getX(), block1.getY());
        }
        Block block2 = gw.getBlock(x-1, y);
        if (block2 != null && block2.currentPiece() == null) {
            Piece skeleton = new Piece(PieceType.SKELETON, block2, this.isWhite);
            gw.addObject(skeleton, block2.getX(), block2.getY());
        }
        Block block3 = gw.getBlock(x, y+1);
        if (block3 != null && block3.currentPiece() == null) {
            Piece skeleton = new Piece(PieceType.SKELETON, block3, this.isWhite);
            gw.addObject(skeleton, block3.getX(), block3.getY());
        }
        Block block4 = gw.getBlock(x, y-1);
        if (block4 != null && block4.currentPiece() == null) {
            Piece skeleton = new Piece(PieceType.SKELETON, block4, this.isWhite);
            gw.addObject(skeleton, block4.getX(), block4.getY());
        }
    }
    
    private boolean checkIfMoveIsValid(Block targetBlock) {
        Piece pieceOnTarget = targetBlock.currentPiece();
        
        if (pieceOnTarget != null && isWhite == pieceOnTarget.checkIsWhite()) return false;
        
        int x = currentBlock.getBoardX();
        int y = currentBlock.getBoardY();
        
        int targetX = targetBlock.getBoardX();
        int targetY = targetBlock.getBoardY();
        
        int dx = targetX - x; 
        int dy = targetY - y; 
        
        int direction = isWhite ? -1 : 1;
    
        switch (type) {
            case PieceType.ROYAL_RECRUITS:
                int startRow = isWhite ? 6 : 1;
                boolean isFirstMove = (x == startRow);
    
                if (pieceOnTarget == null) {
                    if (dy != 0) return false;
                    if (dx == direction) return true;
                    if (isFirstMove && dx == 2 * direction) {
                        GridWorld world = (GridWorld) getWorld();
                        Block intermediate = world.getBlock(x + direction, y);
                        if (intermediate.currentPiece() == null) return true;
                        return false;
                    }
                    return false;
                } 
                else {
                    if (dx == direction && Math.abs(dy) == 1) return true;
                    if (dx == direction && dy == 0 && abilityUsed) return true;
                    return false;
                }
    
            case PieceType.DARK_PRINCE:
                if (dx != 0 && dy != 0) return false; // must be straight line
                return isPathClear(x, y, targetX, targetY);
    
            case PieceType.WITCH:
                if (dx != 0 && dy != 0 && Math.abs(dx) != Math.abs(dy)) return false; // diagonal or straight
                return isPathClear(x, y, targetX, targetY);
    
            case PieceType.SKELETON:
                if (dy != 0) return false;
                if (dx != direction) return false;
                return true; // 1-square forward
    
            case PieceType.ROYAL_GIANT:
                if (Math.abs(dx) > 1 || Math.abs(dy) > 1) return false;
                return true;
    
            case PieceType.KNIGHT:
                return (Math.abs(dx) == 2 && Math.abs(dy) == 1) || (Math.abs(dx) == 1 && Math.abs(dy) == 2);
    
            case PieceType.MUSKETEER:
                if (Math.abs(dx) != Math.abs(dy)) return false;
                return isPathClear(x, y, targetX, targetY);
        }
    
        return false;
    }
    
    private boolean isPathClear(int startX, int startY, int endX, int endY) {
        GridWorld world = (GridWorld) getWorld();
        
        // calculates direction
        int dx = Integer.compare(endX, startX); 
        int dy = Integer.compare(endY, startY); 
    
        int x = startX + dx;
        int y = startY + dy;
        
        while (x != endX || y != endY) {
            Block block = world.getBlock(x, y);
            if (block.currentPiece() != null) return false;
            x += dx;
            y += dy;
        }
        
        return true;
    }
    
    private void snipe() {
        int x = currentBlock.getBoardX();
        int y = currentBlock.getBoardY();
        
        int direction = isWhite ? -1 : 1;
        
        GridWorld gw = (GridWorld) getWorld();
        
        int step = isWhite ? -1 : 1;
        
        int cx = x + step;

        while (cx < gw.CELLS_TALL) {
            Block b = gw.getBlock(cx, y);
            if (b.currentPiece() != null) {
                b.removePiece(true);
                break;
            }
            cx += step;
        }
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
            GridWorld world = (GridWorld)getWorld();
            if (isSelected) {
                deselect();
                world.setSelectedPiece(null);
            } else {
                world.setSelectedPiece(this);
                isSelected = true;
                updateHitbox();
                showPossibleMoves();
            }
        }
        else if (isSelected) {
            //System.out.println(checkIfMoveIsValid(selectedBlock));
            if (checkIfMoveIsValid(selectedBlock)) {
                moveTo(selectedBlock);
                endTurn();
            }
        }
    }
    
    private void endTurn() {
        ((GridWorld) getWorld()).endTurn();
        deselect();
    }
    
    private void showPossibleMoves() {
        GridWorld world = (GridWorld) getWorld();
        clearHighlights(); 
        
        for (int x = 0; x < GridWorld.CELLS_TALL; x++) {
            for (int y = 0; y < GridWorld.CELLS_WIDE; y++) {
                Block block = world.getBlock(x, y);
                if (block != null && checkIfMoveIsValid(block)) {
                    Piece pieceOnTarget = block.currentPiece();
                    if (pieceOnTarget != null && pieceOnTarget.checkIsWhite() != this.isWhite) {
                        if (abilityState == 1 && type == PieceType.DARK_PRINCE) {
                            block.highlight(Color.ORANGE);
                        }
                        else block.highlight(Color.RED);
                    } else {
                        block.highlight(Color.GREEN); 
                    }
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
    
    public void deselect() {
        isSelected = false;
        clearHighlights();
        updateHitbox();
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
    
    private void updateHitbox() {
        Color color = isSelected ? Color.GREEN : Color.RED;
        if (isSelected && abilityState == 1 && type == PieceType.DARK_PRINCE) {
            color = Color.ORANGE;
        }
        GreenfootImage img = getImage();
        GreenfootImage hitboxImg = new GreenfootImage(img); // copy to avoid scaling issues
        hitboxImg.setColor(color);
        hitboxImg.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);
        setImage(hitboxImg);
    }
    
    public void useAbility() {
        abilityUsed = true;
        
        GridWorld gw = (GridWorld) getWorld();
        gw.removeElixir(isWhite, abilityCost);
        
        int x = currentBlock.getBoardX();
        int y = currentBlock.getBoardY();
        
        int direction = isWhite ? -1 : 1;
        
        boolean endTurn = false;
        
        switch (type) {
            case KNIGHT:
                Block block1 = gw.getBlock(x+direction, y-1);
                if (block1 != null && block1.currentPiece() != null ) {
                    if (block1.currentPiece().checkIsWhite() != this.isWhite) {
                        block1.removePiece(true);
                    }
                }
                Block block2 = gw.getBlock(x+direction, y);
                if (block2 != null && block2.currentPiece() != null ) {
                    if (block2.currentPiece().checkIsWhite() != this.isWhite) {
                        block2.removePiece(true);
                    }
                }
                Block block3 = gw.getBlock(x+direction, y+1);
                if (block3 != null && block3.currentPiece() != null ) {
                    if (block3.currentPiece().checkIsWhite() != this.isWhite) {
                        block3.removePiece(true);
                    }
                }
                endTurn = true;
                break;
            case DARK_PRINCE:
                abilityState = 1;
                break;
            case WITCH:
                spawnSkeletons();
                endTurn = true;
                break;
            case MUSKETEER:
                snipe();
                endTurn = true;
                break;
        }
        
        // refresh the possible moves and update hitbox
        if (!endTurn) {
            updateHitbox();
            clearHighlights();
            showPossibleMoves();
        } 
        else {
            endTurn();
        }
    }
    
    public void clearBlock() {
        currentBlock = null;
        isSelected = false;
        clearHighlights();
    }
    
    public boolean checkIsWhite() {
        return isWhite;
    }
    
    public void act()
    {
        GridWorld gw = (GridWorld) getWorld();
        
        if (isSelected && (!abilityUsed || abilityState == 0) && 
            gw.isButtonClicked(isWhite) && 
            gw.getElixir(isWhite) >= abilityCost) 
                useAbility();
        
        move();
    }
}
