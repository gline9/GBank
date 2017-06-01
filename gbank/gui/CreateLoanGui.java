package gbank.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gbank.gui.elem.DefaultTextField;
import gbank.statics.ImageStatics;
import gbank.statics.WindowStatics;
import gbank.types.Account;
import gbank.types.LoanAgainstAccount;
import gbank.types.User;

public class CreateLoanGui extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private static final String WINDOW_ID = "Create Loan Gui";

	public CreateLoanGui(UserGui parent, User user) {
		super(parent, "Create Loan", true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setIconImage(ImageStatics.getFavicon());

		DefaultTextField name = new DefaultTextField(20, "Account Name");
		name.setFont(new Font(name.getFont().getFontName(), Font.PLAIN, 30));
		name.setVisible(true);
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(name);

		DefaultTextField balance = new DefaultTextField(20, "Amount to Take Out");
		balance.setFont(new Font(balance.getFont().getFontName(), Font.PLAIN, 30));
		balance.setVisible(true);
		balance.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(balance);

		DefaultTextField interest = new DefaultTextField(20, "Interest Rate");
		interest.setFont(new Font(interest.getFont().getFontName(), Font.PLAIN, 30));
		interest.setVisible(true);
		interest.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(interest);

		DefaultTextField compound = new DefaultTextField(20, "Compounds per Year");
		compound.setFont(new Font(compound.getFont().getFontName(), Font.PLAIN, 30));
		compound.setVisible(true);
		compound.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(compound);

		DefaultTextField maxOwed = new DefaultTextField(20, "Maximum Owed");
		maxOwed.setFont(new Font(maxOwed.getFont().getFontName(), Font.PLAIN, 30));
		maxOwed.setVisible(true);
		maxOwed.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(maxOwed);

		JLabel accountFromLabel = new JLabel("Account Loan is Against");
		accountFromLabel.setFont(new Font(accountFromLabel.getFont().getFontName(), Font.PLAIN, 30));
		accountFromLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(accountFromLabel);

		Object[] accounts = user.getAccounts().stream().map(a -> new AccountItem(a.getSecond(), a.getFirst()))
				.toArray();
		JComboBox<Object> accountFrom = new JComboBox<>(accounts);
		accountFrom.setFont(new Font(accountFrom.getFont().getFontName(), Font.PLAIN, 30));
		accountFrom.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(accountFrom);

		JButton confirm = new JButton("Create");
		confirm.setFont(new Font(confirm.getFont().getFontName(), Font.PLAIN, 30));
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// grab the data from the fields
				double principal = 0;
				double rate = 0;
				double compoundTime = 0;
				double maximumOwed = -1;
				try {
					principal = Double.valueOf(balance.getText());

					// make sure the amount is positive
					if (principal < 0)
						throw new Exception();

				} catch (Exception e) {
					// if they enter principal incorrectly
					JOptionPane.showMessageDialog(null, "Amount needs to be a positive number!");
					return;
				}
				try {
					rate = Double.valueOf(interest.getText());
				} catch (NumberFormatException e) {
					// if they enter interest rate incorrectly
					JOptionPane.showMessageDialog(null, "Interest rate needs to be a number!");
					return;
				}
				try {
					compoundTime = Double.valueOf(compound.getText());
				} catch (NumberFormatException e) {
					// if they enter compound time incorrectly
					JOptionPane.showMessageDialog(null, "Compounds per year needs to be a number!");
					return;
				}
				// check to see if there is a maximum owed amount entered
				String maxOwedString = maxOwed.getText();
				if (!maxOwedString.equals("")) {
					try {
						maximumOwed = Double.valueOf(maxOwed.getText());
					} catch (NumberFormatException e) {
						// if they enter maximum owed incorrectly
						JOptionPane.showMessageDialog(null, "Maximum amount owed needs to be a number or blank!");
						return;
					}
				}

				Object selected = accountFrom.getSelectedItem();
				Account accountAgainst = ((AccountItem) selected).getAccount();

				// remove that amount from the account this loan is against
				double withdrawn = accountAgainst.withdraw(principal);

				// put that amount into the account
				LoanAgainstAccount account = new LoanAgainstAccount(withdrawn, rate, compoundTime, accountAgainst.getAccountID());
				account.setName(name.getText());
				account.setMaximumOwed(maximumOwed);

				parent.addAccount(account);

				// close the current window
				dispose();
			}
		});
		add(confirm);

		// set the button for creation to be pressed when enter is pressed.
		getRootPane().setDefaultButton(confirm);

		pack();
		setLocation(WindowStatics.getWindowLocation(WINDOW_ID));
		setResizable(false);
		setVisible(true);

	}

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

	@Override
	public void dispose() {
		// save the location of the window
		WindowStatics.setWindowLocation(getLocation(), WINDOW_ID);
		
		// dispose the window
		super.dispose();
	}
}
