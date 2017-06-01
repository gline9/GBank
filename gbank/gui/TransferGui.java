package gbank.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.UIManager;

import gbank.gui.elem.DefaultTextField;
import gbank.statics.ImageStatics;
import gbank.statics.WindowStatics;
import gbank.types.Account;
import gbank.types.User;
import gcore.units.FrequencyUnit;
import gcore.units.TimeUnit;

public class TransferGui extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final String WINDOW_ID = "Transfer Gui";

	private final Object[] accounts;

	private final Timer updateTimer;

	public static final Account deposit = getDeposit();
	public static final Account withdraw = getWithdraw();

	private static final AccountItem depositItem = new AccountItem(deposit, 0);
	private static final AccountItem withdrawItem = new AccountItem(withdraw, 0);

	public TransferGui(Window parent, User user) {
		this(parent, user, null, null);
	}

	public TransferGui(Window parent, User user, Account defaultFromAccount, Account defaultToAccount) {
		this(parent, user, defaultFromAccount, true, defaultToAccount, true);
	}

	public TransferGui(Window parent, User user, Account defaultFromAccount, boolean fromEditable,
			Account defaultToAccount, boolean toEditable) {
		super(parent, "Transfer Funds", ModalityType.APPLICATION_MODAL);
		setIconImage(ImageStatics.getFavicon());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		accounts = user.getAccounts().stream().map(a -> new AccountItem(a.getSecond(), a.getFirst())).toArray();

		// change the color of a disabled combo box
		UIManager.put("ComboBox.disabledForeground", new Color(0xff808080));

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JComboBox<Object> from = new JComboBox<>(accounts);

		// add the deposit account
		from.addItem(depositItem);

		// if we pass in null don't set the default item
		if (defaultFromAccount != null)
			from.setSelectedItem(new AccountItem(defaultFromAccount, 0));

		// if combo box is not editable set that
		from.setEnabled(fromEditable);

		from.setFont(new Font(from.getFont().getFontName(), Font.PLAIN, 30));
		from.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(from);

		JComboBox<Object> to = new JComboBox<>(accounts);

		// add the withdraw account
		to.addItem(withdrawItem);

		// if we pass in null don't set the default item
		if (defaultToAccount != null)
			to.setSelectedItem(new AccountItem(defaultToAccount, 0));

		// if combo box is not editable set that
		to.setEnabled(toEditable);

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
		setLocation(WindowStatics.getWindowLocation(WINDOW_ID));
		setResizable(false);
		setVisible(true);
	}

	@Override
	public void dispose() {

		updateTimer.stop();

		// save the location of the window
		WindowStatics.setWindowLocation(getLocation(), WINDOW_ID);

		super.dispose();
	}

	// class for handling the string conversion of an account
	private static class AccountItem {
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

	// initializer methods for the deposit and withdraw accounts
	private static Account getDeposit() {
		Account deposit = new Account(Double.POSITIVE_INFINITY, 0, 1);
		deposit.setName("Deposit");
		return deposit;
	}

	private static Account getWithdraw() {
		Account withdraw = new Account(Double.NEGATIVE_INFINITY, 0, 1);
		withdraw.setName("Withdraw");
		return withdraw;
	}
}
