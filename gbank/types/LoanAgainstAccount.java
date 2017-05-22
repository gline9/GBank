package gbank.types;

/**
 * class for creating a loan against an account
 * 
 * @author Gavin
 *
 */
public class LoanAgainstAccount extends Account {

	private final int againstID;

	// stores the account this account is a loan against
	private Account against;

	// stores the most this loan can take against the account
	private double maximumLoan = Double.POSITIVE_INFINITY;

	public LoanAgainstAccount(double principal, double rate, double compoundRate, int againstID) {
		super(principal > 0 ? -principal : principal, rate, compoundRate);
		this.againstID = againstID;
	}

	public LoanAgainstAccount(double principal, double rate, double compoundRate, long lastCompoundTime,
			int againstID) {
		super(principal > 0 ? -principal : principal, rate, compoundRate, lastCompoundTime);
		this.againstID = againstID;
	}

	/**
	 * used to set the most money that can be owed against the other account
	 * 
	 * @param maximumOwed
	 *            maximum amount that can be owed, for instance 10,000 is more
	 *            than 1000. Must be a positive number.
	 * @since May 10, 2017
	 */
	public void setMaximumOwed(double maximumOwed) {
		if (maximumOwed > 0)
			maximumLoan = maximumOwed;
	}

	/**
	 * returns the maximum amount of money that can be owed against the account.
	 * 
	 * @return maximum amount that can be owed, Infinity if there is no maximum.
	 * @since May 10, 2017
	 */
	public double getMaximumOwed() {
		return maximumLoan;
	}

	/**
	 * 
	 * @return the account that this loan is against
	 * @since May 10, 2017
	 */
	public Account getAccountLoanIsAgainst() {
		return against;
	}

	public int getAgainstID() {
		return againstID;
	}

	@Override
	public double deposit(double amount) {
		// make sure the balance isn't zero and we are depositing
		if (getBalance() == 0)
			return 0;

		// otherwise deposit the amount into the account
		double deposited = super.deposit(amount);

		// deposit the same amount into the account this loan is against
		double finalDeposit = against.deposit(deposited);

		// if there is a difference withdraw that difference
		double difference = finalDeposit - deposited;
		if (difference != 0)
			super.withdraw(difference);

		// return the amount that was deposited
		return finalDeposit;
	}

	@Override
	public double withdraw(double amount) {
		// make sure the balance isn't below or at the maximum amount owed
		if (getBalance() < -maximumLoan)
			return 0;

		// if not withdraw the amount from the account this loan is against
		double withdrawn = against.withdraw(amount);

		// withdraw the same amount from this account
		super.withdraw(withdrawn);

		// if the account is below the maximum amount owed then bring it back
		double amountToMaximum = -maximumLoan - getBalance();

		if (amountToMaximum > 0) {
			super.deposit(amountToMaximum);
			against.deposit(amountToMaximum);
		}

		// return the amount that was withdrawn
		return withdrawn - Math.max(amountToMaximum, 0);

	}

	@Override
	protected void finalize() {
		super.finalize();
		against = getOwner().getAccount(againstID);
	}

}
