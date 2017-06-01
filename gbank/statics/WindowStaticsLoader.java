package gbank.statics;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import gfiles.file.VirtualFile;
import gfiles.text.ConfigFile;
import gfiles.text.ConfigRegistry;

public class WindowStaticsLoader extends ConfigRegistry {

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

		print.printf("Main Window X Location = %f%n", WindowStatics.getMainWindowXPercent());

		print.printf("Main Window Y Location = %f%n", WindowStatics.getMainWindowYPercent());

		try {
			cfg.save(new File(FileLocations.getWindowStaticsConfig()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initConfigMap() {
		addConfigElement("Main Window X Location", s -> {
			if (!s.equals(""))
				WindowStatics.setMainWindowXPercent(Double.valueOf(s));
		});

		addConfigElement("Main Window Y Location", s -> {
			if (!s.equals(""))
				WindowStatics.setMainWindowYPercent(Double.valueOf(s));
		});
	}

}
