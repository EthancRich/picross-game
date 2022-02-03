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

/**RowHintPane extends StackPane. RowHintPane generates the hint numbers for the rows
 *  of the puzzle and displays them in a gridPane. **/
public class RowHintPane extends StackPane {

	//Instance Variables
	private int cellSize, dimension, maxHintLineLength;										
	private ArrayList<ArrayList<Integer>> rowHintNumbers;
	private ArrayList<ArrayList<Label>> rowHintLabels;
	private PuzzleSolution puzzleSolution;
	private GridPane rowPane;
	
	//Constructor
	public RowHintPane(PuzzleSolution puzzleSolution) {
		
		//Instantiation and Initialization
		dimension = puzzleSolution.getDimension();											//Dimension is the n x n value of the size of the picross
		cellSize = 450/dimension;															//Divides the cells to be certain size based on the number of cells
		maxHintLineLength = 0;																//maxHintLineLength measures the longest hint line in the pane
		this.puzzleSolution = puzzleSolution;
		
		rowHintNumbers = new ArrayList<ArrayList<Integer>>();								//Initialize the Lists of Lists
		rowHintLabels = new ArrayList<ArrayList<Label>>();	
				
		//Preparing rowHintNumbers and rowHintLabels
		for (int i = 0; i < dimension; i++) {												
			rowHintNumbers.add(puzzleSolution.getSolutionHintRow(i));								//Add each hint row to the rowHintNumbers
			rowHintLabels.add(new ArrayList<Label>());										//Add a new List of Labels to each row of rowHintLabels
			if (puzzleSolution.getSolutionHintRow(i).size() > maxHintLineLength) {					//When the length of a hint line is larger than current, record it
				maxHintLineLength = puzzleSolution.getSolutionHintRow(i).size();
			}
		}
		
		for (int line = 0; line < dimension; line++) {										
			for (Integer hintNum : rowHintNumbers.get(line)) {
				Label hintLabel = new Label(""+hintNum);									//Set each label in rowHintLabels to the int in rowHintNumbers
				rowHintLabels.get(line).add(hintLabel);	
			}
		}
		
		//Creating rowPane, then filling it with the appropriate content (this is a complex section)
		rowPane = new GridPane();
		for (int line = 0; line < dimension; line++) {										//For each row, position is the right side index, and ends after the maxHintLineLength
			for (int position = dimension-1; position >= dimension - maxHintLineLength; position--) {
									
				int index = (dimension - 1) - position;										//Index is the index of the rowHintLabels
				if (index < rowHintNumbers.get(line).size()) {
					/* If there is an hintNumber associated with this cell, create a Pane and add lines and a label for it */
					
					//Set up hintCell
					Pane hintCell = new Pane();
					hintCell.setPrefSize(cellSize, cellSize);
					hintCell.setBackground(ColorTheme.getBackCell());
					hintCell.setOnMousePressed(new MouseHandlerClick()); 					//set the Pane to react upon being clicked
					
					//Set up Labels in
					Label hintLabel = rowHintLabels.get(line).get(rowHintNumbers.get(line).size()-index-1);
					switch (dimension) {
					case 5:																	//5x5 font and layout
						hintLabel.setFont(new Font("Arial", cellSize));
						hintLabel.setLayoutX(20);											
						hintLabel.setLayoutY(-7);
						break;
					case 10:																//10x10 font and layout
						if (hintLabel.getText().length() > 1) {								//Scale down the numbers w/ 2 digits
							hintLabel.setFont(new Font("Arial", cellSize*0.85));
							hintLabel.setLayoutX(-2);
							hintLabel.setLayoutY(1);
						} else {															//Numbers w/ 1 digit
							hintLabel.setFont(new Font("Arial", cellSize));
							hintLabel.setLayoutX(9);										
							hintLabel.setLayoutY(-3);
						}
						break;
					case 15:																//15x15 font and layout
						if (hintLabel.getText().length() > 1) {								//Scale down the numbers w/ 2 digits
							hintLabel.setFont(new Font("Arial", cellSize*0.85));
							hintLabel.setLayoutX(0);
							hintLabel.setLayoutY(-2);
						} else {															//Numbers w/ 1 digit
							hintLabel.setFont(new Font("Arial", cellSize));
							hintLabel.setLayoutX(6);										
							hintLabel.setLayoutY(-3);
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
					Line topLine = new Line(0,0,cellSize,0);
					Line bottomLine = new Line(0,cellSize, cellSize, cellSize);
					topLine.setStroke(ColorTheme.getItemColor());
					bottomLine.setStroke(ColorTheme.getItemColor());
					topLine.setStrokeWidth(4-dimension/5);									//The stroke width varies depending on the dimension
					bottomLine.setStrokeWidth(4-dimension/5);
					if (position == dimension - maxHintLineLength) {						//If the cell is at the edge of the hintPane, draw a left line too
						Line leftLine = new Line(0,0,0,cellSize);
						leftLine.setStrokeWidth(4-dimension/5);
						leftLine.setStroke(ColorTheme.getItemColor());
						hintCell.getChildren().add(leftLine);
					}
				
					//Add items to linePane, add linePane to rowPane (a grid) at current position
					hintCell.getChildren().addAll(bottomLine, topLine);
					rowPane.add(hintCell,position,line);
					
				} else {
					/* If there is no hintNumber associated with this cell, make an empty white cell with lines */
					
					//Set up Pane
					Pane emptyPane = new Pane();
					emptyPane.setPrefSize(cellSize, cellSize);
					emptyPane.setBackground(ColorTheme.getBackCell());
					
					//Set up lines
					Line topLine = new Line(0,0,cellSize,0);
					Line bottomLine = new Line(0,cellSize, cellSize, cellSize);
					topLine.setStrokeWidth(4-dimension/5);
					bottomLine.setStrokeWidth(4-dimension/5);
					topLine.setStroke(ColorTheme.getItemColor());
					bottomLine.setStroke(ColorTheme.getItemColor());
					if (position == dimension - maxHintLineLength) {
						Line leftLine = new Line(0,0,0,cellSize);
						leftLine.setStrokeWidth(4-dimension/5);
						leftLine.setStroke(ColorTheme.getItemColor());
						emptyPane.getChildren().add(leftLine);
					}
					
					//Add lines to emptyPane, add emptyPane to rowPane (a grid) at current position
					emptyPane.getChildren().addAll(topLine,bottomLine);	
					rowPane.add(emptyPane, position, line);
				}
				
			}
		}
		
		for (int i = 0; i < dimension; i++) {												//Constrain the columns and rows to be the same size as the cells
			rowPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
			rowPane.getRowConstraints().add(new RowConstraints(cellSize));
		}
		
		this.getChildren().add(rowPane);													//Add the rowPane to the rowHintPane
		this.setLayoutX(50);																//Place rowHintPane in the larger scene
		this.setLayoutY(500);
		
	}
	
	//Getters
	public GridPane getRowPane() {
		return rowPane;
	}
	
	public int getMaxHintLineLength() {
		return maxHintLineLength;
	}
	
	public PuzzleSolution getPuzzleSolution() {
		return puzzleSolution;
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
