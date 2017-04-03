package gbank.statics;

import java.io.File;
import java.io.IOException;

import gfiles.file.VirtualFile;
import gfiles.text.ConfigFile;
import gfiles.text.ConfigRegistry;

public class StaticsLoader extends ConfigRegistry{

	public static void loadConfig(){
		StaticsLoader loader = new StaticsLoader();
		try{
			ConfigFile cfg = new ConfigFile(VirtualFile.load(new File("config.cfg")));
			cfg.loadConfig(loader);
			
		}catch(IOException e){
			System.err.println("Config not found loading defaults");
			loader.loadDefaults();
		}
	}
	
	@Override
	protected void initConfigMap() {
		addConfigElement("Data Root", s -> FileLocations.setDataRoot(s));
	}
	
	private void loadDefaults(){
		applyConfigOption("Data Root", "Accounts");
	}

}
