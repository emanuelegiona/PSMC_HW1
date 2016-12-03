import java.util.concurrent.ForkJoinPool;

public class Main {
    public static ForkJoinPool fjp=new ForkJoinPool();

    public static void main(String[] args) {
        //String filePath="input.txt";
        for(int i=0;i<args.length;i++) {
            String filePath = args[i];
            Sudoku game = SudokuSolver.readSudoku(filePath);
            //System.out.println(game);
            long time = System.currentTimeMillis();
            SudokuSolver.solve(game);
            time = System.currentTimeMillis() - time;
            //System.out.println(game);
            System.out.println("Empty cells: " + game.getEmptyCells() + "\nFill factor: " + ((float) (81 - game.getEmptyCells()) / 81) * 100 + "%");
            System.out.println("Search space: " + game.getSolSpace());
            System.out.println("Parallel solver done in " + time + " ms");
            System.out.println("Solutions: " + game.getSolCount());
        }
    }
}