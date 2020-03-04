import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class board implements Serializable{
	private static final long serialVersionUID = 1L;
	final int SUM_TOTAL = 15;
	String clientName;
	int[][] board;
	int[] inputData;
	int gameWon; // Variable for if game is over or not
	int userWon; // Variable for which PARTICULAR user won or not. (I used the temr userWon in server shared mem by accident. That
				 // one is unrelated sorry
	int typeThread; //Added value specifically for part b. Coordinates which client starts first.
	
	public board(){
		inputData = new int[3];
		board = new int[3][];
		gameWon = 0;
		for (int i=0;i<3;i++){
			int[] row = new int[3];
			board[i]=row;
		}
	}
	public void setTypeThread(int n){ //typeThread setter method. Will be 0 or 1. 0 is player that moves first, 
		typeThread = n;				  // 1 is player that moves second
	}
	
	/*public void add1(int number,int row,int column){
		board[row][column] = number;
	}*/
	
	public int add(){
		int number = inputData[0];
		int row = inputData[1];
		int column = inputData[2];
		board[row][column] = number;
		
		if(this.rowCheck(row) || this.columnCheck(column) || this.diagonalCheck(row,column)){
			return 1;
			
		}else{
			return 0;
		}
	}
	public Boolean rowCheck(int r){
		int sum = 0;
		int filled = 1;
		for (int i=0;i<3;i++){
			sum+=board[r][i];
			if(board[r][i] == 0)
				filled = 0;
		}
		return (sum == SUM_TOTAL && filled == 1);
	}
	
	public Boolean columnCheck(int c){
		int sum = 0;
		int filled = 1;
		for (int i=0;i<3;i++){
			sum+=board[i][c];
			if(board[i][c] == 0)
				filled = 0;
		}
		return (sum == SUM_TOTAL && filled == 1);
	}
	
	public Boolean diagonalCheck(int r, int c){
		int sum = 0 , sum2 = 0;
		int orientation = Math.abs(r-c); //checks to scan which horizontal. 
		int filled = 1 , filled2 = 1;
		
		if(orientation == 1) //Checks in case non diagonal parts are chosen
			return false;
		
		if (orientation == 0 && !(r==1 && c==1) ){ 
			for (int i=0;i<3;i++){
				sum+=board[i][i];
				if(board[i][i] == 0)
					filled = 0;
			}
			
		}else if (orientation == 2 && !(r==1 && c==1) ){ //If r and c equals (2,2) then need to check both orientations
			if(sum != 0) sum = 0; //resets sum for (2,2) 
			
			for (int i=0;i<3;i++){
				sum+=board[i][2-i];
				if(board[i][2-i] == 0)
					filled = 0;
			}	
			
		}else if ( r==1 && c==1 ){
			for (int i=0;i<3;i++){
				sum+=board[i][i];
				if(board[i][i] == 0)
					filled = 0;
			}
			
			for (int i=0;i<3;i++){
				sum2+=board[i][2-i];
				if(board[i][2-i] == 0)
					filled2 = 0;
			}	
		}
		
		return ( (sum==SUM_TOTAL && filled == 1) || (sum2==SUM_TOTAL && filled2 == 1) );
	}
	
	public void getInput() throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			System.out.println("User please put down your move. The number 0 is not allowed.");
			System.out.println("The syntax is as follows: number row# column# ");
			System.out.println("Note: First row starts at 0. Spaces are also included. ");
			
			int number,row,column; //Takes inputs from user and turns them into numbers we need
			String input = reader.readLine();
			String[] tokens;
			tokens = input.split(" ");
			
			try{
				number=Integer.parseInt(tokens[0]);
				if(number==0) throw new NumberFormatException(); // 0 is not valid input
				row=Integer.parseInt(tokens[1]);				
				column=Integer.parseInt(tokens[2]);
				inputData[0]=number; 
				inputData[1]=row;
				inputData[2]=column;
				
				System.out.printf("You have placed %d at row %d and at column %d \n \n", number, row, column);
				//System.out.println("The board now is: ");
				//this.prettyPrint();	
				break;
				
			}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
				System.out.println("You put the wrong input. Please try again.");
			}
		}
	}
	
	public void prettyPrint(){
		String row1, row2 , row3;
		String line = "\n"+"-------------"+ "\n";
		row1 = "| " + board[0][0] + " | " + board[0][1] + " | " + board[0][2] + " |";
		row2 = "| " + board[1][0] + " | " + board[1][1] + " | " + board[1][2] + " |";
		row3 = "| " + board[2][0] + " | " + board[2][1] + " | " + board[2][2] + " |";
		
		System.out.println(line+row1 +line+row2+line+row3+line);
	}
	
	public static Boolean compareBoard(board b1, board b2){
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(b1.board[i][j] != b2.board[i][j]) return false;		
			}			
		}
		return true;
	}
	
	public void copyBoard(board b1){
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				this.board[i][j] = b1.board[i][j];
			}			
		}
	}
	

}
