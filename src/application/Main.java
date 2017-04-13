package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * @author Arafin
 *
 */

public class Main extends Application {

	public static Stage stage;

	@Override
	public void start(final Stage primaryStage) {
		try {
			setStage(primaryStage);

			Parent root = FXMLLoader.load(getClass().getResource("/application/Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setTitle("Simple Media PLayer");
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("icon/Simple Media Player.png"));
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		launch(args);
	}

	public static void setStage(Stage stage) {
		Main.stage = stage;
	}

	public static Stage getStage() {
		return stage;
	}
}
