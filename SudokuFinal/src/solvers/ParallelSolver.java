package solvers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ParallelSolver extends RecursiveTask<Integer> {
    private Matrix matrix;
    private Integer globalCount = 0;
    private final int CUTOFF;
    private long countSolvers;

    public ParallelSolver(Matrix matrix, int CUTOFF) {
        this.matrix = matrix;
        this.CUTOFF=CUTOFF;
    }

    protected Integer compute(){
        if (matrix.isComplete())
            return ++globalCount;

        if(matrix.emptyCells < CUTOFF)
            return new SequentialSolver().resolve(new Matrix(matrix.matrixCopy()));

        List<ParallelSolver> ret;
        int min=10;
        int currentCount=0;
        int choiceRow=0, choiceColumn=0;

        for (int row = 0; row < 9; row++)
            for (int column = 0; column < 9; column++)
                if(matrix.isCellEmpty(row, column)){
                    for (int n = 1; n < 10; n++)
                        if(matrix.check(n, row, column)){
                            currentCount++;
                        }
                    // se è il minimo aggiorno le coordinate della casella da scegliere
                    if(currentCount < min){
                        min = currentCount;
                        choiceRow=row;choiceColumn=column;
                    }
                    currentCount=0;
                }

        ret = new ArrayList<>();
        for (int number = 1; number < 10; number++)
            if (matrix.check(number, choiceRow, choiceColumn)) {
                Integer[][] copyMat = matrix.matrixCopy();
                matrix.put(number, choiceRow, choiceColumn);
                ParallelSolver ps=new ParallelSolver(matrix, CUTOFF);
                ret.add(ps);
                ps.fork();
                countSolvers++;
                matrix = new Matrix(copyMat);
            }

        for (ParallelSolver ps : ret) {
            globalCount+=ps.join();
            countSolvers+=ps.countSolvers;
        }
        return globalCount;
    }

    public long getSolvers() {
        return countSolvers;
    }
}
