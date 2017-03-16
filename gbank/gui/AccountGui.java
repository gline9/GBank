package gbank.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.Timer;

import gbank.io.UserIO;
import gbank.statics.FileLocations;
import gbank.types.Account;
import gbank.types.User;
import gcore.tuples.Pair;
import gcore.units.FrequencyUnit;
import gcore.units.TimeUnit;

public class AccountGui extends JFrame {
	private static final long serialVersionUID = 1L;

	private final User user;

	private final String username;
	private final String password;

	private final HashMap<Integer, Pair<AccountPane, Account>> accountPanes = new HashMap<>();

	private final Timer updateTimer;

	public AccountGui(String username, String password) {

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

		// add the account components for each account the user has.
		for (Pair<Integer, Account> account : user.getAccounts()) {
			AccountPane pane = new AccountPane(account.getSecond(), account.getFirst());
			pane.setAlignmentX(Component.LEFT_ALIGNMENT);
			add(pane);
			accountPanes.put(account.getFirst(), new Pair<>(pane, account.getSecond()));
		}

		// add the account editing pane
		AccountEditingPane edit = new AccountEditingPane();
		edit.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(edit);

		// add a listener for new components in order to resize the window
		// appropriately.
		addContainerListener(new ContainerListener() {

			@Override
			public void componentAdded(ContainerEvent event) {
				validate();
				pack();
				System.out.println("added");
			}

			@Override
			public void componentRemoved(ContainerEvent event) {}
		});

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
		AccountPane pane = new AccountPane(a, id);
		pane.setAlignmentX(Component.LEFT_ALIGNMENT);

		// add the account pane to the main window
		int location = accountPanes.size();

		// add the account to the list of accounts
		accountPanes.put(id, new Pair<>(pane, a));
		
		add(pane, location);
		validate();
		setResizable(true);
		pack();
		setResizable(false);
	}

	public void removeAccount(AccountPane pane, int id) {

		// remove from the window
		this.remove(pane);
		validate();
		setResizable(true);
		pack();
		setResizable(false);

		// remove from the user
		user.removeAccount(id);
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

}
