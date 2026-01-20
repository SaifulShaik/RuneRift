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
    public static final int SIZE = 60;
    public static final int CELLS_WIDE = 8;
    public static final int CELLS_TALL = 8;
    public static final int gridWidth = 480;
    public static final int gridHeight = 480;
    
    private ElixirBar elixirBarWhite;
    private ElixirBar elixirBarBlack;
    private TurnManager turnManager;
    private GameTimer whiteTimer;
    private GameTimer blackTimer;
    //private EndTurnButton endTurnButton;
    private Button blackAbilityButton;
    private Button whiteAbilityButton;
    
    private List<Bomb> bombs;
    
    private List<Piece> whitePieces;
    private List<Piece> blackPieces;
    
    // Game settings loaded from configuration
    private int elixirMultiplier;
    private int timeLimitSeconds;
    private AbilityCostDisplay whiteCostDisplay;
    private AbilityCostDisplay blackCostDisplay;

    private Block[][] blockGrid;
    private Piece selectedPiece;
    private boolean promotionMenuActive;
    
    // En passant tracking - stores the block that can be captured via en passant
    private Block enPassantTarget;
    private Piece enPassantVulnerablePawn;
    
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
        
        whiteTimer.setActive(true);
        blackTimer.setActive(false);
        
        blackAbilityButton = new Button("Use ability", 120, 40);
        addObject(blackAbilityButton, 500, 25);
        
        blackCostDisplay = new AbilityCostDisplay();
        addObject(blackCostDisplay, 580, 25);
        
        whiteAbilityButton = new Button("Use ability", 120, 40);
        addObject(whiteAbilityButton, 500, 575);
        
        whiteCostDisplay = new AbilityCostDisplay();
        addObject(whiteCostDisplay, 580, 575);
        
        turnManager = new TurnManager(elixirBarWhite, elixirBarBlack, elixirMultiplier);
        
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
    }
    
    /**
     * Gets the TurnManager
     *
     * @return  TurnManager
     */
    public TurnManager getTurnManager()
    {
        return turnManager;
    }

    /**
     * Checks whether or not the game ended, and displays the end message
     */
    public void checkIfGameEnd() {
        int whiteCount = whitePieces.size();
        int blackCount = blackPieces.size();
        
        if (whiteCount == 0 || blackCount == 0) {
            String winner = whiteCount > 0 ? "WHITE" : "BLACK";
            endGame(winner + " wins!");
        }
        else if (whiteCount == 1 && blackCount == 1) {
            endGame("Draw! Both players have 1 piece remaining.");
        }
    }
    
    /**
     * Takes out a piece from the list of pieces a player has
     * 
     * @param piece the piece that should be removed from the list
     */
    public void removePieceFromList(Piece piece) {
        if (piece.checkIsWhite()) {
            whitePieces.remove(piece);
        } 
        else {
            blackPieces.remove(piece);
        }
        System.out.println("Piece removed. White: " + whitePieces.size() + ", Black: " + blackPieces.size());
        checkIfGameEnd();
    }
    
    /**
     * Displays the message to indicate the game ending
     * 
     * @param message   the message that is printed
     */
    private void endGame(String message) {
        System.out.println("Game Over! " + message);
        whiteTimer.setActive(false);
        blackTimer.setActive(false);
        Greenfoot.stop();
    }
    
    /**
     * Used every time a turn ends to manage game logic
     */
    public void endTurn(){
        // Clear en passant opportunity BEFORE switching turns
        // En passant must be done immediately after opponent's two-square pawn move
        // So we clear it when the player who had the opportunity to capture ends their turn
        if (enPassantVulnerablePawn != null) {
            String currentPlayer = turnManager.getCurrentPlayer();
            boolean enPassantPawnIsWhite = enPassantVulnerablePawn.checkIsWhite();
            // If it's currently WHITE's turn and the vulnerable pawn is WHITE,
            // that means BLACK just had their chance to capture and didn't. Clear it.
            // (And vice versa)
            if ((currentPlayer.equals("WHITE") && !enPassantPawnIsWhite) ||
                (currentPlayer.equals("BLACK") && enPassantPawnIsWhite)) {
                clearEnPassant();
            }
        }
        
        turnManager.nextTurn();
        progressBombExplosions();

        checkIfGameEnd();
        
        // Hide ability cost displays when turn ends
        whiteCostDisplay.hide();
        blackCostDisplay.hide();
        
        // Switch which timer is active based on current player
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
        System.out.println("[En Passant] Target set, pawn is " + (pawn.checkIsWhite() ? "WHITE" : "BLACK"));
    }
    
    /**
     * Clear the en passant opportunity.
     */
    public void clearEnPassant() {
        if (enPassantTarget != null) {
            System.out.println("[En Passant] Cleared");
        }
        this.enPassantTarget = null;
        this.enPassantVulnerablePawn = null;
    }
    
    /**
     * Get the current en passant target block.
     */
    public Block getEnPassantTarget() {
        return enPassantTarget;
    }
    
    /**
     * Get the pawn that is vulnerable to en passant capture.
     */
    public Piece getEnPassantVulnerablePawn() {
        return enPassantVulnerablePawn;
    }
    
    /**
     * Creates the main board using 2D array
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
     * Determines the block based on coordinates in the world
     * 
     * @param worldX X  position in the world
     * @param worldY Y  position in the world
     * 
     * @return Block    provides the row and column of the block
     */
    public Block worldToBlockPos(int worldX, int worldY) {
        if (worldX > 540 || worldX < 60 || worldY > 540 || worldY < 60) return null;
        
        int startX = (getWidth() - gridWidth) / 2;
        int startY = (getHeight() - gridHeight) / 2;
    
        int col = (worldX - startX) / SIZE;
        int row = (worldY - startY) / SIZE;
    
        if (row < 0 || row >= CELLS_TALL || col < 0 || col >= CELLS_WIDE)
        {
            return null;
        }
    
        return blockGrid[row][col];
    }
    
    /**
     * Adds a bomb to a specific location on the board
     * 
     * @param location  coordinates of the block
     * @param isWhite   true if the bomb is the white players, false if it belongs to the black player
     * 
     */
    public void addBomb(Block location, boolean isWhite) {
        Bomb bomb = new Bomb(location, isWhite);
        addObject(bomb, location.getX(), location.getY());
        bombs.add(bomb);
    }
    
    /**
     * calls progress explosion for all bombs in order to lower their countdown
     */
    private void progressBombExplosions() {
        for (Bomb b : bombs) {
            b.progressExplosion();
        }
    }
    
    /**
     * Returns the block located at the specified grid coordinates
     * 
     * @param x row index
     * @param y column index
     * 
     * @return Block    the block at given coordinate, or null if no block  
     */
    public Block getBlock(int x, int y) {
        if (x >= CELLS_TALL || y >= CELLS_WIDE || x < 0 || y < 0) return null;
        return blockGrid[x][y];
    }
    
    /**
     * retrieves the piece selected 
     * 
     * @return Piece the piece that was selected
     */
    public Piece getSelectedPiece() {
        return selectedPiece;
    }
    
    /**
     * Determines whether or not the ability button was clicked
     * 
     * @param isWhite true to check the white player’s button, false for the black player
     * 
     * @return boolean true if ability button was clicked
     */
    public boolean isButtonClicked(boolean isWhite) {
        return isWhite ? whiteAbilityButton.wasClicked() : blackAbilityButton.wasClicked();
    }
    
    /**
     * Determines how much elixir a player has
     * 
     * @param isWhite true to check the white player’s elixir, false for the black player
     * 
     * @return int  how much elixir the specified player has
     */
    public int getElixir(boolean isWhite) {
        return isWhite ? elixirBarWhite.getElixir() : elixirBarBlack.getElixir();
    }
    
    /**
     * Removes elixir from a player
     * 
     * @param isWhite   true to remove white players, false for the black player
     * @param amount    the amount of elixir you want to remove
     */
    public void removeElixir(boolean isWhite, int amount) {
        if (isWhite) elixirBarWhite.removeElixir(amount);
        else elixirBarBlack.removeElixir(amount);
    }
    
    /**
     * Determines which piece is selected to display the proper information on the board
     * 
     * @param piece  the piece that is selected
     */
    public void setSelectedPiece(Piece piece) {
        if (selectedPiece != null && selectedPiece != piece) {
            selectedPiece.deselect();
        }
        selectedPiece = piece;
        
        //hide or show ability cost
        whiteCostDisplay.hide();
        blackCostDisplay.hide();
        
        if (piece != null)
        {
            if (piece.checkIsWhite())
            {
                whiteCostDisplay.showCost(piece.getAbilityCost());
            }
            else
            {
                blackCostDisplay.showCost(piece.getAbilityCost());
            }
        }
    }
    
    /**
     * Load game settings from configuration file.
     * Falls back to defaults if settings are missing.
     */
    private void loadGameSettings()
    {
        GameSettings settings = GameSettings.getInstance();
        elixirMultiplier = settings.getElixirMultiplier();
        timeLimitSeconds = settings.getTimeSeconds();
        
        // Log loaded settings for debugging
        System.out.println("Game starting with settings: " + settings);
    }
    
    /**
     * Get the current elixir multiplier setting
     * 
     * @return int the elixir multipler
     */
    public int getElixirMultiplier()
    {
        return elixirMultiplier;
    }
    
    /**
     * Get the time limit
     * 
     * @return int time limit in seconds
     */
    public int getTimeLimitSeconds()
    {
        return timeLimitSeconds;
    }
    
    /**
     * Get the list of white pieces
     * 
     * @return List<piece> the list of all pieces white has
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
     * 
     * @param active    whether or not the promotion menu should be active
     */
    public void setPromotionMenuActive(boolean active)
    {
        this.promotionMenuActive = active;
    }
    
    /**
     * Show the promotion menu for a piece that reached the end
     * 
     * @param piece the piece that has reached the end and is eligible for promotion
     */
    public void showPromotionMenu(Piece piece)
    {
        promotionMenuActive = true;
        int elixir = getElixir(piece.checkIsWhite());
        PromotionMenu menu = new PromotionMenu(piece, elixir);
        addObject(menu, 300, 300);
    }
}









