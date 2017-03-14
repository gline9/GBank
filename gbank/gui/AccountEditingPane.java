package gbank.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gbank.types.Account;
import gbank.types.User;
import gcore.tuples.Pair;

public class AccountEditingPane extends JPanel {
	private static final long serialVersionUID = 1L;

	private final HashMap<Integer, Pair<AccountPane, Account>> accounts;
	private final User user;

	private final JButton add = new JButton("Add Account");

	public AccountEditingPane(HashMap<Integer, Pair<AccountPane, Account>> accounts, User user) {
		this.accounts = accounts;
		this.user = user;

		add.addActionListener(this::addAccount);
		add.setFont(new Font(add.getFont().getFontName(), Font.PLAIN, 30));
		add(add);

		setVisible(true);
	}

	private void addAccount(ActionEvent e) {
		Account a = new Account(1000000, 1, 100);

		// add the new account to the user
		int id = user.addAccount(a);

		// create a new account pane
		AccountPane pane = new AccountPane(a, id);
		pane.setAlignmentX(Component.LEFT_ALIGNMENT);

		// add the account pane to the main window
		((AccountGui) SwingUtilities.getRoot(this)).addAccount(pane, accounts.size());

		// add the account to the list of accounts
		accounts.put(id, new Pair<>(pane, a));
	}

}
