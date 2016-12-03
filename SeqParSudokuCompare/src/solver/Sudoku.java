package solver;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Definisce un'istanza di solver.Sudoku
 */
public class Sudoku {
    private int[][] matrix;
    private int[][] rMatrix;
    private int[][] cMatrix;
    private int[][] qMatrix;
    private BigInteger solSpace;
    private long solCount;
    private int emptyCells;

    /**
     * Costruttore esplicito
     * @param matrix griglia di gioco di questo solver.Sudoku
     * @param rMatrix per ogni riga, un array caratteristico t.c. rMatrix[i][val]=1 sse nella riga i è presente val+1, 0 altrimenti
     * @param cMatrix per ogni colonna, un array caratteristico t.c. cMatrix[i][val]=1 sse nella colonna i è presente val+1, 0 altrimenti
     * @param qMatrix per ogni quadrante, un array caratteristico t.c. qMatrix[i][val]=1 sse nel quadrante i è presente val+1, 0 altrimenti
     */
    public Sudoku(int[][] matrix, int[][] rMatrix, int[][] cMatrix, int[][] qMatrix){
        this.matrix=matrix;
        this.rMatrix=rMatrix;
        this.cMatrix=cMatrix;
        this.qMatrix=qMatrix;
        solSpace=BigInteger.ZERO;
        solCount=0;
        emptyCells=0;
    }

    /**
     * Costruttore a partire da un altro solver.Sudoku, ne esegue la deep copy
     * @param game il solver.Sudoku da copiare
     */
    public Sudoku(Sudoku game){
        matrix=new int[9][];
        rMatrix=new int[9][];
        cMatrix=new int[9][];
        qMatrix=new int[9][];
        for(int i=0;i<9;i++){
            matrix[i]=new int[]{0,0,0,0,0,0,0,0,0};
            rMatrix[i]=new int[]{0,0,0,0,0,0,0,0,0};
            cMatrix[i]=new int[]{0,0,0,0,0,0,0,0,0};
            qMatrix[i]=new int[]{0,0,0,0,0,0,0,0,0};
        }
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                this.matrix[i][j]=game.matrix[i][j];
                this.rMatrix[i][j]=game.rMatrix[i][j];
                this.cMatrix[i][j]=game.cMatrix[i][j];
                this.qMatrix[i][j]=game.qMatrix[i][j];
            }
        }
        this.solCount=game.solCount;
        this.solSpace=game.solSpace;
        this.emptyCells=game.emptyCells;
    }

    /**
     * Ritorna il numero di celle vuote del solver.Sudoku
     * @return
     */
    public int getEmptyCells() {
        return emptyCells;
    }

    /**
     * Imposta il numero di celle vuote del solver.Sudoku
     * @param emptyCells
     */
    public void setEmptyCells(int emptyCells) {
        this.emptyCells = emptyCells;
    }

    /**
     * Ritorna lo spazio delle soluzioni del solver.Sudoku
     * @return
     */
    public BigInteger getSolSpace() {
        return solSpace;
    }

    /**
     * Imposta lo spazio delle soluzioni del solver.Sudoku
     * @return
     */
    public void setSolSpace(BigInteger solSpace) {
        this.solSpace = solSpace;
    }

    /**
     * Ritorna il numero delle soluzioni legali del solver.Sudoku
     * @return
     */
    public long getSolCount() {
        return solCount;
    }

    /**
     * Imposta il numero delle soluzioni legali del solver.Sudoku
     * @return
     */
    public void setSolCount(long solCount) {
        this.solCount = solCount;
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
     * Restituisce i possibili valori di una cella tali che rimanga un'istanza legale di solver.Sudoku
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
        rMatrix[row][val-1]=1;
        cMatrix[col][val-1]=1;
        qMatrix[getQuad(row,col)][val-1]=1;
    }

    /**
     * Costruisce la stringa associata al solver.Sudoku
     * @return la stringa che rappresenta il solver.Sudoku
     */
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