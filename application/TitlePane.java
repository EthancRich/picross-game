/* Ethan Rich
 * ASU ID: 1221664977
 * Honors Contract Project - Picross
 * CSE 205 - MWF 10:10 - 11:00
 * */
package application;

//imports
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;

/** TitlePane extends the Pane class. This pane draws up the main menu upon running the application.
 * It contains 3 buttons that generate a GamePane for 3 different puzzle sizes. One button quits
 * the application, and another button transitions to the Theme Pane, where themes to recolor the
 * GUI are laid out.*/
public class TitlePane extends Pane {

	//instance variables
	private Label titleLabel, puzzleSizeLabel;
	private Button puzzleFiveButton, puzzleTenButton, puzzleFifteenButton, quitButton, themeButton;
	private TilePane puzzleButtonBox, lowerButtonBox;	
	
	//constructor
	public TitlePane() {
		
		this.setPrefSize(920, 920);
		this.setBackground(ColorTheme.getBackWindow());
		
		//set up Buttons
		puzzleFiveButton = new Button("5x5");
		puzzleFiveButton.setPrefSize(150,35);
		puzzleFiveButton.setOnAction(new PuzzleButtonHandler(5));
		puzzleTenButton = new Button("10x10");
		puzzleTenButton.setPrefSize(150,35);
		puzzleTenButton.setOnAction(new PuzzleButtonHandler(10));
		puzzleFifteenButton = new Button("15x15");
		puzzleFifteenButton.setPrefSize(150,35);
		puzzleFifteenButton.setOnAction(new PuzzleButtonHandler(15));
		
		quitButton = new Button("Quit");
		quitButton.setPrefSize(100, 20);
		quitButton.setOnAction(new QuitButtonHandler());
		themeButton = new Button("Themes");
		themeButton.setPrefSize(100, 20);
		themeButton.setOnAction(new ThemeButtonHandler());
		
		//Set up labels
		titleLabel = new Label("PICROSS");
		titleLabel.setFont(new Font("Times New Roman",175));
		titleLabel.setTextFill(ColorTheme.getTextColor());
		titleLabel.setTranslateX(100);
		titleLabel.setTranslateY(50);
		this.getChildren().add(titleLabel);
		
		puzzleSizeLabel = new Label("Choose the puzzle size:");
		puzzleSizeLabel.setFont(new Font("Arial",20));
		puzzleSizeLabel.setTextFill(ColorTheme.getTextColor());
		puzzleSizeLabel.setTranslateX(352);
		puzzleSizeLabel.setTranslateY(560);
		this.getChildren().add(puzzleSizeLabel);
		
		//Set up puzzleButtonBox, add it to TitlePane
		puzzleButtonBox = new TilePane();
		puzzleButtonBox.getChildren().addAll(puzzleFiveButton, puzzleTenButton, puzzleFifteenButton);
		puzzleButtonBox.setHgap(50);
		puzzleButtonBox.setLayoutX(185);
		puzzleButtonBox.setLayoutY(600);
		this.getChildren().add(puzzleButtonBox);
		
		//Set up lowerButtonBox, add it to TitlePane
		lowerButtonBox = new TilePane();
		lowerButtonBox.getChildren().addAll(themeButton,quitButton);
		lowerButtonBox.setHgap(50);
		lowerButtonBox.setLayoutX(335);
		lowerButtonBox.setLayoutY(700);
		this.getChildren().add(lowerButtonBox);
	
	}
	
	public class PuzzleButtonHandler implements EventHandler<ActionEvent> {

		private int dimension;
			
		public PuzzleButtonHandler() {
			this.dimension = 0;
		}
		
		public PuzzleButtonHandler(int dimension) {
			this.dimension = dimension;
		}
		
		public void handle(ActionEvent actionEvent) {
			//get the root of the application, clear the titlePane from it, then add a GamePane to it instead
			Pane titlePane = (Pane) ((TilePane)((Button)actionEvent.getSource()).getParent()).getParent();
			BorderPane root = (BorderPane)titlePane.getParent();
			root.getChildren().clear();
			root.setCenter(new GamePane(dimension));
		}
		
	}
	
	public class QuitButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent actionEvent) {
			Platform.exit();
		}
		
	}
	
	public class ThemeButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent actionEvent) {
			//get the root of the application, clear the titlePane from it, then add a ThemePane to it instead
			Pane titlePane = (Pane) ((TilePane)((Button)actionEvent.getSource()).getParent()).getParent();
			BorderPane root = (BorderPane)titlePane.getParent();
			root.getChildren().clear();
			root.setCenter(new ThemePane());
		}
		
	}
	
}
