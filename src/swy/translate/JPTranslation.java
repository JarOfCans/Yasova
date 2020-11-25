package swy.translate;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import drafterdat.settings.Setting;
import drafterdat.settings.SettingsFolder;
/**
 * class to hold settings for the program. It is not intended to be constructed.
 * @author JohnH
 */
public class JPTranslation {
	//Make sure to create way to default setting if it doesn't exist
	/**
	 * Setting url for the settings folder. Currently: CardHelperSettings
	 */
	private static final String SETTINGS_URL = "YasovaSettings/";
	/**
	 * Setting url for the settings folder. Currently: CardHelperSettings
	 */
	private static final String SETTINGS_FILE = SettingsFolder.programDataFolder()+"YasovaSettings/TranslateJP.txt";
	/**
	 * Array of all settings for the program.
	 */
	private ArrayList<Setting> settings = new ArrayList<Setting>(0);
	
	/**
	 * To be called at the start of the program. Creates a setting file and folder if there is none, else it loads the file.
	 */
	public JPTranslation()
	{
		File f = new File(SettingsFolder.programDataFolder()+SETTINGS_URL);
		if(!f.exists())
		{ 
			System.out.printf("%s doesn't exits%s", SETTINGS_URL, System.lineSeparator());
			System.out.println((SettingsFolder.prepFolder(SETTINGS_URL))? "Settings folder successfully created.":"Settings Folder Failed to be created.");
			File f1 = new File(SETTINGS_FILE);
			if (!f1.exists())
			{
				try {
					f1.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			File f1 = new File(SETTINGS_FILE);
			if (!f1.exists())
			{
				try {
					System.out.println(f1.getPath());
					f1.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.printf("Getting Translation File %s%s", SETTINGS_FILE, System.lineSeparator());
				getSettingFile();
			}
		}
		//printSettings();
	}
	/**
	 * Creates a setting for the Program.
	 * @param nameIn New name for the setting. Null and blank string are illegal arguments.
	 * @param valueIn New value for the setting.
	 * @throws InterruptedException 
	 */
	private void createSetting(String nameIn, String valueIn)
	{
		try {
			settings.add(new Setting(nameIn, valueIn));
		}
		catch (NullPointerException | IllegalArgumentException e)
		{
			System.out.println(e.getLocalizedMessage());
		}
	}
	/**
	 * Gets the setting name and value from an appropriate Setting string.
	 * @param input A setting string with one '=' and a name with at least one character.
	 * @return String array with the name and the value.
	 */
	private String[] getSettingValues(String input)
	{
		return new String[] {getValueHelper(input) ,getNameHelper(input)};
	}
	/**
	 * Retrieves the value of the string in setting format: Name=Value. String must contain one equal sign.
	 * @param input String String in the setting format.
	 * @return
	 */
	private String getValueHelper(String input)
	{
		if (input == null || input.equals(""))
		{
			return "";
		}
		else if (input.charAt(0) == '=') {
			return input.substring(1);
		}
		else {
			return getValueHelper(input.substring(1));
		}
	}
	private String getNameHelper(String input)
	{
		if (input == null || input.equals(""))
		{
			return "";
		}
		else if (input.charAt(input.length() - 1) == '=') {
			return input.substring(0,input.length()-1);
		}
		else {
			return getNameHelper(input.substring(0,input.length()-1));
		}
	}
	public void getSettingFile()
	{
		String next;
		try (BufferedReader br = new BufferedReader(new FileReader(SETTINGS_FILE))){
			while ((next = br.readLine()) != null) {
				//System.out.println(next);
				String newName = getNameHelper(next);
				String newValue = getValueHelper(next);
				createSetting(newName, newValue);
			}
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	/**
	 * Returns the value of the appropriate setting name, and will create a setting with the default
	 * value if it does not exist.
	 * @param nameIn Name of the setting to retrieve the value of.
	 * @param defaultValue The default value of the setting.
	 * @return Value of the setting, or the default value if it did not exist prehand.
	 */
	public String settingValue(String nameIn)
	{
		String output = null;
		for (Setting hoi: settings)
		{
			if (hoi.getName().equals(nameIn)) {
				output = hoi.getValue();
			}
		}
		if (output == null)
		{
			output = nameIn;
		}
		return output;
	}
	public boolean checkValid(String valueIn)
	{
		if (valueIn != null && valueIn.length() > 1)
		{
			int check = 0;
			boolean firstChar = valueIn.charAt(0) != '=';
			for (int i = 0; i < valueIn.length(); i++)
			{
				if (valueIn.charAt(i) == '=')
				{
					check++;
				}
			}
			return (check == 1) && firstChar;
		}
		else
		{
			return false;
		}
	}
	
	public void printSettings() {
		System.out.println("Printing Translations");
		for (Setting setting: settings) {
			System.out.printf("%s=%s%s", setting.getName(), setting.getValue(), System.lineSeparator());
		}
	}
	
	public String translateAll(String input) {
		String output = input;
		//System.out.println(input);
		for (Setting translation: settings) {
			//System.out.println(translation.getName() +" , " + translation.getValue());
			output = //output.replaceAll(translation.getName(), translation.getValue());
			output.replaceAll("(?i)"+Pattern.quote(translation.getName()), translation.getValue());
		}
		return output;
	}
}
