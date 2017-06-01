package gbank.statics;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gfiles.file.VirtualFile;
import gfiles.text.ConfigFile;
import gfiles.text.ConfigRegistry;

public class WindowStaticsLoader extends ConfigRegistry {

	private static Pattern configPattern = Pattern.compile("^(.*) ([XY]) Location$");

	public static void loadConfig() {
		WindowStaticsLoader loader = new WindowStaticsLoader();
		try {
			ConfigFile cfg = new ConfigFile(VirtualFile.load(new File(FileLocations.getWindowStaticsConfig())));
			cfg.loadConfig(loader);

		} catch (IOException e) {
			System.err.println("Window save config not found loading defaults");
		}

		// create hook to save config when program quits
		Runtime.getRuntime().addShutdownHook(new Thread(WindowStaticsLoader::saveConfig));
	}

	public static void saveConfig() {
		ConfigFile cfg = new ConfigFile();
		PrintStream print = new PrintStream(cfg.getOutputStream());
		
		// grab the iterator for the window statics and iterate through the ids
		Iterator<String> ids = WindowStatics.getIDIterator();
		Iterable<String> idsi = () -> ids;
		for (String id : idsi){
			// grab the x and y locations for the windows
			double x = WindowStatics.getWindowXPercent(id);
			double y = WindowStatics.getWindowYPercent(id);
			
			// save the config options
			print.printf("%s X Location = %f%n", id, x);
			print.printf("%s Y Location = %f%n", id, y);
		}

		try {
			cfg.save(new File(FileLocations.getWindowStaticsConfig()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean applyConfigOption(String variable, String value) {
		Matcher matcher = configPattern.matcher(variable);

		// check if the config line matches the pattern
		if (!matcher.matches()) {
			return false;
		}
		
		// grab the information for the data
		String type = matcher.group(2);
		String name = matcher.group(1);
		
		// save the information
		try{
			switch(type){
			case "X":
				WindowStatics.setWindowXPercent(Double.valueOf(value), name);
				break;
			case "Y":
				WindowStatics.setWindowYPercent(Double.valueOf(value), name);
				break;
			default:
				return false;
			}
		}catch(NumberFormatException e){
			// if a number is entered incorrectly return false
			return false;
		}

		return true;
	}

	@Override
	protected void initConfigMap() {}

}
