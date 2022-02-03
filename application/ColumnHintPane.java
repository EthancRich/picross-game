/* Ethan Rich
 * ASU ID: 1221664977
 * Honors Contract Project - Picross
 * CSE 205 - MWF 10:10 - 11:00
 * */
package application;

//Imports
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.*;

/**ColumnHintPane extends StackPane. ColumnHintPane generates the hint numbers for the columns
 *  of the puzzle and displays them in a gridPane. **/
public class ColumnHintPane extends StackPane {

	//Instance Variables
	private int cellSize, dimension, maxHintLineLength;										
	private ArrayList<ArrayList<Integer>> columnHintNumbers;
	private ArrayList<ArrayList<Label>> columnHintLabels;
	private PuzzleSolution puzzleSolution;
	private GridPane columnPane;
	
	//Constructor
	public ColumnHintPane(PuzzleSolution puzzleSolution) {
		
		//Instantiation and Initialization
		dimension = puzzleSolution.getDimension();											//dimension is the n x n value of the puzzle grid								
		cellSize = 450/dimension;															//cellSize is the size of each of the cells
		maxHintLineLength = 0;																//Describes the longest hintLine in the columnHintNumbers
		this.puzzleSolution = puzzleSolution;							
		
		columnHintNumbers = new ArrayList<ArrayList<Integer>>();							//Initialize the lists of lists
		columnHintLabels = new ArrayList<ArrayList<Label>>();
				
		//Preparing columnHintNumbers and columnHintLabels
		for (int i = 0; i < dimension; i++) {												
			columnHintNumbers.add(puzzleSolution.getSolutionHintColumn(i));					//Add each hint column to columnHintNumbers
			columnHintLabels.add(new ArrayList<Label>());									//Add a new LabelList for each column
			if (puzzleSolution.getSolutionHintColumn(i).size() > maxHintLineLength) {		//When the length of a hint line is larger than current, record it
				maxHintLineLength = puzzleSolution.getSolutionHintColumn(i).size();
			}
		}
		
		//Set each Label in hintLabels to the int in hintNumbers
		for (int line = 0; line < dimension; line++) {							
			for (Integer hintNum : columnHintNumbers.get(line)) {
				Label hintLabel = new Label(""+hintNum);									
				columnHintLabels.get(line).add(hintLabel);
			}
		}
		
		//Creating columnPane, then filling it with the appropriate content (this is a complex section)
		columnPane = new GridPane();
		for (int line = 0; line < dimension; line++) {										//For each col, position is the right side index, and ends after the maxHintLineLength
			for (int position = dimension-1; position >= dimension - maxHintLineLength; position--) {
									
				int index = (dimension - 1) - position;										//Index is the index of the columnHintLabels
				if (index < columnHintNumbers.get(line).size()) {
					/* If there is an hintNumber associated with this cell, create a Pane and add lines and a label for it */
					
					//Set up linePane
					Pane hintCell = new Pane();
					hintCell.setPrefSize(cellSize, cellSize);
					hintCell.setBackground(ColorTheme.getBackCell());
					hintCell.setOnMousePressed(new MouseHandlerClick()); 					//set the Pane to react upon being clicked
							
					//Set up Label
					Label hintLabel = columnHintLabels.get(line).get(columnHintNumbers.get(line).size()-index-1);
					switch (dimension) {
					case 5:																	//5x5 font and layout
						hintLabel.setFont(new Font("Arial", cellSize));
						hintLabel.setLayoutX(20);											
						hintLabel.setLayoutY(-7);
						break;
					case 10:																//10x10 font and layout
						if (hintLabel.getText().length() > 1) {								//Scale down the numbers w/ 2 digits
							hintLabel.setFont(new Font("Arial", cellSize*0.85));
							hintLabel.setLayoutX(0);
							hintLabel.setLayoutY(1);
						} else {															//Numbers w/ 1 digit
							hintLabel.setFont(new Font("Arial", cellSize));
							hintLabel.setLayoutX(9);										
							hintLabel.setLayoutY(-4);
						}
						break;
					case 15:																//15x15 font and layout
						if (hintLabel.getText().length() > 1) {								//Scale down the numbers w/ 2 digits
							hintLabel.setFont(new Font("Arial", cellSize*0.85));
							hintLabel.setLayoutX(0);
							hintLabel.setLayoutY(-1);
						} else {															//Numbers w/ 1 digit
							hintLabel.setFont(new Font("Arial", cellSize));
							hintLabel.setLayoutX(7);										
							hintLabel.setLayoutY(-5);
						}
						break;
					default:
						System.out.println("Puzzle is not a standard shape");
						hintLabel.setFont(new Font("Arial", cellSize));
						hintLabel.setLayoutX(9);										
						hintLabel.setLayoutY(-3);
						break;
					}

					hintLabel.setTextFill(ColorTheme.getTextColor());
					hintCell.getChildren().add(hintLabel); 									//hintLabel must be first in Pane so mouseHandlerClick works correctly

					//Define the lines that go above and below the Label
					Line leftLine = new Line(0,0,0,cellSize);
					Line rightLine = new Line(cellSize, 0, cellSize, cellSize);
					leftLine.setStrokeWidth(4-dimension/5);									//The stroke width varies depending on the dimension
					rightLine.setStrokeWidth(4-dimension/5);
					leftLine.setStroke(ColorTheme.getItemColor());
					rightLine.setStroke(ColorTheme.getItemColor());
					if (position == dimension - maxHintLineLength) {						//If the cell is at the edge of the hintPane, draw a left line too
						Line topLine = new Line(0,0,cellSize,0);
						topLine.setStrokeWidth(4-dimension/5);
						topLine.setStroke(ColorTheme.getItemColor());
						hintCell.getChildren().add(topLine);
					}
							
					
							
					//Add items to linePane, add linePane to rowPane (a grid) at current position
					hintCell.getChildren().addAll(leftLine, rightLine);
					columnPane.add(hintCell,line,position);
							
				} else {
					/* If there is no hintNumber associated with this cell, make an empty white cell with lines */
					
					//Set up Pane
					Pane emptyPane = new Pane();
					emptyPane.setPrefSize(cellSize, cellSize);
					emptyPane.setBackground(ColorTheme.getBackCell());
							
					//Set up lines
					Line leftLine = new Line(0,0,0,cellSize);
					Line rightLine = new Line(cellSize, 0, cellSize, cellSize);
					leftLine.setStrokeWidth(4-dimension/5);									//The stroke width varies depending on the dimension
					rightLine.setStrokeWidth(4-dimension/5);
					leftLine.setStroke(ColorTheme.getItemColor());
					rightLine.setStroke(ColorTheme.getItemColor());
					if (position == dimension - maxHintLineLength) {						//If the cell is at the edge of the hintPane, draw a left line too
						Line topLine = new Line(0,0,cellSize,0);
						topLine.setStrokeWidth(4-dimension/5);
						topLine.setStroke(ColorTheme.getItemColor());
						emptyPane.getChildren().add(topLine);
					}
							
					//Add lines to emptyPane, add emptyPane to rowPane (a grid) at current position
					emptyPane.getChildren().addAll(leftLine,rightLine);		
					columnPane.add(emptyPane, line, position);
				}		
			}
		}
		
		for (int i = 0; i < dimension; i++) {												//Constrain the columns and rows to be the same size as the cells
			columnPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
			columnPane.getRowConstraints().add(new RowConstraints(cellSize));
		}
		
		this.getChildren().add(columnPane);													//Add the columnPane to the columnHintPane
		this.setLayoutX(500);
		this.setLayoutY(50);
		
	}
	
	//Getters
	public PuzzleSolution getPuzzleSolution() {
		return puzzleSolution;
	}
	
	public GridPane getColumnPane() {
		return columnPane;
	}
	
	public int getMaxHintLineLength() {
		return maxHintLineLength;
	}
	
	/**MouseHandlerClick occurs when a right or left click occurs on the hint labels. Clicking
	 * the labels allows the player to change the color of the labels to keep track of which
	 * hints have been completed.**/
	public class MouseHandlerClick implements EventHandler<MouseEvent> {
		
		public void handle(MouseEvent mouseEvent) {
			
			if (((Label) ((Pane) mouseEvent.getSource()).getChildren().get(0)).getTextFill().equals(ColorTheme.getTextColor())) {
				((Label) ((Pane) mouseEvent.getSource()).getChildren().get(0)).setTextFill(ColorTheme.getTextFadedColor());
			} else {
				((Label) ((Pane) mouseEvent.getSource()).getChildren().get(0)).setTextFill(ColorTheme.getTextColor());
			}
			
			
		}
		
	}
	
}
