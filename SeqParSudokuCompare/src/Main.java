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
        List<String> ss=new ArrayList<>();
        for(int i=0;i<args.length;i++) {
            String filePath = args[i];
            Sudoku gamePar = SudokuSolver.readSudoku(filePath);
            Sudoku gameSeq = new Sudoku(gamePar);
            System.out.println("------------\nSolving the Sudoku from: "+filePath+"\n");
            int eCells = gamePar.getEmptyCells();
            float fillFactor = (((float)(81-eCells)/81)*100);
            BigInteger solSpace=gamePar.getSolSpace();
            String s="Empty cells: " + eCells + "\nFill factor: " + (int)fillFactor + "%\n";
            s+="Search space: " + solSpace+"\n";
            System.out.println(s);

            System.out.println("Solving in parallel...");
            long timePar = System.currentTimeMillis();
            long parSolvers = SudokuSolver.parallelSolve(gamePar);
            timePar = System.currentTimeMillis() - timePar;

            String sPar=timePar + " ms, using "+parSolvers+" parallel forked solvers\n";
            System.out.print("-> done in " + sPar);
            long sol=gamePar.getSolCount();
            System.out.println("Solutions: "+sol);

            System.out.println("\nSolving sequentially...");
            long timeSeq = System.currentTimeMillis();
            SudokuSolver.sequentialSolve(gameSeq);
            timeSeq=System.currentTimeMillis()-timeSeq;
            String sSeq=timeSeq+" ms\n";
            System.out.print("-> done in "+sSeq);
            sol=gameSeq.getSolCount();
            System.out.println("Solutions: "+sol);

            String sSU="\nSpeedup: "+(float)timeSeq/timePar+"\n";
            System.out.print(sSU);

            s+="Parallel: "+sPar;
            s+="Sequential: "+sSeq;
            s+="Solutions: " +sol;
            s+=sSU;
            s+=fjp+"\n------------\n";
            //System.out.println(s);
            ss.add(s);
        }

        //quick export
        /**/
        try {
            Files.write(Paths.get("benchmark.txt"), ss, StandardCharsets.UTF_8, StandardOpenOption.APPEND,StandardOpenOption.CREATE);
        }catch(Exception e){
            e.printStackTrace();
        }
        /**/
    }
}
