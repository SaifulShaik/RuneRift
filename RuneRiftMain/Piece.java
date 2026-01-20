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
    private boolean hasMoved;
    
    private boolean canMove;
    
    private boolean abilityUsed;
    private int abilityState;
    private int abilityCost;
    
    private boolean waitingForPromotion;  // True if Royal Recruit is at promotion row but canceled promotion

    private List<Block> highlightedBlocks = new ArrayList<>();
    
    public Piece(PieceType type, Block block, boolean isWhite) {
        this.type = type;
        this.currentBlock = block;
        this.isWhite = isWhite;
        
        switch(type) {
            case DARK_PRINCE: abilityCost = 5; break;
            case KNIGHT: abilityCost = 3; break;
            case MUSKETEER: abilityCost = 2; break;
            case ROYAL_GIANT: abilityCost = 8; break;
            case WITCH: abilityCost = 3; break;
            case ROYAL_RECRUITS: abilityCost = 1; break;
        }
        
        setImage(type, isWhite);
        moveTo(currentBlock);
        updateHitbox();

        hasMoved = false;  
    }
    
    private boolean isMyTurn()
    {
        GridWorld world = (GridWorld) getWorld();
        TurnManager tm = world.getTurnManager();
    
        return isWhite ? tm.isPlayerTurn("WHITE") : tm.isPlayerTurn("BLACK");
    }
    
    private void moveTo(Block target) {
        boolean hadPiece = target.currentPiece() != null;
        target.removePiece(true);
        
        if (currentBlock != null) currentBlock.setPiece(null);
        
        if (type == PieceType.ROYAL_RECRUITS && abilityState == 1 && getWorld() != null) {
            SpearStrikeEffect spear = new SpearStrikeEffect(target.getX(), target.getY(), isWhite);
            getWorld().addObject(spear, target.getX(), target.getY());
            abilityState = 0;
        }
        
        setLocation(target.getX(), target.getY());
        currentBlock = target;
        target.setPiece(this);
        hasMoved = true;

        if (type == PieceType.DARK_PRINCE && abilityState == 1 && getWorld ()!= null && hadPiece) {
            getWorld().addObject(new ChargeEffect(currentBlock.getX(), currentBlock.getY()), currentBlock.getX(), currentBlock.getY());
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
            case ROYAL_RECRUITS:
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
    
            case DARK_PRINCE:
                if (dx != 0 && dy != 0) return false; // must be straight line
                return isPathClear(x, y, targetX, targetY);
    
            case WITCH:
                if (dx != 0 && dy != 0 && Math.abs(dx) != Math.abs(dy)) return false; // diagonal or straight
                return isPathClear(x, y, targetX, targetY);
    
            case SKELETON:
                if (Math.abs(dy) == Math.abs(dx) && pieceOnTarget != null) {
                    if (pieceOnTarget.checkIsWhite() != this.isWhite) {
                        return true;
                    }
                }
                if (dy == 0 && dx == direction && pieceOnTarget == null) return true;
                return false;
    
            case ROYAL_GIANT:
                if (abilityState == 1) return false; 
            
                if (!hasMoved && dx == 0 && Math.abs(dy) == 2) {
                    int rookY = (dy > 0) ? 7 : 0; 
                    
                    GridWorld gw = (GridWorld) getWorld();
                    Block rookBlock = gw.getBlock(x, rookY);
                    
                    if (rookBlock != null) {
                        Piece potentialRook = rookBlock.currentPiece();
            
                        if (potentialRook != null && 
                            potentialRook.getType() == PieceType.DARK_PRINCE && 
                            !potentialRook.checkHasMoved() && 
                            isPathClear(x, y, x, rookY)) {
                            System.out.println("[King] Can Castle");
                            return true;
                        }
                    }
                }
                if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) return true;
                return false;
    
            case KNIGHT:
                return (Math.abs(dx) == 2 && Math.abs(dy) == 1) || (Math.abs(dx) == 1 && Math.abs(dy) == 2);
    
            case MUSKETEER:
                if (Math.abs(dx) != Math.abs(dy)) return false;
                return isPathClear(x, y, targetX, targetY);
        }
    
        return false;
    }
    
    public PieceType getType() {
        return type;
    }
    
    public boolean checkHasMoved() {
        return hasMoved;
    }
    
    private boolean isPathClear(int startX, int startY, int endX, int endY) {
        GridWorld world = (GridWorld) getWorld();
        
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
                if (b.currentPiece().checkIsWhite() != isWhite) {
                    b.removePiece(true);
                    break;
                }
                else break;
            }
            cx += step;
        }
    }

    private void move() {        
        if (!Greenfoot.mouseClicked(null)) return;
        if (!isMyTurn()) return;
        
        // Don't allow moves while promotion menu is active
        GridWorld gw = (GridWorld) getWorld();
        if (gw.isPromotionMenuActive()) return;
        
        if (type == PieceType.ROYAL_GIANT && abilityState == 1) {
            if (!Greenfoot.mouseClicked(null)) return;
        }
        
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
            } 
            else {
                world.setSelectedPiece(this);
                isSelected = true;
                updateHitbox();
                showPossibleMoves();
            }
        }
        else if (isSelected) {
            if (type == PieceType.ROYAL_GIANT && abilityState == 1) {
                queueRoyalGiantExplosion(selectedBlock);
                endTurn();
                return;
            }
            else if (checkIfMoveIsValid(selectedBlock)) {
                if (type == PieceType.ROYAL_GIANT && Math.abs(selectedBlock.getBoardY() - currentBlock.getBoardY()) == 2) {
                    executeCastling(selectedBlock);
                    endTurn();
                }
                else {
                    moveTo(selectedBlock);
                    
                    // Check for pawn promotion
                    if (canPromote()) {
                        gw.showPromotionMenu(this);
                        // Don't end turn yet - promotion menu will handle it
                    } else {
                        endTurn();
                    }
                }
            }
        }
    }
    
    private void executeCastling(Block targetBlock) {
        GridWorld gw = (GridWorld) getWorld();
        
        int row = currentBlock.getBoardX();
        int kingOrigY = currentBlock.getBoardY();
        int kingTargY = targetBlock.getBoardY();
        
        int rookOrigY = (kingTargY > kingOrigY) ? 7 : 0;
        int rookTargY = (kingTargY > kingOrigY) ? kingTargY - 1 : kingTargY + 1;

        moveTo(targetBlock);
    
        Block rookOrigBlock = gw.getBlock(row, rookOrigY);
        Block rookTargBlock = gw.getBlock(row, rookTargY);
        Piece rook = rookOrigBlock.currentPiece();
        
        if (rook != null) {
            rook.currentBlock.setPiece(null); 
            rook.setLocation(rookTargBlock.getX(), rookTargBlock.getY());
            rook.currentBlock = rookTargBlock;
            rookTargBlock.setPiece(rook);
            rook.hasMoved = true;
        }
    }

    private void queueRoyalGiantExplosion(Block target) {
        abilityState = 0;
        GridWorld gw = (GridWorld) getWorld();
        gw.addBomb(target, isWhite);
        
        clearHighlights();
    }
    
    private void endTurn() {
        ((GridWorld) getWorld()).endTurn();
        abilityUsed = false;
        deselect();
    }
    
    private void showPossibleMoves() {
        GridWorld world = (GridWorld) getWorld();
        
        clearHighlights(); 
        
        for (int x = 0; x < GridWorld.CELLS_TALL; x++) {
            for (int y = 0; y < GridWorld.CELLS_WIDE; y++) {
                Block block = world.getBlock(x, y);
                if (block == null ) continue;

                if (type == PieceType.ROYAL_GIANT && abilityState == 1) {
                    block.highlight(Color.ORANGE);
                    highlightedBlocks.add(block);
                }
                else if (checkIfMoveIsValid(block)) {
                    Piece pieceOnTarget = block.currentPiece();
                    if (pieceOnTarget != null && pieceOnTarget.checkIsWhite() != this.isWhite) {
                        if (abilityState == 1 && type == PieceType.DARK_PRINCE) {
                            block.highlight(Color.ORANGE);
                        }
                        else block.highlight(Color.RED);
                    } 
                    else {
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
            case DARK_PRINCE:
                if (isWhite) setImage("images/WDarkPrince.png");
                else setImage("images/BDarkPrince.png");
                break;
            case KNIGHT:
                if (isWhite) setImage("images/WKnight.png");
                else setImage("images/BKnight.png");
                break;
            case MUSKETEER:
                if (isWhite) setImage("images/WMusketeer.png");
                else setImage("images/BMusketeer.png");
                break;
            case ROYAL_GIANT:
                if (isWhite) setImage("images/WRoyalGiant.png");
                else setImage("images/BRoyalGiant.png");
                break;
            case SKELETON:
                if (isWhite) setImage("images/WSkeleton.png");
                else setImage("images/BSkeleton.png");
                break;
            case WITCH:
                if (isWhite) setImage("images/WWitch.png");
                else setImage("images/BWitch.png");
                break;
            case ROYAL_RECRUITS:
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
        
        switch (type) {
            case KNIGHT:
                KnightSlashEffect slash = new KnightSlashEffect(getX(), getY(),isWhite);
                gw.addObject(slash, getX(), getY());
                
                Block block1 = gw.getBlock(x+direction, y-1);
                if (block1 != null) {
                    block1.removePiece(true);
                }
                Block block2 = gw.getBlock(x+direction, y);
                if (block2 != null) {
                    block2.removePiece(true);
                }
                Block block3 = gw.getBlock(x+direction, y+1);
                if (block3 != null) {
                    block3.removePiece(true);
                }
                endTurn();
                break;
                
            case DARK_PRINCE:
                abilityState = 1;
                updateHitbox();
                clearHighlights();
                showPossibleMoves();
                break;
                
            case WITCH:
                SummonEffect summon = new SummonEffect(getX(), getY());
                gw.addObject(summon, getX(), getY());
                
                spawnSkeletons();
                endTurn();
                break;
                
            case MUSKETEER:
                int cx = x + (isWhite ? -1 : 1);
                Block targetBlock = null;
                while (cx >= 0 && cx < gw.CELLS_TALL) {
                    Block b = gw.getBlock(cx, y);
                    if (b.currentPiece() != null) {
                        targetBlock = b;
                        break;
                    }
                    cx += (isWhite ? -1 : 1);
                }
                
                if (targetBlock != null) {
                    SnipeEffect snipe = new SnipeEffect(getX(), getY(), 
                        targetBlock.getX(), targetBlock.getY());
                    gw.addObject(snipe, getX(), getY());
                }
                
                snipe();
                endTurn();
                break;
                
            case ROYAL_GIANT:
                abilityState = 1;
                updateHitbox();
                clearHighlights();
                showPossibleMoves();
                break;
                
            case ROYAL_RECRUITS:
                // If waiting for promotion, reopen the promotion menu
                if (waitingForPromotion && canPromote()) {
                    gw.showPromotionMenu(this);
                } else {
                    abilityState = 1;
                    updateHitbox();
                    clearHighlights();
                    showPossibleMoves();
                }
                break;
        }
    }
    
    public void clearBlock() {
        currentBlock = null;
        isSelected = false;
        clearHighlights();
    }
    
    /**
     * Removes this piece from the GridWorld's piece list.
     * Should be called when this piece is captured/removed from the game.
     */
    public void removePieceFromList() {
        GridWorld gw = (GridWorld) getWorld();
        if (gw != null) {
            gw.removePieceFromList(this);
        }
    }
    
    public boolean checkIsWhite() {
        return isWhite;
    }
    
    /**
     * Get the current block this piece is on
     */
    public Block getCurrentBlock() {
        return currentBlock;
    }
    
    /**
     * Check if this Royal Recruit has reached the promotion row
     * White pieces promote at row 0, Black pieces promote at row 7
     */
    private boolean canPromote() {
        if (type != PieceType.ROYAL_RECRUITS) return false;
        if (currentBlock == null) return false;
        
        int row = currentBlock.getBoardX();
        // White promotes at row 0 (top), Black promotes at row 7 (bottom)
        return (isWhite && row == 0) || (!isWhite && row == 7);
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
    
    public int getAbilityCost()
    {
        return abilityCost;
    }
    
    /**
     * Set whether this piece is waiting for promotion
     */
    public void setWaitingForPromotion(boolean waiting) {
        this.waitingForPromotion = waiting;
    }
    
    /**
     * Check if this piece is waiting for promotion
     */
    public boolean isWaitingForPromotion() {
        return waitingForPromotion;
    }
}
