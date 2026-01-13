import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GridWorld extends World
{
    public static final int SIZE = 60;
    public static final int CELLS_WIDE = 8;
    public static final int CELLS_TALL = 8;
    private int gridWidth, gridHeight;
    
    private ElixirBar elixirBarWhite;
    private ElixirBar elixirBarBlack;
    private TurnManager turnManager;
    private GameTimer whiteTimer;
    private GameTimer blackTimer;
    private EndTurnButton endTurnButton;

    private Block[][] blockGrid;
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public GridWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 600, 1); 
        
        gridWidth = SIZE * CELLS_WIDE;
        gridHeight = SIZE * CELLS_TALL;
        
        blockGrid = new Block[CELLS_TALL][CELLS_WIDE];
        layoutGrid();
        
        endTurnButton = new EndTurnButton();
        addObject(endTurnButton, 300, 300);
        // add elixir bars
        elixirBarWhite = new ElixirBar();
        addObject(elixirBarWhite, 180, 30);
        elixirBarBlack = new ElixirBar();
        addObject(elixirBarBlack, 180, 570);
        
        // Create separate timers 
        whiteTimer = new GameTimer("WHITE", 300);
        addObject(whiteTimer, 420, 570);
        
        blackTimer = new GameTimer("BLACK", 300);
        addObject(blackTimer, 420, 30); 
        
        whiteTimer.setActive(true);
        blackTimer.setActive(false);
        
        turnManager = new TurnManager(elixirBarWhite, elixirBarBlack);
        
        // white pieces
        Piece wDarkPrince1 = new Piece(Piece.PieceType.DARK_PRINCE, blockGrid[7][0], true);
        addObject(wDarkPrince1, blockGrid[7][0].getX(), blockGrid[7][0].getY());
    
        Piece wDarkPrince2 = new Piece(Piece.PieceType.DARK_PRINCE, blockGrid[7][7], true);
        addObject(wDarkPrince2, blockGrid[7][7].getX(), blockGrid[7][7].getY());
        
        Piece wMusketeer1 = new Piece(Piece.PieceType.MUSKETEER, blockGrid[7][2], true);
        addObject(wMusketeer1, blockGrid[7][2].getX(), blockGrid[7][2].getY());

        Piece wMusketeer2 = new Piece(Piece.PieceType.MUSKETEER, blockGrid[7][5], true);
        addObject(wMusketeer2, blockGrid[7][5].getX(), blockGrid[7][5].getY());
        
        Piece wRoyalGiant = new Piece(Piece.PieceType.ROYAL_GIANT, blockGrid[7][4], true);
        addObject(wRoyalGiant, blockGrid[7][4].getX(), blockGrid[7][4].getY());
        
        Piece wWitch = new Piece(Piece.PieceType.WITCH, blockGrid[7][3], true);
        addObject(wWitch, blockGrid[7][3].getX(), blockGrid[7][3].getY());
        
        Piece wKnight1 = new Piece(Piece.PieceType.KNIGHT, blockGrid[7][1], true);
        addObject(wKnight1, blockGrid[7][1].getX(), blockGrid[7][1].getY());

        Piece wKnight2 = new Piece(Piece.PieceType.KNIGHT, blockGrid[7][6], true);
        addObject(wKnight2, blockGrid[7][6].getX(), blockGrid[7][6].getY());

        for (int i = 0; i < 8; i++) {
            Piece recruit = new Piece(Piece.PieceType.ROYAL_RECRUITS, blockGrid[6][i], true);
            addObject(recruit, blockGrid[6][i].getX(), blockGrid[6][i].getY());
        }
        
        // black pieces
        Piece bDarkPrince1 = new Piece(Piece.PieceType.DARK_PRINCE, blockGrid[0][0], false);
        addObject(bDarkPrince1, blockGrid[0][0].getX(), blockGrid[0][0].getY());
        
        Piece bDarkPrince2 = new Piece(Piece.PieceType.DARK_PRINCE, blockGrid[0][7], false);
        addObject(bDarkPrince2, blockGrid[0][7].getX(), blockGrid[0][0].getY());
        
        Piece bMusketeer1 = new Piece(Piece.PieceType.MUSKETEER, blockGrid[0][2], false);
        addObject(bMusketeer1, blockGrid[0][2].getX(), blockGrid[0][2].getY());

        Piece bMusketeer2 = new Piece(Piece.PieceType.MUSKETEER, blockGrid[0][5], false);
        addObject(bMusketeer2, blockGrid[0][5].getX(), blockGrid[0][5].getY());
        
        Piece bRoyalGiant = new Piece(Piece.PieceType.ROYAL_GIANT, blockGrid[0][4], false);
        addObject(bRoyalGiant, blockGrid[0][4].getX(), blockGrid[0][4].getY());
        
        Piece bWitch = new Piece(Piece.PieceType.WITCH, blockGrid[0][3], false);
        addObject(bWitch, blockGrid[0][3].getX(), blockGrid[0][3].getY());
        
        Piece bKnight1 = new Piece(Piece.PieceType.KNIGHT, blockGrid[0][1], false);
        addObject(bKnight1, blockGrid[0][1].getX(), blockGrid[0][1].getY());
        
        Piece bKnight2 = new Piece(Piece.PieceType.KNIGHT, blockGrid[0][6], false);
        addObject(bKnight2, blockGrid[0][6].getX(), blockGrid[0][6].getY());
        
        for (int i = 0; i < 8; i++) {
            Piece recruit = new Piece(Piece.PieceType.ROYAL_RECRUITS, blockGrid[1][i], false);
            addObject(recruit, blockGrid[1][i].getX(), blockGrid[1][i].getY());
        }
    }
    
    public TurnManager getTurnManager()
    {
        return turnManager;
    }
    
    public void endTurn(){
        turnManager.nextTurn();
        
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
}









