

public class StringCompare {
	
	private static final boolean PRINT_MATRIX = true;
	
	//costs
	public static final int COST_DEL=1;
	public static final int COST_INS=1;
	public static final int COST_SUBST=1;
	public static final int COST_KEEP=0;
	
	
	private StringCompare() {}
	
	
	public static int getDistance(String x, String y){
		int[][] D = new int[y.length() + 1][x.length() + 1];	
				
		for(int i = 1; i < D[0].length; i++){
			D[0][i] = D[0][i-1] + COST_INS;
		}		
		for (int i = 1; i < D.length; i++){
			D[i][0] = D[i-1][0] + COST_DEL; 
		}		
		//main-loop
		for(int i = 1; i < D.length; i++){
			for(int j=1; j < D[i].length; j++){				
				//same chars??
				boolean sameChar = x.charAt(j-1) == y.charAt(i-1);				
				
				int diag = D[i-1][j-1] + (sameChar ? COST_KEEP : COST_SUBST);
				int del  = D[i-1][j]   + COST_DEL;
				int ins  = D[i][j-1]   + COST_INS;
				
				int min = Math.min(diag, del);
				min = Math.min(ins, min);				
				D[i][j] = min;				                      
			}
		}
		
		if (PRINT_MATRIX) printMatrix(D, x, y);
		
		return D[y.length()][x.length()];
	}
	
	public static String getLcs(String x, String y){
		int[][] D = new int[y.length() + 1][x.length() + 1];
		String lcs = "";	
		for(int i = 1; i < D[0].length; i++){
			D[0][i] = 0;
		}		
		for (int i = 1; i < D.length; i++){
			D[i][0] = 0; 
		}
		for(int i = 1; i < D.length; i++){
			for(int j=1; j < D[i].length; j++){				
				
				boolean sameChar = x.charAt(j-1) == y.charAt(i-1);				
				
				if (sameChar) {
					D[i][j] = D[i-1][j-1] + 1;
				} else {
					D[i][j] = Math.max(D[i][j-1],D[i-1][j]);
				}                      
			}
		}
		
		lcs = backtrack(D,x,y,x.length(),y.length());
		
		if (PRINT_MATRIX) printMatrix(D, x, y);
	
		return lcs;
		
	}
	
	private static String backtrack(int[][] D, String x, String y, int xi, int yi) {
		/*exit condition*/
		if (xi == 0 || yi == 0) return "";
		/*if chars are equal move up-left and append this char*/
		else if (x.charAt(xi-1) == y.charAt(yi -1))
			return backtrack(D,x,y,xi - 1, yi -1) + x.charAt(xi-1);
		else {
			/*if number on the left is bigger than the one above, move left.
			 *otherwise move up*/
			if (D[yi][xi-1] > D[yi-1][xi]) return backtrack(D,x,y,xi-1,yi);
			else return backtrack(D,x,y,xi,yi-1);
		}
	}

	private static void printMatrix(int[][] D, String x, String y){
		
		System.out.print("\t\t");
		for(int i = 0; i < x.length(); i++ ){
			System.out.print(x.charAt(i) + "\t");
		}
		System.out.println();
		int rowIdx = 0;
		for( int[] row : D) {
			if (rowIdx != 0)
			 System.out.print(y.charAt(rowIdx-1)+"\t");
			else
				System.out.print("\t");
			for( int col : row) {
				System.out.print( col + "\t");
			}
			System.out.println();
			rowIdx++;
		}
		
		
	}
	
	public static void main(String[] args) {		
		String x = "";
		String y = "";
		int option = 0;
		int argIdx = 0;
		
		if ( args.length < 2 || args.length > 3) {
			System.out.println("Usage: "+StringCompare.class.getName()+" [ 0 | 1 ] x y");
			System.out.println("Compare String 'x' to 'y'");
			System.out.println("\t 0: compute Levenshtein distance (default)");
			System.out.println("\t 1: compute Longest Common Sequence ");
			
			System.exit(2);
		}
		
		if( args.length == 3 ) {
			option = Integer.parseInt(args[argIdx++]);
		}			
		x = args[argIdx++];
		y = args[argIdx];				
		
		System.out.println("comparing: "+x+" <-> "+y );
		
		switch(option){
		case 0:
			System.out.println("distance: "+StringCompare.getDistance(x, y));	
			
			break;
		default:			
			System.out.println("lcs: "+StringCompare.getLcs(x, y));
			break;
		}
	}
	
}
