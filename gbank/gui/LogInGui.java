package gbank.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import gbank.io.LogIn;

public class LogInGui extends JFrame {

	private static final long serialVersionUID = 1L;

	public LogInGui() {
		super("Gavin's Banking Software");
		setVisible(true);
		this.setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// field for the user to input their username
		JTextField username = new JTextField(20);
		username.setVisible(true);
		this.add(username);

		// field for the user to input their password
		JPasswordField password = new JPasswordField(20);
		password.setVisible(true);
		this.add(password);

		// button to confirm login credentials
		JButton confirm = new JButton("Confirm");
		confirm.setVisible(true);
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// grab username and password from the field
				String passString = String.valueOf(password.getPassword());
				String userString = username.getText();

				// check if the login credentials are correct
				boolean validLogin = LogIn.isValidLogin(userString, passString);
				if (validLogin) {
					// if correct clear the login fields

					password.setText("");
					username.setText("");

					// open the account gui with the given credentials
					new AccountGui(userString, passString);

					// close the current window
					LogInGui.this.dispose();
				} else {
					// if the login credentials are not correct just clear the
					// password field
					password.setText("");

					// give the user a prompt that he logged in incorrectly
					JOptionPane.showMessageDialog(null, "Either username or password is incorrect");
				}
			}
		});
		this.add(confirm);

		this.pack();
	}
}