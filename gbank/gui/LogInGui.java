package gbank.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
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
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// field for the user to input their username
		JTextField username = new JTextField(20);
		username.setFont(new Font(username.getFont().getFontName(), Font.PLAIN, 30));
		username.setVisible(true);
		username.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(username);

		// field for the user to input their password
		JPasswordField password = new JPasswordField(20);
		password.setFont(new Font(password.getFont().getFontName(), Font.PLAIN, 30));
		password.setVisible(true);
		password.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(password);

		// button to confirm login credentials
		JButton confirm = new JButton("Confirm");
		confirm.setFont(new Font(confirm.getFont().getFontName(), Font.PLAIN, 30));
		confirm.setAlignmentX(Component.LEFT_ALIGNMENT);
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
					dispose();
				} else {
					// if the login credentials are not correct just clear the
					// password field
					password.setText("");

					// give the user a prompt that he logged in incorrectly
					JOptionPane.showMessageDialog(null, "Either username or password is incorrect");
				}
			}
		});
		add(confirm);

		pack();
	}

	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
}