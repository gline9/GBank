package gbank.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AccountEditingPane extends JPanel {
	private static final long serialVersionUID = 1L;

	private final JButton add = new JButton("Add Account");
	private final JButton quit = new JButton("Quit");

	public AccountEditingPane() {
		setLayout(new BorderLayout());

		add.addActionListener(this::addAccount);
		add.setFont(new Font(add.getFont().getFontName(), Font.PLAIN, 30));
		add(add, BorderLayout.WEST);

		quit.addActionListener((ActionEvent event) -> ((JFrame) SwingUtilities.getRoot(this)).dispose());
		quit.setFont(new Font(quit.getFont().getFontName(), Font.PLAIN, 30));
		add(quit, BorderLayout.EAST);

		setVisible(true);
	}

	private void addAccount(ActionEvent e) {
		new CreateAccountGui((UserGui) SwingUtilities.getRoot(this));
	}

}
