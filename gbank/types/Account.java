package gbank.types;

import gcore.units.TimeUnit;

public class Account {

	private String name = "";

	private User owner = null;

	/**
	 * stores the balance before capitalization
	 */
	private double principal;

	/**
	 * stores the interest rate of the account
	 */
	private double rate;

	/**
	 * stores how many times a year the account is compounded
	 */
	private double compoundRate;

	/**
	 * stores the last time the account was successfully compounded
	 */
	private long lastCompoundTime;

	/**
	 * stores the account id for this account
	 */
	private int accountID = -1;

	/**
	 * initializes a new account with the given principal rate and compound rate
	 * 
	 * @param principal
	 *            amount for the account to start with
	 * @param rate
	 *            rate of interest for the account
	 * @param compoundRate
	 *            how many times a year the amount is compounded
	 */
	public Account(double principal, double rate, double compoundRate) {

		// set the principle, rate and compound rate
		this.principal = principal;
		this.rate = rate;
		this.compoundRate = compoundRate;

		// set the last compound time to the current time
		lastCompoundTime = System.currentTimeMillis();
	}

	public Account(double principal, double rate, double compoundRate, long lastCompoundTime) {
		// set all of the fields for the class
		this.principal = principal;
		this.rate = rate;
		this.compoundRate = compoundRate;
		this.lastCompoundTime = lastCompoundTime;
	}

	/**
	 * gets the interest rate for the account
	 * 
	 * @return interest rate
	 * @since Aug 16, 2016
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * changes the interest rate for the account
	 * 
	 * @param rate
	 *            new interest rate for the account
	 * @since Aug 16, 2016
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * returns how many times a year the account is compounded
	 * 
	 * @return compound times per year
	 * @since Aug 16, 2016
	 */
	public double getCompoundRate() {
		return compoundRate;
	}

	/**
	 * capitalizes the account by applying the interest rate and updating the
	 * principle
	 * 
	 * @since Aug 16, 2016
	 */
	private void capitalize() {

		// determine the difference in time for the last time the account was
		// compounded the difference will be in milliseconds
		long currentTime = System.currentTimeMillis();
		long difference = currentTime - lastCompoundTime;

		// convert the difference to a time unit for easier handling
		TimeUnit timeDifference = TimeUnit.valueOf(difference, TimeUnit.MILLISECONDS);

		// compute the new principle by the principle equation
		principal = principal
				* Math.pow(1 + rate / compoundRate, compoundRate * timeDifference.convertTo(TimeUnit.YEARS));

		// set the previous compound time to the current time
		lastCompoundTime = currentTime;

	}

	/**
	 * used to get the current balance of the account
	 * 
	 * @return the balance of the account
	 * @since Aug 16, 2016
	 */
	public double getBalance() {
		// capitalize the account
		capitalize();

		// return the new principle for the account
		return principal;
	}

	/**
	 * used to get the last time the account was compounded.
	 * 
	 * @return last compound time of the account
	 * @since Sep 14, 2016
	 */
	public long getLastCompoundTime() {
		return lastCompoundTime;
	}

	/**
	 * used to set the account id for the account
	 * 
	 * @param accountID
	 *            id for the account must be positive otherwise nothing happens
	 * @since May 10, 2017
	 */
	public void setAccountID(int accountID) {
		if (accountID >= 0)
			this.accountID = accountID;
	}

	/**
	 * used to get the account id for the account
	 * 
	 * @return account id for the account, -1 if it hasn't been set
	 * @since May 10, 2017
	 */
	public int getAccountID() {
		return accountID;
	}

	/**
	 * checks if the account ID has been set
	 * 
	 * @return boolean for if account ID has been set
	 * @since May 10, 2017
	 */
	public boolean hasAccountIDBeenSet() {
		return accountID > -1;
	}

	/**
	 * this method will deposit additional funds to the account
	 * 
	 * @return amount actually deposited, different than amount only in the case
	 *         of loans.
	 * @param amount
	 *            amount to deposit must be positive
	 * @since Aug 16, 2016
	 */
	public double deposit(double amount) {
		// if amount is non-positive return 0
		if (amount <= 0)
			return 0;

		setDirty();
		// capitalize the account so the principal is up to date
		capitalize();

		// check if there was more deposited than needed
		if (principal < 0 && Math.abs(principal) < amount) {

			// if there was more taken out than needed take out the maximum
			// amount that we can
			double deposited = -principal;

			principal = 0;

			return deposited;

		}

		// otherwise deposit the actual amount
		principal += amount;

		// return how much was actually deposited
		return amount;
	}

	/**
	 * this method will withdraw the amount asked from the account, it will also
	 * return the actual amount withdrawn in case of an over draw
	 * 
	 * @param amount
	 *            amount to withdraw must be positive
	 * @return amount actually withdrawn
	 * @since Aug 16, 2016
	 */
	public double withdraw(double amount) {
		// if amount is non-positive return 0
		if (amount <= 0)
			return 0;

		setDirty();
		// capitalize the account so the principal is up to date
		capitalize();

		// check if there was an overdraw
		if (principal > 0 && principal < amount) {

			// if there was an overdraw take out the maximum amount that we can
			double withdrawn = principal;

			principal = 0;

			return withdrawn;

		}

		// otherwise withdraw the actual amount
		principal -= amount;

		// return how much was actually withdrawn
		return amount;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		setDirty();
		this.name = name;
	}

	protected void setOwner(User user) {
		this.owner = user;
	}

	public void setDirty() {
		if (owner != null)
			owner.setDirty();
	}

	public String toString() {
		// check if the balance is negative, if so add the appropriate
		// parenthesis.
		String before = "";
		String after = "";
		double balance = getBalance();
		if (balance < 0) {
			before = "(";
			after = ")";
		}
		return String.format(before + "$%,3.2f" + after, Math.abs(balance));
	}
}
