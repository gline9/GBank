package gbank.gui.elem;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class DefaultPasswordField extends JPasswordField {
	private static final long serialVersionUID = 1L;

	private Color defaultTextColor = new Color(0x808080);
	private Color foregroundColor;

	private String defaultText;

	private char echoChar;

	private boolean isDefault = true;

	public DefaultPasswordField(int size, String defaultText) {
		super(size);
		// grab the foreground color from the password field.
		foregroundColor = super.getForeground();

		// grab the echo character
		echoChar = super.getEchoChar();

		// set the text and color to be for the default text
		this.defaultText = defaultText;
		super.setText(defaultText);
		super.setForeground(defaultTextColor);
		super.setEchoChar((char) 0);

		this.defaultText = defaultText;

		// handle the event when someone clicks on or off the password field
		this.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent event) {
				// check if the default text is set
				if (isDefault) {
					// revert settings back to non default
					DefaultPasswordField.super.setForeground(foregroundColor);
					DefaultPasswordField.super.setText("");
					DefaultPasswordField.super.setEchoChar(echoChar);
					isDefault = false;
				}
			}

			@Override
			public void focusLost(FocusEvent event) {
				// check if the field is empty
				if (DefaultPasswordField.super.getPassword().length == 0) {
					DefaultPasswordField.super.setForeground(defaultTextColor);
					DefaultPasswordField.super.setText(DefaultPasswordField.this.defaultText);
					DefaultPasswordField.super.setEchoChar((char) 0);
					// set that we are in default mode
					isDefault = true;
				}
			}
		});
	}

	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String text) {
		// change the default text
		defaultText = text;

		// if we are in default change the text
		if (isDefault) {
			super.setText(text);
		}
	}

	@Override
	public char[] getPassword() {
		// if we are in default mode return nothing otherwise return the text
		// from super class
		if (isDefault) {
			return new char[0];
		}
		return super.getPassword();
	}

	@Override
	public void setText(String text) {

		// if we are not in default change the text
		if (!isDefault) {
			super.setText(text);
		}
	}

	public Color getDefaultTextColor() {
		return defaultTextColor;
	}

	public void setDefaultTextColor(Color color) {
		defaultTextColor = color;

		// if we are in default change the color
		if (isDefault) {
			super.setForeground(color);
		}
	}

	public Color getTextColor() {
		return foregroundColor;
	}

	public void setTextColor(Color color) {
		setForeground(color);
	}

	public void setForeground(Color color) {
		foregroundColor = color;

		// if we are not in default change the color
		if (!isDefault) {
			super.setForeground(color);
		}
	}

	@Override
	public char getEchoChar() {
		return echoChar;
	}

	@Override
	public void setEchoChar(char character) {
		echoChar = character;
		// if we are not in default change the echo char

		if (!isDefault) {
			super.setEchoChar(character);
		}
	}
}
