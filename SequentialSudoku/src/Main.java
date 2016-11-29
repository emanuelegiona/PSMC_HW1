public class Main {

    public static void main(String[] args) {
        String filePath="input.txt";
        Sudoku game=SudokuSolver.readSudoku(filePath);
        System.out.println(game);
        int i=0;
        int j=2;
        System.out.println("candidates for cell ("+i+","+j+")"+": "+game.getCandidates(i,j));
    }
}