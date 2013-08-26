public class Percolation {

	private class Point {
		private int x;
		private int y;

		private Point(int N) { // for auto generation
			x = (int) (1 + Math.random() * N);
			y = (int) (1 + Math.random() * N);
		}

		private Point(int x, int y) { // co-ords provided by user
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return String.format("%d,%d", x, y);
		}
	}

	private WeightedQuickUnionUF wqu;
	private boolean[] statusArray;
	private int side;

	// private static int count = 0;

	/*
	 * all methods of WeightedQuickUnionFind are linear array based. however,
	 * here the the array (N*N) is 2-D , and array calculations are based on [1
	 * to N] naming convention. hence to make them inter-compatible , we use the
	 * toLinear method.
	 */
	private int toLinear(Point point) {
		int row = point.x;
		int col = point.y;
		return (side * (row - 1) + col);
	}

	private int toLinear(int x, int y) {
		int row = x;
		int col = y;
		return (side * (row - 1) + col);
	}

	/*
	 * called from the constructor.
	 */
	private void setup(int N) {
		side = N;
		wqu = new WeightedQuickUnionUF(N * N + 2); // N*N + virtual top +
													// virtual bottom cells
		statusArray = new boolean[N * N + 2];

		for (int i = 0; i < statusArray.length; i++) {
			statusArray[i] = false; // all blocked
		}
		for (int c = 1; c <= N; c++) {
			wqu.union(0, toLinear(1, c)); // 0 represents virtual top , 1 == 1st
											// row , c == column
			wqu.union(N * N + 1, toLinear(N, c)); // N*N + 1 represents virtual
													// bottom , N == last row ,
													// c == column
		}
	}

	/*
	 * the boundary checker
	 */
	private void checkDirectionAndCallUnionOn(Point p) {
		if (northOK(p))
			connectNorth(p);
		if (southOK(p))
			connectSouth(p);
		if (westOK(p))
			connectWest(p);
		if (eastOK(p))
			connectEast(p);
	}

	private boolean northOK(Point p) {
		return (p.y > 1 && isOpen(p.x, (p.y - 1)));
	}

	private boolean southOK(Point p) {
		return (p.y < side && isOpen(p.x, (p.y + 1)));
	}

	private boolean westOK(Point p) {
		return (p.x > 1 && isOpen((p.x - 1), p.y));
	}

	private boolean eastOK(Point p) {
		return (p.x < side && isOpen((p.x + 1), p.y));
	}

	private void connectNorth(Point p) {
		wqu.union(toLinear(p), toLinear(p.x, (p.y - 1)));
	}

	private void connectSouth(Point p) {
		wqu.union(toLinear(p), toLinear(p.x, (p.y + 1)));
	}

	private void connectWest(Point p) {
		wqu.union(toLinear(p), toLinear((p.x - 1), p.y));
	}

	private void connectEast(Point p) {
		wqu.union(toLinear(p), toLinear((p.x + 1), p.y));
	}

	public Percolation(int N) { // create N-by-N grid , with all sites blocked

		setup(N);
		while (true) {

			Point p = new Point(N);
			// System.out.println(p);
			int row = p.x;
			int col = p.y;

			if (!isOpen(row, col)) {
				// System.out.println("opening : " + p);
				open(row, col);
				// count++;
				checkDirectionAndCallUnionOn(p);
				if (percolates()) {
					// System.out.println("System now percolates !\n total open points : "
					// + count);
					break;
				}
			} else {
				// System.out.println(p + " is open.");
			}
		}
	}

	public void open(int row, int col) { // open a site on NxN board , if it not
											// open already
		if (row < 1 || row > side || col < 1 || col > side) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		if(!isOpen(row, col))
			statusArray[toLinear(row, col)] = true;

	}

	public boolean isOpen(int row, int col) { // check if the site is open
		if (row < 1 || row > side || col < 1 || col > side) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		return statusArray[toLinear(row, col)];
	}

	public boolean isFull(int row, int col) { // is the site full ?
		if (row < 1 || row > side || col < 1 || col > side) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		return (wqu.connected(0, toLinear(row, col)) && isOpen(0, toLinear(row, col)));
	}

	public boolean percolates() {
		return wqu.connected(0, side * side + 1);
	}

	public static void main(String[] args) {
		new Percolation(4);
	}
}
