import edu.rit.pj2.Task;


public class FourSquaresSeq extends Task {

	static int valA;
	static int valB;
	static int valC;
	static int valD;
	static int count;

	public FourSquaresSeq() {
		// TODO Auto-generated constructor stub
		valA=0;
		valB=0;
		valC=0;
		valD=0;
	}
	
	public void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length!=1 || Integer.parseInt(args[0])<0) usage();
		
		count = 0;
		final int NUM=Integer.parseInt(args[0]);
		int d=(int) Math.ceil(Math.sqrt(NUM));
		
		for(int i=0; i<=d; ++i) {
			for(int j=i; j<=d; ++j) {
				for(int k=j; k<=d; ++k) {
					for(int l=k; l<=d; ++l) {
						if (NUM==i*i+j*j+k*k+l*l && checkVal(i, j, k, l)) {
							++count;
							setValues(i, j, k, l);
						}
					}
				}
			}
		}
		
		displayValues(NUM);

	}

	private boolean checkVal(int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		if (i==j && j==k && k==l)
			return true;
		if (i<=j && j<=k && k<=l)
			return true;
		return false;
	}

	private void displayValues(int num) {
		// TODO Auto-generated method stub
		System.out.println(num+" = "+valA+"^2 + "+valB+"^2 + "+valC+"^2 + "+valD+"^2");
		System.out.println(count);
	}

	private void setValues(int i, int j, int k, int l) {
		// TODO Auto-generated method stub
		valA=i;
		valB=j;
		valC=k;
		valD=l;
	}
	

	private void usage() {
		// TODO Auto-generated method stub
		System.out.println("Invalid Usage.");
		System.out.println("Correct usage: java FoursquaresSeq <integer>");
		System.exit(1);
	}

}