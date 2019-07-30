package me.gtol.calculatorfx.test;

import javafx.application.Application;
import me.gtol.calculatorfx.Calculator;

public class EclipseMain {
	public static void main(String[] args) {
		System.setProperty("java.library.path", "C:\tmp");
		Application.launch(Calculator.class, args);
	}
}
