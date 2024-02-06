// Name: Negar Ahani
// USC NetID: ahani
// CS 455 PA3
// Fall 2023


/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because
  it's what the user can see about the minefield). Client can call getStatus(row, col) for any 
  square.  It actually has data about the whole current state of the game, including the underlying
  minefield (getMineField()).  Other accessors related to game status: numMinesLeft(),
  isGameOver().  It also has mutators related to actions the player could do (resetGameDisplay(),
  cycleGuess(), uncover()), and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms the Model for the
  game application, whereas GameBoardPanel is the View and Controller in the MVC design pattern.  It
  contains the MineField that it's partially displaying.  That MineField can be accessed
  (or modified) from outside this class via the getMineField accessor.  
 */
public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus values [0,8] mentioned in comments below) are the
   // possible states of one location (a "square") in the visible field (all are values that can be
   // returned by public method getStatus(row, col)).
   
   // The following are the covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // The following are the uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this opened square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already
                                          // (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of
                                                  // losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused
                                                 // you to lose)
   // ----------------------------------------------------------   
  
   // <put instance variables here>
   MineField mineLocations;
   int numberMinesTotal;
   int numberMinesGuess;
   int numberNonMines;
   boolean gameOver; //it is false when we are not game over
   int[][] squareStatus;

   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the locations covered, no mines guessed, and the game not
      over.
      @param mineField  the minefield to use for this VisibleField
    */
   public VisibleField(MineField mineField) {
      mineLocations = mineField;
      numberMinesTotal = mineField.numMines();
      //numberMinesGuess = 0; I do it in resetGameDisplay
      //gameOver = false; I do it in resetGameDisplay
      squareStatus = new int[mineField.numRows()][mineField.numCols()];
      resetGameDisplay();
   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
      public void resetGameDisplay() {
//      mineLocations.resetEmpty(); >> this is not required because we are using the same underlying minefield
      numberMinesGuess = 0;
      numberNonMines = 0;
      gameOver = false;
      for (int i=0; i < mineLocations.numRows(); i++){
         for (int j=0; j < mineLocations.numCols(); j++){
            squareStatus[i][j] = COVERED;
         }
      }
   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return mineLocations;
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the
            beginning of the class for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      return squareStatus[row][col];
   }

   
   /**
      Returns the number of mines left to guess.  This has nothing to do with whether the mines
      guessed are correct or not.  Just gives the user an indication of how many more mines the user
      might want to guess.  This value will be negative if they have guessed more than the number of
      mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
      return numberMinesTotal - numberMinesGuess;       // DUMMY CODE so skeleton compiles

   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on
      a COVERED square changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to
      QUESTION;  call on a QUESTION square changes it to COVERED again; call on an uncovered square
      has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
      if (squareStatus[row][col] == COVERED){
         squareStatus[row][col] = MINE_GUESS;
         numberMinesGuess++;
      }
      else if (squareStatus[row][col] == MINE_GUESS){
         squareStatus[row][col] = QUESTION;
         numberMinesGuess--;
      }
      else if (squareStatus[row][col] == QUESTION){
         squareStatus[row][col] = COVERED;
      }
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in the
      neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form (possibly along with
      parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
      if(mineLocations.hasMine(row, col)){
         // if a mine is exploded the game ends and the display of the game is updated
         squareStatus[row][col] = EXPLODED_MINE;
         gameOver = true;
         lostGameDisplay();
         return false;
      }
      uncover_helper(row,col);
      // checks if the number of nonmine locations is what it's supposed to be for a winning end; if it's true the game is over
      if (numberNonMines == mineLocations.numRows() * mineLocations.numCols() - numberMinesTotal){
         gameOver = true;
      }
      return true;
   }
/**
 helper function to recursively uncover the squares that are not a mine or adjacent to any mines
 @param row  of the square
 @param col  of the square
 PRE: getMineField().inRange(row, col)
 **/
   private void uncover_helper(int row, int col){
      //base cases for recursion
      if(!mineLocations.inRange(row,col) || isUncovered(row, col) || getStatus(row,col) == MINE_GUESS || getStatus(row,col) == QUESTION){
         return;
      }
      squareStatus[row][col] = mineLocations.numAdjacentMines(row, col);
      numberNonMines++;
      // System.out.println("Number of non-mine locations up to this point is:" + numberNonMines);
      //another base case: square adjacent to a mine
      if (squareStatus[row][col] > 0){
         return;
      }
      uncover_helper(row-1, col);
      uncover_helper(row+1, col);
      uncover_helper(row, col-1);
      uncover_helper(row, col + 1);
      uncover_helper(row+1, col+1);
      uncover_helper(row-1, col+1);
      uncover_helper(row+1, col-1);
      uncover_helper(row-1, col-1);
   }
/**
 * Updates the display of a losing game to show the incorrect guesses and mines. The correct guesses are left as they are.
 **/
   private void lostGameDisplay(){
      for (int row=0; row < mineLocations.numRows(); row++){
         for (int col=0; col < mineLocations.numCols(); col++){
            if (mineLocations.hasMine(row, col) && squareStatus[row][col] != EXPLODED_MINE && squareStatus[row][col] != MINE_GUESS){
               squareStatus[row][col] = MINE;
            }
            if (!mineLocations.hasMine(row,col) && squareStatus[row][col] == MINE_GUESS){
               squareStatus[row][col] = INCORRECT_GUESS;
            }
         }
      }
   }

   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game has ended
    */
   public boolean isGameOver() {
      return gameOver;       // DUMMY CODE so skeleton compiles
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
      return squareStatus[row][col] >= 0;

      // DUMMY CODE so skeleton compiles
   }
   
 
   // <put private methods here>
   
}
