package gbank.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gbank.types.Account;
import gbank.types.User;

public class AccountPane extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final int Height = 55;
	public static final int Width = 900;

	private final Account account;

	private final int id;

	private final JLabel idLabel = new JLabel();
	private final JLabel amountLabel = new JLabel();

	private Color hoverColor = new Color(0xB0B0B0);
	private Color backgroundColor;

	private boolean isHovering = false;

	public AccountPane(User user, Account account, int id) {
		this.account = account;
		this.id = id;
		this.setLayout(new BorderLayout());

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());
		
		// add the drag button
		JLabel drag = new JLabel("\u2630");
		drag.setFont(new Font(drag.getFont().getFontName(), Font.PLAIN, 40));
		add(drag, BorderLayout.WEST);

		// grab the background color
		backgroundColor = getBackground();

		setLabels();

		// set the maximum size for id label and amount label
		idLabel.setPreferredSize(new Dimension(Width / 2, Height));

		// this is so the account pane will always have a definite size
		textPanel.setPreferredSize(new Dimension(Width, Height));
		textPanel.setMaximumSize(new Dimension(Width, Height));

		idLabel.setFont(new Font(idLabel.getFont().getFontName(), Font.PLAIN, 40));
		textPanel.add(idLabel, BorderLayout.WEST);

		amountLabel.setFont(new Font(amountLabel.getFont().getFontName(), Font.PLAIN, 40));
		amountLabel.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {}

			@Override
			public void componentResized(ComponentEvent event) {
				// check if the component went past the maximum width
				if (amountLabel.getWidth() > Width / 2) {
					// if so resize to the appropriate width
					amountLabel.setPreferredSize(new Dimension(Width / 2, amountLabel.getHeight()));
				}
			}

			@Override
			public void componentShown(ComponentEvent arg0) {}
		});
		textPanel.add(amountLabel, BorderLayout.EAST);
		
		// add the text panel
		add(textPanel, BorderLayout.CENTER);

		// add a mouse listener so we can change color when the mouse hovers
		// over the account pane
		textPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// open the Account Gui to edit the account
				new AccountGui((UserGui) SwingUtilities.getRoot(AccountPane.this), user, AccountPane.this, account, id);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// update the flag on if component is being hovered
				isHovering = true;

				// when the mouse enters change the color to the hover color
				textPanel.setBackground(hoverColor);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// update the flag on if component is being hovered
				isHovering = false;
				// when the mouse exists change the color to the background
				// color
				textPanel.setBackground(backgroundColor);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
//		setPreferredSize(new Dimension(Width, Height));
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
	public void setLabels() {
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
	
	public static int getFullWidth(){
		return Width + 36;
	}

}
