/*
 * a GUI unit test to observe the functioning of Percolation program.
 * Dependencies : Percolation.java , and StdDraw.class
 */
public class percTester {
	StringBuilder s = new StringBuilder("");
	int N;
	String[] parsed;
	Percolation perc;
	private void createPoints(String delim) {
		for (int row = 1; row <= N; row++) {
			for (int col = 1; col <= N; col++) {
				s.append(String.format("%d %d%s", row, col , delim));
			}
		}
		System.out.println("created points : \n" + s.toString());
	}
	
	private void parsePoints(String delim) {
		String dataLine = s.toString();
		String pattern = String.format("[%s]+",delim);
		parsed = dataLine.split(pattern);
		knuthShuffle(parsed);
	}

	private void knuthShuffle(String[] parsed) {
		int N = parsed.length;
		int r;
		String temp;
		for (int i = 0; i < N; i++) {
			r = i + (int) (Math.random() * (N - i));
			temp = parsed[i];
			parsed[i] = parsed[r];
			parsed[r] = temp;
		}
	}
	
	private void setupCanvas(){
		System.out.println("arr side length : " + N);
		StdDraw.clear();
	    StdDraw.setPenColor(StdDraw.BLACK);
	    StdDraw.setXscale(0, N);
	    StdDraw.setYscale(0, N);
	    StdDraw.filledSquare(N/2.0, N/2.0, N/2.0);
	}
	
	private void paintIt(int row , int col , String status){
//		switch (status) {
//		case "open":
//			StdDraw.setPenColor(StdDraw.WHITE);
//			break;
//		case "full":
//			StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
//			break;
//		default:
//			break;
//		}
		for (row = 1; row <= N; row++) {
            for (col = 1; col <= N; col++) {
                if (perc.isFull(row, col)) {
                    StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                }
                else if(perc.isOpen(row, col)){
                	StdDraw.setPenColor(StdDraw.WHITE);
                }
                else{
                	StdDraw.setPenColor(StdDraw.BLACK);
                }
                StdDraw.filledSquare(col - 0.5, N - row + 0.5, 0.45);
            }
        }
		
		StdDraw.filledSquare(col - 0.5, N - row + 0.5, 0.45);
	}

	private void drawSites() {
		int row, col;
		int i = 0;
		String status;
		String[] points;
//		try{
			while (i < parsed.length) {
				points = parsed[i++].split("\\s+");
				if(points.length != 2){
					throw new java.lang.IllegalArgumentException("points[] size must be equal to 2. but it is : " + points.length);
				}
				row = Integer.parseInt(points[0]);
				col = Integer.parseInt(points[1]);
				System.out.println("opening : " + row + "," + col);				
				if (!perc.isOpen(row, col)) {
					perc.open(row, col);
					status = "open";
					if(perc.isFull(row, col)) status = "full";
					paintIt(row, col, status);
//					Thread.sleep(20);
					if (perc.percolates()) {
						System.out.println("system percolates");
						status = "percolates";
						paintIt(row, col, status);
						break;
					}
				} else {
					System.out.println(row + "," + col + " is open.");
				}
			}
//		}catch(InterruptedException e){ e.printStackTrace();}
		
	}

	public percTester(int N) {
		String delim = "|";
		this.N = N;
		perc = new Percolation(N);
		createPoints(delim);
		parsePoints(delim);
		setupCanvas();
		drawSites();
	}

	public static void main(String[] args) {
		final long StartTime = System.currentTimeMillis();
		new percTester(15);
		System.out.println("\n\n Total time taken : " + (System.currentTimeMillis() - StartTime));
	}
}
