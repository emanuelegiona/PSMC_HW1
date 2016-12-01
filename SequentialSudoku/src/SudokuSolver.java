import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Definisce un tool per la risoluzione di Sudoku
 */
public class SudokuSolver {
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
                    int q=Sudoku.getQuad(i,j);  //ottengo il quadrante di riferimento
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
            game=new Sudoku(m,rM,cM,qM);  //credo l'istanza di Sudoku con i valori letti dal file di input
        } catch (IOException e) {
            e.printStackTrace();
        }
        return game;
    }

    /**
     * Risolve un Sudoku ricorsivamente, completando prima le celle con meno valori legali (candidates)
     * @param game un'istanza della classe Sudoku
     */
    public static void solve(Sudoku game){
        BigInteger n=BigInteger.ONE;
        int row=-1;
        int col=-1;
        Set<Integer> candidates=new HashSet<>();  //mantiene i candidati della cella con meno candidati nel Sudoku game
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(game.getValue(i,j)==0) {
                    Set<Integer> currCandidates = game.getCandidates(i, j);  //ottengo i candidati della cella vuota
                    if (game.getSolSpace().equals(BigInteger.ZERO))  //se è la prima iterazione sul Sudoku game, devo calcolare lo spazio delle soluzioni
                        n = n.multiply(BigInteger.valueOf(currCandidates.size()));
                    if (currCandidates.size() < candidates.size() || candidates.size() == 0) {  //controllo se è la prima cella vuota oppure è la cella con il minor numero di candidati
                        row = i;
                        col = j;
                        candidates = currCandidates;
                    }
                }
            }
        }
        if(row==-1 && col==-1) {  //se non c'è alcuna cella vuota, il Sudoku game ha una soluzione legale
            game.setSolCount(game.getSolCount()+1);
            //System.out.println(game);
            return;
        }
        if (game.getSolSpace().equals(BigInteger.ZERO))  //se è la prima iterazione sul Sudoku game, imposto il valore calcolato dello spazio delle soluzioni
            game.setSolSpace(n);

        for(int choice:candidates){  //itero su tutti i candidati della cella con il minor numero di candidati
            Sudoku updatedGame=new Sudoku(game);  //creo un'istanza clone di quella attuale di game
            updatedGame.setValue(row,col,choice);  //inserisco il candidato scelto
            solve(updatedGame);  //ricorsivamente risolvo l'istanza di Sudoku updatedGame
            game.setSolCount(updatedGame.getSolCount());  //le soluzioni di game sono quelle trovate a partire da updatedGame
        }
    }
}