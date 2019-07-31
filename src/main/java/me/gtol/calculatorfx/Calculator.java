package me.gtol.calculatorfx;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Calculator extends Application {
	
	@Override
	public void init() throws Exception {
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader rootPaneloader = new FXMLLoader(Calculator.class.getResource("view/RootPane.fxml"));
		BorderPane rootPane = rootPaneloader.load();
		
		FXMLLoader standardLoader = new FXMLLoader(Calculator.class.getResource("view/StandardPane.fxml"));
		GridPane standardPane = standardLoader.load();
		rootPane.setCenter(standardPane);
		
		Scene scene = new Scene(rootPane, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
