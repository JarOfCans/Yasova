package swy.yoink;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import drafterdat.settings.Settings;
import drafterdat.settings.SettingsFolder;
import swy.core.RaceTime;

public class InnocentTreasures {
	WebDriver driver;
	private int WEBSITE_BREAK;
	private boolean appendToTA = false;
	private int courseTotal = 0;
	private int characterTotal = 0;
	private int initCourse = 0;
	private int initChar1 = 0;
	private int initChar2 = 0;
	private String prefix;

	public InnocentTreasures(String prefix) throws InterruptedException {
		driver = new FirefoxDriver();
		this.prefix = prefix;
		//WAIT_TIME = Math.max(50, Integer.parseInt(Settings.settingValue("WaitTime", "80")));
		WEBSITE_BREAK = Math.max(500, Integer.parseInt(Settings.settingValue("InnocentTreasuresWebsiteBreak", "250")));
		courseTotal = Integer.parseInt(Settings.settingValue("CourseTotal", "18"));
		characterTotal = Integer.parseInt(Settings.settingValue("CharacterTotal", "20")) + 1;
		//FREEZE_CHECKS = Math.max(50, Integer.parseInt(Settings.settingValue("FreezeChecks", "100")));
		//slowDownIncrease = Integer.parseInt(Settings.settingValue("SlowDownIncrease", "20"));
		//slowDownMaximum = Integer.parseInt(Settings.settingValue("SlowDownMaxiumum", "2000"));
		//slowDownDecrease = Integer.parseInt(Settings.settingValue("SlowDownDecrease", "20"));
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
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder() + prefix+"TA-Leaderboards.txt", appendToTA))) {
			//long time = System.currentTimeMillis();
			for (int i = initCourse; i < courseTotal; i++) {
				initCourse = 0;
				for (int c1 = initChar1; c1 < characterTotal; c1++) {
					initChar1 = 0;
					for (int c2 = (initChar2 != 0)? initChar2:c1; c2 < characterTotal; c2++) {
						System.out.println(String.format("%d/%d: %d/%d + %d/%d",i,courseTotal,c1,characterTotal,c2,characterTotal));
						initChar2 = 0;
						driver.get(String.format("https://ranking.skydrift.info/en/records?course_id=%d&character1=%d&character2=%d", i, c1 - 1, c2 - 1));
						Thread.sleep(WEBSITE_BREAK);
						// TODO Add in try/catch to catch org.openqu.selenium.WebDriverException
						try {
						
						} catch (WebDriverException wde) {
							wde.printStackTrace();
							Settings.setSettingValue("CourseInit", i);
							Settings.setSettingValue("Char1Init", c1);
							Settings.setSettingValue("Char2Init", c2);
							System.out.println("Pick up where left off set up, enable in Settings");
						}
						List<WebElement> pTags = driver.findElements(By.tagName("p"));
						if (c1 == 0) {
							bw.write(String.format("%s: %s + %s",pTags.get(0).getText(),capFirstLowerRest(pTags.get(2).getText()),capFirstLowerRest(pTags.get(1).getText())));
						}
						else {
							bw.write(String.format("%s: %s + %s",pTags.get(0).getText(),capFirstLowerRest(pTags.get(1).getText()),capFirstLowerRest(pTags.get(2).getText())));
						}
						bw.newLine();
						System.out.println(String.format("%s: %s + %s",pTags.get(0).getText(),capFirstLowerRest(pTags.get(2).getText()),capFirstLowerRest(pTags.get(1).getText())));

						List<String> val = getValuesOnPage();
						if (val.size() == 0) {
							bw.write("    1. No Data");
							bw.newLine();
						}
						else {
							for (int j = 0; j < val.size(); j+= 7) {
								bw.write(String.format("    %s. %s (%s): %s, %s, %s | %s", val.get(j+0), val.get(j+1), val.get(j+2), val.get(j+3), val.get(j+4), val.get(j+5), val.get(j+6)));
								bw.newLine();
								bw.flush();
							}
						}
					}
					//bw.flush();
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
	
	public List<String> getValuesOnPage() {
		List<String> output = new ArrayList<String>();
		List<WebElement> webElements = driver.findElements(By.tagName("td"));
		for (WebElement hoi: webElements) {
			output.add(hoi.getText());
			//System.out.println(hoi.getText());
		}
		return output;
	}
	
	private String prefixFolder() {
		String output = SettingsFolder.programDataFolder() + prefix.substring(0, prefix.length()-1) + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
	
	private String capFirstLowerRest(String input) {
		if (input.equals("ALL")) {
			return "Any";
		}
		else return input.substring(0, 1) + input.substring(1).toLowerCase();
	}
}
