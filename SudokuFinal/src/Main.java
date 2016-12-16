import solvers.Matrix;
import java.util.concurrent.ForkJoinPool;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.out;

public class Main {
    public static final ForkJoinPool fjPool = new ForkJoinPool();
    private static final int CUTOFF=50;

    public static void main(String[] args){
        int solutionsSeq;
        long[] solutionsPar;
        long time, timeSeq, timePar;
        long ms, s, m;
		
		//export benchmark
		List<String> ss=new ArrayList<>();
		
        for(String arg : args){
			String sExport="file: "+arg+"\n"; //export

            Matrix matrix = new Matrix(SolverAssistant.readFile(arg));
            out.println(matrix.getStats());//stampa le celle vuote e il fill factor
            out.println("search space before elimination: "+matrix.searchSpace() + " branches");
            out.println();
            out.println("solving in parallel...");
            time = System.currentTimeMillis();
            solutionsPar = SolverAssistant.parallelSolve(matrix,CUTOFF);
            timePar = System.currentTimeMillis() - time;
			time=timePar;
            m = ( timePar / 1000 ) / 60;
            timePar -= m * 60 * 1000;
            s = timePar / 1000;
            ms = timePar % 1000;
			timePar=time;
            out.println("done in: " + m + "m " + s + "s " + ms + "ms" );
            out.println("solutions: " + solutionsPar[0]);
            out.println("forked solvers: "+solutionsPar[1]);
            out.println();
			sExport+="parallel: "+timePar+" ms, using "+solutionsPar[1]+" parallel forked solvers\n";
            out.println("solving sequentially...");
            time = System.currentTimeMillis();
            Matrix sMatrix = new Matrix(SolverAssistant.readFile(arg));
            solutionsSeq = SolverAssistant.sequentialSolve(sMatrix);
            timeSeq = System.currentTimeMillis() - time;
			time=timeSeq;
            m = ( timeSeq / 1000 ) / 60;
            timeSeq -= m * 60 * 1000;
            s = timeSeq / 1000;
            ms = timeSeq % 1000;
			timeSeq=time;
            out.println("done in: " + m + "m " + s + "s " + ms + "ms" );
            out.println("solutions: " + solutionsSeq);
			sExport+="sequential: "+timeSeq+" ms\n";
			String speed="speedup: "+(float)timeSeq/timePar+"\n------------\n";
			sExport+=speed;

			try{
            out.println("Speedup: " + timeSeq / timePar +"."+ timeSeq % timePar);
			}catch(Exception e){}
			ss.add(sExport);
        }
		
		//quick export
        /**/
        try {
            Files.write(Paths.get("benchmark.txt"), ss, StandardCharsets.UTF_8, StandardOpenOption.APPEND,StandardOpenOption.CREATE);
        }catch(Exception e){
            e.printStackTrace();
        }
        /**/
    }
}
