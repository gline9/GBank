package gbank.gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class DefaultTextField extends JTextField {
	private static final long serialVersionUID = 1L;

	private Color defaultTextColor = new Color(0x808080);
	private Color foregroundColor;

	private String defaultText;

	private boolean isDefault = true;

	public DefaultTextField(int size, String defaultText) {
		super(size);
		// grab the foreground color from the textfield.
		foregroundColor = super.getForeground();

		// set the text and color to be for the default text
		this.defaultText = defaultText;
		super.setText(defaultText);
		super.setForeground(defaultTextColor);

		this.defaultText = defaultText;

		// handle the event when someone clicks on or off the text field
		this.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent event) {
				// check if the default text is set
				if (isDefault) {
					// revert settings back to non default
					DefaultTextField.super.setForeground(foregroundColor);
					DefaultTextField.super.setText("");
					isDefault = false;
				}
			}

			@Override
			public void focusLost(FocusEvent event) {
				// check if the field is empty
				if (DefaultTextField.super.getText().equals("")) {
					DefaultTextField.super.setForeground(defaultTextColor);
					DefaultTextField.super.setText(DefaultTextField.this.defaultText);
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
	public String getText() {
		// if we are in default mode return nothing otherwise return the text
		// from super class
		if (isDefault) {
			return "";
		}
		return super.getText();
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
}
