package gbank.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import gbank.gui.elem.DefaultTextField;
import gbank.types.Account;
import gbank.types.User;
import gcore.tuples.Pair;

public class TransferGui extends JDialog {
	private static final long serialVersionUID = 1L;

	private final Object[] accounts;

	public TransferGui(Window parent, User user, Account defaultAccount) {
		super(parent, "Transfer Funds", ModalityType.APPLICATION_MODAL);
		accounts = user.getAccounts().stream().map(a -> new AccountItem(a.getSecond(), a.getFirst())).toArray();
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		JComboBox from = new JComboBox(accounts);
		from.setFont(new Font(from.getFont().getFontName(), Font.PLAIN, 30));
		from.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(from);
		
		JComboBox to = new JComboBox(accounts);
		to.setFont(new Font(to.getFont().getFontName(), Font.PLAIN, 30));
		to.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(to);
		
		DefaultTextField amount = new DefaultTextField(20, "Transfer Amount");
		amount.setFont(new Font(amount.getFont().getFontName(), Font.PLAIN, 30));
		amount.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(amount);
		
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	// class for handling the string conversion of an account
	private class AccountItem{
		private final Account account;
		private final int id;
		public AccountItem(Account account, int id){
			this.account = account;
			this.id = id;
		}
		
		public String toString(){
			String name = account.getName();
			name = name.equals("") ? String.valueOf(id) : name;
			return String.format("%s:\t%s", name, account.toString());
		}
	}
}
