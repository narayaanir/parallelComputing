
import edu.rit.pj2.IntVbl;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
//import edu.rit.pj2.Vbl;

/**
 * Class FourSquaresSmp is used to launch the program to calculate the 
 * squares and individual numbers.
 * 
 * 
 * @author  Anirudh S N
 * @version 30-Sep-2014
 */

public class FourSquaresSmp extends Task {
	
	Element best;
	IntVbl sx;
	
	public void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length != 1 || Integer.parseInt(args[0]) < 0)
			usage();


		final int NUM = Integer.parseInt(args[0]);
		final int d = (int) Math.sqrt(NUM);
		final int[] squares = new int[d + 1];

		// Generate Squares
		parallelFor(0, d).schedule(leapfrog).exec(new Loop() {
			@Override
			public void run(int i) throws Exception {
				// TODO Auto-generated method stub
				squares[i] = i * i;
			}
		});



		best = new Element();
		sx=new IntVbl.Sum(0);
		// Generate sum and check
			// Use separate class to do the reduction
		parallelFor(0, d).schedule(proportional).chunk(35).exec(new Loop() {
			
			
			Element current;
			Element now;
			Element thrBest;
			IntVbl thrsx;

			@Override
			public void start() throws Exception {
				// TODO Auto-generated method stub
				super.start();
				current=new Element();
				now=new Element();
				thrBest=threadLocal(best);
				thrsx=threadLocal(sx);
			}
			
			@Override
			public void run(int i) throws Exception {
				// TODO Auto-generated method stub
				for (int l=i; l<=d; ++l) {
					for (int k=l; k<=d; ++k) {
						for (int j=k; j<=d; ++j) {
							if (NUM == squares[j] + squares[k] + squares[l] + squares[i]) {
								// code to set values
								current.setVal(i, l, k, j);
								++thrsx.item;
								now.reduce(current);
							}
						}
					}	
				}
				// code for reduce
				thrBest.reduce(now);
			}
		});
		System.out.println(NUM+" = "+best.i+"^2 + "+best.j+"^2 + "+best.k+"^2 + "+best.l+"^2");
		System.out.println(sx.item);
	}

	private static void usage() {
		// TODO Auto-generated method stub
		System.out.println("Invalid Usage.");
		System.out.println("Correct usage: java FoursquaresSmp <integer>");
		throw new IllegalArgumentException();
	}

}
