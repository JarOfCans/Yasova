package swy.yoink;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import drafterdat.settings.Settings;
import drafterdat.settings.SettingsFolder;
import swy.core.RaceTime;

public class Yasova {
	WebDriver driver;
	private int WAIT_TIME;
	private int WEBSITE_BREAK;
	private int FREEZE_CHECKS;
	private int slowDownIncrease;
	private int slowDownMaximum;
	private int slowDownDecrease;
	private int modifiedBreak = 0;
	private boolean appendToTA = false;
	private int initCourse = 0;
	private int initChar1 = 0;
	private int initChar2 = 0;
	public static final String[] NAME = new String[] {"tu_name_0", "tu_name_1", "tu_name_2", "tu_name_3", "tu_name_4", "tu_name_5", "tu_name_6", "tu_name_7", "tu_name_8", "tu_name_9"};
	public static final String[] TIME_TOTAL = new String[] {"tu_total_0", "tu_total_1", "tu_total_2", "tu_total_3", "tu_total_4", "tu_total_5", "tu_total_6", "tu_total_7", "tu_total_8", "tu_total_9"};
	public static final String[] TIME1 = new String[] {"tu_time1_0", "tu_time1_1", "tu_time1_2", "tu_time1_3", "tu_time1_4", "tu_time1_5", "tu_time1_6", "tu_time1_7", "tu_time1_8", "tu_time1_9"};
	public static final String[] TIME2 = new String[] {"tu_time2_0", "tu_time2_1", "tu_time2_2", "tu_time2_3", "tu_time2_4", "tu_time2_5", "tu_time2_6", "tu_time2_7", "tu_time2_8", "tu_time2_9"};
	public static final String[] TIME3 = new String[] {"tu_time3_0", "tu_time3_1", "tu_time3_2", "tu_time3_3", "tu_time3_4", "tu_time3_5", "tu_time3_6", "tu_time3_7", "tu_time3_8", "tu_time3_9"};
	public static final String[] CHARAS = new String[] {"tu_chara_0", "tu_chara_1", "tu_chara_2", "tu_chara_3", "tu_chara_4", "tu_chara_5", "tu_chara_6", "tu_chara_7", "tu_chara_8", "tu_chara_9"};
	public static final String[] QUICKCHAR = new String[] {"Any", "Reimu", "Marisa", "Sakuya", "Remilia", "Sanae", "Suwako", "Koishi", "Kokoro", "Youmu", "Udonge", "Nue", "Futo", "Cirno", "Seija", "Suika", "Kasen", "Tenshi", "Yukari", "Clown", "Flan", "Alice", "Orin"};
	public static final String[] COURSE = new String[] {"MARI-CIRCUIT", "FOREST OF MAGIC", "HUMAN VILLAGE", "SCARLET DEVIL MANSION", "LOST BAMBOO THICKET", "YOUKAI MOUNTAIN", "SEIRENSEN", "HAKUGYOKURO", "EMBERS OF BLAZING HELL", "MISTY LAKE", "OLD CAPITAL", "MARI-CIRCUIT 2", "THE OUTSIDE WORLD", "VOILE LIBRARY", "SEA OF TRANQUILITY", "MOONLIT THICKET", "UNDERWORLD CITY DEPTHS", "REDEVELOPMENT AREA A", "REDEVELOPMENT AREA B", "HEAVEN"};
	public static final String[] COURSE_PROPER = new String[] {"Mari-Circuit", "Forest of Magic", "Human Village", "Scarlet Devil Mansion", "Lost Bamboo Thicket", "Youkai Mountain", "Seirensen", "Hakugyokuro", "Embers of Blazing Hell", "Misty Lake", "Old Capital", "Mari-Circuit 2", "The Outside World", "Voile Library", "Sea of Tranquility", "Moonlit Thicket", "Underworld City Depths", "Redevelopment Area A", "Redevelopment Area B", "Heaven"};
	public static final String[] SMOL_COURSE = new String[] {"Mari-1", "Forest", "Village", "Mansion", "Bamboo", "Mountain", "Seirensen", "Haku", "Embers", "Misty", "Old Cap", "Mari-2", "Outside", "Library", "Sea", "Moonlit", "Depths", "RDA", "RDB", "Heaven"};
	private String prefix;

	public Yasova(String prefix) throws InterruptedException {
		driver = new FirefoxDriver();
		this.prefix = prefix;
		WAIT_TIME = Math.max(50, Integer.parseInt(Settings.settingValue("WaitTime", "80")));
		WEBSITE_BREAK = Math.max(50, Integer.parseInt(Settings.settingValue("WebsiteBreak", "250")));
		FREEZE_CHECKS = Math.max(50, Integer.parseInt(Settings.settingValue("FreezeChecks", "100")));
		slowDownIncrease = Integer.parseInt(Settings.settingValue("SlowDownIncrease", "20"));
		slowDownMaximum = Integer.parseInt(Settings.settingValue("SlowDownMaxiumum", "2000"));
		slowDownDecrease = Integer.parseInt(Settings.settingValue("SlowDownDecrease", "20"));
		if (Settings.settingValue("DoCourseInit", "0").equals("1")) {
			appendToTA = true;
			initCourse = Integer.parseInt(Settings.settingValue("CourseInit", "0"));
			initChar1 = Integer.parseInt(Settings.settingValue("Char1Init", "0"));
			initChar2 = Integer.parseInt(Settings.settingValue("Char2Init", "0"));
		}
		Settings.setSettingValue("DoCourseInit", "0");
		start();
	}
	
	public void start() throws InterruptedException {
		try {
			driver.get("https://ranking.skydrift.info/en/index");
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder() + prefix+"TA-Leaderboards.txt", appendToTA))) {
			long time = System.currentTimeMillis();
			String check, name1, name2, name3;
			Thread.sleep(WEBSITE_BREAK*10);
			for (int i = initCourse; i < COURSE.length; i++) {
				initCourse = 0;
				name3 = getId(TIME_TOTAL[0]);
				changeCourse(i);
				for (int c1 = initChar1; c1 < QUICKCHAR.length; c1++) {
					initChar1 = 0;
					name2 = getId(TIME_TOTAL[0]);
					changeChar1(c1);
					for (int c2 = (initChar2 != 0)? initChar2:c1; c2 < QUICKCHAR.length; c2++) {
						initChar2 = 0;
						name1 = getId(TIME_TOTAL[0]);
						int unfreeze = 0;
						String waitedOn = "";
						changeChar2(c2);
						System.out.println(String.format("%s: %s + %s.%s",COURSE[i],QUICKCHAR[c1],QUICKCHAR[c2],
								(modifiedBreak > 0)? String.format(" Break for %d", modifiedBreak):""));
						Thread.sleep(modifiedBreak);
						// TODO Add in try/catch to catch org.openqu.selenium.WebDriverException
						try {
						while ((((check = getId(TIME_TOTAL[0])).equals(name1) || check.equals(name2) || check.equals(name3))&& unfreeze < FREEZE_CHECKS)
								|| !validCharacter(c1, c2, getId(CHARAS[0]))) {
							waitedOn = check;
							Thread.sleep(WAIT_TIME);
							unfreeze++;
							if (unfreeze == FREEZE_CHECKS + 1) {
								System.out.printf("Stuck on %s%s", getId(CHARAS[0]), System.lineSeparator());
							}
							else if (unfreeze > FREEZE_CHECKS && unfreeze%10 == 0) {
								quickEdit(c1,c2);
							}
						}
						} catch (WebDriverException wde) {
							wde.printStackTrace();
							Settings.setSettingValue("CourseInit", i);
							Settings.setSettingValue("Char1Init", c1);
							Settings.setSettingValue("Char2Init", c2);
							System.out.println("Pick up where left off set up, enable in Settings");
						}
						
						if (unfreeze > 0) {
							System.out.printf("Waited on %s for %d miliseconds%s", waitedOn, WAIT_TIME*unfreeze, System.lineSeparator());
							if (unfreeze != FREEZE_CHECKS) {
								modifiedBreak += Math.min(unfreeze*slowDownIncrease, slowDownMaximum) + slowDownDecrease;
							}
						}
						modifiedBreak = Math.max(0, modifiedBreak - slowDownDecrease);
						name2 = "";
						name3 = "";
						if (c1 == 0) {
							bw.write(String.format("%s: %s + %s",COURSE[i],QUICKCHAR[c2],QUICKCHAR[c1]));
						}
						else {
							bw.write(String.format("%s: %s + %s",COURSE[i],QUICKCHAR[c1],QUICKCHAR[c2]));
						}
						bw.newLine();

						String[][] val = getValuesOnPage();
						for (int j = 0; j < val.length; j++) {
							if (val[j][1].length() > 1) {
								bw.write(String.format("    %d. %s (%s): %s, %s, %s | %s", j+1, val[j][0], val[j][1], val[j][2], val[j][3], val[j][4], val[j][5]));
							}
							else if (j == 0){
								bw.write("    1. No Data");
								bw.newLine();
								j += val.length;
							}
							else {
								j += val.length;
							}
							bw.newLine();
							bw.flush();
						}
					}
				}
			}
			//bw.write(Long.toString(System.currentTimeMillis() - time));
			//bw.newLine();
			bw.close();
			/*changeChar1(1);
			changeChar2(2);
			changeCourse(1);
			String[][] val = getValuesOnPage();
			for (int i = 0; i < val.length; i++) {
				System.out.printf("%s %s: %s%s", val[i][0],val[i][5],val[i][1],System.lineSeparator());
			}*/
			} catch (IOException e) {
				e.printStackTrace();
			}
			Thread.sleep(5000);
			
		} finally {
			driver.quit();
		}
	}
	
	public void end() {
		driver.quit();
	}
	
	public String[][] getValuesOnPage() {
		String[][] output = new String[10][6];
		for (int i = 0; i < 10; i++) {
			output[i][0] = getId(NAME[i]);
			output[i][1] = getId(TIME_TOTAL[i]);
			output[i][2] = getId(TIME1[i]);
			output[i][3] = getId(TIME2[i]);
			output[i][4] = getId(TIME3[i]);
			output[i][5] = getId(CHARAS[i]);
		}
		return output;
	}
	
	private String getId(String input) {
		return driver.findElement(By.id(input)).getText();
	}
	private void changeChar1(int input) throws InterruptedException {
		WebElement element = driver.findElement(By.id("ta_chara_search1"));
		Select select = new Select(element);
		if (input > QUICKCHAR.length) {
			select.selectByIndex(0);
		}
		else {
			select.selectByIndex(input);
		}
		Thread.sleep(WEBSITE_BREAK);
	}
	private void changeChar2(int input) throws InterruptedException {
		WebElement element = driver.findElement(By.id("ta_chara_search2"));
		Select select = new Select(element);
		if (input > QUICKCHAR.length) {
			select.selectByIndex(0);
		}
		else {
			select.selectByIndex(input);
		}
		Thread.sleep(WEBSITE_BREAK);
	}
	private void changeCourse(int input) throws InterruptedException {
		WebElement element = driver.findElement(By.id("course_search"));
		Select select = new Select(element);
		if (input > COURSE.length) {
			select.selectByIndex(0);
		}
		else {
			select.selectByIndex(input);
		}
		System.out.println(COURSE[input]);
		Thread.sleep(WEBSITE_BREAK);
	}
	
	public static String getCourse(int i) {
		return COURSE[i];
	}
	public static String getCharacter(int i) {
		if (i == -1) {
			return "Any";
		}
		return QUICKCHAR[i];
	}
	
	public static boolean validCharacter(int slot1, int slot2, String input) {
		if (input.length() == 0 ||(slot1 == 0 && slot2 == 0)) {
			//System.out.printf("[%d][%d] valid for clearence of \"%s\"%s", slot1, slot2, input, System.lineSeparator());
			return true;
		}
		String character1 = RaceTime.capFirstLowerRest(input.substring(0, input.indexOf("/")));
		String character2 = RaceTime.capFirstLowerRest(input.substring(input.indexOf("/") + 1));
		int c1 = 21;
		int c2 = 21;
		for (int i = 1; i < QUICKCHAR.length; i++) {
			if (character1.equals(QUICKCHAR[i])) {
				c1 = i;
			}
			if (character2.equals(QUICKCHAR[i])) {
				c2 = i;
			}
		}
		if (c1 > c2) {
			int temp = c1;
			c1 = c2;
			c2 = temp;
		}
		boolean output = (slot1 == 0)? c1 == slot2 || c2 == slot2:c1 == slot1 && c2 == slot2;
		/*if (output == false) {
			System.out.printf("Characters %s returns %b with characters %s/%s%s", input, output, QUICKCHAR[slot1], QUICKCHAR[slot2], System.lineSeparator());
		}*/
		return output;
	}
	
	private void quickEdit(int c1, int c2) throws InterruptedException {
		int quick1 = (c1 == 0)? 1:0;
		int quick2 = (c2 == 0)? 1:0;
		changeChar1(quick1);
		Thread.sleep(WAIT_TIME);
		changeChar1(c1);
		Thread.sleep(WAIT_TIME);
		changeChar2(quick2);
		Thread.sleep(WAIT_TIME);
		changeChar2(c2);
		Thread.sleep(WAIT_TIME);
	}
	
	private String prefixFolder() {
		String output = SettingsFolder.programDataFolder() + prefix + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
}
