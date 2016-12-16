package solvers;

public class SequentialSolver {
    private Matrix matrix;
    private int count;

    public SequentialSolver(){
        count=0;
    }

    public int resolve(Matrix matrix){
        this.matrix = matrix;
        recursiveSolve(this.matrix);
        return count;
    }

    private void recursiveSolve(Matrix mat){
        if(mat.isComplete()){
            count++;
            return;
        }

        int min=10;
        int momentCount=0;
        int choicesRow=0, choicesColumn=0;
        for (int row = 0; row < 9; row++)
            for (int column = 0; column < 9; column++)
                if(mat.isCellEmpty(row, column)){
                    for (int n = 1; n < 10; n++)
                        if(mat.check(n, row, column))
                            momentCount++;
                    if(momentCount < min){
                        min = momentCount;
                        choicesRow=row;choicesColumn=column;
                    }
                    momentCount=0;
                }

        for (int number = 1; number < 10; number++)
            if(mat.check(number, choicesRow, choicesColumn)){
                Integer[][] copyMat = mat.matrixCopy();
                mat.put(number, choicesRow, choicesColumn);
                recursiveSolve(mat);
                mat = new Matrix(copyMat);
            }
        }
    }
