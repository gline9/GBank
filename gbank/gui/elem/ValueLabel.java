package gbank.gui.elem;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ValueLabel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel nameLabel = new JLabel();
	private JLabel valueLabel = new JLabel();

	public ValueLabel() {
		this("", "");
	}

	public ValueLabel(String name) {
		this(name, "");
	}

	public ValueLabel(String name, String value) {
		setLayout(new BorderLayout());

		// set the labels
		nameLabel.setText(name);
		valueLabel.setText(value);

		// add in the labels
		add(nameLabel, BorderLayout.WEST);
		add(valueLabel, BorderLayout.EAST);

		setVisible(true);
	}

	public void setNameText(String name) {
		nameLabel.setText(name);
	}

	public void setValueText(String value) {
		valueLabel.setText(value);
	}

	public void setFont(Font font) {
		if (null != nameLabel)
			nameLabel.setFont(font);
		if (null != valueLabel)
			valueLabel.setFont(font);
	}
	
	public Font getFont() {
		return null != nameLabel ? nameLabel.getFont() : null;
	}
}
