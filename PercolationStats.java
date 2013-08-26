public class PercolationStats {
	private double mean;
	private double stddev;
	private double confLo;
	private double confHi;
	private Percolation perc;
	private double arr[]; // stores threshold of each percolation

	private int countOpenSites(Percolation p, int N) {
		int openSiteCount = 0;
		// there will be N*N (+2) sites from 1 to N*N. 0 and N*N+1 are virtual
		// sites.
		for (int row = 1; row <= N; row++) {
			for (int col = 1; col <= N; col++) {
				if (p.isOpen(row, col))
					openSiteCount++;
			}
		}
		return openSiteCount;
	}

	private double calculateThreshold(Percolation p, int N) {
		int openSites = countOpenSites(p, N);
		double t = (double) openSites / (N * N);
		return t;
	}

	private void calculateData() {
		mean = mean();
		stddev = stddev();
		confLo = confidenceLo();
		confHi = confidenceHi();
	}

	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(arr);
	}

	// sample std.dev of percolation threshold
	public double stddev() {
		return StdStats.stddev(arr);
	}

	// returns lower bound of 95% confidence level
	public double confidenceLo() {
		return (mean - (1.96 * stddev) / Math.sqrt(arr.length));
	}

	// returns upper bound of 95% confidence level
	public double confidenceHi() {
		return (mean + (1.96 * stddev) / Math.sqrt(arr.length));
	}

	// perform T independent computational experiments on an NxN grid
	public PercolationStats(int N, int T) {
		arr = new double[T];
		for (int i = 0; i < T; i++) {
			perc = new Percolation(N);
			arr[i] = calculateThreshold(perc, N);
		}
		calculateData();
	}

	public static void main(String[] args) {
		final long StartTime = System.currentTimeMillis();
		if (Integer.parseInt(args[0]) < 0 || Integer.parseInt(args[1]) < 0 ) {
			throw new java.lang.IllegalArgumentException();
		}
		PercolationStats P = new PercolationStats(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		System.out.println("mean						= " + P.mean);
		System.out.println("stddev						= " + P.stddev);
		System.out.println("95% confidence interval	= " + P.confLo + " , "
				+ P.confHi);
		System.out.println("\n\n Total time taken : "
				+ (System.currentTimeMillis() - StartTime));
	}

}
