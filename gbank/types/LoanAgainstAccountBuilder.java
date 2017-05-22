package gbank.types;

public class LoanAgainstAccountBuilder extends DefaultAccountBuilder {

	private double maximumOwed = Double.POSITIVE_INFINITY;

	private int againstID = -1;

	public void setMaximumOwed(double maximumOwed) {
		this.maximumOwed = maximumOwed;
	}

	public void setAgainstID(int againstID) {
		this.againstID = againstID;
	}

	@Override
	public Account build() {
		LoanAgainstAccount results;
		// make sure the againstID was set
		if (againstID < 0)
			throw new IllegalArgumentException("Against ID must be set for a loan against account builder");

		// apply the appropriate constructor for the values provided
		if (lastCompoundTime < 0) {
			results = new LoanAgainstAccount(principal, rate, compoundRate, againstID);
		} else {
			results = new LoanAgainstAccount(principal, rate, compoundRate, lastCompoundTime, againstID);
		}

		// set the account id
		results.setAccountID(accountID);

		// set the name of the account
		results.setName(name);

		return results;
	}
}
