/* Ethan Rich
 * ASU ID: 1221664977
 * Honors Contract Project - Picross
 * CSE 205 - MWF 10:10 - 11:00
 * */
package application;

//imports
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;

/**GamePane extends BorderPane. GamePane is the main pane that contains the PuzzlePane,
 * RowHintPane, ColumnHintPane, as well as a set of buttons that check the puzzle,
 * generate a new puzzle, display the best times for completion, and quit to the menu.
 * A timer records the time it takes to complete the puzzle.*/
public class GamePane extends BorderPane {
	
	//instance variables
	private ArrayList<String> solveTimes;
	private PuzzleSolution puzzGoal;
	private ColumnHintPane cHintPane;
	private RowHintPane rHintPane;
	private PuzzlePane puzzlePane;
	private TilePane buttonControls;
	private TextArea solveTimesTextArea;
	private Button confirmPuzzleButton, newPuzzleButton, backButton, solveTimesButton;
	private Label infoLabel, timerLabel;
	private Timer timer;
	private String secondsString, minutesString, timeString;
	private int dimension, secondsElapsed, seconds, minutes;
	private boolean warningFlagNewPuzzle, warningFlagQuit, isSolveTimesDisplayed;
	
	//constructors
	public GamePane(int dimension) {
		
		//Set values for booleans and integers
		warningFlagNewPuzzle = false;
		warningFlagQuit = false;
		isSolveTimesDisplayed = false;
		secondsElapsed = 0;
		seconds = 0;
		minutes = 0;
		
		//Set up GamePane
		this.dimension = dimension;
		this.setBackground(ColorTheme.getBackWindow());
		this.setPrefSize(920, 920);
		
		//Instantiate Objects
		solveTimes = new ArrayList<String>();
		puzzGoal = new PuzzleSolution(dimension);
		puzzlePane = new PuzzlePane(puzzGoal);
		rHintPane = new RowHintPane(puzzGoal);
		cHintPane = new ColumnHintPane(puzzGoal);
		
		//Set up TilePane that contains the buttons
		buttonControls = new TilePane(Orientation.HORIZONTAL);
		buttonControls.setHgap(50);
		buttonControls.setTranslateY(-110);
		buttonControls.setTranslateX(200);
		
		//Set up each button, then add it to buttonControls
		confirmPuzzleButton = new Button("Check Puzzle");
		confirmPuzzleButton.setOnAction(new ConfirmPuzzleButtonHandler());
		buttonControls.getChildren().add(confirmPuzzleButton);
		
		newPuzzleButton = new Button("New Puzzle");
		newPuzzleButton.setOnAction(new NewPuzzleButtonHandler());
		buttonControls.getChildren().add(newPuzzleButton);
		
		solveTimesButton = new Button("Solve Times");
		solveTimesButton.setOnAction(new SolveTimesButtonHandler());
		buttonControls.getChildren().add(solveTimesButton);
		
		backButton = new Button("Back");
		backButton.setOnAction(new BackButtonHandler());
		buttonControls.getChildren().add(backButton);
		
		//Set up the Labels
		infoLabel = new Label("");
		infoLabel.setTextFill(ColorTheme.getTextColor());
		infoLabel.setPadding(new Insets(25));
		infoLabel.setTranslateY(850);
		infoLabel.setFont(new Font("Arial",20));
		
		timerLabel = new Label("00:00");
		timerLabel.setTextFill(ColorTheme.getTextColor());
		timerLabel.setPadding(new Insets(35));
		timerLabel.setTranslateY(50);
		timerLabel.setFont(new Font("Arial",80));
		
		//Set up the TextArea for solve times
		solveTimesTextArea = new TextArea("- Solve Times -");
		solveTimesTextArea.setEditable(false);
		solveTimesTextArea.setFont(new Font("Arial",20));
		solveTimesTextArea.setTranslateX(-16);
		solveTimesTextArea.setTranslateY(200);
		solveTimesTextArea.setPrefWidth(161);
		solveTimesTextArea.setMaxHeight(455);
		
		//Create a Runnable that handles FX elements. Needed because the awt ActionListener can't handle FX content in that thread
		Runnable timerLabelUpdater = new Runnable() {
			public void run() {
				timeString = minutesString+":"+secondsString;
				timerLabel.setText(timeString);
			}
		};
		
		//Defines what the timer does each time the timer activates (delay = 1000 milseconds, or every 1 sec)
		ActionListener performer = new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent arg0) {
				if (secondsElapsed < 3599) { 						//Stops incrementing the timer at 1 hour	
					secondsElapsed++;
					seconds = secondsElapsed%60;
					minutes = secondsElapsed/60;
					if (seconds/10 > 0) { 							//if the number of seconds is two digits
						secondsString = seconds+"";
					} else {
						secondsString = "0"+seconds;
					}
					if (minutes/10 > 0) {
						minutesString = minutes+"";
					} else {
						minutesString = "0"+minutes;
					}
					Platform.runLater(timerLabelUpdater);			//Call the Runnable to set the seconds and minutes to the timerLabel
				}
			}
		};
		int delay = 1000;
		timer = new Timer(delay, performer);
		timer.start();
		
		//Set the buttons size to be equal to the size of the largest button
		for (Node button : buttonControls.getChildren()) {
			((Button)button).setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
		}
		
		//Set the nodes to different borders of GamePane
		this.getChildren().addAll(rHintPane,cHintPane,puzzlePane);
		this.setTop(infoLabel);
		this.setBottom(buttonControls);
		this.setLeft(timerLabel);
		
	}
	
	/** ConfirmPuzzleButtonHandler is a handler that updates the current puzzle in the PuzzleSolution,
	 * then compares the currentPuzzle to the generated solution. If the currentPuzzle is an equivalent
	 * yet different solution, the puzzle will still be accepted.*/
	class ConfirmPuzzleButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent actionEvent) {
			
			puzzGoal.updateCurrentPuzzle(puzzlePane.getCellList());
			warningFlagNewPuzzle = false;									//Set the two warning flags to false, as the infoLabel will no longer be of a warning
			warningFlagQuit = false;
			infoLabel.setTextFill(ColorTheme.getTextColor());				//Set the text color back to normal in case a warning was displayed previously
			
			if (puzzGoal.isCurrentSolved()) {
				timer.stop();
				solveTimes.add(timerLabel.getText());						//Add the solve time to the arrayList solveTimes
				sortArrayList(solveTimes);
				updateTextArea();											//Update the textArea with the new sorted list of solve times
				timerLabel.setTextFill(ColorTheme.getTextWarningColor());	//Set the timerLabel's color to the warning color
				infoLabel.setText("Correct! Your Time: "+timerLabel.getText());
			} else {
				infoLabel.setText("Sorry, that is not the correct solution.");
			}
			
		}
		
		//sortArrayList is a helper method that sorts the arrayList. Implements a Selection Sort
		private void sortArrayList(ArrayList<String> list) {
			String min;
			int minIndex;
			for (int i = 0; i < list.size()-1; i++) {
				min = list.get(i);
				minIndex = 0;
				for (int j = i; j < list.size(); j++) {			//For every element after i, find the minimum String lexigraphically
					if (min.compareTo(list.get(j)) > 0) {
						min = list.get(j);
						minIndex = j;
					}
				}
				if (!min.equals(list.get(i))) {					//If the minimum isn't already the same, then swap i and j Strings
					//swap them
					String temp = list.get(i);
					list.set(i, min);
					list.set(minIndex, temp);
				}
					
			}
			
		}
		
		//updateTextArea is a helper method that concatenates each time in ArrayList solveTimes and puts that String in textArea
		private void updateTextArea() {
			String text = "- Solve Times -\n";
			for (int i = 0; i < solveTimes.size(); i++) {
				text+=solveTimes.get(i)+"\n";
			}
			solveTimesTextArea.setText(text);
		}
		
	}
	
	/** NewPuzzleButtonHandler is a handler that generates a new PuzzleSolution, and the three
	 * accompanying Panes along with it. If there is a current puzzle being worked on, then
	 * the button will first prompt the user with a warning. If the warning is present, or
	 * the current puzzle is solved, then a new puzzle will be created. */
	class NewPuzzleButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent actionEvent) {
			
			warningFlagQuit = false;										//Sets flag to false, as a different flag is being raised
			
			if(warningFlagNewPuzzle || puzzGoal.isCurrentSolved()) {		//If the current flag is raised, or the puzzle is completed
				infoLabel.setText("");
				infoLabel.setTextFill(ColorTheme.getTextColor());
				warningFlagNewPuzzle = false;								
				
				getChildren().remove(rHintPane);							//Remove the three panes that compose the game board
				getChildren().remove(cHintPane);
				getChildren().remove(puzzlePane);
				puzzGoal = new PuzzleSolution(puzzGoal.getDimension());		//Generate a new PuzzleSolution, and make three new game objects
				rHintPane = new RowHintPane(puzzGoal);
				cHintPane = new ColumnHintPane(puzzGoal);
				puzzlePane = new PuzzlePane(puzzGoal);
				getChildren().add(0,rHintPane);								//Add them to GamePane
				getChildren().add(1,cHintPane);
				getChildren().add(2,puzzlePane);
				
				secondsElapsed = 0;											//Reset the timer, secondsElasped, and timerLabel
				timerLabel.setText("00:00");
				timer.restart();
				timerLabel.setTextFill(ColorTheme.getTextColor());
				return;
			}
			
			if(!warningFlagNewPuzzle) {										//Display warning
				infoLabel.setText("Are you sure? The current puzzle won't be saved.");
				infoLabel.setTextFill(ColorTheme.getTextWarningColor());
				warningFlagNewPuzzle = true;
			}
			
		}
		
	}
	
	/** SolveTimesButtonHandler is a handler that toggles the solveTimesTextArea visible or invisible. */
	class SolveTimesButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent actionEvent) {
			if (isSolveTimesDisplayed) {
				getChildren().remove(solveTimesTextArea);
				isSolveTimesDisplayed = false;
			} else {
				setRight(solveTimesTextArea);
				isSolveTimesDisplayed = true;
			}
			warningFlagQuit = false;							//Set the other flags to false and reset the infoLabel
			warningFlagNewPuzzle = false;
			infoLabel.setText("");
			infoLabel.setTextFill(ColorTheme.getTextColor());
		}
		
	}
	
	/** BackButtonHandler is a handler that erases the GamePane and replaces it with a
	 * TitlePane, allowing the user to return to the previous menu. */
	class BackButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent actionEvent) {
			
			warningFlagNewPuzzle = false;						//Set the other flag to false because a different flag is being raised
			
			if(warningFlagQuit || puzzGoal.isCurrentSolved()) {
				BorderPane thisGamePane = (BorderPane) ((TilePane)((Button)actionEvent.getSource()).getParent()).getParent();
				BorderPane root = (BorderPane)thisGamePane.getParent();
				root.getChildren().clear();
				root.setCenter(new TitlePane());
				return;
			}
			
			if(!warningFlagQuit) {
				infoLabel.setText("Are you sure you want to quit? The current puzzle won't be saved.");
				infoLabel.setTextFill(ColorTheme.getTextWarningColor());
				warningFlagQuit = true;
			}
			
		}
		
	}
	
}
