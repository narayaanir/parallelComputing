import edu.rit.crypto.SHA256;
import edu.rit.pj2.Job;
import edu.rit.pj2.LongLoop;
import edu.rit.pj2.Task;
import edu.rit.util.Hex;
import edu.rit.util.Packing;


public class PreimageClu  extends Job  {

	public void main(String[] args) throws Exception {
		if (args.length!=2) usage();
		
		final int NUMBER=Integer.parseInt(args[0]);

		
		for (int num=0; num<=NUMBER/*(0xFFFFFFFFFFFFFFFFL>>>(64-NUMBER))*/; ++num) {
//			System.out.println("==>"+Hex.toString(num));
			rule().task (PreImgCluTask.class) .args (""+num, args[1]);
		}
		
	}
	
	private static class PreImgCluTask extends Task {

		byte[] bt=new byte[4];
		
		public void main(String[] args) throws Exception {
			
			final long NUMEND=0x1L<<Integer.parseInt(args[0]);
			final long NUMSTART=NUMEND>>>1;

			Hex.toByteArray(args[1], bt);
			final int CMDDIGESTLEN=args[1].length();
			final int CMDDIGEST=Packing.packIntBigEndian(bt, 0);

			parallelFor(NUMSTART, NUMEND).exec(new LongLoop() {



				SHA256 sha256;
				byte[] db;
				byte[] byteStore;
//				String y=new String();				
				@Override
				public void start() throws Exception {
					// TODO Auto-generated method stub
					super.start();
					sha256 = new SHA256();
					db = new byte [32];
					byteStore = new byte [32];
				}
				
				@Override
				public void run(long iter) throws Exception {
					// TODO Auto-generated method stub
//					String d;
					Packing.unpackLongBigEndian(iter, byteStore, 0);
					
					sha256.hash (byteStore, 0, 8);
					sha256.digest (db);
//					d = Hex.toString (db);
					
					int x=Packing.packIntBigEndian(db, 28);
					if (((CMDDIGEST^x&(0xFFFFFFFF>>>(32-4*CMDDIGESTLEN))))==0)  {
//						y="<==";
//						System.out.println(Hex.toString(iter)+" "+d+" "+Hex.toString(x&(0xFFFFFFFF>>>(32-4*CMDDIGESTLEN)))+" "+y);
							System.out.println(Hex.toString(iter));
					}
				}
				
			});

		}
	}
	
	private static void usage() {
		System.err.println ("Usage: java pj2 PreimageClu <n> <digest>");
		throw new IllegalArgumentException();
	}

}
