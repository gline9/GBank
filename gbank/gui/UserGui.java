package gbank.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.Timer;

import gbank.io.UserIO;
import gbank.statics.FileLocations;
import gbank.types.Account;
import gbank.types.User;
import gcore.tuples.Pair;
import gcore.units.FrequencyUnit;
import gcore.units.TimeUnit;

public class UserGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int accountHeight = 300;

	private final User user;

	private final String username;
	private final String password;

	private final HashMap<Integer, Pair<AccountPane, Account>> accountPanes = new HashMap<>();

	private final Timer updateTimer;

	private final ScrollPane accountPanel;

	public UserGui(String username, String password) {
		super(String.format("Welcome %s", username));

		// save the username and password fields
		this.username = username;
		this.password = password;

		// load the user file to the user field.
		File file = new File(FileLocations.getAccountInfoFile(username));

		User user = null;

		try {
			user = UserIO.loadFromFile(file, password);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}

		this.user = user;

		// set normal window conditions
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		// add the account components for each account the user has into a
		// separate JPanel
		accountPanel = new ScrollPane();

		accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
		for (Pair<Integer, Account> account : user.getAccounts()) {
			AccountPane pane = new AccountPane(user, account.getSecond(), account.getFirst());
			pane.setAlignmentX(Component.LEFT_ALIGNMENT);
			accountPanel.add(pane);
			accountPanes.put(account.getFirst(), new Pair<>(pane, account.getSecond()));
		}

		// create the scroll pane for the account panel
		JScrollPane scrollPane = new JScrollPane(accountPanel);

		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10),
				BorderFactory.createLoweredBevelBorder()));

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(18);

		// add the accounts to main window
		add(scrollPane);

		// add the buttons on the bottom
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		// create the button to add a new account
		JButton add = new JButton("Add Account");
		add.addActionListener((ActionEvent e) -> new CreateAccountGui(this));
		add.setFont(new Font(add.getFont().getFontName(), Font.PLAIN, 30));
		buttonPanel.add(add);

		// create the button to perform a transfer
		JButton transfer = new JButton("Transfer");
		transfer.addActionListener(e -> new TransferGui(this, this.user));
		transfer.setFont(new Font(transfer.getFont().getFontName(), Font.PLAIN, 30));
		buttonPanel.add(transfer);

		// create the log out button
		JButton logOut = new JButton("Log Out");
		logOut.addActionListener((ActionEvent event) -> {
			dispose();
			new LogInGui();
		});
		logOut.setFont(new Font(logOut.getFont().getFontName(), Font.PLAIN, 30));
		buttonPanel.add(logOut);

		// create the quit button
		JButton quit = new JButton("Quit");
		quit.addActionListener((ActionEvent event) -> dispose());
		quit.setFont(new Font(quit.getFont().getFontName(), Font.PLAIN, 30));
		buttonPanel.add(quit);

		// add the button panel
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(buttonPanel);

		// set the window to visible and pack its components
		pack();
		setVisible(true);
		setResizable(false);

		// set up an auto repainting routine
		int fps = 60;
		updateTimer = new Timer(
				(int) new FrequencyUnit(fps, FrequencyUnit.PER_SECOND).getDelay().convertTo(TimeUnit.MILLISECONDS),
				(ActionEvent e) -> repaint());
		updateTimer.start();
	}

	public void addAccount(Account a) {
		int id = user.addAccount(a);

		// create a new account pane
		AccountPane pane = new AccountPane(user, a, id);
		pane.setAlignmentX(Component.LEFT_ALIGNMENT);

		// add the account pane to the main window
		int location = accountPanes.size();

		// add the account to the list of accounts
		accountPanes.put(id, new Pair<>(pane, a));

		accountPanel.add(pane, location);
		guiEdited();
	}

	public void removeAccount(int id) {
		// grab the account pane from the account id
		AccountPane pane = accountPanes.get(id).getFirst();

		// remove from the window
		accountPanel.remove(pane);
		guiEdited();

		// remove from the user
		user.removeAccount(id);

		// remove from the list of account panes
		accountPanes.remove(id);
	}

	/**
	 * use this method if you change one of the components of the gui, this will
	 * re-scale as necessary
	 * 
	 * @since Mar 16, 2017
	 */
	public void guiEdited() {
		validate();
		setResizable(true);
		pack();
		setResizable(false);
	}

	@Override
	public void dispose() {
		// stop the repainting routine
		updateTimer.stop();

		try {
			// if the user file has been edited, re-save to the disk.
			if (user.isDirty()) {
				// save the user's account information file
				File file = new File(FileLocations.getAccountInfoFile(username));
				UserIO.saveToFile(file, password, user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		// call the super's dispose method
		super.dispose();
	}

	private class ScrollPane extends JPanel implements Scrollable {
		private static final long serialVersionUID = 1L;

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return new Dimension(AccountPane.Width, accountHeight);
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
			return 55;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return false;
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
			return 55;
		}
	}

}
