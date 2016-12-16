import solvers.Matrix;
import solvers.ParallelSolver;
import solvers.SequentialSolver;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SolverAssistant {
    /**
     * @return una matrice {@code matrix} di Integer 9*9 relativa all'input.txt dato (il numero 0 indica la cella vuota)
     * @throws IOException se il file {@code fileName} non esiste
     */
    public static Integer[][] readFile(String fileName){
        Integer[][] matrix = new Integer[9][9];
        try {
            FileReader reader = new FileReader(fileName);
            Scanner in = new Scanner(reader);
            String line;
            for(int i = 0; i < 9; i++){
                line = in.nextLine();
                for(int j = 0; j < 9; j++){
                    matrix[i][j] = Integer.parseInt(line.charAt(j) != '.' ? (String.valueOf(line.charAt(j))) : "0") ; //da migliorare
                }
            }
        }catch(IOException e){ e.printStackTrace(); }
        return matrix;
    }

    public static int sequentialSolve(Matrix m){
        return new SequentialSolver().resolve(m);
    }

    public static long[] parallelSolve(Matrix m, int CUTOFF){
        ParallelSolver sdk = new ParallelSolver(m,CUTOFF);
        return new long[]{Main.fjPool.invoke(sdk),sdk.getSolvers()};
    }
}
