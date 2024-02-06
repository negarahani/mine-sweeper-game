public class MineFieldTester {
    public static void main(String[] args) {
        // Example hard-coded array for testing
        boolean[][] mineData = {
                {false, true, false},
                {true, false, true},
                {false, false, true}
        };

        MineField mineField_1 = new MineField(mineData);

        System.out.println("MineField created with 1-arg constructor:");
        System.out.println(mineField_1.toString());
        System.out.println("Number of rows: " + mineField_1.numRows());
        System.out.println("Number of columns: " + mineField_1.numCols());
        System.out.println("Number of mines: " + mineField_1.numMines());
        System.out.println("Number of adjacent mines of (2,2):" + mineField_1.numAdjacentMines(2,2));
        System.out.println(mineField_1.inRange(2,2));
        System.out.println(mineField_1.inRange(6,7));

        MineField mineField_2 = new MineField(5, 3, 10);
        System.out.println("MineField created with 3-arg constructor:");
        System.out.println(mineField_2.toString());
        System.out.println("Number of rows: " + mineField_2.numRows());
        System.out.println("Number of columns: " + mineField_2.numCols());
        System.out.println("Number of mines: " + mineField_2.numMines());
//        mineField_2.populateMineField(2,2);
//        System.out.println(mineField_2.toString());
//        mineField_2.resetEmpty();
//        System.out.println(mineField_2.toString());
    }
}
