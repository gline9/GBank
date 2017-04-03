package gbank.statics;

import java.io.File;
import java.io.IOException;

public final class FileLocations {

	private static String dataRoot = "";
	
	private static String accountRoot = "Accounts/";

	private static String userLoginName = "userLogins.csv";
	
	private static String resourceRoot = "resources/";

	private FileLocations() {}

	public static String getUserLoginFile() {
		return dataRoot + accountRoot + userLoginName;
	}

	private static String getAccountFolder(String username) {
		return dataRoot + accountRoot + username + "/";
	}

	public static String getAccountInfoFile(String username) {
		return getAccountFolder(username) + "AccountInfo.xml";
	}
	
	public static String getImageLocation(String image){
		return resourceRoot + image;
	}
	
	public static String getFaviconLocation(){
		return getImageLocation("icon.png");
	}
	
	public static void setDataRoot(String root){
		FileLocations.dataRoot = root;
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
