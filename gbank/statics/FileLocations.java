package gbank.statics;

import java.io.File;
import java.io.IOException;

public final class FileLocations {

	private static final String root = "I:/In Development/Java/GBank/Accounts/";

	private static final String userLoginName = "userLogins.csv";

	public static final String userLoginFile = getUserLoginFile();

	private FileLocations() {}

	private static String getUserLoginFile() {
		return root + userLoginName;
	}

	private static String getAccountFolder(String username) {
		return root + username + "/";
	}

	public static String getAccountInfoFile(String username) {
		return getAccountFolder(username) + "AccountInfo.xml";
	}

	public static void initAccountFiles(String username) throws IOException {

		String accountRoot = getAccountFolder(username);

		File accountRootDirectory = new File(accountRoot);
		accountRootDirectory.mkdir();

		String accountInfo = getAccountInfoFile(username);

		File accountInfoFile = new File(accountInfo);
		accountInfoFile.createNewFile();
	}
}
