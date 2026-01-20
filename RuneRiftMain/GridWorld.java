import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * Main world that uses a 2d array to create the board 
 * using the coding techniques mentioned in class
 * 
 * @author Owen, Joe, Saiful
 */

public class GridWorld extends World
{
    // size of the world
    public static final int SIZE = 60;
    public static final int CELLS_WIDE = 8;
    public static final int CELLS_TALL = 8;
    public static final int gridWidth = 480;
    public static final int gridHeight = 480;
    
    // elixir bars
    private ElixirBar elixirBarWhite;
    private ElixirBar elixirBarBlack;

    // turn manager
    private TurnManager turnManager;

    // game timer
    private GameTimer whiteTimer;
    private GameTimer blackTimer;

    // ability buttons
    private Button blackAbilityButton;
    private Button whiteAbilityButton;
    
    // list of bombs in the world
    private List<Bomb> bombs;
    
    // global piece lists
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;
    
    // Game settings loaded from configuration
    private int elixirMultiplier;
    private int timeLimitSeconds;

    // elixir cost display
    private AbilityCostDisplay whiteCostDisplay;
    private AbilityCostDisplay blackCostDisplay;

    private Block[][] blockGrid;
    private Piece selectedPiece;
    private boolean promotionMenuActive;
    
    // en passant tracking - stores the block that can be captured via en passant
    private Block enPassantTarget;
    private Piece enPassantVulnerablePawn;
    
    // Fade-in effect
    private FadeOverlay fadeOverlay;
    private boolean gameStarted = false;
    
    /**
     * Constructor for objects of class MyWorld.
     * Loads settings from GameSettings configuration.
     */
    public GridWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 600, 1); 
        
        // Load game settings from configuration
        loadGameSettings();
        
        blockGrid = new Block[CELLS_TALL][CELLS_WIDE];
        layoutGrid();
        
        //endTurnButton = new EndTurnButton();
        //addObject(endTurnButton, 300, 300);
        
        // add elixir bars (start with 0 elixir - gain is at end of turn)
        elixirBarWhite = new ElixirBar();
        addObject(elixirBarWhite, 120, 575);
        elixirBarBlack = new ElixirBar();
        addObject(elixirBarBlack, 120, 25);
        
        // Create separate timers with loaded time limit
        whiteTimer = new GameTimer("WHITE", timeLimitSeconds);
        addObject(whiteTimer, 330, 575);
        blackTimer = new GameTimer("BLACK", timeLimitSeconds);
        addObject(blackTimer, 330, 25); 
        
        // Keep timers inactive until loading is complete
        whiteTimer.setActive(false);
        blackTimer.setActive(false);
        
        // adds ability button and cost displays
        blackAbilityButton = new Button("Use ability", 120, 40);
        addObject(blackAbilityButton, 500, 25);
        blackCostDisplay = new AbilityCostDisplay();
        addObject(blackCostDisplay, 580, 25);
        whiteAbilityButton = new Button("Use ability", 120, 40);
        addObject(whiteAbilityButton, 500, 575);
        whiteCostDisplay = new AbilityCostDisplay();
        addObject(whiteCostDisplay, 580, 575);
        
        // turn manager
        turnManager = new TurnManager(elixirBarWhite, elixirBarBlack, elixirMultiplier);
        
        // initialize piece and bomb lists
        bombs = new ArrayList<>();
        whitePieces = new ArrayList<>();
        blackPieces = new ArrayList<>();
        
        // white pieces
        Piece wDarkPrince1 = new Piece(Piece.PieceType.DARK_PRINCE, blockGrid[7][0], true);
        addObject(wDarkPrince1, blockGrid[7][0].getX(), blockGrid[7][0].getY());
        whitePieces.add(wDarkPrince1);
    
        Piece wDarkPrince2 = new Piece(Piece.PieceType.DARK_PRINCE, blockGrid[7][7], true);
        addObject(wDarkPrince2, blockGrid[7][7].getX(), blockGrid[7][7].getY());
        whitePieces.add(wDarkPrince2);
        
        Piece wMusketeer1 = new Piece(Piece.PieceType.MUSKETEER, blockGrid[7][2], true);
        addObject(wMusketeer1, blockGrid[7][2].getX(), blockGrid[7][2].getY());
        whitePieces.add(wMusketeer1);

        Piece wMusketeer2 = new Piece(Piece.PieceType.MUSKETEER, blockGrid[7][5], true);
        addObject(wMusketeer2, blockGrid[7][5].getX(), blockGrid[7][5].getY());
        whitePieces.add(wMusketeer2);
        
        Piece wRoyalGiant = new Piece(Piece.PieceType.ROYAL_GIANT, blockGrid[7][4], true);
        addObject(wRoyalGiant, blockGrid[7][4].getX(), blockGrid[7][4].getY());
        whitePieces.add(wRoyalGiant);
        
        Piece wWitch = new Piece(Piece.PieceType.WITCH, blockGrid[7][3], true);
        addObject(wWitch, blockGrid[7][3].getX(), blockGrid[7][3].getY());
        whitePieces.add(wWitch);
        
        Piece wKnight1 = new Piece(Piece.PieceType.KNIGHT, blockGrid[7][1], true);
        addObject(wKnight1, blockGrid[7][1].getX(), blockGrid[7][1].getY());
        whitePieces.add(wKnight1);

        Piece wKnight2 = new Piece(Piece.PieceType.KNIGHT, blockGrid[7][6], true);
        addObject(wKnight2, blockGrid[7][6].getX(), blockGrid[7][6].getY());
        whitePieces.add(wKnight2);

        for (int i = 0; i < 8; i++) {
            Piece recruit = new Piece(Piece.PieceType.ROYAL_RECRUITS, blockGrid[6][i], true);
            addObject(recruit, blockGrid[6][i].getX(), blockGrid[6][i].getY());
            whitePieces.add(recruit);
        }
        
        // black pieces
        Piece bDarkPrince1 = new Piece(Piece.PieceType.DARK_PRINCE, blockGrid[0][0], false);
        addObject(bDarkPrince1, blockGrid[0][0].getX(), blockGrid[0][0].getY());
        blackPieces.add(bDarkPrince1);
        
        Piece bDarkPrince2 = new Piece(Piece.PieceType.DARK_PRINCE, blockGrid[0][7], false);
        addObject(bDarkPrince2, blockGrid[0][7].getX(), blockGrid[0][0].getY());
        blackPieces.add(bDarkPrince2);
        
        Piece bMusketeer1 = new Piece(Piece.PieceType.MUSKETEER, blockGrid[0][2], false);
        addObject(bMusketeer1, blockGrid[0][2].getX(), blockGrid[0][2].getY());
        blackPieces.add(bMusketeer1);

        Piece bMusketeer2 = new Piece(Piece.PieceType.MUSKETEER, blockGrid[0][5], false);
        addObject(bMusketeer2, blockGrid[0][5].getX(), blockGrid[0][5].getY());
        blackPieces.add(bMusketeer2);
        
        Piece bRoyalGiant = new Piece(Piece.PieceType.ROYAL_GIANT, blockGrid[0][4], false);
        addObject(bRoyalGiant, blockGrid[0][4].getX(), blockGrid[0][4].getY());
        blackPieces.add(bRoyalGiant);
        
        Piece bWitch = new Piece(Piece.PieceType.WITCH, blockGrid[0][3], false);
        addObject(bWitch, blockGrid[0][3].getX(), blockGrid[0][3].getY());
        blackPieces.add(bWitch);
        
        Piece bKnight1 = new Piece(Piece.PieceType.KNIGHT, blockGrid[0][1], false);
        addObject(bKnight1, blockGrid[0][1].getX(), blockGrid[0][1].getY());
        blackPieces.add(bKnight1);
        
        Piece bKnight2 = new Piece(Piece.PieceType.KNIGHT, blockGrid[0][6], false);
        addObject(bKnight2, blockGrid[0][6].getX(), blockGrid[0][6].getY());
        blackPieces.add(bKnight2);
        
        for (int i = 0; i < 8; i++) {
            Piece recruit = new Piece(Piece.PieceType.ROYAL_RECRUITS, blockGrid[1][i], false);
            addObject(recruit, blockGrid[1][i].getX(), blockGrid[1][i].getY());
            blackPieces.add(recruit);
        }
        
        // Add fade overlay on top of everything and play start sound
        fadeOverlay = new FadeOverlay(600, 600);
        addObject(fadeOverlay, 300, 300);
        SoundManager.getInstance().playStart();
    }
    
    /**
     * Act method - checks if loading is complete to start the game
     */
    public void act()
    {
        // Start the game once fade animation is complete
        if (!gameStarted && fadeOverlay != null && fadeOverlay.isFadeComplete())
        {
            gameStarted = true;
            whiteTimer.setActive(true); // White goes first
        }
    }
    
    /**
     * get the turn manager
     * @return TurnManager the TurnManager instance
     */
    public TurnManager getTurnManager()
    {
        return turnManager;
    }

    /**
     * check if the game has ended due to win/loss/draw conditions
     */
    public void checkIfGameEnd() {
        int whiteCount = whitePieces.size();
        int blackCount = blackPieces.size();
        
        // check win/loss conditions
        if (whiteCount == 0 || blackCount == 0) {
            String winner = whiteCount > 0 ? "WHITE" : "BLACK";
            endGame(winner + " wins!");
        }
        // draw condition: both players have only 1 piece remaining
        else if (whiteCount == 1 && blackCount == 1) {
            endGame("Draw! Both players have 1 piece remaining.");
        }
    }
    
    /**
     * remove a piece from the appropriate piece list
     * @param piece the piece to remove
     */
    public void removePieceFromList(Piece piece) {
        if (piece.checkIsWhite()) {
            whitePieces.remove(piece);
        } 
        else {
            blackPieces.remove(piece);
        }
        //System.out.println("Piece removed. White: " + whitePieces.size() + ", Black: " + blackPieces.size());
        checkIfGameEnd();
    }
    
    /**
     * end the game with a message
     * @param message the message to display
     */
    private void endGame(String message) {
        System.out.println("Game Over! " + message);
        whiteTimer.setActive(false);
        blackTimer.setActive(false);
        Greenfoot.stop();
    }
    
    /**
     * End the current player's turn, switch to the next player,
     * progress bomb explosions, and check for game end conditions.
     * clears en passant if applicable.
     */
    public void endTurn(){
        if (enPassantVulnerablePawn != null) {
            String currentPlayer = turnManager.getCurrentPlayer();
            boolean enPassantPawnIsWhite = enPassantVulnerablePawn.checkIsWhite();

            if ((currentPlayer.equals("WHITE") && !enPassantPawnIsWhite) ||
                (currentPlayer.equals("BLACK") && enPassantPawnIsWhite)) {
                clearEnPassant();
            }
        }
        
        turnManager.nextTurn();
        progressBombExplosions();

        checkIfGameEnd();
        
        // hide ability cost displays when turn ends
        whiteCostDisplay.hide();
        blackCostDisplay.hide();
        
        // switch which timer is active based on current player
        String currentPlayer = turnManager.getCurrentPlayer();
        whiteTimer.setActive(currentPlayer.equals("WHITE"));
        blackTimer.setActive(currentPlayer.equals("BLACK"));
    }
    
    /**
     * Set the en passant target block and vulnerable pawn.
     * Called when a pawn moves two squares forward.
     */
    public void setEnPassant(Block target, Piece pawn) {
        this.enPassantTarget = target;
        this.enPassantVulnerablePawn = pawn;
        //System.out.println("[En Passant] Target set, pawn is " + (pawn.checkIsWhite() ? "WHITE" : "BLACK"));
    }
    
    /**
     * Clear the en passant opportunity.
     */
    public void clearEnPassant() {
        this.enPassantTarget = null;
        this.enPassantVulnerablePawn = null;
    }
    
    /**
     * Get the current en passant target block.
     * 
     * @return Block the block that is the target of en passant
     */
    public Block getEnPassantTarget() {
        return enPassantTarget;
    }
    
    /**
     * Get the pawn that is vulnerable to en passant capture.
     * 
     * @return Piece the pawn that is vulnerable to en passant
     */
    public Piece getEnPassantVulnerablePawn() {
        return enPassantVulnerablePawn;
    }
    
    /**
     * Layout the grid by creating Block objects and adding them to the world.
     */
    private void layoutGrid() {
        for (int i = 0; i < blockGrid.length; i++){
            for (int j = 0; j < blockGrid[i].length; j++){
                int worldX = (getWidth() - gridWidth)/2 + j * SIZE + (SIZE / 2);
                int worldY = (getHeight() - gridHeight)/2 + i * SIZE + (SIZE / 2);
                
                blockGrid[i][j] = new Block (i, j, worldX, worldY);
                addObject(blockGrid[i][j], worldX, worldY);
            }
        }
    }
    
    /**
     * convert world coordinates to the corresponding Block position.
     * @param worldX the x-coordinate in the world.
     * @param worldY the y-coordinate in the world.
     * @return Block the block at the given world coordinates, or null if out of bounds.
     */
    public Block worldToBlockPos(int worldX, int worldY) {
        if (worldX > 540 || worldX < 60 || worldY > 540 || worldY < 60) return null;
        
        int startX = (getWidth() - gridWidth) / 2;
        int startY = (getHeight() - gridHeight) / 2;
    
        int col = (worldX - startX) / SIZE;
        int row = (worldY - startY) / SIZE;
    
        if (row < 0 || row >= CELLS_TALL || col < 0 || col >= CELLS_WIDE) return null;

        return blockGrid[row][col];
    }
    
    /**
     * Add a bomb to the world at the specified location.
     * @param location the block where the bomb will be placed.
     * @param isWhite true if the bomb belongs to the white player, false for black.
     */
    public void addBomb(Block location, boolean isWhite) {
        Bomb bomb = new Bomb(location, isWhite);
        addObject(bomb, location.getX(), location.getY());
        bombs.add(bomb);
    }
    
    /**
     * Progress the explosion timers for all bombs in the world.
     */
    private void progressBombExplosions() {
        for (Bomb b : bombs) {
            b.progressExplosion();
        }
    }
    
    /**
     * Get the block at the specified grid coordinates.
     * @param x the row index.
     * @param y the column index.
     * @return Block the Block at the specified coordinates, or null if out of bounds.
     */
    public Block getBlock(int x, int y) {
        if (x >= CELLS_TALL || y >= CELLS_WIDE || x < 0 || y < 0) return null;
        return blockGrid[x][y];
    }
    
    /**
     * get the currently selected piece
     * @return Piece the selected Piece, or null if none is selected.
     */
    public Piece getSelectedPiece() {
        return selectedPiece;
    }
    
    /**
     * check if the ability button for the specified player was clicked
     * @param isWhite true for white player, false for black player
     * @return boolean true if the button was clicked, false otherwise
     */
    public boolean isButtonClicked(boolean isWhite) {
        return isWhite ? whiteAbilityButton.wasClicked() : blackAbilityButton.wasClicked();
    }
    
    /**
     * get the current elixir amount for the specified player
     * @param isWhite true for white player, false for black player
     * @return int the current elixir amount
     */
    public int getElixir(boolean isWhite) {
        return isWhite ? elixirBarWhite.getElixir() : elixirBarBlack.getElixir();
    }
    
    /**
     * remove elixir from the specified player
     * @param isWhite true for white player, false for black player
     */
    public void removeElixir(boolean isWhite, int amount) {
        if (isWhite) elixirBarWhite.removeElixir(amount);  
        else elixirBarBlack.removeElixir(amount);
    }
    
    /**
     * set the currently selected piece
     * @param piece the Piece to select, or null to deselect
     */
    public void setSelectedPiece(Piece piece) {
        if (selectedPiece != null && selectedPiece != piece) {
            selectedPiece.deselect();
        }
        selectedPiece = piece;
        
        // hide or show ability cost
        whiteCostDisplay.hide();
        blackCostDisplay.hide();
        
        if (piece != null)
        {
            if (piece.checkIsWhite()) whiteCostDisplay.showCost(piece.getAbilityCost());
            else blackCostDisplay.showCost(piece.getAbilityCost()); 
        }
    }
    
    /**
     * load game settings from configuration file.
     * falls back to defaults if settings are missing.
     */
    private void loadGameSettings()
    {
        GameSettings settings = GameSettings.getInstance();
        elixirMultiplier = settings.getElixirMultiplier();
        timeLimitSeconds = settings.getTimeSeconds();
        
        // Log loaded settings for debugging
        // System.out.println("Game starting with settings: " + settings);
    }
    
    /**
     * get the current elixir multiplier setting
     * @return int the elixir multiplier
     */
    public int getElixirMultiplier()
    {
        return elixirMultiplier;
    }
    
    /**
     * get the time limit in seconds
     * @return int the time limit in seconds
     */
    public int getTimeLimitSeconds()
    {
        return timeLimitSeconds;
    }
    
    /**
     * Get the list of white pieces
     * @return List<piece> the list of white pieces
     */
    public List<Piece> getWhitePieces()
    {
        return whitePieces;
    }
    
    /**
     * Get the list of black pieces
     * 
     * @return List<piece> the list of all pieces black has
     */
    public List<Piece> getBlackPieces()
    {
        return blackPieces;
    }
    
    /**
     * Check if a promotion menu is currently active
     * 
     * @return boolean true if the promotion menu is active, false if it is not
     */
    public boolean isPromotionMenuActive()
    {
        return promotionMenuActive;
    }
    
    /**
     * Set whether a promotion menu is active
     * @param active whether or not the promotion menu should be active
     */
    public void setPromotionMenuActive(boolean active)
    {
        this.promotionMenuActive = active;
    }
    
    /**
     * Show the promotion menu for a piece that reached the end of the board
     * @param piece the Piece to promote
     */
    public void showPromotionMenu(Piece piece)
    {
        promotionMenuActive = true;
        int elixir = getElixir(piece.checkIsWhite());
        PromotionMenu menu = new PromotionMenu(piece, elixir);
        addObject(menu, 300, 300);
    }
}









