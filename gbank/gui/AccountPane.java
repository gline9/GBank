package gbank.gui;

import java.awt.BorderLayout;
import java.awt.Color;
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

	private Color hoverColor = new Color(0xB0B0B0);
	private Color backgroundColor;

	private boolean isHovering = false;

	public AccountPane(Account account, int id) {
		this.account = account;
		this.id = id;
		this.setLayout(new BorderLayout());

		// grab the background color
		backgroundColor = getBackground();

		setLabels();

		idLabel.setFont(new Font(idLabel.getFont().getFontName(), Font.PLAIN, 40));
		add(idLabel, BorderLayout.WEST);

		amountLabel.setFont(new Font(amountLabel.getFont().getFontName(), Font.PLAIN, 40));
		add(amountLabel, BorderLayout.EAST);

		// add a mouse listener so we can change color when the mouse hovers
		// over the account pane
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// open the Account Gui to edit the account
				new AccountGui((UserGui) SwingUtilities.getRoot(AccountPane.this), AccountPane.this, account, id);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// update the flag on if component is being hovered
				isHovering = true;

				// when the mouse enters change the color to the hover color
				AccountPane.super.setBackground(hoverColor);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// update the flag on if component is being hovered
				isHovering = false;
				// when the mouse exists change the color to the background
				// color
				AccountPane.super.setBackground(backgroundColor);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {}

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
		String name = account.getName();
		idLabel.setText(name.equals("") ? String.valueOf(id) : name);
		amountLabel.setText(account.toString());
	}

	public Color getHoverColor() {
		return hoverColor;
	}

	public void setHoverColor(Color hoverColor) {
		// if hovering set the color and hover color otherwise just hover color
		this.hoverColor = hoverColor;

		if (isHovering) {
			super.setBackground(hoverColor);
		}
	}

	public Color getNonHoverColor() {
		return backgroundColor;
	}

	@Override
	public void setBackground(Color color) {
		// if not hovering set the color and the background color otherwise just
		// the background color
		this.backgroundColor = color;

		if (!isHovering) {
			super.setBackground(color);
		}
	}

}
