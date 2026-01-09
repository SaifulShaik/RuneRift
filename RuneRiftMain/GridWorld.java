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

    private Block[][] blockGrid;
    
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public GridWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1); 
        
        gridWidth = SIZE * CELLS_WIDE;
        gridHeight = SIZE * CELLS_TALL;
        
        blockGrid = new Block[CELLS_TALL][CELLS_WIDE];
        layoutGrid();
        
    }
    
    
    private void layoutGrid() {
        for (int i = 0; i < blockGrid.length; i++){
            for (int j = 0; j < blockGrid[i].length; j++){
                blockGrid[i][j] = new Block (j, i);
                addObject(blockGrid[i][j], 
                          (getWidth() - gridWidth)/2 + j * SIZE + (SIZE / 2),
                          (getHeight() - gridHeight)/2 + i * SIZE + (SIZE / 2));
            }
        }
    }
    
}









