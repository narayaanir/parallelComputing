import edu.rit.pj2.Task;
import edu.rit.gpu.GpuDoubleVbl;
import edu.rit.gpu.Kernel;
import edu.rit.gpu.Gpu;
import edu.rit.gpu.Module;

/**
 * Class RandomWalkGpu is used to launch the program to 
 * find the average distance traveled in an N body problem 
 * with T iterations.
 * 
 * @author  Anirudh S N
 * @version 09-Nov-2014
 */

public class RandomWalkGpu extends Task {
	/**
	 * Description: RandomWalk Kernel interface to CUDA.
	 * 
	 * @param:
	 * 		N: Number of particles
	 * 		T: Number of iterations required
	 * 		seed: seed number to calculate random numbers for threads
	 */
	private static interface RandomWalkGpuKernel 
	extends Kernel {
		public void RandomWalkGpu (int N, long T, long seed);
	}

	/**
	 * Description: Main starts here.
	 * 
	 * @param args: command line argument
	 */
	public void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length!=3) usage();
		int N=Integer.parseInt(args[0]);
		long T=Long.parseLong(args[1]);
		long seed=Long.parseLong(args[2]);
		
		if(N<1 || T<1) usage();
		
		Gpu gpu=Gpu.gpu();
		gpu.ensureComputeCapability(2, 0);
		Module module=gpu.getModule("RandomWalkGpu.cubin");
		GpuDoubleVbl pos=module.getDoubleVbl("devPos");
		pos.item=0;
		pos.hostToDev();
		
		
		RandomWalkGpuKernel kernel=
		    module.getKernel(RandomWalkGpuKernel.class);
		kernel.setBlockDim(1024);
		kernel.setGridDim(N);
		kernel.RandomWalkGpu(N, T, seed);
		
		pos.devToHost();
		System.out.println(pos.item/N);
//		System.out.printf("Dist:%d", pos.item);
	}
	
	private static void usage() {
		System.out.println(
		    "Usage: java pj2 RandomWalkGpu <N> <T> <seed>");
		throw new IllegalArgumentException();
	}
	
	protected static int coresRequired() {
		return 1;
	}

	protected static int gpusRequired() {
		return 1;
	}
}
