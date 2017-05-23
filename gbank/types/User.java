package gbank.types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import gcore.tuples.Pair;

public class User {

	// stores the users accounts as a hashmap to its account id
	private HashMap<Integer, Account> accounts = new HashMap<>();

	private int nextID = 0;

	// set dirty when something changes from file to minimize file system
	// writes.
	private boolean dirty = false;
	private boolean finalized = false;

	/**
	 * creates a new user using default constructor
	 */
	public User() {}

	/**
	 * pushes the next id forward to this id to make sure accounts are created
	 * in increasing size
	 * 
	 * @param id
	 *            id to update to
	 * @since May 10, 2017
	 */
	private void updateNextID(int id) {
		nextID = id;
	}

	/**
	 * used to get the next account id that hasn't been used
	 * 
	 * @return next usable account id
	 * @since May 10, 2017
	 */
	private int getNextID() {

		// keep iterating until the id isn't taken
		while (idTaken(nextID)) {
			nextID++;
		}
		// once found return the new id
		return nextID;
	}

	/**
	 * checks to see if the id is taken
	 * 
	 * @param id
	 *            id to check
	 * @return if that id has been taken
	 * @since May 10, 2017
	 */
	private boolean idTaken(int id) {
		return accounts.containsKey(id);
	}

	/**
	 * adds an account and returns the id of the added account.
	 * 
	 * @return id of added account
	 */
	public int addAccount(Account account) {
		// set dirty
		setDirty();

		// get the id of the account
		int id = account.getAccountID();

		// if the account doesn't have an id assign it one
		if (!account.hasAccountIDBeenSet()) {
			// get the next account id for the account
			id = getNextID();

			// set the id in the account
			account.setAccountID(id);
		} else {
			// if it has been set update the next id appropriately
			updateNextID(id);
		}

		// add account with the current id
		accounts.put(id, account);

		// set the user as the owner of the account
		account.setOwner(this);

		// finalize the account if user is finalized
		if (finalized)
			account.finalize();

		// return the id of the account added and increment counter
		return id;
	}

	/**
	 * removes the account from the user
	 * 
	 * @param id
	 *            id of account to remove
	 * @return if the removal was successful
	 * @since Mar 14, 2017
	 */
	public boolean removeAccount(int id) {
		Account removed = accounts.remove(id);

		// if successful return true and set dirty otherwise return false
		if (removed != null)
			setDirty();

		return removed != null;
	}

	/**
	 * gets an account by the id of the account
	 * 
	 * @param id
	 *            id to look up
	 * @return account with the given id
	 * @since May 22, 2017
	 */
	public Account getAccount(int id) {
		return accounts.get(id);
	}

	/**
	 * used to get a list of accounts of the user
	 * 
	 * @return a sorted list of accounts of the user sorted by the id
	 */
	public List<Pair<Integer, Account>> getAccounts() {
		return accounts.keySet().stream().map(a -> new Pair<Integer, Account>(a, accounts.get(a)))
				.sorted((a, b) -> a.getFirst() - b.getFirst()).collect(Collectors.toList());
	}

	/**
	 * call this method to indicate that the user has fully been read from the
	 * drive.
	 */
	public void finalize() {
		// finalize all of the accounts
		Iterator<Integer> idIterator = accounts.keySet().iterator();

		while (idIterator.hasNext()) {
			accounts.get(idIterator.next()).finalize();
		}

		// finalize the user
		finalized = true;
	}

	/**
	 * Method to tell if the user data has been changed
	 * 
	 * @return whether the user's data has been changed.
	 * @since Mar 13, 2017
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * sets the user account to dirty only if the structure has been finalized
	 * 
	 * @since Mar 14, 2017
	 */
	public void setDirty() {
		if (finalized)
			dirty = true;
	}

	public String toString() {
		StringBuilder results = new StringBuilder();

		// grab the accounts of the user
		List<Pair<Integer, Account>> accounts = getAccounts();

		// iterate through the user's accounts
		for (Pair<Integer, Account> account : accounts) {

			// for each account add a line
			results.append(String.format("%05d:\t%s%n", account.getFirst(), account.getSecond()));
		}

		// trim off any leading or trailing white space and return
		return results.toString().substring(0, results.length() - 1);
	}

}
