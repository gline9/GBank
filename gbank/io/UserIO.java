package gbank.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import gbank.types.Account;
import gbank.types.User;
import gencode.encrypt.file.EncryptedFileUtils;
import gfiles.file.VirtualFile;
import gfiles.text.xml.XMLFile;
import gfiles.text.xml.XMLFileReader;
import gfiles.text.xml.XMLTag;

public final class UserIO {

	// private so it can't be instantiated
	private UserIO() {}

	/**
	 * loads a user from a file using the given password to decrypt the file
	 * 
	 * @param location
	 *            location to load from
	 * @param password
	 *            password for decrypting the file
	 * @return user data stored in that file
	 * @throws IOException,
	 *             Throwable
	 */
	public static User loadFromFile(File location, String password) throws IOException, Throwable {
		// load the file at that location
		VirtualFile raw = VirtualFile.load(location);

		// decrypt the file
		VirtualFile decrypted = EncryptedFileUtils.decryptFile(raw, password);

		// load the decrypted file into an xml document
		XMLFile xmlFile = new XMLFile(decrypted);

		BufferedReader reader = new BufferedReader(new InputStreamReader(xmlFile.getInputStream()));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append("\n" + line);
		}
		System.out.println(out.toString()); // Prints the string content read
											// from input stream
		reader.close();

		// parse the xml file
		XMLFileReader parsed = new XMLFileReader(xmlFile);

		// grab the root tag from the parsed xml file
		XMLTag root = parsed.getRoot();

		// create a new user to return at the end
		User user = new User();

		// loop through the root for each of the user tags
		XMLTag current = null;
		int index = 0;
		while ((current = root.getNthOccurrence("Account", index++)) != null) {
			// load the account from the tag and save it to the user
			Account account = AccountIO.loadFromTag(current);

			user.addAccount(account);
		}

		// finalize the user
		user.finalize();

		// return the user with all of the accounts loaded in
		return user;
	}

	/**
	 * saves the given user to the location given and encrypts the data with the
	 * given password.
	 * 
	 * @param location
	 *            where to save the file
	 * @param password
	 *            encryption password
	 * @param user
	 *            user to save
	 * @throws Throwable
	 */
	public static void saveToFile(File location, String password, User user) throws IOException, Throwable {

		// create the tag that the file will be saved in
		XMLTag root = new XMLTag("User");

		// grab the account data from the user
		List<Account> accounts = user.getAccounts().stream().map(a -> a.getSecond()).collect(Collectors.toList());

		// iterate through the accounts and add each one to the root tag
		for (Account account : accounts) {

			// add the account as a tag to the root
			root.addElement(AccountIO.saveToTag(account));
		}

		// save the tag to a virtual file
		VirtualFile xmlFile = XMLFile.saveTagAsFile(root);

		// encrypt the file with the given password
		VirtualFile encrypted = EncryptedFileUtils.encryptFile(xmlFile, password);

		// save the file to the given location
		encrypted.save(location);
	}

}
