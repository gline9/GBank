package gbank.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import gbank.io.LogIn;

public class LogInGui extends JFrame {

	private static final long serialVersionUID = 1L;

	private final int defaultTextColor = 0xff808080;
	private final String usernameDefaultText = "Username";
	private final String passwordDefaultText = "Password";

	public LogInGui() {
		super("Gavin's Banking Software");
		setVisible(true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// field for the user to input their username
		JTextField username = new JTextField(20);
		username.setFont(new Font(username.getFont().getFontName(), Font.PLAIN, 30));

		// set the default text for the username field
		username.setForeground(new Color(defaultTextColor));
		username.setText(usernameDefaultText);
		username.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent event) {
				Color color = username.getForeground();
				// check if the color is still the default text color
				if (color.getRGB() == defaultTextColor) {
					username.setForeground(Color.BLACK);
					username.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// check if the field is empty
				if (username.getText().trim().equals("")) {
					username.setForeground(new Color(defaultTextColor));
					username.setText(usernameDefaultText);
				}
			}
		});

		username.setVisible(true);
		username.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(username);

		// field for the user to input their password
		JPasswordField password = new JPasswordField(20);
		password.setFont(new Font(password.getFont().getFontName(), Font.PLAIN, 30));

		// set the default text for the password field
		password.setForeground(new Color(defaultTextColor));
		// used to show the text instead of just the * character until the user
		// enters something
		password.setEchoChar((char) 0);
		password.setText(passwordDefaultText);
		password.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent event) {
				Color color = password.getForeground();
				// check if the color is still the default text color
				if (color.getRGB() == defaultTextColor) {
					password.setForeground(Color.BLACK);
					password.setText("");
					password.setEchoChar('*');
				}
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// check if the field is empty
				if (password.getPassword().length == 0) {
					password.setForeground(new Color(defaultTextColor));
					password.setText(passwordDefaultText);
					password.setEchoChar((char) 0);
				}
			}
		});

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
		
		// set the button for logon to be pressed when enter is pressed.
		getRootPane().setDefaultButton(confirm);

		pack();
	}
}