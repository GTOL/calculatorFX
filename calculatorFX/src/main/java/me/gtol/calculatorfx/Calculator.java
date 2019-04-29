package me.gtol.calculatorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Calculator extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Calculator.class.getResource("view/RootPane.fxml"));
		BorderPane rootPane = loader.load();
		
		Scene scene = new Scene(rootPane, 1024, 768);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
