import edu.rit.crypto.SHA256;
import edu.rit.pj2.Job;
import edu.rit.pj2.LongLoop;
import edu.rit.pj2.Task;
import edu.rit.util.Hex;
import edu.rit.util.Packing;

/**
 * Class PreimageClu is used to launch the program to calculate the 
 * preimage matching for the digest.
 * 
 * 
 * @author  Anirudh S N
 * @version 21-Oct-2014
 */

public class PreimageClu  extends Job  {

	/**
	 * The Main method starts here.
	 * 
	 * @param argss: Command line arguments
	 */
	public void main(String[] args) throws Exception {
		if (args.length!=2) usage();
		
		final int POW=Integer.parseInt(args[0]);
		if (POW<1 || POW>63) usage();
		
		// Sets up task group for 2^N worker tasks
		masterFor(0, (0xFFFFFFFFFFFFFFFFL>>>(64-POW)), WorkerTask.class).args(args[1]);
		
		// Sets up a reduction task
		rule().atFinish().task(ReduceTask.class).args().runInJobProcess();
		
	}
	
	
	/**
	 * Class WorkerTask is used to launch the worker tasks
	 * to take chunks of inputs.
	 * 
	 * @author  Anirudh S N
	 * @version 21-Oct-2014
	 */
	private static class WorkerTask extends Task {


		Store master;
//		int NUM;
		int DIGEST;
		int NUMLEN;

		
		public void main(String[] args) throws Exception {

//			NUM=Integer.parseInt(args[0]);
			NUMLEN=args[0].length();
			
			byte[] bt=new byte[4];
			Hex.toByteArray(args[0], bt);
			DIGEST=Packing.packIntBigEndian(bt, 0);
			
			master=new Store();

			
			workerFor().exec(new LongLoop() {
				Store thrStore;
				SHA256 sha256;
				byte[] db;
				byte[] byteStore;
				int x;
				Store intermediate;
				Store current;
				
				@Override
				public void start() throws Exception {
					// TODO Auto-generated method stub
					super.start();
					sha256=new SHA256();
					db = new byte [32];
					byteStore=new byte [32];
					thrStore=threadLocal(master);
				}
				
				@Override
				public void run(long iter) throws Exception {
					// TODO Auto-generated method stub
					Packing.unpackLongBigEndian(iter, byteStore, 0);
					
					sha256.hash (byteStore, 0, 8);
					sha256.digest (db);
					
					x=Packing.packIntBigEndian(db, 28);
					intermediate=new Store();
					
					if (((DIGEST^x&(0xFFFFFFFF>>>(32-4*NUMLEN))))==0) {
						current=(Store)intermediate.clone();
						current.add(iter);
						intermediate.reduce(current);
					}
					thrStore.reduce(intermediate);
				}
			});
			putTuple(master);
		}
	}

	/**
	 * Class ReduceTask is used to do the reduction by workers.
	 * 
	 * @author  Anirudh S N
	 * @version 21-Oct-2014
	 */
	private static class ReduceTask extends Task {
		// Print total count

		public void main (String[] args) throws Exception {
			Store master=new Store();
			Store taskCount;
			Store template=new Store();
			
			while ((taskCount=tryToTakeTuple(template))!=null) master.reduce(taskCount);
			for (Long x : master.al) System.out.println(Hex.toString(x));
			System.out.println(master.al.size());
		}
	}
	
	/**
	 * The usage method runs if the arguments do not follow specifications.
	 * 
	 * @param: None
	 */
	private static void usage() {
		System.err.println ("Usage: java pj2 PreimageClu <n> <digest>");
		throw new IllegalArgumentException();
	}

}
