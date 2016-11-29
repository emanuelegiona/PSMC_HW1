import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Definisce un tool per la risoluzione di Sudoku
 */
public class SudokuSolver {
    private BigInteger solSpace;
    private long solCount;

    /**
     * Crea un Sudoku a partire dalla lettura di un file di testo di 9 righe, ognuna di 9 caratteri in [0,9] U {.}
     * @param filePath stringa rappresentante il percorso del file
     * @return il Sudoku associato al file dato in input
     */
    public static Sudoku readSudoku(String filePath){
        Path p=Paths.get(filePath);
        List<String> ss=new ArrayList<>();
        Sudoku game=null;
        try {
            ss= Files.readAllLines(p);
            int[][] m=new int[9][];
            int[][] rM=new int[9][];
            int[][] cM=new int[9][];
            int[][] qM=new int[9][];

            int i,j;
            for(i=0;i<9;i++){
                m[i]=new int[]{0,0,0,0,0,0,0,0,0};
                rM[i]=new int[]{0,0,0,0,0,0,0,0,0};
                cM[i]=new int[]{0,0,0,0,0,0,0,0,0};
                qM[i]=new int[]{0,0,0,0,0,0,0,0,0};
            }

            i=0;
            for(String s:ss){
                j=0;
                for(char c:s.toCharArray()){
                    int q=Sudoku.getQuad(i,j);
                    try {
                        int val=Integer.parseInt("" + c);
                        m[i][j]=val;
                        rM[i][val-1]=1;
                        cM[j][val-1]=1;
                        qM[q][val-1]=1;
                    }catch(Exception e){}
                    j++;
                }
                i++;
            }
            game=new Sudoku(m,rM,cM,qM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return game;
    }
}