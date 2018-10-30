/**
 * The constructor should take time proportional to N2; all methods should take constant time 
 * plus a constant number of calls to the union-find methods union(), find(), connected(), and count().
 */
public class Percolation {
    
    private static int count;
    private QuickFindUF quf;
    private Cell topCell, bottomCell;
    private Cell[][] grid;

    /**
     * create N-by-N grid, with all sites blocked
     * 
     * should take time proportional to N^2
     */
    public Percolation(int N) throws IllegalArgumentException {
        if (N <= 0)
            throw new IllegalArgumentException("N may not be less than or equal to zero.");
        
        count = N;
        int qufLength = Double.class.cast(Math.pow(N,2)).intValue() + 2;
        quf = new QuickFindUF(qufLength);
        grid = new Cell[N][N];
        
        Cell site;
        // Initialize all sites to be blocked (open = false; default) and, in the QUF array, pointing to themselves
        int qufCount = 1; // topCell has quf id 0
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                site = new Cell();
                site.setRow(x); site.setCol(y);
                site.setId(qufCount++); // reference to qufArray, which holds connected state
                grid[x][y] = site;
            }
        }
        
        topCell = new Cell();
        topCell.setId(0); // quf entry 0
        topCell.setOpen(true);
        for (int x = 0; x < N; x++)
            quf.union( grid[0][x].getId(), 0 ); // connect first row of grid to topCell

        bottomCell = new Cell();
        bottomCell.setId(Double.class.cast((Math.pow(N,2)+1)).intValue()); // quf last entry
        for (int x = 0; x < N; x++)
            quf.union( grid[N-1][x].getId(), Double.class.cast((Math.pow(N,2)+1)).intValue() ); // connect Nth row of grid to bottomCell
        bottomCell.setOpen(true);
    }
    
    /**
     * open site (row i, column j) if it is not already
     * 
     * should take constant time plus a constant number of calls to the union-find methods union(), find(), connected(), and count()
     */
    public void open(int i, int j) throws IndexOutOfBoundsException {
        
        // the indices i and j are integers between 1 and N, where (1, 1) is the upper-left site: 
        // throw a java.lang.IndexOutOfBoundsException if either i or j is outside this range
        if (i < 0 || j < 0 || i > count || j > count)
            throw new IndexOutOfBoundsException("i or j are outside range 0 to " + count);
        
        if (!isOpen(i, j)) {
            grid[i][j].setOpen(true);
            
            // connect to neighbors
            if (i > 0 && isOpen(i-1,j))
                quf.union(grid[i][j].getId(), grid[i-1][j].getId()); // union with cell above (if any)
            if (i < count-1 && isOpen(i+1,j))
                quf.union(grid[i][j].getId(), grid[i+1][j].getId()); // union with cell below (if any)
            if (j > 0 && isOpen(i,j-1))
                quf.union(grid[i][j].getId(), grid[i][j-1].getId()); // union with cell to the left (if any)
            if (j < count-1 && isOpen(i,j+1))
                quf.union(grid[i][j].getId(), grid[i][j+1].getId()); // union with cell to the right (if any)
        }
    }
    
    /**
     * is site (row i, column j) open?
     * 
     * should take constant time plus a constant number of calls to the union-find methods union(), find(), connected(), and count()
     */
    public boolean isOpen(int i, int j) throws IndexOutOfBoundsException {
        // the indices i and j are integers between 1 and N, where (1, 1) is the upper-left site:
        // throw a java.lang.IndexOutOfBoundsException if either i or j is outside this range
        if (i < 0 || j < 0 || i > count || j > count)
            throw new IndexOutOfBoundsException("i or j are outside range 0 to " + count);
        
        return grid[i][j].isOpen();
    }

    /**
     * is site (row i, column j) full?
     * 
     * A full site is an open site that can be connected to an open site in the top row via a chain of neighboring (left, right, up, down) open sites
     * 
     * should take constant time plus a constant number of calls to the union-find methods union(), find(), connected(), and count()
     */
    public boolean isFull(int i, int j) throws IndexOutOfBoundsException {

        System.out.println("isFull: i,j: " + i + "," + j);

        // the indices i and j are integers between 1 and N, where (1, 1) is the upper-left site: 
        // Throw a java.lang.IndexOutOfBoundsException if either i or j is outside this range
        if (i < 0 || j < 0 || i > count || j > count)
            throw new IndexOutOfBoundsException("i or j are outside range 0 to " + count);
        
        return quf.connected(0, grid[i][j].getId());
    }

    /**
     * does the system percolate?
     * 
     * A system percolates if we fill all open sites connected to the top row and that process fills some open site on the bottom row
     * 
     * should take constant time plus a constant number of calls to the union-find methods union(), find(), connected(), and count()
     */
    public boolean percolates() {
        return quf.connected(topCell.getId(), bottomCell.getId());
    }

    private Cell getCellFromQufId(int qufId) {
        int row = (qufId-1)/count;
        int col = (qufId-1) % count;
        return grid[row][col];
    }

    /**
     * class runner
     */
    public double getPercolationThreshold() {
        int openCount = 0, rand, x, y;
        Cell randomCell;
        double cellsInGrid = Math.pow(count,2);

        // Repeat the following until the system percolates:
        while (!percolates() && openCount < cellsInGrid+1) {
            do {
                rand = StdRandom.uniform(1, Double.class.cast(Math.pow(count,2)).intValue()+1);
            } while (getCellFromQufId(rand).isOpen() && openCount < Math.pow(count,2));
            randomCell = getCellFromQufId(rand);
            if (!randomCell.isOpen()) {
                x = randomCell.getRow();
                y = randomCell.getCol();
                open(x,y);
                openCount++;
            }
        }
        double percThr = openCount/cellsInGrid;
        return percThr;
    }

    /**
     * test client
     * 
     * use standard random from our standard libraries to generate random numbers
     */
    public static void main(String[] args) {
        int someCount = StdIn.readInt();
        Percolation percolation = new Percolation(someCount);
        double percolationThreshold = percolation.getPercolationThreshold();

        // print the fraction of sites that are opened when the system percolates (provides an estimate of the percolation threshold)
        System.out.println("Estimated percolation threshold: " + percolationThreshold);
    }

    class Cell {
        int id = 0; // maps the cell to the QuickFindUF array
        int row;
        int col;
        boolean open = false;

        public void setId(int id) { this.id = id; }
        public int getId() { return this.id; }

        public void setRow(int row) { this.row = row; }
        public int getRow() { return this.row; }

        public void setCol(int col) { this.col = col; }
        public int getCol() { return this.col; }

        public void setOpen(boolean open) { this.open = open; }
        public boolean isOpen() { return this.open; }
    }
}

