/* Ethan Rich
 * Honors Contract Project - Picross
 * CSE 205 
 */
package application;

//Imports
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**PicrossCell extends StackPane. PicrossCell stores the state of the cell.
 * This state will determine what shape is displayed in the box. 
 * The state can be updated in the updateCell method.**/
public class PicrossCell extends StackPane {

	//instance Variables
	private int state;									//state 0: empty, state 1: box, state 2: X'ed, state 3: circle
	private int cellSize;								//The size in pixels of the StackPane PicrossCell object
	
	//Constructors
	public PicrossCell(int state, int cellSize) {		
		this.cellSize = cellSize;						
		this.setAlignment(Pos.CENTER);					//Sets the alignment of children to the center
		this.setMinSize(cellSize, cellSize);			//Sets the size of the cell to dimensions of the cellSize as a square
		updateCell(state);								//Updates the Cell's state
	}
	
	//Getters
	public int getState() {
		return state;
	}
	
	public int getCellSize() {
		return cellSize;
	}
	
	//Utility Methods
	public void clearCell() {			
		this.getChildren().clear();						//Clears the Arraylist of children of PicrossCell
	}
	
	public void drawBox() {								//Adds a black 180x180 square to the Cell
		this.getChildren().add(new Rectangle((int)(0.8*cellSize),(int)(0.8*cellSize),ColorTheme.getItemColor()));
	}
	
	public void drawX() {								//Adds two lines to the Cell
		Line upLine = new Line((int)(0.2*cellSize), (int)(0.8*cellSize), (int)(0.8*cellSize), (int)(0.2*cellSize));
		Line downLine = new Line((int)(0.2*cellSize), (int)(0.2*cellSize), (int)(0.8*cellSize), (int)(0.8*cellSize));
		upLine.setStroke(ColorTheme.getItemColor());
		downLine.setStroke(ColorTheme.getItemColor());
		upLine.setStrokeWidth((int)(0.15*cellSize));
		downLine.setStrokeWidth((int)(0.15*cellSize));
		this.getChildren().add(upLine);	
		this.getChildren().add(downLine);
	}
	
	public void drawCircle() {
		Circle circle = new Circle(cellSize/2,cellSize/2,0.3*cellSize);
		circle.setFill(Color.TRANSPARENT);
		circle.setStroke(ColorTheme.getItemColor());
		circle.setStrokeWidth(0.15*cellSize);
		this.getChildren().add(circle);
	}
	
	//Update Cell method draws or clears the Cell based on the state inputed. Then, assigns the local var state.
	public void updateCell(int newState) {
		switch (newState) {
			case 0: 
				clearCell(); 
				break;
			case 1:
				clearCell();
				drawBox();
				break;
			case 2:
				clearCell();
				drawX();
				break;
			case 3:
				clearCell();
				drawCircle();
				break;
			default:
				System.out.println("Invalid state integer. Cell not updated.");
				return;
		}
		this.state = newState;
	}
	
	//Override object methods
	public boolean equals(PicrossCell input) {	//Checks if inputted PicrossCell object has the same state 
		if (input.getState()==this.state) {
			return true;
		}
		return false;
	}
	
	public boolean equals(int state) { 			//Checks if the inputted state is the same state as this PicrossCell	
		if (state == this.state) {
			return true;
		}
		return false;
	}
	
	public String toString() {					//Returns the state of this PicrossCell
		return "PicrossCell - State: " + state;
	}
	
}
