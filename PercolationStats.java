public class PercolationStats {
	private double mean;
	private double stddev;
	private double confLo;
	private double confHi;
	private double arr[]; // stores threshold of each percolation

	private double runPercolation(int N){
		int row , col;
		int opensites = 0;
		Percolation perc = new Percolation(N);
	
		while(true){			
			row = (int)( 1 + Math.random()*N );
			col = (int)( 1 + Math.random()*N );
//			++step;
			if( !perc.isOpen( row,col ) ){
//				String op = String.format("%2d. opening : %d,%d",step,row,col);
//				System.out.println(op);
				perc.open(row,col);
				++opensites;
				if(perc.percolates()){
//					System.out.println("System now percolates !\n total open points : " + opensites);
					break;
				}
			}else{
//				String op = String.format("%2d. site %d,%d is open",step,row,col);
//				System.out.println(op);
			}			
		}
		return (double)opensites/(N*N);
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
			arr[i] = runPercolation(N);
		}
		calculateData();
	}
	public static void main(String[] args) {
		final long StartTime = System.currentTimeMillis();
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		if(!(N>1 && T>1)){
			throw new IllegalArgumentException(" N , T both must be positive");
		}
		PercolationStats P = new PercolationStats(N,T);
		System.out.println("mean						= " + P.mean);
		System.out.println("stddev						= " + P.stddev);
		System.out.println("95% confidence interval	= " + P.confLo + " , " +P.confHi);
		System.out.println("\n\n Total time taken : " + (System.currentTimeMillis() - StartTime));
	}

}
