import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * Piece class
 * Represents a piece on the board
 * handles ability usage, movement, and piece taking
 * 
 * all images are from clash royale
 * 
 * @author Joe Zhuo
 * @version 1/13/2026
 * 
 * Modified by Saiful Shaik
 */
public class Piece extends Actor
{
    // piece types
    public static enum PieceType { 
        DARK_PRINCE, 
        KNIGHT, 
        MUSKETEER, 
        ROYAL_GIANT, 
        SKELETON, 
        WITCH, 
        ROYAL_RECRUITS 
    }
    
    // size of piece in pixels
    public static final int size = 60;

    // type of piece
    private PieceType type;
    private boolean isWhite;
    
    // location 
    private Block currentBlock;
    
    // piece selection
    private boolean isSelected;
    
    // used for checking if can castle
    private boolean hasMoved;
    
    // things related to abilities
    private boolean abilityUsed;
    private int abilityState;
    private int abilityCost;
    
    // tracks if a pawn (recruit) is at promotion row but canceled promotion
    private boolean waitingForPromotion;  

    // tracks highlighted blocks
    private List<Block> highlightedBlocks = new ArrayList<>();
    
    /**
     * Constructor for a Piece object
     * 
     * @param type the type of piece
     * @param block starting block of the piece
     * @param isWhite whether the piece is white (false means the piece is black)
     */
    public Piece(PieceType type, Block block, boolean isWhite) {
        this.type = type;
        this.currentBlock = block;
        this.isWhite = isWhite;
        
        // sets image and ability cost based on piece type
        setAbilityCost();
        setImage(type, isWhite);

        // moves to the starting block
        moveTo(currentBlock);
        updateHitbox();

        // has not moved yet
        hasMoved = false;  
    }

    /**
     * utility method to set the ability cost based on piece type
     */
    private void setAbilityCost() {
        switch(type) {
            case DARK_PRINCE: abilityCost = 5; break;
            case KNIGHT: abilityCost = 3; break;
            case MUSKETEER: abilityCost = 3; break;
            case ROYAL_GIANT: abilityCost = 8; break;
            case WITCH: abilityCost = 4; break;
            case ROYAL_RECRUITS: abilityCost = 1; break;
            case SKELETON: abilityCost = 0; break;
            default: abilityCost = 0; break;
        }
    }
    
    /**
     * check if it is currently this piece's turn
     * @return true if it is this piece's turn, false otherwise
     */
    private boolean isMyTurn()
    {
        // gets the world's turn manager
        GridWorld world = (GridWorld) getWorld();
        TurnManager tm = world.getTurnManager();
    
        // checks if it is the piece's turn based on color
        return isWhite ? tm.isPlayerTurn("WHITE") : tm.isPlayerTurn("BLACK");
    }
    
    /**
     * move this piece to a specified target block
     * @param target the block to move to
     */
    private void moveTo(Block target) {
        boolean hadPiece = target.currentPiece() != null;
        GridWorld gw = (GridWorld) getWorld();
        
        // handle en passant capture
        if (type == PieceType.ROYAL_RECRUITS && gw != null) {
            Block enPassantTarget = gw.getEnPassantTarget();
            if (enPassantTarget != null && target == enPassantTarget) {
                Piece capturedPawn = gw.getEnPassantVulnerablePawn();
                if (capturedPawn != null && capturedPawn.getCurrentBlock() != null) {
                    capturedPawn.getCurrentBlock().removePiece(true);
                }
            }
        }
        
        // remove any piece on the target block
        target.removePiece(true);
        
        // track two-square pawn moves for en passant
        if (type == PieceType.ROYAL_RECRUITS && currentBlock != null && gw != null) {
            int dx = target.getBoardX() - currentBlock.getBoardX();
            if (Math.abs(dx) == 2) {
                int direction = isWhite ? -1 : 1;
                Block enPassantSquare = gw.getBlock(currentBlock.getBoardX() + direction, currentBlock.getBoardY());
                gw.setEnPassant(enPassantSquare, this);
                // System.out.println("[En Passant] Set target at (" + enPassantSquare.getBoardX() + ", " + enPassantSquare.getBoardY() + ") for " + (isWhite ? "WHITE" : "BLACK") + " pawn");
            }
        }
        
        // remove the piece from the current block
        if (currentBlock != null) currentBlock.setPiece(null);
        
        // royal recruit s spear strike effect if its ability is used
        if (type == PieceType.ROYAL_RECRUITS && abilityState == 1 && getWorld() != null) {
            SpearStrikeEffect spear = new SpearStrikeEffect(target.getX(), target.getY(), isWhite);
            getWorld().addObject(spear, target.getX(), target.getY());
            abilityState = 0;
        }
        
        // Only play move sound if game has started (not during initial setup)
        if (gw != null && gw.isGameStarted()) {
            SoundManager.getInstance().playMove();
        }
        
        setLocation(target.getX(), target.getY());

        // updates current block
        currentBlock = target;

        // set this piece on the target block
        target.setPiece(this);

        // mark as moved
        hasMoved = true;

        // dark prince splash damage effect
        if (type == PieceType.DARK_PRINCE && abilityState == 1 && getWorld ()!= null && hadPiece) {
            getWorld().addObject(new ChargeEffect(currentBlock.getX(), currentBlock.getY()), currentBlock.getX(), currentBlock.getY());
            dealSplashDamage();
        }
        
        // clear highlights after moving
        clearHighlights();
    }
    
    /**
     * deal splash damage to adjacent pieces in a + shape
     * used by Dark Prince ability
     */
    private void dealSplashDamage() {
        GridWorld gw = (GridWorld) getWorld();
            
        int x = currentBlock.getBoardX();
        int y = currentBlock.getBoardY();
        
        Block block1 = gw.getBlock(x+1, y);
        if (block1 != null && block1.currentPiece() != null) {
            if (block1.currentPiece().checkIsWhite() != isWhite) {
                block1.removePiece(true);
            }
        }
        Block block2 = gw.getBlock(x-1, y);
        if (block2 != null && block2.currentPiece() != null) {
            if (block2.currentPiece().checkIsWhite() != isWhite) {
                block2.removePiece(true);
            }
        }
        Block block3 = gw.getBlock(x, y+1);
        if (block3 != null && block3.currentPiece() != null) {
            if (block3.currentPiece().checkIsWhite() != isWhite) {
                block3.removePiece(true);
            }
        }
        Block block4 = gw.getBlock(x, y-1);
        if (block4 != null && block4.currentPiece() != null) {
            if (block4.currentPiece().checkIsWhite() != isWhite) {
                block4.removePiece(true);
            }
        }

        // reset ability state
        abilityState = 0;
    }
    
    /**
     * spawn skeleton pieces in adjacent blocks
     * used by Witch ability
     */
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
    
    /**
     * check if a move to the target block is valid
     * @param targetBlock
     * @return true if the move is valid, false otherwise
     */
    private boolean checkIfMoveIsValid(Block targetBlock) {
        // checks for a piece on the target block
        Piece pieceOnTarget = targetBlock.currentPiece();
        
        // cannot move if there is a piece of the same color on the target block
        if (pieceOnTarget != null && isWhite == pieceOnTarget.checkIsWhite()) return false;
        
        // current location
        int x = currentBlock.getBoardX();
        int y = currentBlock.getBoardY();
        
        // target location
        int targetX = targetBlock.getBoardX();
        int targetY = targetBlock.getBoardY();
        
        // distance between current and target
        int dx = targetX - x; 
        int dy = targetY - y; 
        
        // direction multiplier for forward movement
        int direction = isWhite ? -1 : 1;
    
        switch (type) {
            case ROYAL_RECRUITS:
                if (pieceOnTarget == null) {
                    if (dy != 0) {
                        // check for en passant capture
                        if (dx == direction && Math.abs(dy) == 1) {
                            GridWorld world = (GridWorld) getWorld();
                            Block enPassantTarget = world.getEnPassantTarget();
                            if (enPassantTarget != null && targetBlock == enPassantTarget) {
                                return true;
                            }
                        }
                        return false;
                    }
                    if (dx == direction) return true;
                    // first move can move two squares
                    if (!hasMoved && dx == 2 * direction) {
                        GridWorld world = (GridWorld) getWorld();
                        Block intermediate = world.getBlock(x + direction, y);
                        return intermediate.currentPiece() == null;
                    }
                    return false;
                } 
                else {
                    if (dx == direction && Math.abs(dy) == 1) return true;
                    return dx == direction && dy == 0 && abilityUsed;
                }
    
            case DARK_PRINCE:
                if (dx != 0 && dy != 0) return false; // must be straight line
                return isPathClear(x, y, targetX, targetY);
    
            case WITCH:
                if (dx != 0 && dy != 0 && Math.abs(dx) != Math.abs(dy)) return false; // diagonal or straight
                return isPathClear(x, y, targetX, targetY);
    
            case SKELETON:
                if (Math.abs(dx) > 1 || Math.abs(dy) > 1) return false;
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
                    
                    // check for castling
                    if (rookBlock != null) {
                        Piece potentialRook = rookBlock.currentPiece();
            
                        if (potentialRook != null && 
                            potentialRook.getType() == PieceType.DARK_PRINCE && 
                            !potentialRook.checkHasMoved() && 
                            isPathClear(x, y, x, rookY)) {
                            // System.out.println("[King] Can Castle");
                            return true;
                        }
                    }
                }
                // default movement
                if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) return true;
                return false;
    
            case KNIGHT:
                return (Math.abs(dx) == 2 && Math.abs(dy) == 1) || 
                       (Math.abs(dx) == 1 && Math.abs(dy) == 2);
    
            case MUSKETEER:
                if (Math.abs(dx) != Math.abs(dy)) return false;
                return isPathClear(x, y, targetX, targetY);
        }
        return false;
    }
    
    /**
     * Get the type of this piece
     * @return the PieceType of this piece
     */
    public PieceType getType() {
        return type;
    }
    
    /**
     * Check if this piece has moved
     * @return true if the piece has moved, false otherwise
     */
    public boolean checkHasMoved() {
        return hasMoved;
    }
    
    /**
     * check if the path between two blocks is clear (no pieces in between)
     * @param startX starting block x
     * @param startY starting block y
     * @param endX ending block x
     * @param endY ending block y
     * @return true if the path is clear, false otherwise
     */
    private boolean isPathClear(int startX, int startY, int endX, int endY) {
        GridWorld world = (GridWorld) getWorld();
        
        int dx = Integer.compare(endX, startX); 
        int dy = Integer.compare(endY, startY); 
    
        int x = startX + dx;
        int y = startY + dy;
        
        // check if any blocks in between the start and end are occupied
        while (x != endX || y != endY) {
            Block block = world.getBlock(x, y);
            if (block.currentPiece() != null) return false;
            x += dx;
            y += dy;
        }
        return true;
    }
    
    /**
     * Perform the snipe ability 
     * used by the musketeer
     */
    private void snipe() {
        int x = currentBlock.getBoardX();
        int y = currentBlock.getBoardY();
        
        int direction = isWhite ? -1 : 1;
        
        GridWorld gw = (GridWorld) getWorld();
        
        int step = isWhite ? -1 : 1;
        
        int cx = x + step;

        // keep going until hitting a piece or the edge of the board
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

    /**
     * handle piece movement based on mouse clicks
     */
    private void move() {        
        // cannot move if no mouse click
        if (!Greenfoot.mouseClicked(null)) return;
        
        GridWorld gw = (GridWorld) getWorld();
        
        // cannot move if game hasn't started (still loading/fading)
        if (!gw.isGameStarted()) return;

        // cannot move if not this piece's turn
        if (!isMyTurn()) return;

        // cannot move if promotion menu is active
        if (gw.isPromotionMenuActive()) return;
        
        // cannot move if royal giant ability is active and waiting for target
        if (type == PieceType.ROYAL_GIANT && abilityState == 1) {
            if (!Greenfoot.mouseClicked(null)) return;
        }
        
        // gets the mouse info
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;
        
        int mouseX = mouse.getX();
        int mouseY = mouse.getY();
        
        // gets the currently selected block from the world location
        Block selectedBlock = ((GridWorld) getWorld()).worldToBlockPos(mouseX, mouseY);
        if (selectedBlock == null) return;
        
        // logic if user clicks on the piece
        if (selectedBlock == currentBlock) {
            GridWorld world = (GridWorld)getWorld();
            // deselect if already selected
            if (isSelected) {
                deselect();
                world.setSelectedPiece(null);
            } 
            // select if not already selected
            else {
                SoundManager.getInstance().playSelect();
                world.setSelectedPiece(this);
                isSelected = true;

                // updates hitbox and shows available moves
                updateHitbox();
                showPossibleMoves();
            }
        }
        // logic if user clicks on a different block when piece is selected
        else if (isSelected) {
            // handle royal giant explosion ability
            if (type == PieceType.ROYAL_GIANT && abilityState == 1) {
                queueRoyalGiantExplosion(selectedBlock);
                endTurn();
                return;
            }
            // check if the move is valid
            else if (checkIfMoveIsValid(selectedBlock)) {
                // castling logic for royal giant (king)
                if (type == PieceType.ROYAL_GIANT && Math.abs(selectedBlock.getBoardY() - currentBlock.getBoardY()) == 2) {
                    executeCastling(selectedBlock);
                    endTurn();
                }
                // move to target block
                else {
                    moveTo(selectedBlock);
                    
                    // show promotion menu if applicable
                    if (canPromote()) {
                        gw.showPromotionMenu(this);
                        deselect();
                    } 
                    // end the turn
                    else endTurn();
                }
            }
        }
    }
    
    /**
     * execute castling move
     * @param targetBlock the block the king is moving to
     */
    private void executeCastling(Block targetBlock) {
        GridWorld gw = (GridWorld) getWorld();
        
        // gets king and rook original and target positions
        int row = currentBlock.getBoardX();
        int kingOrigY = currentBlock.getBoardY();
        int kingTargY = targetBlock.getBoardY();
        
        int rookOrigY = (kingTargY > kingOrigY) ? 7 : 0;
        int rookTargY = (kingTargY > kingOrigY) ? kingTargY - 1 : kingTargY + 1;

        // move to target block
        moveTo(targetBlock);
    
        Block rookOrigBlock = gw.getBlock(row, rookOrigY);
        Block rookTargBlock = gw.getBlock(row, rookTargY);
        Piece rook = rookOrigBlock.currentPiece();
        
        if (rook != null) {
            // remove rook from starting block
            rookOrigBlock.setPiece(null); 
            
            // move rook
            rook.setLocation(rookTargBlock.getX(), rookTargBlock.getY());

            // update rook's current block
            rook.setCurrentBlock(rookTargBlock);

            // set rook on target block
            rookTargBlock.setPiece(rook);

            // rook cannot castle again
            rook.hasMoved = true;
        }
    }

    /**
     * set the current block of this piece
     * @param block the block to set as current
     */
    public void setCurrentBlock(Block block) {
        this.currentBlock = block;
    }

    /**
     * queue the royal giant explosion ability
     * @param target the block to target for the explosion
     */
    private void queueRoyalGiantExplosion(Block target) {
        // reset ability state
        abilityState = 0;

        GridWorld gw = (GridWorld) getWorld();
        // add bomb to the target block
        gw.addBomb(target, isWhite);
        
        // clear the highlight block
        clearHighlights();
    }
    
    /**
     * end the turn for this piece's color
     */
    public void endTurn() {
        ((GridWorld) getWorld()).endTurn();

        // reset ability flag
        abilityUsed = false;

        // deselect the piece
        deselect();
    }
    
    /**
     * show possible moves by highlighting valid blocks
     */
    private void showPossibleMoves() {
        GridWorld world = (GridWorld) getWorld();
        
        // first clear any existing highlights
        clearHighlights(); 
        
        // iterate through all blocks to check for valid moves
        for (int x = 0; x < GridWorld.CELLS_TALL; x++) {
            for (int y = 0; y < GridWorld.CELLS_WIDE; y++) {
                Block block = world.getBlock(x, y);

                // safety check
                if (block == null ) continue;

                // all blocks can be targeted by royal giant ability
                if (type == PieceType.ROYAL_GIANT && abilityState == 1) {
                    block.highlight(Color.ORANGE);
                    highlightedBlocks.add(block);
                }

                // highlight valid move blocks
                else if (checkIfMoveIsValid(block)) {
                    Piece pieceOnTarget = block.currentPiece();
                    if (pieceOnTarget != null && pieceOnTarget.checkIsWhite() != this.isWhite) {
                        if (abilityState == 1 && type == PieceType.DARK_PRINCE) {
                            block.highlight(Color.ORANGE);
                        }
                        else block.highlight(Color.RED);
                    } 
                    else {
                        // Check if this is an en passant capture (empty square but valid diagonal)
                        Block enPassantTarget = world.getEnPassantTarget();
                        if (type == PieceType.ROYAL_RECRUITS && enPassantTarget != null && block == enPassantTarget) {
                            block.highlight(Color.RED); // Highlight en passant as a capture
                        } else {
                            block.highlight(Color.GREEN); 
                        }
                    }
                    // add to highlighted blocks list
                    highlightedBlocks.add(block);
                }
            }
        }
    }

    /**
     * clear all highlighted blocks
     */
    private void clearHighlights() {
        for (Block block : highlightedBlocks) {
            block.clearHighlight(); 
        }
        highlightedBlocks.clear();
    }
    
    /**
     * deselect this piece and clear highlights
     */
    public void deselect() {
        isSelected = false;
        clearHighlights();
        updateHitbox();
    }
    
    /**
     * set the image of the piece based on its type and color   
     * @param type type of piece
     * @param isWhite whether the piece is white
     */
    private void setImage(PieceType type, boolean isWhite) {
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
        // scale to size
        getImage().scale(size, size);
    }
    
    /**
     * update the hitbox color based on selection and ability state
     */
    private void updateHitbox() {
        // orange if dark prince splash
        Color color = isSelected ? Color.GREEN : Color.RED;
        if (isSelected && abilityState == 1 && type == PieceType.DARK_PRINCE) {
            color = Color.ORANGE;
        }

        GreenfootImage img = getImage();

        // copy to avoid scaling issues
        GreenfootImage hitboxImg = new GreenfootImage(img); 

        // draw hitbox
        hitboxImg.setColor(color);
        hitboxImg.drawRect(0, 0, img.getWidth()-1, img.getHeight()-1);

        // update image
        setImage(hitboxImg);
    }

    private void slash(GridWorld gw, int x, int y, int direction) {
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
    }

    /**
     * use this piece's ability
     */
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
                slash(gw, x, y, direction);
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

                // add snipe animation bullet
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
                // if waiting for promotion, reopen the promotion menu
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
    
    /**
     * clears the current block of this piece
     */
    public void clearBlock() {
        currentBlock = null;
        isSelected = false;
        clearHighlights();
    }
    
    /**
     * remove this piece from the global list of pieces
     */
    public void removePieceFromList() {
        GridWorld gw = (GridWorld) getWorld();
        if (gw != null) {
            gw.removePieceFromList(this);
        }
    }
    
    /**
     * check if this piece is white
     * @return true if white, false if black
     */
    public boolean checkIsWhite() {
        return isWhite;
    }
    
    /**
     * get the current block this piece is on
     */
    public Block getCurrentBlock() {
        return currentBlock;
    }
    
    /**
     * check if the pawn can promote
     * @return true if can promote, false otherwise
     */
    private boolean canPromote() {
        if (type != PieceType.ROYAL_RECRUITS) return false;
        if (currentBlock == null) return false;
        
        int row = currentBlock.getBoardX();
        
        return (isWhite && row == 0) || (!isWhite && row == 7);
    }
    
    /**
     * main act method called by Greenfoot framework
     */
    public void act()
    {
        GridWorld gw = (GridWorld) getWorld();
        
        // use ability if clicked
        if (isSelected && (!abilityUsed || abilityState == 0) && 
            gw.isButtonClicked(isWhite) && 
            gw.getElixir(isWhite) >= abilityCost) 
                useAbility();
        
        
        // handle movement
        move();
    }
    
    /**
     * get the ability cost of this piece
     * @return the ability cost
     */
    public int getAbilityCost()
    {
        return abilityCost;
    }
    
    /**
     * set whether this piece is waiting for promotion
     */
    public void setWaitingForPromotion(boolean waiting) {
        this.waitingForPromotion = waiting;
    }
    
    /**
     * check if this piece is waiting for promotion
     */
    public boolean isWaitingForPromotion() {
        return waitingForPromotion;
    }
}
