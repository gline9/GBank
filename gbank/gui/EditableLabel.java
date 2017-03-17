package gbank.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditableLabel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel text = new JLabel();
	private JTextField field = new JTextField();

	private boolean isEditing = false;

	public EditableLabel(String text, Consumer<String> setText) {
		setLayout(new BorderLayout());
		this.text = new JLabel(text);
		this.field = new JTextField(text);

		add(this.text, BorderLayout.WEST);

		String pencil = "\u270E";
		String check = "\u2713";
		JButton edit = new JButton(pencil);
		edit.setFont(new Font(edit.getFont().getFontName(), Font.PLAIN, 30));
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// switch between states when the button is pressed
				if (!isEditing) {
					isEditing = true;
					edit.setText(check);
				} else {
					isEditing = false;
					edit.setText(pencil);

					// change the text in the label and the text to what was
					// typed
					String typedText = field.getText();
					EditableLabel.this.text.setText(typedText);

					// set that the text has changed
					setText.accept(typedText);
				}
				setComponents();
			}

		});
		add(edit, BorderLayout.EAST);

		setVisible(true);

	}

	@Override
	public void setFont(Font font) {
		if (text != null) {
			text.setFont(font);
			field.setFont(font);
		}
		super.setFont(font);
	}

	public void setComponents() {
		if (isEditing) {
			// if we are editing put in the JTextField
			remove(text);
			add(field, BorderLayout.WEST);
		} else {
			// otherwise put in the JLabel
			remove(field);
			add(text, BorderLayout.WEST);
		}
		validate();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}
