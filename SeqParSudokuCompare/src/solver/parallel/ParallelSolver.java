package solver.parallel;
import solver.Sudoku;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveAction;

/**
 * Risolve il solver.Sudoku in parallelo
 */
public class ParallelSolver extends RecursiveAction{
    private Sudoku game;
    private volatile long solvers;

    public ParallelSolver(Sudoku game){
        this.game=game;
        solvers=0;
    }

    public ParallelSolver(Sudoku game, int row, int col, int choice){
        this.game=new Sudoku(game);
        this.game.setValue(row,col,choice);
        solvers=0;
    }

    public void compute(){
        BigInteger n=BigInteger.ONE;
        int eCells=0;
        int row=-1;
        int col=-1;
        Set<Integer> candidates=new HashSet<>();  //mantiene i candidati della cella con meno candidati nel solver.Sudoku game
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(game.getValue(i,j)==0) {
                    Set<Integer> currCandidates = game.getCandidates(i, j);  //ottengo i candidati della cella vuota
                    if (game.getSolSpace().equals(BigInteger.ZERO)) {  //se è la prima iterazione sul solver.Sudoku game, devo calcolare lo spazio delle soluzioni e le celle vuote
                        n = n.multiply(BigInteger.valueOf(currCandidates.size()));
                        eCells++;
                    }
                    if (currCandidates.size() < candidates.size() || candidates.size() == 0) {  //controllo se è la prima cella vuota oppure è la cella con il minor numero di candidati
                        row = i;
                        col = j;
                        candidates = currCandidates;
                    }
                }
            }
        }
        if(row==-1 && col==-1) {  //se non c'è alcuna cella vuota, il solver.Sudoku game ha una soluzione legale
            game.setSolCount(game.getSolCount()+1);
            return;
        }
        if (game.getSolSpace().equals(BigInteger.ZERO)) {  //se è la prima iterazione sul solver.Sudoku game, imposto il valore calcolato dello spazio delle soluzioni
            game.setSolSpace(n);
            game.setEmptyCells(eCells);
        }

        List<ParallelSolver> tasks=new ArrayList<>();
        for(int choice:candidates){  //itero su tutti i candidati della cella con il minor numero di candidati
            ParallelSolver ps=new ParallelSolver(game,row,col,choice); //clono l'istanza del solver.Sudoku attuale ed effettuo l'inserimento del valore, in parallelo
            tasks.add(ps);
            ps.fork();
            solvers++;
        }
        for(ParallelSolver ps:tasks){
            ps.join();
            game.setSolCount(game.getSolCount()+ps.getGame().getSolCount());
            solvers+=ps.solvers;
        }
    }

    public long getSolvers() {
        return solvers;
    }

    public Sudoku getGame() {
        return game;
    }

    public void setGame(Sudoku game) {
        this.game = game;
    }
}
