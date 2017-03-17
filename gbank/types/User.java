package gbank.types;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import gcore.tuples.Pair;

public class User {

	// stores the users accounts as a pair with its account id
	private ArrayList<Pair<Integer, Account>> accounts = new ArrayList<>();

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
	 * adds an account and returns the id of the added account.
	 * 
	 * @return id of added account
	 */
	public int addAccount(Account account) {
		// set dirty
		setDirty();

		// add account with the current id
		accounts.add(new Pair<>(nextID, account));
		
		// set the user as the owner of the account
		account.setOwner(this);

		// return the id of the account added and increment counter
		return nextID++;
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
		try {
			Pair<Integer, Account> results = accounts.stream().filter(a -> a.getFirst().equals(id)).findFirst().get();

			// remove the found element from the list of accounts
			boolean success = accounts.remove(results);

			// if successful return true and set dirty otherwise return false
			if (success)
				setDirty();

			return success;

		} catch (NoSuchElementException e) {
			// if nothing found return false
			return false;
		}
	}

	/**
	 * grabs the account at the specified id, if no account at id returns null
	 * 
	 * @param id
	 *            id of account to retrieve
	 * @return account at id or null if none
	 */
	public Account getAccount(int id) {
		try {
			// filters the accounts by accounts that match the id then returns
			// the first
			Pair<Integer, Account> results = accounts.stream().filter(a -> a.getFirst().equals(id)).findFirst().get();

			// if no error so far return the results
			return results.getSecond();

		} catch (NoSuchElementException e) {
			// if an error was thrown there is no account at that id so return
			// null
			return null;

		}
	}

	/**
	 * used to get a list of accounts of the user
	 * 
	 * @return a sorted list of accounts of the user sorted by the id
	 */
	public List<Pair<Integer, Account>> getAccounts() {
		return accounts.stream().sorted((a, b) -> a.getFirst().compareTo(b.getFirst())).collect(Collectors.toList());
	}

	/**
	 * call this method to indicate that the user has fully been read from the
	 * drive.
	 */
	public void finalize() {
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
