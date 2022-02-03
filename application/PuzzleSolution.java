/* Ethan Rich
 * ASU ID: 1221664977
 * Honors Contract Project - Picross
 * CSE 205 - MWF 10:10 - 11:00
 * */
package application;

//imports
import java.util.ArrayList;
import javafx.scene.layout.StackPane;

/**PuzzleSolution stores information about the current puzzle. It randomly generates
 * a random 2D array of 1's and 0's that describe the solution. The class also generates
 * Arrays that contain the hints for the rows and columns, and can compare the state of
 * the PuzzlePane to the solution. */
public class PuzzleSolution {

	//Instance Variables
	private int[][] puzzleSolution, currentPuzzle;
	private int dimension;
	
	//Constructor
	public PuzzleSolution(int dimension) {
		this.dimension = dimension;
		puzzleSolution = new int[dimension][dimension];
		currentPuzzle = new int[dimension][dimension];
		//Randomly add 1's to the solution at 50% of the time, set current puzzle to array of zeros
		for (int r = 0; r < dimension; r++) {
			for (int c = 0; c < dimension; c++) {
				puzzleSolution[r][c] = (int) Math.round(Math.random());
				currentPuzzle[r][c] = 0;
			}
		}
	}
	//Setter - PuzzleSolution
	public void setPuzzleSolution(int[][] inputPuzzle) {
		for (int r = 0; r < inputPuzzle.length; r++) {
			for (int c = 0; c < inputPuzzle[r].length; c++) {
				puzzleSolution[r][c]=inputPuzzle[r][c];
			}
		}
	}
	
	//Getters - PuzzleSolution
	public int[][] getPuzzleSolution() {
		return puzzleSolution;
	}
	
	public int[] getPuzzleSolutionRow(int row) {
		return puzzleSolution[row];
	}
	
	public int[] getPuzzleSolutionColumn(int column) {
		int[] puzzleColumn = new int[dimension];
		for (int row = 0; row < dimension; row++) {
			puzzleColumn[row] = puzzleSolution[row][column];
		}
		return puzzleColumn;
	}
	
	//Getters - CurrentPuzzle
	public int[][] getCurrentPuzzle() {
		return currentPuzzle;
	}
	
	public int[] getCurrentPuzzleRow(int row) {
		return currentPuzzle[row];
	}
	
	public int[] getCurrentPuzzleColumn(int column) {
		int[] puzzleColumn = new int[dimension];
		for (int row = 0; row < dimension; row++) {
			puzzleColumn[row] = currentPuzzle[row][column];
		}
		return puzzleColumn;
	}
	
	//Getters - Hints
	public ArrayList<Integer> getSolutionHintRow(int row) {
		
		ArrayList<Integer> hintRow = new ArrayList<Integer>();
		int count = 0;
		
		//get a count of how many consecutive 1's are in the puzzle, then add that count to the list
		for (int column = 0; column < dimension; column++) {
			if (puzzleSolution[row][column] == 1) {
				count++;
			} else if (count != 0) {
				hintRow.add(count);
				count = 0;
			}
		}
		//After the loop, if there's some number in the count, add it as well
		if (count != 0) {
			hintRow.add(count);
		} else if (hintRow.size() == 0) {
			hintRow.add(0);
		}
		
		return hintRow;
	}
	
	public ArrayList<Integer> getSolutionHintColumn(int column) {
		
		//Same process as the hintRow version
		ArrayList<Integer> hintColumn = new ArrayList<Integer>();
		int[] solutionColumn = getPuzzleSolutionColumn(column);
		int count = 0;
		
		for (int row = 0; row < dimension; row++) {
			if (solutionColumn[row] == 1) {
				count++;
			} else if (count != 0) {
				hintColumn.add(count);
				count = 0;
			}
		}
		if (count != 0) {
			hintColumn.add(count);
		} else if (hintColumn.size() == 0) {
			hintColumn.add(0);
		}
		
		return hintColumn;
	}

	public ArrayList<Integer> getCurrentHintRow(int row) {
		
		ArrayList<Integer> hintRow = new ArrayList<Integer>();
		int count = 0;
		
		for (int column = 0; column < dimension; column++) {
			if (currentPuzzle[row][column] == 1) {
				count++;
			} else if (count != 0) {
				hintRow.add(count);
				count = 0;
			}
		}
		if (count != 0) {
			hintRow.add(count);
		} else if (hintRow.size() == 0) {
			hintRow.add(0);
		}
		
		return hintRow;
	}
	
	public ArrayList<Integer> getCurrentHintColumn(int column) {
		
		ArrayList<Integer> hintColumn = new ArrayList<Integer>();
		int[] currentPuzzleColumn = getCurrentPuzzleColumn(column);
		int count = 0;
		
		for (int row = 0; row < dimension; row++) {
			if (currentPuzzleColumn[row] == 1) {
				count++;
			} else if (count != 0) {
				hintColumn.add(count);
				count = 0;
			}
		}
		if (count != 0) {
			hintColumn.add(count);
		} else if (hintColumn.size() == 0) {
			hintColumn.add(0);
		}
		
		return hintColumn;
	}
	
	//Getters - Others
	public int getDimension() {
		return dimension;
	}
	
	//Other Methods
	public void generateNewPuzzleSolution() {
		for (int r = 0; r < dimension; r++) {
			for (int c = 0; c < dimension; c++) {
				puzzleSolution[r][c] = (int) Math.round(Math.random());
			}
		}
	}
	
	public void updateCurrentPuzzle(ArrayList<StackPane> cellList) {
		int index = 0;
		for (int r = 0; r < dimension; r++) {
			for (int c = 0; c < dimension; c++) {
				currentPuzzle[c][r] = ((PicrossCell)cellList.get(index).getChildren().get(0)).getState();
				index++;
			}
		}
	}
	
	public boolean isCurrentSolved() {
		
		//If the hint rows and columns of both the solution and the current puzzle are the same, return true.
		for (int r = 0; r < dimension; r++) {
			ArrayList<Integer> solHintRow = getSolutionHintRow(r);
			ArrayList<Integer> curHintRow = getCurrentHintRow(r);
			if (solHintRow.size() != curHintRow.size()) {
				return false;
			}
			for (int i = 0; i < solHintRow.size(); i++) {
				if (solHintRow.get(i)!=curHintRow.get(i))
					return false;
			}
		}
		
		for (int c = 0; c < dimension; c++) {
			ArrayList<Integer> solHintCol = getSolutionHintColumn(c);
			ArrayList<Integer> curHintCol = getCurrentHintColumn(c);
			if (solHintCol.size() != curHintCol.size())
				return false;
			for (int i = 0; i < solHintCol.size(); i++) {
				if (solHintCol.get(i)!=curHintCol.get(i))
					return false;
			}
		}
		return true;
	}
	
	public String arrayToString(int[][] array) {
		String output = "";
		for (int r = 0; r < array.length; r++) {
			output+="{";
			for (int c = 0; c < array[r].length; c++) {
				output+=array[r][c]+",";
			}
			output+="}\n";
		}
		return output;
	}
	
	public String toString() {
		String output = "";
		for (int r = 0; r < puzzleSolution.length; r++) {
			output += "{";
			for(int c = 0; c < puzzleSolution[r].length; c++) {
				output = output + puzzleSolution[r][c] + ",";
			}
			output += "}\n";
		}
		return output;
	}
	
	
}
