package me.gtol.calculatorfx.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class StandardPaneController implements Initializable {
	@FXML
	private GridPane	standardPane;
	@FXML
	private Label		subDisplay;
	@FXML
	private Label		mainDisplay;
	@FXML
	private Button		seven;
	@FXML
	private Button		eight;
	@FXML
	private Button		nine;
	@FXML
	private Button		four;
	@FXML
	private Button		five;
	@FXML
	private Button		six;
	@FXML
	private Button		minusButton;
	@FXML
	private Button		one;
	@FXML
	private Button		two;
	@FXML
	private Button		three;
	@FXML
	private Button		plusButton;
	@FXML
	private Button		negateButton;
	@FXML
	private Button		zero;
	@FXML
	private Button		dotButton;
	@FXML
	private Button		equalButton;

	// data model
	private boolean			isResult		= false;
	private boolean			isWaiting		= false;
	private BooleanProperty	isDividedByZero	= new SimpleBooleanProperty(standardPane, "dividedByZero", false);
	private StringProperty	mainText		= new SimpleStringProperty(standardPane, "mainText", "0");
	private StringProperty	subText			= new SimpleStringProperty(standardPane, "subText", "");
	private BigDecimal		result;
	private String			operand;
	private BigDecimal		numCache;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainDisplay.textProperty().bind(mainText);
		subDisplay.textProperty().bind(subText);
	}

	@FXML
	private void appendNumber(ActionEvent event) {
		String currentText = mainText.get();
		String number = ((Button) event.getSource()).getText();
		if (isResult || isDividedByZero.get()) {
			result = null;
			operand = null;
			subText.set("");
			mainText.set(number);
		} else if (isWaiting || currentText.equals("0")) {
			mainText.set(number);
		} else if (number.equals("0")) {
			if (currentText.contains(".")) {
				mainText.set(currentText + number);
			}
		} else {
			mainText.set(currentText + number);
		}
		isWaiting = false;
		isResult = false;
		isDividedByZero.set(false);
	}

	@FXML
	private void appendDot(ActionEvent event) {
		String currentText = mainText.get();
		if (isResult || isDividedByZero.get()) {
			result = null;
			operand = null;
			subText.set("");
			mainText.set("0.");
		} else if (isWaiting || currentText.equals("0")) {
			mainText.set("0.");
		} else if (!currentText.contains(".")) {
			mainText.set(currentText + ".");
		}
		isWaiting = false;
		isResult = false;
		isDividedByZero.set(false);
	}

	@FXML
	private void appendOperator(ActionEvent event) {
		String main = mainText.get();
		String sub = subText.get();
		String sign = ((Button) event.getSource()).getText();

		if (operand == null) {
			result = parseNumber(main);
			subText.set(String.join(" ", main, sign));
		} else if (isWaiting) {
			subText.set(sub.substring(0, sub.length() - 1) + sign);
		} else if (isResult) {
			isResult = false;
			subText.set(String.join(" ", main, sign));
		} else { // needs calculate
			subText.set(String.join(" ", sub, main, sign));
			result = binaryOperation(operand, result, parseNumber(main));

			if (isDividedByZero.get()) {
				mainText.set("Cannot divide by zero");
			} else {
				mainText.set(result.toString());
			}
		}
		operand = sign;
		isWaiting = true;
	}

	@FXML
	private void negate(ActionEvent event) {
		String currentText = mainText.get();
	}

	@FXML
	private void getResult(ActionEvent event) {
		String main = mainText.get();
		String sub = subText.get();
		if (isResult) {
			if (operand != null) {
				subText.set(String.join(" ", main, operand, numCache.toString(), "=").strip());
				result = binaryOperation(operand, result, numCache);

				if (isDividedByZero.get()) {
					mainText.set("Cannot divide by zero");
				} else {
					mainText.set(result.toString());
				}
			}
		} else {
			subText.set(String.join(" ", sub, main, "=").strip());
			if (operand != null) {
				numCache = parseNumber(main);
				System.out.println(operand + result + numCache);
				result = binaryOperation(operand, result, numCache);
				System.out.println(result);
				if (isDividedByZero.get()) {
					mainText.set("Cannot divide by zero");
				} else {
					mainText.set(result.toString());
				}
			}
		}
		isResult = true;
	}

	private BigDecimal binaryOperation(String operator, BigDecimal num1, BigDecimal num2) {
		BigDecimal ret;
		switch (operator) {
		case "+":
			ret = num1.add(num2);
			break;
		case "−":
			ret = num1.subtract(num2);
			break;
		case "×":
			ret = num1.multiply(num2);
			break;
		case "÷":
			if (num2.doubleValue() == 0) {
				isDividedByZero.set(true);
				ret = BigDecimal.valueOf(0);
			} else {
				ret = num1.divide(num2);
			}
			break;
		default:
			ret = BigDecimal.valueOf(0);
			break;
		}
		return ret;
	}

//	private void calculate() {
//		if (subText.get().isEmpty()) {
//			return;
//		}
//		String[] formula = subText.get().split(" ");
//		result = parseNumber(formula[0]);
//		for (int i = 1; i < formula.length; i++) {
//			if (i == formula.length - 1) {
//				break;
//			}
//			if (i % 2 == 1) {
//				Number num = parseNumber(formula[i+1]);
//				switch (formula[i]) {
//				case "+":
//					result = result.doubleValue() + num.doubleValue();
//					break;
//				case "−":
//					result = result.doubleValue() - num.doubleValue();
//					break;
//				case "×":
//					result = result.doubleValue() * num.doubleValue();
//					break;
//				case "÷":
//					if (num.doubleValue() == 0) {
//						result = 0;
//						isDividedByZero = true;
//					} else {
//						result = result.doubleValue() / num.doubleValue();
//					}
//					break;
//				}
//			}
//		}
//		if (isDividedByZero) {
//			mainText.set("Cannot divide by zero");
//		} else if (result.doubleValue() % 1.0 == 0) {
//			mainText.set(Long.toString(result.longValue()));
//		} else {
//			mainText.set(result.toString());
//		}
//	}

	private static BigDecimal parseNumber(String expression) throws NumberFormatException {
		int br1 = expression.indexOf('(');
		if (br1 != -1) {
			int br2 = expression.lastIndexOf(')');
			BigDecimal number = parseNumber(expression.substring(br1 + 1, br2));
			switch (expression.substring(0, br1)) {
			case "sqr":
				break;
			case "1/":
				break;
			case "√":
				break;
			case "negate":
				break;
			}
			return null;
		} else {
			return new BigDecimal(expression);
		}
	}
}
