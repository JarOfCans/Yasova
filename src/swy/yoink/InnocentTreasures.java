package swy.yoink;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
import swy.testing.TimeTeller;
import swy.websitereader.Top100Reader;
import swy.websitereader.Top100WebsiteData;

public class InnocentTreasures {
	//WebDriver driver;
	URLConnection connection;
	private int WEBSITE_BREAK;
	private boolean appendToTA = false;
	private int courseTotal = 0;
	private int characterTotal = 0;
	private int initCourse = 0;
	private int initChar1 = 0;
	private int initChar2 = 0;
	private String prefix;
	public static TimeTeller check;

	public InnocentTreasures(String prefix) throws InterruptedException {
		//driver = new FirefoxDriver();
		this.prefix = prefix;
		check = new TimeTeller();
		WEBSITE_BREAK = Integer.parseInt(Settings.settingValue("InnocentTreasuresWebsiteBreak", "25"));
		courseTotal = Integer.parseInt(Settings.settingValue("CourseTotal", "21"));
		characterTotal = Integer.parseInt(Settings.settingValue("CharacterTotal", "22")) + 1;
		
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
			//final int CONNECT = check.newSlot("URL create");
			final int BREAK = check.newSlot("Manual Website Break");
			final int READ = check.newSlot("Read website total");
			final int WRITE = check.newSlot("Write to output");
			//try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder() + prefix+"TA-Leaderboards.txt", appendToTA))) {
			try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(prefixFolder() + "TA-Leaderboards.txt"), StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.CREATE,(appendToTA)?StandardOpenOption.APPEND:StandardOpenOption.TRUNCATE_EXISTING)) {
			//long time = System.currentTimeMillis();
				int i = initCourse;
				int c1 = initChar1;
				int c2 = (initChar2 != 0)? initChar2:c1;
				try {
			for (i = initCourse; i < courseTotal; i++) {
				initCourse = 0;
				for (c1 = initChar1; c1 < characterTotal; c1++) {
					initChar1 = 0;
					for (c2 = (initChar2 != 0)? initChar2:c1; c2 < characterTotal; c2++) {
						System.out.println(String.format("%d/%d: %d/%d + %d/%d",i,courseTotal,c1,characterTotal,c2,characterTotal));
						initChar2 = 0;
						//driver.get(String.format("https://ranking.skydrift.info/en/records?course_id=%d&character1=%d&character2=%d", i, c1 - 1, c2 - 1));
						/*check.start(CONNECT);
						connection = new URL(String.format("https://ranking.skydrift.info/en/records?course_id=%d&character1=%d&character2=%d", i, c1 - 1, c2 - 1)).openConnection();
						check.stop(CONNECT);*/
						check.start(BREAK);
						Thread.sleep(WEBSITE_BREAK);
						check.stop(BREAK);
						// TODO Add in try/catch to catch org.openqu.selenium.WebDriverException
						
						
						//List<WebElement> pTags = driver.findElements(By.tagName("p"));
						check.start(READ);
						Top100WebsiteData values = Top100Reader.getValues(i, c1-1, c2-1);
						check.stop(READ);
						check.start(WRITE);
						if (c1 == 0) {
							bw.write(String.format("%s: %s + %s",values.getCourse(),capFirstLowerRest(values.getCharacter2()),capFirstLowerRest(values.getCharacter1())));
						}
						else {
							bw.write(String.format("%s: %s + %s",values.getCourse(),capFirstLowerRest(values.getCharacter1()),capFirstLowerRest(values.getCharacter2())));
						}
						bw.newLine();
						System.out.println(String.format("%s: %s + %s",values.getCourse(),capFirstLowerRest(values.getCharacter2()),capFirstLowerRest(values.getCharacter1())));

						//List<String> val = getValuesOnPage();
						if (!values.hasData()) {
							bw.write("  1. No Data");
							bw.newLine();
						}
						else {
							int placement = 0;
							while (values.hasData(placement)) {
								ArrayList<String> thisData = values.getData(placement++);
								bw.write(String.format("  %s. %s (%s): %s, %s, %s | %s", thisData.get(0), thisData.get(1), thisData.get(2), thisData.get(3), thisData.get(4), thisData.get(5), thisData.get(6)));
								bw.newLine();
								bw.flush();
							}
						}
						check.stop(WRITE);
					}
					//bw.flush();
				}
				check.print();
			}} catch (IndexOutOfBoundsException wde) {
				wde.printStackTrace();
				Settings.setSettingValue("CourseInit", i);
				Settings.setSettingValue("Char1Init", c1);
				Settings.setSettingValue("Char2Init", c2);
				Settings.setSettingValue("DoCourseInit", "1");
				check.print();
				System.out.println("Pick up where left off set up, enable in Settings");
			}
			bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			check.print();
			//Thread.sleep(5000);
			
		} finally {
			//driver.quit();
		}
	}
	
	public void end() {
		//driver.quit();
	}
	
	/*public List<String> getValuesOnPage() {
		List<String> output = new ArrayList<String>();
		List<WebElement> webElements = driver.findElements(By.tagName("td"));
		for (WebElement hoi: webElements) {
			output.add(hoi.getText());
			//System.out.println(hoi.getText());
		}
		return output;
	}*/
	
	private String prefixFolder() {
		String output = SettingsFolder.programDataFolder() + prefix + "\\";
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
