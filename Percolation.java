
public class Percolation {
	
	private WeightedQuickUnionUF wqu;
	private boolean[] statusArray;
	private int side;

	private int toLinear(int x, int y){
		int row = x;
		int col = y;
		return (side*(row-1) + col);
	}
	
	/*
	 * the boundary checker
	 */	
	private void checkDirectionAndCallUnionOn(int row , int col){
		if(northOK(row , col)) connectNorth(row , col);
		if(southOK(row , col)) connectSouth(row , col);
		if(westOK(row , col)) connectWest(row , col);
		if(eastOK(row , col)) connectEast(row , col);
	}
	private boolean northOK(int row , int col){
		return ( col > 1 && isOpen(row,(col - 1)));
	}
	private boolean southOK(int row , int col){
		return ( col < side && isOpen(row,(col + 1)));
	}
	private boolean westOK(int row , int col){
		return ( row > 1 && isOpen((row - 1 ),col));
	}
	private boolean eastOK(int row , int col){
		return ( row < side && isOpen((row + 1 ),col));
	}
	private void connectNorth(int row , int col){
		wqu.union(toLinear(row , col), toLinear(row, (col - 1)));
	}
	private void connectSouth(int row , int col){
		wqu.union(toLinear(row , col), toLinear(row, (col + 1)));
	}
	private void connectWest(int row , int col){
		wqu.union(toLinear(row , col), toLinear((row - 1), col));
	}
	private void connectEast(int row , int col){
		wqu.union(toLinear(row , col), toLinear((row + 1), col));
	}
	
	public Percolation(int N){ // create N-by-N grid , with all sites blocked
		
		side = N;
		wqu = new WeightedQuickUnionUF(N*N + 2); // N*N + virtual top + virtual bottom cells
		statusArray = new boolean[N*N + 2];
		
		for(int i = 0 ; i < statusArray.length ; i++){
			statusArray[i] = false; // all blocked
		}
		for(int c = 1 ; c <= N ; c++){
			wqu.union(0, toLinear(1,c)); // 0 represents virtual top , 1 == 1st row , c == column
			wqu.union(N*N + 1 , toLinear(N,c)); // N*N + 1 represents virtual bottom , N == last row , c == column
		}	
	}
	public void open(int row,int col){ // open a site on NxN board , if it not open already
		if( row < 1 || row > side || col < 1 || col > side){
			throw new java.lang.IndexOutOfBoundsException();
		}
		checkDirectionAndCallUnionOn(row , col);
		statusArray[toLinear(row,col)] = true;
		
	}
	public boolean isOpen(int row, int col){ // check if the site is open
		if( row < 1 || row > side || col < 1 || col > side){
			throw new java.lang.IndexOutOfBoundsException();
		}
		return statusArray[toLinear(row,col)];
	}
	public boolean isFull(int row , int col){ // is the site full ?
		/*
		 *  SIGNIFICANT POINT : 
		 *  originally , i wrote this test as : 
		 *  return wqu.connected(0, toLinear(row, col)); // THIS WAS A BUG!
		 *  because i had logically connected the top row to the virtual top in the constructor ( that was the correct thing to do),
		 *  and the above code , since it only checked for connection and not if the site was OPEN or not ,
		 *  it made my code completely wrong! by automatically making the top column FULL , without any row even being OPEN!
		 *  and hence i never got a good score in the coursera tests. 
		 *  suffered a lot in the "CORRECTNESS" part of the code testing at princeton's auto code tester.
		 */
		return isOpen(row, col) && wqu.connected(0, toLinear(row, col));
	}
	public boolean percolates(){
		return wqu.connected(0, side*side+1);
	}
}
