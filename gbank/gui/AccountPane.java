package gbank.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gbank.types.Account;

public class AccountPane extends JPanel {
	private static final long serialVersionUID = 1L;

	private final Account account;

	private final int id;

	private final JLabel idLabel = new JLabel();
	private final JLabel amountLabel = new JLabel();
	private final JButton remove = new JButton();

	public AccountPane(Account account, int id) {
		this.account = account;
		this.id = id;
		this.setLayout(new FlowLayout());

		setLabels();

		idLabel.setFont(new Font(idLabel.getFont().getFontName(), Font.PLAIN, 40));
		add(idLabel);

		amountLabel.setFont(new Font(amountLabel.getFont().getFontName(), Font.PLAIN, 40));
		add(amountLabel);

		remove.setText("Remove");
		remove.setFont(new Font(remove.getFont().getFontName(), Font.PLAIN, 30));
		remove.addActionListener((ActionEvent e) -> {
			((AccountGui) SwingUtilities.getRoot(this)).removeAccount(this, id);
		});
		add(remove);

		setVisible(true);
	}

	public void paint(Graphics g) {

		// update the labels for the account
		setLabels();

		// call the paint routing of JPanel
		super.paint(g);
	}

	/**
	 * helper method to set the labels for the account pane.
	 * 
	 * @since Mar 13, 2017
	 */
	private void setLabels() {
		idLabel.setText(String.valueOf(id));
		amountLabel.setText(account.toString());
	}

}
