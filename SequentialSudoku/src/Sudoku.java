import java.util.HashSet;
import java.util.Set;

/**
 * Definisce un'istanza di Sudoku
 */
public class Sudoku {
    private int[][] matrix;
    private int[][] rMatrix;
    private int[][] cMatrix;
    private int[][] qMatrix;

    public Sudoku(int[][] matrix, int[][] rMatrix, int[][] cMatrix, int[][] qMatrix){
        this.matrix=matrix;
        this.rMatrix=rMatrix;
        this.cMatrix=cMatrix;
        this.qMatrix=qMatrix;
    }

    /**
     * Restituisce il quadrante relativo ad una cella
     * @param row la riga della cella
     * @param col la colonna della cella
     * @return il quadrante 3x3 della cella
     */
    public static int getQuad(int row, int col){
        if(row<3){
            if(col<3)
                return 0;
            else if(col<6)
                return 1;
            else
                return 2;
        }
        else if(row<6){
            if(col<3)
                return 3;
            else if(col<6)
                return 4;
            else
                return 5;
        }
        else{
            if(col<3)
                return 6;
            else if(col<6)
                return 7;
            else
                return 8;
        }
    }

    /**
     * Restituisce i possibili valori di una cella tali che rimanga un'istanza legale di Sudoku
     * @param row la riga della cella
     * @param col la colonna della cella
     * @return un insieme di possibili valori per la cella (row,col)
     */
    public Set<Integer> getCandidates(int row, int col){
        Set<Integer> candidates=new HashSet<>();

        int quad=getQuad(row,col);
        for(int i=1;i<10;i++)
            if(rMatrix[row][i-1]==0 && cMatrix[col][i-1]==0 && qMatrix[quad][i-1]==0)
                candidates.add(i);

        return candidates;
    }

    /**
     * Restituisce il valore di una cella
     * @param row la riga della cella
     * @param col la colonna della cella
     * @return il valore della cella (row,col)
     */
    public int getValue(int row, int col){
        return matrix[row][col];
    }

    /**
     * Imposta il valore di una cella
     * @param row la riga della cella
     * @param col la colonna della cella
     * @param val il valore da inserire nella cella (row,col)
     */
    public void setValue(int row, int col, int val){
        matrix[row][col]=val;
    }

    @Override
    public String toString(){
        String s="";
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                s+=(matrix[i][j]==0 ? "-" : matrix[i][j])+" ";
            }
            s+="\n";
        }
        return s;
    }
}