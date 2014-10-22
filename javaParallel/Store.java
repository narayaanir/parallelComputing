import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import edu.rit.io.InStream;
import edu.rit.io.OutStream;
import edu.rit.pj2.Tuple;
import edu.rit.pj2.Vbl;

/**
 * Class Store class is used to store the basic values.
 * 
 * @author  Anirudh S N
 * @version 21-Oct-2014
 */

public class Store extends Tuple implements Vbl {

	ArrayList<Long> al=new ArrayList<>();
	
	public Store() {}
	
	public Store(Store store) {
		// TODO Auto-generated constructor stub
		al.addAll(store.al);
	}
	
	/**
	 * The reduce method is used to do the per thread reduction.
	 * 
	 * @param x: Vbl object containing value satisfying the condition.
	 */
	@Override
	public void reduce(Vbl x) {
		// TODO Auto-generated method stub
		Store tx=(Store) x;
//		set(tx);
		for (Long y : tx.al) {
			if (!al.contains(y)) {
				al.add(y);
			}
		}
		Collections.sort(al);
	}
	/**
	 * The clone method does a hard copy of the object.
	 * 
	 * @param : None
	 */
	public Object clone() {
		return new Store(this);
	}

	/**
	 * The set method sets the field based on the arguments provided.
	 * 
	 * @param x: input Vbl argument
	 */
	@Override
	public void set(Vbl x) {
		// TODO Auto-generated method stub
		Store tx=(Store) x;
		al.addAll(tx.al);
	}

	/**
	 * The add method adds the long value to the list.
	 * 
	 * @param iter: value to be added
	 */
	public void add(long iter) {
		// TODO Auto-generated method stub
		al.add(iter);
	}
	
	/**
	 * The writeout method overrides its parent
	 * 
	 * @param x: Outstream object
	 */
	@Override
	public void writeOut(OutStream x) throws IOException {
		// TODO Auto-generated method stub
		for (Long y:al)
			x.writeLong(y);
	}
	
	/**
	 * The readin method is used to override the parent method.
	 * 
	 * @param x: Instream object 
	 */
	@Override
	public void readIn(InStream x) throws IOException {
		// TODO Auto-generated method stub
		Store tx=(Store) x.readObject();
		al.addAll(tx.al);
	}

}
