/*
 * File: RandomWalkGpu.cu
 * FileType: CUDA
 *
 * This file is part of the program RandomWalkGpu to find the average distance
 * of N particles performing a three dimensional Random Walk. The random walks
 * are calculated in the GPU.
 *
 * Author: Anirudh S N
 * Date: Nov 9, 2014
 *
*/
#include "Random.cu"
// USed to define the number of threads in each block
#define NT 1024
// Declaring device and block specific variable names
__device__ double devPos;
__shared__ double shrPosX[NT];
__shared__ double shrPosY[NT];
__shared__ double shrPosZ[NT];

/*
 * Method : atomicAdd
 * 
 * Description: This method is used to perform atomicAdd on two double values.
 * 
 * @Parameters
 *      v: pointer to a double variable indicating target
 *      value: double value indicating the value to be added.
 *
*/
__device__ void atomicAdd(double *v, double value) {
    double oldval, newval; 
    do {
        oldval = *v; 
        newval = oldval + value; 
    } while (atomicCAS((unsigned long long int *)v, 
        __double_as_longlong (oldval), 
        __double_as_longlong (newval)) 
        != __double_as_longlong (oldval)); 
}

/*
 * Method : RandomWalkGpu
 * 
 * Description: The kernel starts.
 * 
 * @Parameters
 *      N: indiacates the number of individual members to be calculated
 *      T: indicates the number of iterations to be performed
 *      seed: indicates the seed value for the random number generation
 *
*/
extern "C" __global__ void RandomWalkGpu 
(unsigned long long int N, 
unsigned long long int T,
unsigned long long int seed) {
    int thr, rank;
    double xCoor=0;
    double yCoor=0;
    double zCoor=0;
    double dist=0;
    prng_t prng;
    
    thr=threadIdx.x;
//  int  size=gridDim.x*NT;
    rank=blockIdx.x*NT + thr;
    prngSetSeed (&prng, seed + rank);
    


        for (unsigned long long int i=thr; i<T; i+=NT) {
            int step=prngNextInt(&prng, 6);
//            printf("==%d", step);
            if (step==0) {
                xCoor+=1;
            } else if (step==1) {
                xCoor-=1;
            } else if (step==2) {
                yCoor+=1;
            } else if (step==3) {
                yCoor-=1;
            } else if (step==4) {
                zCoor+=1;
            } else {
                zCoor-=1;
            }
        }
        shrPosX[thr]=xCoor;
        shrPosY[thr]=yCoor;
        shrPosZ[thr]=zCoor;

    __syncthreads();

    for (int i=NT/2; i>0; i>>=1) {
        if (thr<i) {
//            printf("==%d\n", i);
            shrPosX[thr]+=shrPosX[thr+i];
            shrPosY[thr]+=shrPosY[thr+i];
            shrPosZ[thr]+=shrPosZ[thr+i];
        }
        __syncthreads();
    }
    
    if (thr==0) {
//        printf("--%f %f %f\n", shrPosX[0], shrPosY[0], shrPosZ[0]);
        double xSquared, ySquared, zSquared;
        xSquared=shrPosX[0]*shrPosX[0];
        ySquared=shrPosY[0]*shrPosY[0];
        zSquared=shrPosZ[0]*shrPosZ[0];

        dist+=sqrt(xSquared+ySquared+zSquared);
        atomicAdd (&devPos, dist);      
    }

}
