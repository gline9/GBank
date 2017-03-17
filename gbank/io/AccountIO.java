package gbank.io;

import gbank.types.Account;
import gfiles.text.xml.XMLInfo;
import gfiles.text.xml.XMLTag;

public final class AccountIO {
	private AccountIO() {}

	public static Account loadFromTag(XMLTag tag) {
		// grab the tags from the account root tag
		XMLTag principalTag = tag.getFirstTag("Principal");
		XMLTag rateTag = tag.getFirstTag("Rate");
		XMLTag compoundRateTag = tag.getFirstTag("Compound_Rate");
		XMLTag lastCompoundTimeTag = tag.getFirstTag("Last_Compound_Time");
		XMLTag nameTag = tag.getFirstTag("Name");

		// grab the values from the tags
		double principal = Double.valueOf(((XMLInfo) principalTag.getElements().get(0)).getInfo());
		double rate = Double.valueOf(((XMLInfo) rateTag.getElements().get(0)).getInfo());
		double compoundRate = Double.valueOf(((XMLInfo) compoundRateTag.getElements().get(0)).getInfo());
		long lastCompoundTime = Long.valueOf(((XMLInfo) lastCompoundTimeTag.getElements().get(0)).getInfo());
		String name = "";
		if (nameTag != null) {
			name = ((XMLInfo) nameTag.getElements().get(0)).getInfo();
		}

		// return the account with the given values
		Account account = new Account(principal, rate, compoundRate, lastCompoundTime);
		account.setName(name);
		return account;
	}

	public static XMLTag saveToTag(Account account) {
		// grab the values to save to the tag
		double principal = account.getBalance();
		double rate = account.getRate();
		double compoundRate = account.getCompoundRate();
		long lastCompoundTime = account.getLastCompoundTime();
		String name = account.getName();

		// create a new empty tag with title Account
		XMLTag accountTag = new XMLTag("Account");

		// add the values to the tag
		XMLTag principalTag = new XMLTag("Principal");
		XMLTag rateTag = new XMLTag("Rate");
		XMLTag compoundRateTag = new XMLTag("Compound_Rate");
		XMLTag lastCompoundTimeTag = new XMLTag("Last_Compound_Time");

		principalTag.addElement(new XMLInfo(String.valueOf(principal)));
		rateTag.addElement(new XMLInfo(String.valueOf(rate)));
		compoundRateTag.addElement(new XMLInfo(String.valueOf(compoundRate)));
		lastCompoundTimeTag.addElement(new XMLInfo(String.valueOf(lastCompoundTime)));
		
		if (!name.equals("")){
			XMLTag nameTag = new XMLTag("Name");
			nameTag.addElement(new XMLInfo(name));
			accountTag.addElement(nameTag);
		}

		accountTag.addElement(principalTag);
		accountTag.addElement(rateTag);
		accountTag.addElement(compoundRateTag);
		accountTag.addElement(lastCompoundTimeTag);

		// return the account tag
		return accountTag;
	}
}
