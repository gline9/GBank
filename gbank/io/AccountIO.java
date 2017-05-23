package gbank.io;

import java.util.HashMap;

import gbank.io.savehandlers.AccountSaveHandler;
import gbank.io.savehandlers.DefaultAccountSaveHandler;
import gbank.io.savehandlers.LoanAgainstAccountSaveHandler;
import gbank.types.Account;
import gbank.types.LoanAgainstAccount;
import gfiles.text.xml.XMLTag;

public final class AccountIO {
	private AccountIO() {}

	private static final HashMap<Class<? extends Account>, AccountSaveHandler> handlerRegistry = initHandlerRegistry();

	private static final HashMap<Integer, Class<? extends Account>> typeToAccount = initTypeToAccount();

	private static final HashMap<Class<? extends Account>, Integer> accountToType = initAccountToType();

	private static final AccountSaveHandler defaultHandler = new DefaultAccountSaveHandler();

	private static HashMap<Class<? extends Account>, AccountSaveHandler> initHandlerRegistry() {
		HashMap<Class<? extends Account>, AccountSaveHandler> registry = new HashMap<>();

		// add all of the handlers for the program
		registry.put(Account.class, new DefaultAccountSaveHandler());
		registry.put(LoanAgainstAccount.class, new LoanAgainstAccountSaveHandler());

		return registry;
	}

	private static HashMap<Integer, Class<? extends Account>> initTypeToAccount() {
		HashMap<Integer, Class<? extends Account>> results = new HashMap<>();

		results.put(0, Account.class);
		results.put(1, LoanAgainstAccount.class);

		return results;
	}

	private static HashMap<Class<? extends Account>, Integer> initAccountToType() {
		HashMap<Class<? extends Account>, Integer> results = new HashMap<>();

		results.put(Account.class, 0);
		results.put(LoanAgainstAccount.class, 0);

		return results;
	}

	public static Account loadFromTag(XMLTag tag) {

		// grab the type of account to load
		String accountTypeString = tag.getAttributesValue("TYPE");

		// get the handler for the type of account
		AccountSaveHandler handler;

		int accountType;

		if (accountTypeString == null || !typeToAccount.containsKey(accountType = Integer.valueOf(accountTypeString))) {
			handler = defaultHandler;
		} else {
			handler = handlerRegistry.get(typeToAccount.get(accountType));
		}

		// create the account using the handler
		Account results = handler.loadFromTag(tag);

		return results;
	}

	public static XMLTag saveToTag(Account account) {

		// grab the handler to use for saving the data
		AccountSaveHandler handler = handlerRegistry.get(account.getClass());

		// save the account to a tag
		XMLTag accountTag = handler.saveToTag(account);

		// set the account type in the tag
		accountTag.addAttribute("TYPE", String.valueOf(accountToType.get(account.getClass())));

		return accountTag;
	}
}
