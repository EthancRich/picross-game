/* Ethan Rich
 * ASU ID: 1221664977
 * Honors Contract Project - Picross
 * CSE 205 - MWF 10:10 - 11:00
 * */
package application;

//imports
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/** Main is the runner class that overrides the JavaFX start method. It generates a stage that contains a scene,
 * composed of a root that has a titlePane in it. **/
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane(); 		//Root is the main pane that each pane for the program is added onto and deleted from
			TitlePane menu = new TitlePane();			//Menu is the startup title screen
			root.setCenter(menu);
			Scene scene = new Scene(root,920,920);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Picross Game");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
