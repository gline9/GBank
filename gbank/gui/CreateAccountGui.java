package gbank.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

import gbank.types.Account;

public class CreateAccountGui extends JDialog {
	private static final long serialVersionUID = 1L;

	public CreateAccountGui(AccountGui parent) {
		super(parent, true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		DefaultTextField balance = new DefaultTextField(10, "Balance");
		balance.setFont(new Font(balance.getFont().getFontName(), Font.PLAIN, 30));
		balance.setVisible(true);
		balance.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(balance);

		DefaultTextField interest = new DefaultTextField(10, "Interest");
		interest.setFont(new Font(interest.getFont().getFontName(), Font.PLAIN, 30));
		interest.setVisible(true);
		interest.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(interest);

		DefaultTextField compound = new DefaultTextField(10, "Compound");
		compound.setFont(new Font(compound.getFont().getFontName(), Font.PLAIN, 30));
		compound.setVisible(true);
		compound.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(compound);

		JButton confirm = new JButton("Confirm");
		confirm.setFont(new Font(confirm.getFont().getFontName(), Font.PLAIN, 30));
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// grab the data from the fields
				double principal = Double.valueOf(balance.getText());
				double rate = Double.valueOf(interest.getText());
				double compoundTime = Double.valueOf(compound.getText());

				// put it into an account
				Account account = new Account(principal, rate, compoundTime);
				parent.addAccount(account);

				// close the current window
				dispose();
			}
		});
		add(confirm);
		pack();
		setVisible(true);
		setResizable(false);
		setModal(true);

	}
}
