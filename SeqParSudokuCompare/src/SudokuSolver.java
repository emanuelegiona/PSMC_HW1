import solver.Sudoku;
import solver.parallel.ParallelSolver;
import solver.sequential.SequentialSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Definisce un tool per la risoluzione di solver.Sudoku
 */
public class SudokuSolver {
    /**
     * Crea un solver.Sudoku a partire dalla lettura di un file di testo di 9 righe, ognuna di 9 caratteri in [0,9] U {.}
     * @param filePath stringa rappresentante il percorso del file
     * @return il solver.Sudoku associato al file dato in input
     */
    public static Sudoku readSudoku(String filePath){
        Path p=Paths.get(filePath);
        List<String> ss=new ArrayList<>();
        Sudoku game=null;
        try {
            ss= Files.readAllLines(p);  //leggo l'intero file di input (si assume siano 9 righe ognuna contenente 9 caratteri in [0,9] U {.})
            //inizializzazione matrici per griglia di gioco ed array caratteristici
            int[][] m=new int[9][];
            int[][] rM=new int[9][];
            int[][] cM=new int[9][];
            int[][] qM=new int[9][];

            //inizializzazione a 0 di tutte le matrici
            int i,j;
            for(i=0;i<9;i++){
                m[i]=new int[]{0,0,0,0,0,0,0,0,0};  //nella griglia di gioco, 0 corrisponde a cella vuota ("." nel file)
                rM[i]=new int[]{0,0,0,0,0,0,0,0,0};
                cM[i]=new int[]{0,0,0,0,0,0,0,0,0};
                qM[i]=new int[]{0,0,0,0,0,0,0,0,0};
            }

            i=0;
            for(String s:ss){  //per ogni riga
                j=0;
                for(char c:s.toCharArray()){  //per ogni colonna
                    int q= Sudoku.getQuad(i,j);  //ottengo il quadrante di riferimento
                    try {
                        int val=Integer.parseInt("" + c);
                        m[i][j]=val;  //carico il valore da file, se intero, altrimenti lascio 0
                        rM[i][val-1]=1;  //imposto 1 nell'array caratteristico della riga i nell'indice relativo al valore letto
                        cM[j][val-1]=1;  //imposto 1 nell'array caratteristico della colonna i nell'indice relativo al valore letto
                        qM[q][val-1]=1;  //imposto 1 nell'array caratteristico del quadrante i nell'indice relativo al valore letto
                    }catch(Exception e){}
                    j++;
                }
                i++;
            }
            game=new Sudoku(m,rM,cM,qM);  //credo l'istanza di solver.Sudoku con i valori letti dal file di input
        } catch (IOException e) {
            e.printStackTrace();
        }
        return game;
    }

    /**
     * Risolve un solver.Sudoku in modo sequenziale (solver.sequential.SequentialSolver)
     * @param game un'istanza della classe solver.Sudoku
     */
    public static void sequentialSolve(Sudoku game){
        SequentialSolver.solve(game);
    }

    /**
     * Risolve un solver.Sudoku in modo parallelo
     * @param game un'istanza della classe solver.Sudoku
     * @return il numero di solver.parallel.ParallelSolver utilizzati
     */
    public static long parallelSolve(Sudoku game){
        ParallelSolver p=new ParallelSolver(game);
        Main.fjp.invoke(p);
        return p.getSolvers();
    }
}