package me.gtol.calculatorfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Calculator extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button button = new Button("Hello World");
		Scene scene = new Scene(button, 200, 200);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
