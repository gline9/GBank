package gbank.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gbank.gui.elem.DefaultTextField;
import gbank.statics.ImageStatics;
import gbank.statics.WindowStatics;
import gbank.types.Account;

public class CreateAccountGui extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final String WINDOW_ID = "Create Account Gui";

	public CreateAccountGui(UserGui parent) {
		super(parent, "Create New Account", true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(ImageStatics.getFavicon());

		DefaultTextField name = new DefaultTextField(20, "Account Name");
		name.setFont(new Font(name.getFont().getFontName(), Font.PLAIN, 30));
		name.setVisible(true);
		name.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(name);

		DefaultTextField balance = new DefaultTextField(20, "Principal Balance");
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

		JButton confirm = new JButton("Create");
		confirm.setFont(new Font(confirm.getFont().getFontName(), Font.PLAIN, 30));
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// grab the data from the fields
				double principal = 0;
				double rate = 0;
				double compoundTime = 0;
				try {
					principal = Double.valueOf(balance.getText());

				} catch (NumberFormatException e) {
					// if they enter principal incorrectly
					JOptionPane.showMessageDialog(null, "Balance needs to be a number!");
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

				// put it into an account
				Account account = new Account(principal, rate, compoundTime);
				account.setName(name.getText());
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
		setVisible(true);
		setResizable(false);
		setModal(true);

	}

	@Override
	public void dispose() {
		// save the location of the window
		WindowStatics.setWindowLocation(getLocation(), WINDOW_ID);

		// dispose the window
		super.dispose();
	}
}
