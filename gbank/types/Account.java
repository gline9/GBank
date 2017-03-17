package gbank.types;

import gcore.units.TimeUnit;

public class Account {

	private String name = "";

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
	 * smallest amount of money the account can have in it.
	 */
	private double minimumBalance = 0;

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
	 * this method will deposit additional funds to the account
	 * 
	 * @param amount
	 *            amount to deposit
	 * @since Aug 16, 2016
	 */
	public void deposit(double amount) {
		// capitalize the account so the principal is up to date
		capitalize();

		// add the amount to the principal
		principal += amount;
	}

	/**
	 * this method will withdraw the amount asked from the account, it will also
	 * return the actual amount withdrawn in case of an over draw
	 * 
	 * @param amount
	 *            amount to withdraw
	 * @return amount actually withdrawn
	 * @since Aug 16, 2016
	 */
	public double withdraw(double amount) {
		// capitalize the account so the principal is up to date
		capitalize();

		// subtract the amount to withdraw
		principal -= amount;

		// check if there was an overdraw
		if (principal < minimumBalance) {

			// if so change the amount withdrawn
			amount -= minimumBalance - principal;

			// also set the principal to the minimum balance
			principal = minimumBalance;

		}

		// return how much was actually withdrawn
		return amount;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return String.format("$%,3.2f", getBalance());
	}
}
