package solvers;
import java.math.BigInteger;

public class Matrix {
    public int emptyCells = 81;
    private Integer[][] matrix;
    private int[][] caratteristicRow = new int[9][9];
    private int[][] caratteristicColumn = new int[9][9];
    private int[][] quadrants = new int[9][9];
    private int[][] getCaratteristicQuad = {{0,1,2},{3,4,5},{6,7,8}};

    public Matrix(Integer[][] matrix){
        this.matrix=matrix;
        for(int row = 0; row < 9; row++){
            for(int column = 0; column < 9; column++){
                Integer n = this.matrix[row][column];
                if(n != 0){
                    this.emptyCells--;
                    this.caratteristicRow[row][n-1] = 1;
                    this.caratteristicColumn[column][n-1] = 1;
                    this.quadrants[getCaratteristicQuad[row/3][column/3]][n-1] = 1;
                }
            }
        }
    }

    public BigInteger searchSpace() {
        BigInteger count = new BigInteger("0"); BigInteger searchSpace= new BigInteger("1");
        for (int row = 0; row < 9; row++)
            for (int column = 0; column < 9; column++)
                if (this.matrix[row][column] == 0) {
                    for (int n = 1; n < 10; n++) {
                        if (this.check(n, row, column)) {
                            count = count.add(BigInteger.ONE);
                        }
                    }
                    searchSpace = searchSpace.multiply(count);
                    count = BigInteger.ZERO;
                }
        return searchSpace;
        }

    public String getStats(){
        return "empty cells: " + emptyCells +"\nfill factor: " + ( 100 * ( 81 - emptyCells) ) / 81 + "%";
    }

    protected boolean isComplete(){
        return this.emptyCells ==0;//true solo se non ci sono celle vuote
    }

    /**
     * @param n il numero di cui si vuole controllare se è possibile inserirlo
     * @param row la riga
     * @param column la colonna
     * @return true se il numero può essere inserito rispettando le regole del solvers.SequentialSolver, false altrimenti
     */
    protected boolean check(int n, int row, int column) {
        if(this.caratteristicRow[row][n-1] == 1) return false;
        if(this.caratteristicColumn[column][n-1] == 1) return false;
        return this.quadrants[getCaratteristicQuad[row/3][column/3]][n-1] != 1;
    }

    /**
     * @param n il numero da inserire
     * @param row la riga
     * @param column la colonna
     * @return false se il numero non è stato inserito perchè non rispetta le regole del solvers.SequentialSolver
     *         oppure true se il numero è stato inserito rispettando le regole del solvers.SequentialSolver
     */

    protected boolean put(int n, int row, int column){
        emptyCells--;
        this.quadrants[getCaratteristicQuad[row/3][column/3]][n-1] = 1;
        this.matrix[row][column] = n;
        this.caratteristicRow[row][n-1] = 1;
        this.caratteristicColumn[column][n-1] = 1;
        return true;
    }

    public Integer[][] matrixCopy(){
        Integer[][] ret = new Integer[9][9];
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                ret[i][j] = this.matrix[i][j];
        return ret;
    }

    protected boolean isCellEmpty(int row, int column){
        return this.matrix[row][column]==0;
    }
}
