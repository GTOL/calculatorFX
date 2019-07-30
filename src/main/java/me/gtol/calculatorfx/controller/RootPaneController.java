package me.gtol.calculatorfx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.layout.HBox;

public class RootPaneController implements Initializable {
	@FXML
	MenuBar		menuBar;
	@FXML
	Menu		typeMenu;
	@FXML
	MenuItem	standard;
	@FXML
	MenuItem	scientific;
	@FXML
	Menu		aboutMenu;
	@FXML
	MenuItem	about;
	@FXML
	HBox		statusBar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

}
