/* Ethan Rich
 * Honors Contract Project - Picross
 * CSE 205 
 */
package application;

//imports
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**ColorTheme is a "static" class that cannot be instantiated. The variables can be 
 * accessed statically with get methods, and contains the sets of themes that certain
 * nodes. Depending on the state, the colors are modified to suit the theme. */
public class ColorTheme {

	//instance variables
	private static int state;					//Color themes are associated with certain states. 0, 1, 2, so on.
	private static Background backCell = new Background(new BackgroundFill(Color.WHITE, null,null));
	private static Background backCellSelected = new Background(new BackgroundFill(Color.BEIGE, null,null));
	private static Background backWindow = new Background(new BackgroundFill(Color.FLORALWHITE, null,null));
	private static Color textColor = Color.BLACK;
	private static Color textFadedColor = Color.GRAY;
	private static Color textWarningColor = Color.RED;
	private static Color itemColor = Color.BLACK;
	private static Color gridLineColor = Color.BLACK;
	private static Color gridLineAccentColor = Color.DARKBLUE;
	
	//constructors - Object is not supposed to be created, but they are here anyway
	private ColorTheme() {	
	}
	
	//getters
	public static int getState() {
		return state;
	}
	
	public static Background getBackCell() {
		return backCell;
	}
	
	public static Background getBackCellSelected() {
		return backCellSelected;
	}
	
	public static Background getBackWindow() {
		return backWindow;
	}
	
	public static Color getTextColor() {
		return textColor;
	}
	
	public static Color getTextFadedColor() {
		return textFadedColor;
	}
	
	public static Color getTextWarningColor() {
		return textWarningColor;
	}
	
	public static Color getItemColor() {
		return itemColor;
	}
	
	public static Color getGridLineColor() {
		return gridLineColor;
	}
	
	public static Color getGridLineAccentColor() {
		return gridLineAccentColor;
	}
	
	
	//sets the state and updates the current background variables
	public static void setAndUpdate(int state) {
		setState(state);
		updateState();
	}
	
	//setters - private because only the above method should be called.
	private static void setState(int s) {
		state = s;
	}
	
	private static void updateState() {
		switch (state) {
		
		case 0: //Beige & White theme
			backCell = new Background(new BackgroundFill(Color.WHITE, null,null));
			backCellSelected = new Background(new BackgroundFill(Color.BEIGE, null,null));
			backWindow = new Background(new BackgroundFill(Color.FLORALWHITE, null,null));
			textColor = Color.BLACK;
			textFadedColor = Color.GRAY;
			textWarningColor = Color.RED;
			itemColor = Color.BLACK;
			gridLineColor = Color.BLACK;
			gridLineAccentColor = Color.DARKBLUE;
			break;
			
		case 1: //Gray & Black theme
			backCell = new Background(new BackgroundFill(Color.DARKSLATEGRAY, null,null));
			backCellSelected = new Background(new BackgroundFill(Color.SLATEGRAY, null,null));
			backWindow = new Background(new BackgroundFill(Color.BLACK, null,null));
			textColor = Color.WHITE;
			textFadedColor = Color.DARKGRAY;
			textWarningColor = Color.CYAN;
			itemColor = Color.WHITE;
			gridLineColor = Color.WHITE;
			gridLineAccentColor = Color.CORNFLOWERBLUE;
			break;
			
		default:
			System.out.println("Invalid Color Theme State.");
			backCell = Background.EMPTY;
			backCellSelected = Background.EMPTY;
			backWindow = Background.EMPTY;
			textColor = Color.TRANSPARENT;
			textFadedColor = Color.TRANSPARENT;
			textWarningColor = Color.TRANSPARENT;
			itemColor = Color.TRANSPARENT;
			gridLineColor = Color.TRANSPARENT;
			gridLineAccentColor = Color.TRANSPARENT;
			break;
			
		}
	}
	
	public String toString() {
		
		return  "State: " + getState() + "\n" +
				"backCell: " + getBackCell() + "\n" +
				"backCellSelected: " + getBackCellSelected() + "\n" +
				"backWindow: " + getBackWindow() + "\n" +
				"TextColor: " + getTextColor() + "\n" +
				"TextFadedColor: " + getTextFadedColor() + "\n" +
				"ItemColor: " + getItemColor() + "\n" +
				"GridLineColor: " + getGridLineColor() + "\n";
		
	}
	
}
