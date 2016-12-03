package solver.sequential;
import solver.Sudoku;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class SequentialSolver {
    public static void solve(Sudoku game){
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

        for(int choice:candidates){  //itero su tutti i candidati della cella con il minor numero di candidati
            Sudoku updatedGame=new Sudoku(game);  //creo un'istanza clone di quella attuale di game
            updatedGame.setValue(row,col,choice);  //inserisco il candidato scelto
            solve(updatedGame);  //ricorsivamente risolvo l'istanza di solver.Sudoku updatedGame
            game.setSolCount(updatedGame.getSolCount());  //le soluzioni di game sono quelle trovate a partire da updatedGame
        }
    }
}
