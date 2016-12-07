package solver.parallel;
import solver.Sudoku;
import solver.sequential.SequentialSolver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * Risolve il solver.Sudoku in parallelo
 */
public class ParallelSolver extends RecursiveAction{
    private Sudoku game;
    private volatile long solvers;  //il numero di istanze di ParallelSolver usati per risolvere l'instanza di Sudoku game
    private final int seqCutoff=25;  //il numero di celle vuote sotto il quale è migliore l'algoritmo sequenziale

    /**
     * Rappresenta una cella della griglia di gioco del Sudoku game
     */
    private class Cell{
        private int row,col;
        private Set<Integer> candidates;

        /**
         * Costruttore di una cella di gioco
         * @param row riga
         * @param col linea
         * @param candidates valori legali per quella cella
         */
        protected Cell(int row, int col, Set<Integer> candidates){
            this.row=row;
            this.col=col;
            this.candidates=candidates;
        }

        protected int getRow(){
            return row;
        }

        protected int getCol(){
            return col;
        }

        protected Set<Integer> getCandidates(){
            return candidates;
        }

        public String toString(){
            return "("+row+","+col+")";
        }
    }

    /**
     * Ricerca la cella vuota con il minor numero di candidati del Sudoku game in una regione, in parallelo
     */
    private class SearchEmptyCell extends RecursiveTask<Cell> {
        private int rowStart;
        private int rowEnd;
        private int colStart;
        private int colEnd;
        private Cell min;

        /**
         * Imposta la regione in cui cercare a (rowStart,colStart)~(rowEnd,colEnd)
         * @param rowStart riga di inizio
         * @param colStart colonna di inizio
         * @param rowEnd riga di fine (esclusa)
         * @param colEnd colonna di fine (esclusa)
         */
        protected SearchEmptyCell(int rowStart,int colStart, int rowEnd, int colEnd){
            this.rowStart=rowStart;
            this.rowEnd=rowEnd;
            this.colStart=colStart;
            this.colEnd=colEnd;
            min=null;
        }

        protected Cell compute(){
            for(int i=rowStart;i<rowEnd;i++) {
                for (int j = colStart; j < colEnd; j++) {
                    if(game.getValue(i,j)==0) {
                        Set<Integer> currCandidates = game.getCandidates(i, j);
                        if(min==null)
                            min = new Cell(i, j, currCandidates);
                        else if (currCandidates.size() < min.getCandidates().size())
                            min = new Cell(i, j, currCandidates);
                    }
                }
            }
            //System.out.println(min);
            return min;
        }
    }
    /**/

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
        //se la griglia di gioco del Sudoku game è completa, incrementa il numero di soluzioni
        if(game.getEmptyCells()==0){
            game.setSolCount(game.getSolCount()+1);
            return;
        }

        //se la griglia di gioco del Sudoku game ha meno di seqCutoff celle vuote, passa all'algoritmo sequenziale
        if(game.getEmptyCells()<seqCutoff){
            SequentialSolver.solve(game);
            return;
        }

        int row=-1;
        int col=-1;
        Set<Integer> candidates=new HashSet<>();  //mantiene i candidati della cella con meno candidati nel solver.Sudoku game

        //parallel search
        List<ForkJoinTask<Cell>> minCells=new ArrayList<>();
        //per ogni quadrante, cerco la cella vuota con il minor numero di candidati in parallelo
        for(int i=0;i<9;i+=3) {
            for (int j = 0; j < 9; j += 3) {
                //System.out.println("quad: ("+i+","+j+")-("+(i+3)+","+(j+3)+")");
                minCells.add(new SearchEmptyCell(i, j, i + 3, j + 3).fork());
            }
        }

        Cell min=new Cell(0,0,new HashSet<>());
        //tra le celle vuote con minor numero di candidati di ogni quadrante, scelgo quella con il minor numero di candidati
        for(ForkJoinTask<Cell> cellTask:minCells){
            Cell c=cellTask.join();
            if(c==null)
                continue;
            if(c.getCandidates().size()<min.getCandidates().size() || min.getCandidates().size()==0) {
                min = c;
                row=min.getRow();
                col=min.getCol();
                candidates=c.getCandidates();
            }
        }

        List<ParallelSolver> tasks=new ArrayList<>();
        for(int choice:candidates){  //itero su tutti i candidati della cella con il minor numero di candidati
            game.setEmptyCells(game.getEmptyCells()-1);
            ParallelSolver ps=new ParallelSolver(game,row,col,choice); //clono l'istanza del solver.Sudoku attuale ed effettuo l'inserimento del valore, in parallelo
            tasks.add(ps);
            ps.fork();
            solvers++;
        }
        for(ParallelSolver ps:tasks){
            ps.join();
            game.setSolCount(game.getSolCount()+ps.getGame().getSolCount());
            solvers+=ps.getSolvers();
        }
    }

    public long getSolvers() {
        return solvers;
    }

    public Sudoku getGame() {
        return game;
    }
}
