public class Main {

    public static void main(String[] args) {
        //String filePath="input.txt";
        String filePath=args[0];
        Sudoku game=SudokuSolver.readSudoku(filePath);
        System.out.println(game);
        int i=0;
        int j=2;
        //System.out.println("candidates for cell ("+i+","+j+")"+": "+game.getCandidates(i,j));
        SudokuSolver.solve(game);
        //System.out.println(game);
        System.out.println("Solutions: "+game.getSolCount());
        System.out.println("Search space: "+game.getSolSpace());
    }
}