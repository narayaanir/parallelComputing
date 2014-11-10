import edu.rit.gpu.CacheConfig;
import edu.rit.gpu.Gpu;
import edu.rit.gpu.GpuIntArray;
import edu.rit.gpu.Kernel;
import edu.rit.gpu.Module;
import edu.rit.pj2.Task;

/**
 * @author Anirudh S N
 *
 */
public class TestCuda  extends Task {
	
	private static final int N=10;

	private static interface cudaKernel extends Kernel {
		public void testcuda (GpuIntArray a, GpuIntArray res, int N);
	}
	/**
	 * @param args
	 */
	public void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		if (args.length!=0) usage();
		
		Gpu gpu=Gpu.gpu();
		gpu.ensureComputeCapability(2, 0);
		GpuIntArray a=gpu.getIntArray(N);
		GpuIntArray res=gpu.getIntArray(N);
		for (int i=0; i<N; ++i) 
			a.item[i]=1+i;
		a.hostToDev();
		
		Module module=gpu.getModule("testcuda.cubin");
		cudaKernel kernel=module.getKernel(cudaKernel.class);
		kernel.setBlockDim(N);
		kernel.setGridDim(N);
		kernel.setCacheConfig(CacheConfig.CU_FUNC_CACHE_PREFER_L1);
		
		kernel.testcuda(a, res, N);
		res.devToHost();
	}
	
	private void usage() {
		System.out.println("Error.");
		throw new IllegalArgumentException();
	}

}
