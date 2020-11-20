package drafterdat.settings;

import java.io.File;

public class SettingsFolder {
	
	/**
	 * Attempts to prep folder, returns value based on if it was created. A blank or null String will not be attempted.
	 * @param folderDir Folder directory based on main folder for new folder.
	 * @return true if the folder is created and is a valid string, false otherwise.
	 */
	public static boolean prepFolder(String folderDir)
	{
		if (folderDir == null || folderDir.equals("")) {
			return false;
		}
		if (folderDir.substring(folderDir.length() - 1).equals("\\"))
		{
			//System.out.println("Found a \\");
			folderDir = folderDir.substring(0, folderDir.length() - 1);
		}
		File f;
		if (folderDir.equals(programDataFolder())) {
			f = new File(folderDir);
		}
		else {
			if (folderDir.length()>=programDataFolder().length() && folderDir.substring(0, programDataFolder().length()).equals(programDataFolder())) {
				folderDir = folderDir.substring(programDataFolder().length());
			}
			f = new File(String.format("%s%s", programDataFolder(), folderDir));
		}
		//System.out.println(f.getPath());
		return f.mkdirs();
	}
	
	/**
	 * Retrieves the program data folder for this Program.
	 * @return String in the "{This programs data folder}/" format.
	 */
	public static String programDataFolder()
	{
		return String.format("%s\\AppData\\Local\\Yasova\\", System.getProperty("user.home"));
	}
}
