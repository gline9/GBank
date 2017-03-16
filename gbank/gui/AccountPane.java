package gbank.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

	private final Color hoverColor = new Color(0xB0B0B0);
	private final Color backgroundColor;

	public AccountPane(Account account, int id) {
		this.account = account;
		this.id = id;
		this.setLayout(new FlowLayout());

		// grab the background color
		backgroundColor = getBackground();

		setLabels();

		idLabel.setFont(new Font(idLabel.getFont().getFontName(), Font.PLAIN, 40));
		add(idLabel);

		amountLabel.setFont(new Font(amountLabel.getFont().getFontName(), Font.PLAIN, 40));
		add(amountLabel);

		// add a mouse listener so we can change color when the mouse hovers
		// over the account pane
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// when the mouse enters change the color to the hover color
				setBackground(hoverColor);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// when the mouse exists change the color to the background
				// color
				setBackground(backgroundColor);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// for now set the default action to remove the account
				((AccountGui) SwingUtilities.getRoot(AccountPane.this)).removeAccount(AccountPane.this, id);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});

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
