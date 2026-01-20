import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
import java.util.ArrayList;

/**
 * Main world that uses a 2d array to create the board 
 * using the coding techniques mentioned in class
 * 
 * @author
 * @version
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
    
    public TurnManager getTurnManager()
    {
        return turnManager;
    }

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
    
    private void endGame(String message) {
        System.out.println("Game Over! " + message);
        whiteTimer.setActive(false);
        blackTimer.setActive(false);
        Greenfoot.stop();
    }
    
    public void endTurn(){
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
    
    public void addBomb(Block location, boolean isWhite) {
        Bomb bomb = new Bomb(location, isWhite);
        addObject(bomb, location.getX(), location.getY());
        bombs.add(bomb);
    }
    
    private void progressBombExplosions() {
        for (Bomb b : bombs) {
            b.progressExplosion();
        }
    }
    
    public Block getBlock(int x, int y) {
        if (x >= CELLS_TALL || y >= CELLS_WIDE || x < 0 || y < 0) return null;
        return blockGrid[x][y];
    }
    
    public Piece getSelectedPiece() {
        return selectedPiece;
    }
    
    public boolean isButtonClicked(boolean isWhite) {
        return isWhite ? whiteAbilityButton.wasClicked() : blackAbilityButton.wasClicked();
    }
    
    public int getElixir(boolean isWhite) {
        return isWhite ? elixirBarWhite.getElixir() : elixirBarBlack.getElixir();
    }
    
    public void removeElixir(boolean isWhite, int amount) {
        if (isWhite) elixirBarWhite.removeElixir(amount);
        else elixirBarBlack.removeElixir(amount);
    }
    
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
     */
    public int getElixirMultiplier()
    {
        return elixirMultiplier;
    }
    
    /**
     * Get the time limit in seconds
     */
    public int getTimeLimitSeconds()
    {
        return timeLimitSeconds;
    }
    
    /**
     * Get the list of white pieces
     */
    public List<Piece> getWhitePieces()
    {
        return whitePieces;
    }
    
    /**
     * Get the list of black pieces
     */
    public List<Piece> getBlackPieces()
    {
        return blackPieces;
    }
    
    /**
     * Check if a promotion menu is currently active
     */
    public boolean isPromotionMenuActive()
    {
        return promotionMenuActive;
    }
    
    /**
     * Set whether a promotion menu is active
     */
    public void setPromotionMenuActive(boolean active)
    {
        this.promotionMenuActive = active;
    }
    
    /**
     * Show the promotion menu for a piece that reached the end
     */
    public void showPromotionMenu(Piece piece)
    {
        promotionMenuActive = true;
        int elixir = getElixir(piece.checkIsWhite());
        PromotionMenu menu = new PromotionMenu(piece, elixir);
        addObject(menu, 300, 300);
    }
}









