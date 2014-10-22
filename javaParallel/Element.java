import edu.rit.pj2.Vbl;

/**
 * Class Element provides a container for the FourSquareSmp class
 * executing a {@linkplain ParallelStatement}.
 * <P>
 * Class Element supports the <I>parallel reduction</I> pattern. Each thread
 * creates a thread-local copy of the shared variable by calling the {@link
 * Loop#threadLocal(Vbl) threadLocal()} method of class {@linkplain Loop} or the
 * {@link Section#threadLocal(Vbl) threadLocal()} method of class FourSquareSmp.
 * Each thread performs operations on its own copy, without needing to
 * synchronize with the other threads. At the end of the parallel statement, the
 * thread-local copies are automatically <I>reduced</I> together, and the result
 * is stored in the original shared variable. The reduction is performed by the
 * shared variable's {@link #reduce(Vbl) reduce()} method.
 * <P>
 *
 * @author  Anirudh S N
 * @version 30-Sep-2014
 */

public class Element implements Vbl {
	
	int i, j, k, l;
	
	public Element() {
		this.i=0;
		this.j=0;
		this.k=0;
		this.l=0;
	}
	
	
	public Element(int i, int j, int k, int l) {
		this.i=i;
		this.j=j;
		this.k=k;
		this.l=l;
	}

	
	@Override
	public void reduce(Vbl x) {
		// TODO Auto-generated method stub
		Element tx = (Element) x;
		if (tx.i>i || (tx.i==i && tx.j>j) || (tx.i==i && tx.j==j && tx.k>k)) { 
			i = tx.i;
			j = tx.j;
			k = tx.k;
			l = tx.l;	
//			System.out.println(count+"++"+i+"^2 + "+j+"^2 + "+k+"^2 + "+l+"^2");
		}
	}

	@Override
	public void set(Vbl x) {
		// TODO Auto-generated method stub
		Element tx=(Element) x;
		this.i=tx.i;
		this.j=tx.j;
		this.k=tx.k;
		this.l=tx.l;

	}
	
	public Element clone() {
		return new Element(i, j, k, l);
	}

	public void setVal(int iter, int j2, int k2, int l2) {
		// TODO Auto-generated method stub
		Element y=new Element(iter, j2, k2, l2);
		set(y);
	}




}
