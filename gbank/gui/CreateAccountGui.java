package gbank.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class CreateAccountGui extends JDialog {
	private static final long serialVersionUID = 1L;

	public CreateAccountGui(JFrame parent) {
		super(parent, true);
		setVisible(true);
		setModal(true);
	}
}
