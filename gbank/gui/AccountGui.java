package gbank.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

public class AccountGui extends JDialog {
	private static final long serialVersionUID = 1L;

	public AccountGui(UserGui parent, AccountPane accountPane, int id) {
		super(parent, true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JButton remove = new JButton("Remove");
		remove.setFont(new Font(remove.getFont().getFontName(), Font.PLAIN, 30));
		remove.addActionListener((ActionEvent e) -> {
			// remove the account from the window
			parent.removeAccount(accountPane, id);

			// close the gui as the account is gone
			dispose();

		});
		add(remove);

		pack();
		setVisible(true);
		setResizable(false);
	}
}
