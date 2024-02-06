// Name: Negar Ahani   
// USC NetID: ahani
// CS 455 PA3
// Fall 2023


/** 
   MineField
      Class with locations of mines for a minesweeper game.
      This class is mutable, because we sometimes need to change it once it's created.
      Mutators: populateMineField, resetEmpty
      Includes convenience method to tell the number of mines adjacent to a location.
 */
import java.util.Random;
public class MineField {
   
   // <put instance variables here>
    private boolean[][] locations;
    private int numberRows;
    private int numberCols;
    private int numberMines;
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in
      the array such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice
      versa.  numMines() for this minefield will correspond to the number of 'true' values in
      mineData.
      @param mineData  the data for the mines; must have at least one row and one col,
                       and must be rectangular (i.e., every row is the same length)
    */
   public MineField(boolean[][] mineData) {
       numberRows = mineData.length;
       numberCols = mineData[0].length;
       numberMines = 0;
       locations = new boolean[numberRows][numberCols];
       for (int i = 0; i < numberRows; i++){
           for (int j=0 ; j < numberCols; j++){
               locations[i][j] = mineData[i][j];
               if (locations[i][j]){
                   numberMines++;
               }
           }
       }

   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a 
      MineField, numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      numberRows = numRows;
      numberCols = numCols;
      numberMines = numMines;
      locations = new boolean[numRows][numCols];
       for (int i = 0; i < numberRows; i++){
           for (int j=0 ; j < numberCols; j++){
               locations[i][j] = false;
           }
       }

   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on
      the minefield, ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col) and numMines() < (1/3 * numRows() * numCols())
    */
   public void populateMineField(int row, int col) {
      // removing any current mines on the minefield
       resetEmpty();
       Random random = new Random();
       int numTrues = 0;
       while (numTrues != numberMines){
           int randomRow = random.nextInt(numberRows);
           int randomCol = random.nextInt(numberCols);
           // we put mines on random squares other than the one we click on it, also we check if it has not been already assigned a mine
           if (randomRow !=row && randomCol != col && !locations[randomRow][randomCol]){
               locations[randomRow][randomCol] = true;
               numTrues++;
           }
       }

   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or
      numCols().  Thus, after this call, the actual number of mines in the minefield does not match
      numMines().  
      Note: This is the state a minefield created with the three-arg constructor is in at the 
      beginning of a game.
    */
   public void resetEmpty() {
       for(int i = 0; i <numberRows; i++){
           for(int j = 0; j < numberCols; j++){
               locations[i][j] = false;
           }
       }
   }

   
  /**
     Returns the number of mines adjacent to the specified location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
      int adjacentCount = 0;
      if (inRange(row,col+1) && locations[row][col+1] ){
          adjacentCount ++;
      }
      if (inRange(row,col-1) && locations[row][col-1] ){
           adjacentCount ++;
      }
      if (inRange(row+1,col) && locations[row+1][col]){
           adjacentCount ++;
      }
      if (inRange(row-1,col) && locations[row-1][col]){
           adjacentCount ++;
      }
      if (inRange(row+1,col+1) && locations[row+1][col+1]) {
          adjacentCount++;
      }
      if ( inRange(row-1,col+1) && locations[row-1][col+1]) {
           adjacentCount++;
      }
      if ( inRange(row+1,col-1) && locations[row+1][col-1]) {
           adjacentCount++;
      }
      if ( inRange(row-1,col-1) && locations[row-1][col-1]) {
           adjacentCount++;
       }
       return adjacentCount;
   }

   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
       if (row >= numberRows || row < 0  || col >= numberCols || col < 0){
           return false;
       }
      return true;
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return numberRows;       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return numberCols;       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
       return locations[row][col];
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg
      constructor, some of the time this value does not match the actual number of mines currently
      on the field.  See doc for that constructor, resetEmpty, and populateMineField for more
      details.
      @return number of mines
    */
   public int numMines() {
      return numberMines;       // DUMMY CODE so skeleton compiles
   }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < numberRows; row++) {
            for (int col = 0; col < numberCols; col++) {
                if (locations[row][col]) {
                    sb.append("True");
                } else {
                    sb.append("False");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // <put private methods here>
   
         
}

