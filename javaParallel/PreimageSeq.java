import edu.rit.crypto.SHA256;
import edu.rit.pj2.Task;
import edu.rit.util.Hex;
import edu.rit.util.Packing;


public class PreimageSeq extends Task {

	static int count=0;
	public void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length!=2) usage();
		
		final int NUMBER=Integer.parseInt(args[0]);
		byte[] bt=new byte[4];
		Hex.toByteArray(args[1], bt);
		final int CMDDIGESTLEN=args[1].length();
		final int CMDDIGEST=Packing.packIntBigEndian(bt, 0);

		
		for (long num=0; num<=(0xFFFFFFFFFFFFFFFFL>>>(64-NUMBER)); ++num) {
			SHA256 sha256 = new SHA256();
			byte[] db = new byte [32];
//			String d;
			
			byte[] byteStore = new byte [32];
			Packing.unpackLongBigEndian(num, byteStore, 0);
//			String y=new String();
			
			sha256.hash (byteStore, 0, 8);
			sha256.digest (db);
//			d = Hex.toString (db);
			
			int x=Packing.packIntBigEndian(db, 28);
			
//			if (((CMDDIGEST&(0xFFFFFFFF>>>(32-4*CMDDIGESTLEN)))^(x&(0xFFFFFFFF>>>(32-4*CMDDIGESTLEN))))==0) /*y="<==";*/ { // Old Code
//				System.out.println(Hex.toString(num)+" "+d+" "+Hex.toString(x&(0xFFFFFFFF>>>(32-4*CMDDIGESTLEN)))+" "+y);
			if (((CMDDIGEST^x&(0xFFFFFFFF>>>(32-4*CMDDIGESTLEN))))==0) {
					System.out.println(Hex.toString(num));
					++count;
			}
		}
		System.out.println(count);


	}
	
	private static void usage() {
		System.err.println ("Usage: java pj2 PreimageSeq <n> <digest>");
		throw new IllegalArgumentException();
	}
	
	protected static int coresRequired() {
		return 1;
	}


}
