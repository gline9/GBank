package gbank.io;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

import gbank.statics.FileLocations;
import gfiles.file.VirtualFile;
import gfiles.text.CSVFile;
import gfiles.text.CSVFileReader;
import gmath.types.BigInteger;

public final class LogIn {

	private static final CSVFileReader users = loadUsers();

	private static CSVFile userFile;

	// private so you can't instantiate it
	private LogIn() {}

	public static CSVFileReader loadUsers() {
		try {
			userFile = new CSVFile(VirtualFile.load(new File(FileLocations.userLoginFile)));

			return new CSVFileReader(userFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void saveUsers() {
		try {
			userFile.save(new File(FileLocations.userLoginFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean addNewLogin(String username, String password) {

		// check if the username is already taken
		if (users.getEntry("salt", "username", username) != null)
			return false;

		int bits = 256;

		// generate a random salt
		BigInteger lowerBound = new BigInteger(2).pow(bits - 1);

		BigInteger upperBound = new BigInteger(2).pow(bits);

		BigInteger rand = BigInteger.randomBigInteger(upperBound.subtract(lowerBound)).add(lowerBound);

		String salt = rand.toString(16);

		// generate the hash for the password given the salt
		byte[] binary = hashPassword(password.toCharArray(), DatatypeConverter.parseHexBinary(salt), 10, 256);

		String hexBinary = DatatypeConverter.printHexBinary(binary);

		// save the values to the csv file
		String entry = String.format("%s, %s, %s", username, salt, hexBinary);

		char[] entryArray = entry.toCharArray();
		userFile.writeChar('\r');
		userFile.writeChar('\n');
		for (char character : entryArray) {
			userFile.writeChar(character);
		}

		users.refreshData();

		return true;
	}

	public static boolean isValidLogin(String username, String password) {
		// check if the hashed password given matches the hash next to the
		// username
		// in csv file

		// grab the line of csv file corresponding to username if no username
		// found return false
		String salt = users.getEntry("salt", "username", username);

		if (salt == null)
			return false;

		String hashedPass = users.getEntry("password", "username", username);

		// hash the given password
		byte[] binary = hashPassword(password.toCharArray(), DatatypeConverter.parseHexBinary(salt), 10, 256);

		String hexBinary = DatatypeConverter.printHexBinary(binary);

		// if it matches the one on file return true otherwise false
		if (hexBinary.equals(hashedPass))
			return true;

		return false;
	}

	/**
	 * @author https://www.owasp.org/index.php/Hashing_Java
	 * 
	 *         hashing algorithm for the passwords stored in csv file
	 * 
	 * @param password
	 *            password in form of char array
	 * @param salt
	 *            salt for the random generation
	 * @param iterations
	 *            how many iterations to hash for
	 * @param keyLength
	 *            length of the key usually called with value of 256
	 * @return
	 * @since Jan 6, 2017
	 */
	public static byte[] hashPassword(final char[] password, final byte[] salt, final int iterations,
			final int keyLength) {

		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
			SecretKey key = skf.generateSecret(spec);
			byte[] res = key.getEncoded();
			return res;

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}
}
