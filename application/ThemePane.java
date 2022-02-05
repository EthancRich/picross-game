/* Ethan Rich
 * Honors Contract Project - Picross
 * CSE 205 
 */
package application;

//imports
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;

/** ThemePane extends Pane. ThemePane lays out radio buttons associated with different color
 * themes for the GUI. Pressing the confirm button returns to the menu. */
public class ThemePane extends Pane {

	//instance variables
	private RadioButton beigeTheme, grayTheme;
	private Button confirmButton;
	private Label beigeThemeLabel, grayThemeLabel;
	private TilePane radioButtonPane;
	
	//constructor
	public ThemePane() {
		
		//Set parameters of this object
		this.setPrefSize(920, 920);
		this.setBackground(ColorTheme.getBackWindow());
		
		//Set up the radio buttons
		ToggleGroup themeGroup = new ToggleGroup();
		beigeTheme = new RadioButton("");
		beigeTheme.setToggleGroup(themeGroup);
		beigeTheme.setOnAction(new BeigeRadioButtonHandler());
		grayTheme = new RadioButton("");
		grayTheme.setToggleGroup(themeGroup);
		grayTheme.setOnAction(new GrayRadioButtonHandler());
		switch (ColorTheme.getState()) {								//Based on the current ColorTheme state, determine which radio button is preselected
		case 0: beigeTheme.setSelected(true); break;
		case 1: grayTheme.setSelected(true); break;
		default: System.out.println("ColorTheme has an invalid state integer");
		}
		
		//Set up labels
		beigeThemeLabel = new Label("Beige & White");
		beigeThemeLabel.setFont(new Font("Arial",20));
		beigeThemeLabel.setTextFill(ColorTheme.getTextColor());
		beigeThemeLabel.setTranslateX(275);
		beigeThemeLabel.setTranslateY(445);
		grayThemeLabel = new Label("Black & Cyan");
		grayThemeLabel.setFont(new Font("Arial",20));
		grayThemeLabel.setTextFill(ColorTheme.getTextColor());
		grayThemeLabel.setTranslateX(275);
		grayThemeLabel.setTranslateY(500);
		
		//Set up Pane that contains radio buttons
		radioButtonPane = new TilePane(Orientation.VERTICAL);
		radioButtonPane.setVgap(35);
		radioButtonPane.setTranslateX(250);
		radioButtonPane.setTranslateY(450);
		radioButtonPane.getChildren().addAll(beigeTheme, grayTheme);
		
		//Set up button
		confirmButton = new Button("Confirm");
		confirmButton.setOnAction(new ConfirmButtonHandler());
		confirmButton.setLayoutX(600);
		confirmButton.setLayoutY(475);
		
		//Add everything to ThemePane
		this.getChildren().addAll(radioButtonPane, confirmButton, beigeThemeLabel, grayThemeLabel);
		
	}
	
	/** BeigeRadioButtonHandler is a handler that will change the ColorTheme to Beige Theme,
	 * then update the nodes in ThemePane based on those new colors. */
	public class BeigeRadioButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent actionEvent) {
			RadioButton currentButton = (RadioButton)actionEvent.getSource();
			ColorTheme.setAndUpdate(0);
			((ThemePane) currentButton.getParent().getParent()).setBackground(ColorTheme.getBackWindow());
			beigeThemeLabel.setTextFill(ColorTheme.getTextColor());
			grayThemeLabel.setTextFill(ColorTheme.getTextColor());
		}
		
	}
	
	/** GrayRadioButtonHandler is a handler that will change the ColorTheme to Black Theme,
	 * then update the nodes in ThemePane based on those new colors. */
	public class GrayRadioButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent actionEvent) {
			RadioButton currentButton = (RadioButton)actionEvent.getSource();
			ColorTheme.setAndUpdate(1);
			((ThemePane) currentButton.getParent().getParent()).setBackground(ColorTheme.getBackWindow());
			beigeThemeLabel.setTextFill(ColorTheme.getTextColor());
			grayThemeLabel.setTextFill(ColorTheme.getTextColor());
		}
		
	}
	
	/** ConfirmButtonHandler is a handler that will get the root of the scene and clear its
	 * contents, replacing it with a new TitlePane. */
	public class ConfirmButtonHandler implements EventHandler<ActionEvent> {
		
		public void handle(ActionEvent actionEvent) {
			Button currentButton = (Button)actionEvent.getSource();
			BorderPane root = ((BorderPane)((ThemePane) currentButton.getParent()).getParent());
			root.getChildren().clear();
			root.setCenter(new TitlePane());
		}
		
	}
	
}
