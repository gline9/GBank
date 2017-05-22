package gbank.io.savehandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import gbank.types.Account;
import gbank.types.AccountBuilder;
import gcore.tuples.Pair;
import gfiles.text.xml.XMLElement;
import gfiles.text.xml.XMLInfo;
import gfiles.text.xml.XMLTag;

public abstract class AccountSaveHandler {

	private final Supplier<AccountBuilder> createBuilder;

	private HashMap<String, Pair<BiConsumer<AccountBuilder, String>, Function<Account, String>>> handlerRegistry;

	/**
	 * needs to accept a supplier for an account builder so it can create
	 * accounts
	 * 
	 * @param createBuilder
	 *            gives account builders so accounts can be created
	 */
	public AccountSaveHandler(Supplier<AccountBuilder> createBuilder) {
		this.createBuilder = createBuilder;
	}

	/**
	 * Adds a handler for saving the account to a tag or loading it from one
	 * 
	 * @param valueName
	 *            name of the value this handler is for
	 * @param loadHandler
	 *            takes in the value from the tag and the account builder and
	 *            creates a new account
	 * @param saveHandler
	 *            takes in the account and saves the data into a string to load
	 *            from
	 * @since May 22, 2017
	 */
	public final void addHandler(String valueName, BiConsumer<AccountBuilder, String> loadHandler,
			Function<Account, String> saveHandler) {
		handlerRegistry.put(valueName, new Pair<>(loadHandler, saveHandler));
	}

	/**
	 * loads the account from the tag and saves it in an Account object. Ignores
	 * any tags that don't have a handler
	 * 
	 * @param tag
	 *            tag to load the data from
	 * @return account that holds the information from the tag
	 * @since May 22, 2017
	 */
	public final Account loadFromTag(XMLTag tag) {

		// grab the account builder
		AccountBuilder builder = createBuilder.get();

		// grab the elements from the tag and iterate through them
		ArrayList<XMLElement> elements = tag.getElements();

		Iterator<XMLElement> elementIterator = elements.iterator();
		while (elementIterator.hasNext()) {

			// grab the next element from the tag
			XMLElement nextElement = elementIterator.next();

			// if it is just text go to the next element
			if (!(nextElement instanceof XMLTag))
				continue;

			// cast to a tag and grab any info in it
			XMLElement infoElement = ((XMLTag) nextElement).getElements().get(0);

			// grab the name of the tag
			String name = ((XMLTag) nextElement).getName();

			// if the info isn't of form info skip this tag
			if (!(infoElement instanceof XMLInfo))
				continue;

			// get the info from the tag
			String info = ((XMLInfo) infoElement).getInfo();

			// get the handler for the name of this tag
			Pair<BiConsumer<AccountBuilder, String>, Function<Account, String>> handler = handlerRegistry.get(name);

			// if the handler is null go to the next, this is if there isn't a
			// handler for the tag
			if (handler == null)
				continue;

			// otherwise handle the information gathered
			handler.getFirst().accept(builder, info);

			// move on to the next tag

		}

		// return the built account
		return builder.build();
	}

	/**
	 * saves the account to a tag.
	 * 
	 * @param account
	 *            account to save to the tag
	 * @return resulting tag
	 * @since May 22, 2017
	 */
	public final XMLTag saveToTag(Account account) {

		// create a new tag to save the account to
		XMLTag accountTag = new XMLTag("Account");

		// go through all of the handlers and add the information for each one
		Iterator<String> handlers = handlerRegistry.keySet().iterator();

		while (handlers.hasNext()) {
			String value = handlers.next();

			// grab the next handler
			Pair<BiConsumer<AccountBuilder, String>, Function<Account, String>> handler = handlerRegistry.get(value);

			// generate the info string from the handler
			String info = handler.getSecond().apply(account);

			// if info is null go on to the next handler
			if (info == null)
				continue;

			// create the tag
			XMLTag tag = new XMLTag(value);
			tag.addElement(new XMLInfo(info));

			// add the tag to the root tag
			accountTag.addElement(tag);
		}

		// return the tag
		return accountTag;
	}
}
