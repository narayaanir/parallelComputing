import edu.rit.crypto.SHA256;
import edu.rit.pj2.Task;
import edu.rit.util.Hex;
import edu.rit.util.Packing;

/**
 * Class PreimageSeq is used to launch the program to calculate the 
 * preimage matching for the digest.
 * 
 * 
 * @author  Anirudh S N
 * @version 21-Oct-2014
 */
public class PreimageSeq extends Task {

	static int count=0;
	/**
	 * The Main method starts here.
	 * 
	 * @param argss: Command line arguments
	 */
	public void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length!=2) usage();
		
		final int NUMBER=Integer.parseInt(args[0]);
		if (NUMBER<1 || NUMBER>63) usage();
		byte[] bt=new byte[4];
		Hex.toByteArray(args[1], bt);
		final int CMDDIGESTLEN=args[1].length();
		final int CMDDIGEST=Packing.packIntBigEndian(bt, 0);

		
		for (long num=0; num<=(0xFFFFFFFFFFFFFFFFL>>>(64-NUMBER)); ++num) {
			SHA256 sha256 = new SHA256();
			byte[] db = new byte [32];
			
			byte[] byteStore = new byte [32];
			Packing.unpackLongBigEndian(num, byteStore, 0);
			
			sha256.hash (byteStore, 0, 8);
			sha256.digest (db);
			
			int x=Packing.packIntBigEndian(db, 28);
			
			if (((CMDDIGEST^x&(0xFFFFFFFF>>>(32-4*CMDDIGESTLEN))))==0) {
					System.out.println(Hex.toString(num));
					++count;
			}
		}
		System.out.println(count);


	}
	/**
	 * The usage method runs if the arguments do not follow specifications.
	 * 
	 * @param: None
	 */
	private static void usage() {
		System.err.println ("Usage: java pj2 PreimageSeq <n> <digest>");
		throw new IllegalArgumentException();
	}
	
	protected static int coresRequired() {
		return 1;
	}


}
