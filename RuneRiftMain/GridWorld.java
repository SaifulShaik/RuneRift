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
        
        Piece wKnight = new Piece(Piece.PieceType.DARK_PRINCE, blockGrid[0][0], true);
        addObject(wKnight, blockGrid[7][0].getX(), blockGrid[7][0].getY());
        elixirBarWhite = new ElixirBar();
        addObject(elixirBarWhite, 300, 30);
        elixirBarBlack = new ElixirBar();
        addObject(elixirBarBlack, 300, 570);
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









