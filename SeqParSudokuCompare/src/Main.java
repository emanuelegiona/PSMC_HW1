import solver.Sudoku;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static ForkJoinPool fjp=new ForkJoinPool();

    public static void main(String[] args) {
        //String filePath="input.txt";
        //List<String> ss=new ArrayList<>();
        for(int i=0;i<args.length;i++) {
            String filePath = args[i];
            Sudoku gamePar = SudokuSolver.readSudoku(filePath);
            Sudoku gameSeq = new Sudoku(gamePar);
            System.out.println("------------\nSolving the Sudoku from: "+filePath+"\n");

            long timePar = System.currentTimeMillis();
            long parSolvers = SudokuSolver.parallelSolve(gamePar);
            timePar = System.currentTimeMillis() - timePar;

            int eCells = gamePar.getEmptyCells();
            float fillFactor = (((float)(81-eCells)/81)*100);
            BigInteger solSpace=gamePar.getSolSpace();
            long sol=gamePar.getSolCount();

            long timeSeq = System.currentTimeMillis();
            SudokuSolver.sequentialSolve(gameSeq);
            timeSeq=System.currentTimeMillis()-timeSeq;

            String s="Empty cells: " + eCells + "\nFill factor: " + (int)fillFactor + "%\n";
            s+="Search space: " + solSpace+"\n";
            s+="Parallel solver done in " + timePar + " ms, using "+parSolvers+" parallel forked solvers\n\n";
            s+="Sequential solver done in " + timeSeq + " ms\n";
            s+="Solutions: " + sol+"\n";
            s+="Speedup: "+(float)timeSeq/timePar+"\n";
            s+=fjp+"\n------------\n";
            System.out.println(s);
            //ss.add(s);
        }

        //quick export
        /*
        try {
            Files.write(Paths.get("benchmark.txt"), ss, StandardCharsets.UTF_8, StandardOpenOption.APPEND,StandardOpenOption.CREATE);
        }catch(Exception e){
            e.printStackTrace();
        }
        */
    }
}
