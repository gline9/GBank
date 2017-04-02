package gbank.statics;

import java.io.File;
import java.io.IOException;

public final class FileLocations {

	private static final String root = "";
	
	private static final String accountRoot = "Accounts/";

	private static final String userLoginName = "userLogins.csv";
	public static final String userLoginFile = getUserLoginFile();
	
	public static final String resourceRoot = "resources/";
	
	public static final String favicon = getImageLocation("icon.png");

	private FileLocations() {}

	private static String getUserLoginFile() {
		return root + accountRoot + userLoginName;
	}

	private static String getAccountFolder(String username) {
		return root + accountRoot + username + "/";
	}

	public static String getAccountInfoFile(String username) {
		return getAccountFolder(username) + "AccountInfo.xml";
	}
	
	public static String getImageLocation(String image){
		return root + resourceRoot + image;
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
