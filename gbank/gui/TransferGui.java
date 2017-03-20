package gbank.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import gbank.gui.elem.DefaultTextField;
import gbank.types.Account;
import gbank.types.User;
import gcore.units.FrequencyUnit;
import gcore.units.TimeUnit;

public class TransferGui extends JDialog {
	private static final long serialVersionUID = 1L;

	private final Object[] accounts;

	private final Timer updateTimer;

	public TransferGui(Window parent, User user, Account defaultFromAccount, Account defaultToAccount) {
		super(parent, "Transfer Funds", ModalityType.APPLICATION_MODAL);
		accounts = user.getAccounts().stream().map(a -> new AccountItem(a.getSecond(), a.getFirst())).toArray();

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JComboBox<Object> from = new JComboBox<>(accounts);

		// if we pass in null don't set the default item
		if (defaultFromAccount != null)
			from.setSelectedItem(new AccountItem(defaultFromAccount, 0));

		from.setFont(new Font(from.getFont().getFontName(), Font.PLAIN, 30));
		from.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(from);

		JComboBox<Object> to = new JComboBox<>(accounts);

		// if we pass in null don't set the default item
		if (defaultToAccount != null)
			to.setSelectedItem(new AccountItem(defaultToAccount, 0));

		to.setFont(new Font(to.getFont().getFontName(), Font.PLAIN, 30));
		to.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(to);

		DefaultTextField amount = new DefaultTextField(20, "Transfer Amount");
		amount.setFont(new Font(amount.getFont().getFontName(), Font.PLAIN, 30));
		amount.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(amount);

		JButton transfer = new JButton("Make Transfer");
		transfer.setFont(new Font(transfer.getFont().getFontName(), Font.PLAIN, 30));
		transfer.addActionListener(e -> {
			double transferAmount = 0;
			try {
				transferAmount = Double.valueOf(amount.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Transfer Amount must be a valid number.");
				return;
			}
			Account accountFrom = ((AccountItem) from.getSelectedItem()).getAccount();
			Account accountTo = ((AccountItem) to.getSelectedItem()).getAccount();

			// withdraw as much as you can from account from
			double withdrawn = accountFrom.withdraw(transferAmount);

			// deposit as much of that as possible to the account to
			double deposited = accountTo.deposit(withdrawn);

			// if there is a difference return that to the original account
			accountFrom.deposit(withdrawn - deposited);

			// notify how much money was transfered
			JOptionPane.showMessageDialog(null,
					String.format("$%,3.2f was successfully transfered from account %s to account %s", deposited,
							((AccountItem) from.getSelectedItem()).toString(),
							((AccountItem) to.getSelectedItem()).toString()));

			// close the transfer dialog when the pane is closed
			dispose();

		});
		transfer.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(transfer);

		// add a repaint timer
		int fps = 60;
		updateTimer = new Timer(
				(int) new FrequencyUnit(fps, FrequencyUnit.PER_SECOND).getDelay().convertTo(TimeUnit.MILLISECONDS),
				(ActionEvent e) -> {
					repaint();
				});
		updateTimer.start();

		pack();
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void dispose() {

		updateTimer.stop();

		super.dispose();
	}

	// class for handling the string conversion of an account
	private class AccountItem {
		private final Account account;
		
		private final String updateString;

		public AccountItem(Account account, int id) {
			this.account = account;
			String name = account.getName();
			name = name.equals("") ? String.valueOf(id) : name;
			updateString = String.format("%s:\t%s", name, account.toString());
		}

		public Account getAccount() {
			return account;
		}

		public String toString() {
			return updateString;
		}

		public boolean equals(Object obj) {
			// only equal if they are the same account or the same account item
			if (obj instanceof AccountItem)
				return ((AccountItem) obj).account == account;
			return false;
		}
	}
}
