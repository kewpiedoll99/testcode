
public class PercolationStats {

    private final double[] percolationThresholds;
    private final int timeT;

    /**
     * perform T independent computational experiments on an N-by-N grid
     */
    public PercolationStats(int N, int T) {
        // throw an IllegalArgumentException if either N <= 0 or T <= 0.
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("N may not be less than zero; T may not be less than zero.");
        
        System.out.println("N: " + N + "; T: " + T);
        timeT = T;
        percolationThresholds = new double[T];
        double percThr;
        Percolation percolation;
        for (int i = 0; i < T; i++) {
           percolation = new Percolation(N);
           percThr = percolation.getPercolationThreshold();
//           System.out.println("Estimated percolation threshold " + i + ": " + percThr);
           percolationThresholds[i] = percThr;
        }
    }
    
    /**
     * sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    /**
     * sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }
    
    /**
     * returns lower bound of the 95% confidence interval
     * 
     * use standard random from our standard libraries to generate random numbers
     * use standard statistics to compute the sample mean (mu) and standard deviation (rho)
     * 
     * mean - (1.96 stddev / sq root T)
     */
    public double confidenceLo() {
        return (mean() - (1.96 * stddev())/ (Math.pow(timeT, 0.5)));
    }
    
    /**
     * returns upper bound of the 95% confidence interval
     * 
     * mu + (1.96 rho / sq root T)
     */
    public double confidenceHi() {
        return (mean() + (1.96 * stddev())/ (Math.pow(timeT, 0.5)));
    }
    
    /**
     * test client
     * takes two command-line arguments N and T, 
     * performs T independent computational experiments on an N-by-N grid, 
     * and prints out the mean, standard deviation, and the 95% confidence interval for the percolation threshold
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(N, T);

        System.out.println("mean:                   = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
    }
}
