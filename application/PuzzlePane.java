/* Ethan Rich
 * ASU ID: 1221664977
 * Honors Contract Project - Picross
 * CSE 205 - MWF 10:10 - 11:00
 * */
package application;

//Imports
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**PuzzlePane extends StackPane. PuzzlePane is a pane that contains a gridPane of Picross-like cells; these cells can be interacted
 * with clicks and drags in order to draw shapes and erase shapes. The constructor requires a PuzzleSolution, that declares the
 * size of the puzzle as well as its solution. **/
public class PuzzlePane extends StackPane {

	//Instance Variables
	private Pane linePane;
	private GridPane gridPane;
	private Rectangle dragDetectorSquare;
	private ArrayList<StackPane> cellList, cellHighlightedList;
	private PuzzleSolution puzzleSolution;
	private int dragFlag, cellSize, dimension, thinLine, thickLine;
	
	//Constructor
	public PuzzlePane(PuzzleSolution puzzleSolution) {
		
			//Set up the local variables
			this.puzzleSolution = puzzleSolution;
			dimension = this.puzzleSolution.getDimension();
			cellSize = 450/dimension;				
			dragFlag = -1;
			thinLine = 4 - dimension/5;
			thickLine = 6 - dimension/5;
			dragDetectorSquare = new Rectangle(cellSize,cellSize);			//Shape that sits on top of the stackpane to detect dragging
			dragDetectorSquare.setFill(Color.TRANSPARENT);
			cellHighlightedList = new ArrayList<StackPane>();				//List of cells that are highlighted due to a dragging event
			
			//Set up the layout of PuzzlePane
			this.setLayoutX(500);											
			this.setLayoutY(500);
			
			//Populating the cellList with StackPanes that represent the individual cells of the grid
			cellList = new ArrayList<StackPane>();							//List of every StackPane cell in the grid
			for (int cellNum=dimension*dimension; cellNum>0; cellNum--) {
				cellList.add(new StackPane());								//Adding them to the list
			}
			
			//Setting up the cells in cellList
			for (StackPane cell : cellList) {	
				
				//Set parameters for each Cell
				cell.setMinSize(cellSize,cellSize);
				cell.setAlignment(Pos.CENTER);
				cell.setBackground(ColorTheme.getBackCell());
				
				//Add PicrossCell (the visual shape portion of the StackPane Cell) to bottom of StackPane Cell, pos 0
				cell.getChildren().add(new PicrossCell(0,cellSize));		
				
				//Add LinePane, a collection of grid lines, to the middle of StackPane Cell, pos 1
				linePane = new Pane();
				linePane.setBackground(Background.EMPTY);
				linePane.setPrefSize(cellSize, cellSize);
				Line topLine = new Line(0, 0, cellSize, 0);						//the four lines around each cell, making a grid
				Line bottomLine = new Line(0, cellSize, cellSize, cellSize);
				Line leftLine = new Line(0, 0, 0, cellSize);
				Line rightLine = new Line(cellSize, 0, cellSize, cellSize);
				topLine.setStrokeWidth(thinLine);
				bottomLine.setStrokeWidth(thinLine);
				leftLine.setStrokeWidth(thinLine);
				rightLine.setStrokeWidth(thinLine);
				topLine.setStroke(ColorTheme.getItemColor());
				bottomLine.setStroke(ColorTheme.getItemColor());
				leftLine.setStroke(ColorTheme.getItemColor());
				rightLine.setStroke(ColorTheme.getItemColor());
				
				//For puzzles 10x10 or 15x15, make special lines that create sections of 5x5 within the puzzles
				switch (dimension) {
				case 5: break;
				case 10:
					if ((cellList.indexOf(cell)+5)%10 == 0) {
						topLine.setStrokeWidth(thickLine);
						topLine.setStroke(ColorTheme.getGridLineAccentColor());
					}
					if (cellList.indexOf(cell) > 49 && cellList.indexOf(cell) < 60) {
						leftLine.setStrokeWidth(thickLine);
						leftLine.setStroke(ColorTheme.getGridLineAccentColor());
					}
					break;
				case 15:
					if ((cellList.indexOf(cell)+5)%15 == 0 || (cellList.indexOf(cell)+10)%15 == 0) {
						topLine.setStrokeWidth(thickLine);
						topLine.setStroke(ColorTheme.getGridLineAccentColor());
					}
					if ((cellList.indexOf(cell) > 74 && cellList.indexOf(cell) < 90) || (cellList.indexOf(cell) > 149 && cellList.indexOf(cell) < 165)) {
						leftLine.setStrokeWidth(thickLine);
						leftLine.setStroke(ColorTheme.getGridLineAccentColor());
					}
					break;
				default: break;
				}

				linePane.getChildren().addAll(topLine, bottomLine, leftLine, rightLine);
				cell.getChildren().add(linePane);
				
				//Add Drag Square on Top of the other two, Pos 2
				Rectangle rec = new Rectangle(dragDetectorSquare.getHeight(),dragDetectorSquare.getWidth(),dragDetectorSquare.getFill());
				rec.setOnDragDetected(new MouseHandlerDragDetect());
				cell.getChildren().add(rec);								
			}
			
			//Populating the gridPane with the cells from cellList
			gridPane = new GridPane();
			gridPane.setBackground(Background.EMPTY);
			int cellIndex = 0;												//Index of the cellList
			for (int c = 0; c < dimension; c++) {
				for (int r = 0; r < dimension; r++) {
					gridPane.add(cellList.get(cellIndex), c, r);			//Assigns each c,r with a cell from cellList
					cellIndex++;
				}
			}
						
			this.getChildren().add(gridPane);								//Adds the gridPane to StackPane PuzzlePane
			
			//Setting the cells to react to many different mouse events
			for (StackPane cell: cellList) {
				cell.setOnMouseEntered(new MouseHandlerIn()); 				
				cell.setOnMouseExited(new MouseHandlerOut()); 				
				cell.setOnMousePressed(new MouseHandlerPress()); 			
				cell.setOnMouseDragEntered(new MouseHandlerDragIn());		
				cell.setOnMouseDragExited(new MouseHandlerDragOut());		
				cell.setOnMouseDragReleased(new MouseHandlerDragEnd());		
			}
			
	}
	
	//Other Methods
	public ArrayList<StackPane> getCellList() {
		return cellList;
	}
	
	
	
	/* NESTED CLASSES */
	
	/**MouseHandlerIn sets the background of cells to beige when the mouse moves into these cells.**/
	private class MouseHandlerIn implements EventHandler<MouseEvent> {
		
		public void handle(MouseEvent mouseEvent) {
			StackPane currentCell = (StackPane) mouseEvent.getSource();
			currentCell.setBackground(ColorTheme.getBackCellSelected());				//Assign the background to current cell
			
			int index = cellList.indexOf(currentCell);									//find the index of the cell moved into
			int row = index%dimension;
			int column = index/dimension;
			GamePane gamePane = (GamePane) getParent();
			RowHintPane rowHintPane = (RowHintPane) gamePane.getChildren().get(0);
			ColumnHintPane colHintPane = (ColumnHintPane) gamePane.getChildren().get(1);
			GridPane gridPaneR = rowHintPane.getRowPane();								//Get access to the grids in RowHintPane and ColumnHintPane
			GridPane gridPaneC = colHintPane.getColumnPane();
			
			//Set the highlighted background for the according hint cells in the same row and column as the hovered cell
			for (int c = dimension-1; c > dimension-1-rowHintPane.getMaxHintLineLength(); c--) {
				Pane pane1 = (Pane) getNodeByRowAndColumn(gridPaneR,row,c);
				pane1.setBackground(ColorTheme.getBackCellSelected());
			}
			for (int r = dimension-1; r > dimension-1-colHintPane.getMaxHintLineLength(); r--) {
				Pane pane1 = (Pane) getNodeByRowAndColumn(gridPaneC,r,column);
				pane1.setBackground(ColorTheme.getBackCellSelected());
			}	
		}
		
		//helper method that finds a Node given a GridPane based on the known row and column
		private Node getNodeByRowAndColumn (GridPane gridPane,int row,int column) {
		    for (Node node : gridPane.getChildren()) {
		        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
		            return node;
		        }
		    }
		    System.out.println("There isn't a cell at this row and column");
		    return null;
		}
		
	}
	
	/**MouseHandlerOut sets the background of cells to empty when the mouse moves out of these cells.**/
	private class MouseHandlerOut implements EventHandler<MouseEvent> {
		
		public void handle(MouseEvent mouseEvent) {
			StackPane currentCell = (StackPane) mouseEvent.getSource();
			currentCell.setBackground(ColorTheme.getBackCell());						//Set the background of the cell back to normal
			
			int index = cellList.indexOf(currentCell);									//find the index of the cell moved into
			int row = index%dimension;
			int column = index/dimension;
			GamePane gamePane = (GamePane) getParent();
			RowHintPane rowHintPane = (RowHintPane) gamePane.getChildren().get(0);
			ColumnHintPane colHintPane = (ColumnHintPane) gamePane.getChildren().get(1);
			GridPane gridPaneR = rowHintPane.getRowPane();								//Get access to the grids in RowHintPane and ColumnHintPane
			GridPane gridPaneC = colHintPane.getColumnPane();
			
			//Set the highlighted background for the according hint cells in the same row and column as the hovered cell
			for (int c = dimension-1; c > dimension-1-rowHintPane.getMaxHintLineLength(); c--) {
				Pane pane1 = (Pane) getNodeByRowAndColumn(gridPaneR,row,c);
				pane1.setBackground(ColorTheme.getBackCell());
			}
			for (int r = dimension-1; r > dimension-1-colHintPane.getMaxHintLineLength(); r--) {
				Pane pane1 = (Pane) getNodeByRowAndColumn(gridPaneC,r,column);
				pane1.setBackground(ColorTheme.getBackCell());
			}	
		}
		
		//helper method that finds a Node given a GridPane based on the known row and column
		private Node getNodeByRowAndColumn (GridPane gridPane,int row,int column) {
		    for (Node node : gridPane.getChildren()) {
		        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
		            return node;
		        }
		    }
		    System.out.println("There isn't a cell at this row and column");
		    return null;
		
		}
		
	}
	
	/**MouseHandlerClickPress gets the state of the PicrossCell within the cell, and then based on the type
	 * of click, the handler will update the PicrossCell correspondingly. A left-click causes a black
	 * square to be drawn/erased, a right-click causes an X to be drawn/erased, and clicking the
	 * opposite click to the opposite shape causes the shape to be changed to the other.**/
	private class MouseHandlerPress implements EventHandler<MouseEvent> {

		
		public void handle(MouseEvent mouseEvent) {
			//Create a PicrossCell reference so you can access this specific PicrossCell without the hassle
			PicrossCell currentCell = (PicrossCell) ((StackPane)mouseEvent.getSource()).getChildren().get(0);
			
			//Based on what the state of the cell is, different clicks change the cell to a different state
			switch (currentCell.getState()) {
			
				case 0: //Empty Cell
					if (mouseEvent.getButton()==MouseButton.PRIMARY) 	
						currentCell.updateCell(1);
					else if (mouseEvent.getButton()==MouseButton.SECONDARY)
						currentCell.updateCell(2);
					else if (mouseEvent.getButton()==MouseButton.MIDDLE)
						currentCell.updateCell(3);
					break;
				
				case 1:	//Boxed Cell
					currentCell.updateCell(0);
					break;
						
				case 2: //X'ed Cell
					currentCell.updateCell(0);	
					break;
				case 3: //Cirled cell
					if (mouseEvent.getButton()==MouseButton.PRIMARY)
						currentCell.updateCell(1);
					else if (mouseEvent.getButton()==MouseButton.SECONDARY)
						currentCell.updateCell(2);
					else if (mouseEvent.getButton()==MouseButton.MIDDLE)
						currentCell.updateCell(0);
					break;
				default: System.out.println("Somehow the PicrossCell Object stored a state other than 0, 1, or 2."
						+ " There should be checks preventing this from happening in the first place.");
						break;
				
			}			
		}
	}

	/**MouseHandlerDragDetect implements a MouseEvent where upon starting a drag action, the drag is set 
	 * to a FullDrag so other drag-related MouseEvents can still be handled simultaneously. In addition, 
	 * this handler will set a flag that determines what state populates the current cell so that every 
	 * proceeding cell gets changed to the same state. Finally, this handler adds the dragged cell to the 
	 * highlightedList.**/
	private class MouseHandlerDragDetect implements EventHandler<MouseEvent> {
		
		public void handle(MouseEvent mouseEvent) {
			StackPane currentCell = (StackPane) ((Rectangle)mouseEvent.getSource()).getParent();
			PicrossCell currentPicrossCell = (PicrossCell) currentCell.getChildren().get(0);//Getting the PicrossCell in the StackPane that the drag occurred
			dragFlag = currentPicrossCell.getState();										//Set the flag to the state of the initial PicrossCell
			startFullDrag();																//Sets full drag, Other Node interactions can now occur simultaneously
			cellHighlightedList.add(currentCell);							//Adds the currentCell to a list of Highlighted cells to keep track of background colors
		}
		
	}
	
	/**MouseHandlerDragIn occurs when a drag action enters this cell. This handler will set the background
	 * to Beige and add the cell to the cellHighlightedList. Then, depending on the value of the dragFlag,
	 * the state of the cell will be modified to match. If the cell's state already matches the dragFlag,
	 * then the handler will terminate prematurely.**/
	private class MouseHandlerDragIn implements EventHandler<MouseEvent> {
		
		public void handle(MouseEvent mouseEvent) {
			
			cellHighlightedList.add(((StackPane)mouseEvent.getSource()));
			
			StackPane currentStackCell = (StackPane) mouseEvent.getSource();				//Set the currentCell to the selected Background
			currentStackCell.setBackground(ColorTheme.getBackCellSelected());
			
			//Same as the moveinhandler and moveouthandler process, get the grids in the hint panes, then edit their backgrounds
			int index = cellList.indexOf(currentStackCell);									
			int row = index%dimension;
			int column = index/dimension;
			GamePane gamePane = (GamePane) getParent();
			RowHintPane rowHintPane = (RowHintPane) gamePane.getChildren().get(0);
			ColumnHintPane colHintPane = (ColumnHintPane) gamePane.getChildren().get(1);
			GridPane gridPaneR = rowHintPane.getRowPane();								//Get access to the grids in RowHintPane and ColumnHintPane
			GridPane gridPaneC = colHintPane.getColumnPane();
			
			for (int c = dimension-1; c > dimension-1-rowHintPane.getMaxHintLineLength(); c--) {
				Pane pane1 = (Pane) getNodeByRowAndColumn(gridPaneR,row,c);
				pane1.setBackground(ColorTheme.getBackCellSelected());
			}
			for (int r = dimension-1; r > dimension-1-colHintPane.getMaxHintLineLength(); r--) {
				Pane pane1 = (Pane) getNodeByRowAndColumn(gridPaneC,r,column);
				pane1.setBackground(ColorTheme.getBackCellSelected());
			}
			
			//PicrossCell can change it's state if the dragFlag is different. DragFlag is just what the drag is changing all the elements to.
			PicrossCell currentCell = (PicrossCell) ((StackPane) mouseEvent.getSource()).getChildren().get(0);
			
			if (dragFlag == currentCell.getState()) { 			//If the cell being dragged into is already the correct state, just quit the handle
				return;
			}
				
			switch (dragFlag) {									//Update the PicrossCell state according to the dragFlag
				case 0: if (mouseEvent.getButton()!=MouseButton.MIDDLE || currentCell.getState()==3) {
					currentCell.updateCell(0);
				} break;
				case 1: currentCell.updateCell(1); break;
				case 2: currentCell.updateCell(2); break;
				case 3: if (currentCell.getState()==0) {
							currentCell.updateCell(3);
						} break;
				default: System.out.println("Somehow the dragFlag is not 0, 1, or 2, even though"
						+ "that should not be possible."); break;
			}

		}
		
		private Node getNodeByRowAndColumn (GridPane gridPane, int row,int column) {
		    for (Node node : gridPane.getChildren()) {
		        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
		            return node;
		        }
		    }
		    System.out.println("There isn't a cell at this row and column");
		    return null;
		
		}
		
	}
	
	/**MouseHandlerDragOut occurs when a drag action exits this cell. This handler will set the background
	 * color of the cell to Beige. Because when cells get dragged into initially they are already set to
	 * beige, this code is somewhat redundant as its use is to specifically target the first cell where
	 * the dragging begins.**/
	private class MouseHandlerDragOut implements EventHandler<MouseEvent> {
		
		public void handle(MouseEvent mouseEvent) {
			//Same thing. If, when dragging out the cell isn't already highlighted, highlight it. Then, set the hint cell backgrounds back to normal
			if (!((StackPane)mouseEvent.getSource()).getBackground().equals(ColorTheme.getBackCellSelected())) {
				((StackPane)mouseEvent.getSource()).setBackground(ColorTheme.getBackCellSelected());
			}
			
			StackPane currentCell = (StackPane) mouseEvent.getSource();
			
			int index = cellList.indexOf(currentCell);									//find the index of the cell moved into
			int row = index%dimension;
			int column = index/dimension;
			GamePane gamePane = (GamePane) getParent();
			RowHintPane rowHintPane = (RowHintPane) gamePane.getChildren().get(0);
			ColumnHintPane colHintPane = (ColumnHintPane) gamePane.getChildren().get(1);
			GridPane gridPaneR = rowHintPane.getRowPane();								//Get access to the grids in RowHintPane and ColumnHintPane
			GridPane gridPaneC = colHintPane.getColumnPane();
			
			for (int c = dimension-1; c > dimension-1-rowHintPane.getMaxHintLineLength(); c--) {
				Pane pane1 = (Pane) getNodeByRowAndColumn(gridPaneR,row,c);
				pane1.setBackground(ColorTheme.getBackCell());
			}
			for (int r = dimension-1; r > dimension-1-colHintPane.getMaxHintLineLength(); r--) {
				Pane pane1 = (Pane) getNodeByRowAndColumn(gridPaneC,r,column);
				pane1.setBackground(ColorTheme.getBackCell());
			}
			
		}
		
		private Node getNodeByRowAndColumn (GridPane gridPane,int row,int column) {
		    for (Node node : gridPane.getChildren()) {
		        if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
		            return node;
		        }
		    }
		    System.out.println("There isn't a cell at this row and column");
		    return null;
		
		}
		
	}
	
	/**MouseHandlerDragEnd occurs when a drag action is released. This handler will go through each cell
	 * in the cellHighlightedList and set its background to Empty. Then, the list is cleared. **/
	private class MouseHandlerDragEnd implements EventHandler<MouseEvent> {
		
		public void handle(MouseEvent mouseEvent) {
			for (StackPane sePane : cellHighlightedList) {
				sePane.setBackground(ColorTheme.getBackCell());
			}
			cellHighlightedList.clear();
		}
		
	}

}