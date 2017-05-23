package gbank.types;

public class DefaultAccountBuilder extends AccountBuilder {

	protected String name = "";

	protected double principal = 0;

	protected double rate = 0;

	protected double compoundRate = 1;

	protected long lastCompoundTime = -1;

	protected int accountID = -1;

	@Override
	public Account build() {
		Account results;

		// apply the appropriate constructor for the values provided
		if (lastCompoundTime < 0) {
			results = new Account(principal, rate, compoundRate);
		} else {
			results = new Account(principal, rate, compoundRate, lastCompoundTime);
		}

		// set the account id
		results.setAccountID(accountID);

		// set the name of the account
		results.setName(name);

		return results;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setCompoundRate(double compoundRate) {
		this.compoundRate = compoundRate;
	}

	public void setLastCompoundTime(long lastCompoundTime) {
		this.lastCompoundTime = lastCompoundTime;
	}

	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

}
