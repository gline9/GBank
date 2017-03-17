package gbank.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import gbank.types.Account;
import gcore.units.FrequencyUnit;
import gcore.units.TimeUnit;

public class AccountGui extends JDialog {
	private static final long serialVersionUID = 1L;

	private final JLabel balance;
	private final JLabel interest;
	private final JLabel compounds;

	private final Timer updateTimer;

	private final Account account;
	private final int id;

	public AccountGui(UserGui parent, AccountPane accountPane, Account account, int id) {
		super(parent, "Account Details", true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// initialize the final variables
		this.account = account;
		this.id = id;

		// display the details of the account
		String name = account.getName();
		EditableLabel nameLabel = new EditableLabel(name.equals("") ? String.valueOf(id) : name, (String input)-> {
			if (!input.equals(account.getName())){
				account.setName(input);
			}
			
		});
		nameLabel.setFont(new Font(nameLabel.getFont().getFontName(), Font.PLAIN, 40));
		nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(nameLabel);

		balance = new JLabel();
		balance.setFont(new Font(balance.getFont().getFontName(), Font.PLAIN, 40));
		balance.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(balance);

		interest = new JLabel();
		interest.setFont(new Font(interest.getFont().getFontName(), Font.PLAIN, 40));
		interest.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(interest);

		compounds = new JLabel();
		compounds.setFont(new Font(compounds.getFont().getFontName(), Font.PLAIN, 40));
		compounds.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(compounds);

		// set the text for the labels
		setDetails();

		JButton remove = new JButton("Remove");
		remove.setFont(new Font(remove.getFont().getFontName(), Font.PLAIN, 30));
		remove.addActionListener((ActionEvent e) -> {
			// remove the account from the window
			parent.removeAccount(accountPane, id);

			// close the gui as the account is gone
			dispose();

		});
		remove.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(remove);

		// add a repaint timer
		int fps = 60;
		updateTimer = new Timer(
				(int) new FrequencyUnit(fps, FrequencyUnit.PER_SECOND).getDelay().convertTo(TimeUnit.MILLISECONDS),
				(ActionEvent e) -> repaint());
		updateTimer.start();

		pack();
		setResizable(false);
		setVisible(true);
	}

	private void setDetails() {
		String name = account.getName();
		balance.setText(String.format("Balance: %s", account.toString()));
		interest.setText(String.format("Interest Rate: %.2f%%", 100 * account.getRate()));
		compounds.setText(String.format("Compounds per Year: %.0f", account.getCompoundRate()));
	}

	@Override
	public void dispose() {
		// stop the repaint timer
		updateTimer.stop();

		// dispose the main window
		super.dispose();
	}

	@Override
	public void paint(Graphics g) {
		// update the text
		setDetails();

		// paint the main window
		super.paint(g);
	}

}
