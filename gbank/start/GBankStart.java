package gbank.start;

import gbank.gui.LogInGui;
import gbank.statics.StaticsLoader;

public class GBankStart {
	public static void main(String[] args) {
		StaticsLoader.loadConfig();
		new LogInGui();
	}
}