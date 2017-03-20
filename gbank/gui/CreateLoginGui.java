package gbank.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gbank.gui.elem.DefaultPasswordField;
import gbank.gui.elem.DefaultTextField;
import gbank.io.LogIn;
import gbank.io.UserIO;
import gbank.statics.FileLocations;
import gbank.types.User;

public class CreateLoginGui extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final String usernameDefaultText = "Username";
	private final String passwordDefaultText = "Password";
	private final String confirmPasswordDefaultText = "Confirm Password";

	public CreateLoginGui() {
		super("Create an Account");
		setVisible(true);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// field for the user to input their username
		DefaultTextField username = new DefaultTextField(20, usernameDefaultText);
		username.setFont(new Font(username.getFont().getFontName(), Font.PLAIN, 30));
		username.setVisible(true);
		username.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(username);

		// field for the user to input their password
		DefaultPasswordField password = new DefaultPasswordField(20, passwordDefaultText);
		password.setFont(new Font(password.getFont().getFontName(), Font.PLAIN, 30));
		password.setVisible(true);
		password.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(password);

		// field for the user to input their password
		DefaultPasswordField confirmP = new DefaultPasswordField(20, confirmPasswordDefaultText);
		confirmP.setFont(new Font(confirmP.getFont().getFontName(), Font.PLAIN, 30));
		confirmP.setVisible(true);
		confirmP.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(confirmP);

		// create a pane for the buttons so they are on the same level
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());

		// button to confirm login credentials
		JButton confirm = new JButton("Confirm");
		confirm.setFont(new Font(confirm.getFont().getFontName(), Font.PLAIN, 30));
		confirm.setVisible(true);
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// grab username and password from the field
				String passString = String.valueOf(password.getPassword());
				String pass2String = String.valueOf(confirmP.getPassword());
				String userString = username.getText();

				if (!passString.equals(pass2String)) {
					JOptionPane.showMessageDialog(null, "Passwords don't match!!");
					return;
				}

				boolean success = LogIn.addNewLogin(userString, passString);

				// if successful add the file for the user data
				if (success) {
					User newUser = new User();
					try {
						// grab the file to save to
						File location = new File(FileLocations.getAccountInfoFile(userString));

						// make sure all of the directories are created up to
						// the file
						location.getParentFile().mkdirs();

						// save to the location file
						UserIO.saveToFile(location, passString, newUser);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Throwable e) {
						e.printStackTrace();
					}

					// open the account gui with the given credentials
					new UserGui(userString, passString);

					// close the current window
					dispose();
				} else {

					// give the user a prompt that he logged in incorrectly
					JOptionPane.showMessageDialog(null, "Username is already taken!!");
				}
			}
		});
		buttonPanel.add(confirm, BorderLayout.EAST);

		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(buttonPanel);

		// set the button for creation to be pressed when enter is pressed.
		getRootPane().setDefaultButton(confirm);

		pack();
	}
}
