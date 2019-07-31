package me.gtol.calculatorfx.controller;

import java.math.BigDecimal;
import java.math.MathContext;
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
	private State state = State.NEW_VALUE;
	private BooleanProperty	isDividedByZero	= new SimpleBooleanProperty(standardPane, "dividedByZero", false);
	private StringProperty	mainText		= new SimpleStringProperty(standardPane, "mainText", "0");
	private StringProperty	subText			= new SimpleStringProperty(standardPane, "subText", "");
	private BigDecimal		result;
	private String			operator;
	private BigDecimal		numCache;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainDisplay.textProperty().bind(mainText);
		subDisplay.textProperty().bind(subText);
	}

	@FXML
	private void appendNumber(ActionEvent event) {
		String main = mainText.get();
		String number = ((Button) event.getSource()).getText();
		
		switch (state) {
		case NEW_VALUE:
			if (main.equals("0")) {
				mainText.set(number);
			} else if (number.equals("0")) {
				if (main.contains(".")) {
					mainText.set(main + number);
				}
			} else {
				mainText.set(main + number);
			}
			break;
		case B_WAITING:
			mainText.set(number);
			break;
		case U_RESULT:
		case RESULT:
			mainText.set(number);
			subText.set("");
			operator = null;
			result = null;
			break;
		default:
			break;
		}
		state = State.NEW_VALUE;
	}

	@FXML
	private void appendDot(ActionEvent event) {
		String main = mainText.get();
		
		switch (state) {
		case NEW_VALUE:
			if (main.equals("0")) {
				mainText.set("0.");
			} else if (!main.contains(".")) {
				mainText.set(main + ".");
			}
			break;
		case B_WAITING:
			mainText.set("0.");
			break;
		case U_RESULT:
		case RESULT:
			mainText.set("0.");
			subText.set("");
			operator = null;
			result = null;
			break;
		}
		state = State.NEW_VALUE;
	}

	@FXML
	private void appendBiOperator(ActionEvent event) {
		String main = mainText.get();
		String sub = subText.get();
		String operatorNew = ((Button) event.getSource()).getId();
		String symbol = getSymbolByOperator(operatorNew);
		
		try {
		switch (state) {
		case NEW_VALUE:
			subText.set(String.join(" ", sub, main, symbol).strip());
			if (operator == null) {
				result = new BigDecimal(main);
			} else {
				result = binaryOperation(operator, result, new BigDecimal(main));
			}
			mainText.set(result.toString());
			break;
		case U_RESULT:
			subText.set(String.join(" ", sub, symbol).strip());
			if (operator == null) {
				result = new BigDecimal(main);
			} else {
				result = binaryOperation(operator, result, new BigDecimal(main));
			}
			mainText.set(result.toString());
			break;
		case B_WAITING:
			subText.set(sub.substring(0, sub.length() - 1) + symbol);
			break;
		case RESULT:
			subText.set(String.join(" ", main, symbol).strip());
			break;
		}
		operator = operatorNew;
		state = State.B_WAITING;
		} catch (Exception e) {
			result = BigDecimal.ZERO;
			isDividedByZero.set(true);
			mainText.set("Cannot divide by zero");
			state = State.RESULT;
		}
	}

	@FXML
	private void appendUnOperator(ActionEvent event) {
		String main = mainText.get();
		String sub = subText.get();
		String operatorNew = (String) ((Button) event.getSource()).getId();
		String symbol = getSymbolByOperator(operatorNew);
		
		if (operatorNew.equals("negate") && state == State.NEW_VALUE && operator == null) {
			if (main.equals("0")) {
				// consume
			} else if (main.contains("-")) {
				mainText.set(main.substring(1));
			} else {
				mainText.set("-" + main);
			}
		} else {
			try {
				String expression;
				switch (state) {
				case NEW_VALUE:
				case B_WAITING:
					expression = symbol + "(" + main + ")";
					System.out.println(sub);
					subText.set(String.join(" ", sub, expression).strip());
					mainText.set(unaryOperation(operatorNew, new BigDecimal(main)).toString());
					break;
				case U_RESULT:
					int lastIndexOfSpace = sub.lastIndexOf(' ');
					String first, last;
					if (lastIndexOfSpace == -1) {
						first = "";
						last = sub;
					} else {
						first = sub.substring(0, lastIndexOfSpace);
						last = sub.substring(lastIndexOfSpace + 1);
					}
					expression = symbol + "(" + last + ")";
					subText.set(String.join(" ", first, expression));
					mainText.set(unaryOperation(operatorNew, new BigDecimal(main)).toString());
					break;
				case RESULT:
					expression = symbol + "(" + main + ")";
					subText.set(expression);
					mainText.set(unaryOperation(operatorNew, new BigDecimal(main)).toString());
					break;
				default:
					break;
				}
				state = State.U_RESULT;
			} catch (Exception e) {
				result = BigDecimal.ZERO;
				isDividedByZero.set(true);
				mainText.set("Cannot divide by zero");
				state = State.RESULT;
			}
		}
	}

	@FXML
	private void getResult(ActionEvent event) {
		String main = mainText.get();
		String sub = subText.get();
		
		try {
			switch (state) {
			case NEW_VALUE:
				subText.set(String.join(" ", sub, main, "=").strip());
				if (operator == null) {
					result = new BigDecimal(main);
				} else {
					result = binaryOperation(operator, result, new BigDecimal(main));
				}
				mainText.set(result.toString());
				break;
			case U_RESULT:
				subText.set(String.join(" ", sub, "=").strip());
				if (operator == null) {
					result = new BigDecimal(main);
				} else {
					result = binaryOperation(operator, result, new BigDecimal(main));
				}
				mainText.set(result.toString());
				break;
			case B_WAITING:
				subText.set(String.join(" ", sub, main, "=").strip());
				result = binaryOperation(operator, result, new BigDecimal(main));
				mainText.set(result.toString());
				numCache = new BigDecimal(main);
			case RESULT:
				if (operator != null) {
					subText.set(String.join(" ", main, operator, numCache.toString(), "=").strip());
					result = binaryOperation(operator, result, numCache);
					mainText.set(result.toString());
				}
			}
			state = State.RESULT;
		} catch (Exception e) {
			result = BigDecimal.ZERO;
			isDividedByZero.set(true);
			mainText.set("Cannot divide by zero");
			state = State.RESULT;
		}
	}

	private static BigDecimal binaryOperation(String operator, BigDecimal num1, BigDecimal num2) throws Exception {
		BigDecimal ret;
		switch (operator) {
		case "add":
			ret = num1.add(num2);
			break;
		case "subtract":
			ret = num1.subtract(num2);
			break;
		case "multiply":
			ret = num1.multiply(num2);
			break;
		case "divide":
			if (num2.equals(BigDecimal.ZERO)) {
				throw new Exception("divided by zero");
			} else {
				ret = num1.divide(num2);
			}
			break;
		default:
			throw new Exception("unsupported operator");
		}
		return ret;
	}
	
	private static BigDecimal unaryOperation(String operator, BigDecimal num) throws Exception {
		BigDecimal ret;
		switch (operator) {
		case "square":
			ret = num.multiply(num);
			break;
		case "flip":
			if (num.equals(BigDecimal.ZERO)) {
				throw new Exception("divided by zero");
			} else {
				ret = BigDecimal.ONE.divide(num);
			}
			break;
		case "squareRoot":
			ret = num.sqrt(MathContext.UNLIMITED);
			break;
		case "negate":
			ret = num.negate();
			break;
		default:
			throw new Exception("unsupported operator");
		}
		return ret;
	}
	
	private static String getSymbolByOperator(String id) {
		switch (id) {
		case "add":
			return "+";
		case "subtract":
			return "−";
		case "multiply":
			return "×";
		case "divide":
			return "÷";
		case "flip":
			return "1/";
		case "square":
			return "sqr";
		case "squareRoot":
			return "√";
		case "negate":
			return "negate";
		default:
			return "ERROR";
		}
	}
	
	private enum State {
		NEW_VALUE, U_RESULT, B_WAITING, RESULT;
	}
}
