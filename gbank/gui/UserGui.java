package gbank.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.Timer;

import gbank.io.UserIO;
import gbank.statics.FileLocations;
import gbank.statics.ImageStatics;
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

	private final ScrollPanel accountPanel;

	public UserGui(String username, String password) {
		super(String.format("Welcome %s", username));
		setIconImage(ImageStatics.getFavicon());

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

		// add the menu options
		JMenuBar menu = new JMenuBar();

		MouseListener menuListener = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent event) {
				JMenu menu = (JMenu) event.getSource();
				if (!menu.isPopupMenuVisible())
					menu.setSelected(true);
			}

			@Override
			public void mouseExited(MouseEvent event) {
				JMenu menu = (JMenu) event.getSource();
				if (!menu.isPopupMenuVisible())
					menu.setSelected(false);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}

		};

		// create the file menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setFont(new Font(fileMenu.getFont().getFontName(), Font.PLAIN, 30));
		fileMenu.addMouseListener(menuListener);

		// create the log out button
		JMenuItem logOut = new JMenuItem("Log Out");
		logOut.addActionListener((ActionEvent event) -> {
			dispose();
			new LogInGui();
		});
		logOut.setFont(new Font(logOut.getFont().getFontName(), Font.PLAIN, 30));
		fileMenu.add(logOut);

		// create the quit button
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener((ActionEvent event) -> dispose());
		quit.setFont(new Font(quit.getFont().getFontName(), Font.PLAIN, 30));
		
		// set shortcut to ctrl + q
		quit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(quit);

		JMenu editMenu = new JMenu("Edit");
		editMenu.setFont(new Font(editMenu.getFont().getFontName(), Font.PLAIN, 30));
		editMenu.addMouseListener(menuListener);

		// create the button to add a new account
		JMenuItem add = new JMenuItem("Add Account");
		add.addActionListener((ActionEvent e) -> new CreateAccountGui(this));
		add.setFont(new Font(add.getFont().getFontName(), Font.PLAIN, 30));
		
		// set shortcut to ctrl + a
		add.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		editMenu.add(add);

		// create the button to perform a transfer
		JMenuItem transfer = new JMenuItem("Transfer");
		transfer.addActionListener(e -> new TransferGui(this, this.user));
		transfer.setFont(new Font(transfer.getFont().getFontName(), Font.PLAIN, 30));
		editMenu.add(transfer);

		// add the menus to the menu bar
		menu.add(fileMenu);
		menu.add(editMenu);

		// add the menu bar
		setJMenuBar(menu);

		// add the account components for each account the user has into a
		// separate JPanel
		accountPanel = new ScrollPanel();

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

		// set the window to visible and pack its components
		setResizable(false);
		setVisible(true);
		guiEdited();

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

	private class ScrollPanel extends JPanel implements Scrollable {
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
