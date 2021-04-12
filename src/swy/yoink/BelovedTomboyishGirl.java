package swy.yoink;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import drafterdat.settings.Settings;
import drafterdat.settings.SettingsFolder;
import swy.monthyrankings.Ranking;
import swy.translate.JPTranslation;

public class BelovedTomboyishGirl {
	WebDriver driver;
	private int WAIT_TIME;
	private int WEBSITE_BREAK;
	private int FREEZE_CHECKS;
	private int characterTotal;
	private JPTranslation JPTranslator;
	private String prefix;
	
	public BelovedTomboyishGirl(int month, int year) throws InterruptedException {
		month -= 1;
		if (month == 0) {
			year--;
			month = 12;
		}
		JPTranslator = new JPTranslation();
		prefix = String.format("%04d-%02d-", year, month);
		driver = new FirefoxDriver();
		WAIT_TIME = Math.max(50, Integer.parseInt(Settings.settingValue("WaitTime", "80")));
		WEBSITE_BREAK = Math.max(500, Integer.parseInt(Settings.settingValue("WebsiteBreak", "250")));
		FREEZE_CHECKS = Math.max(50, Integer.parseInt(Settings.settingValue("FreezeChecks", "100")));
		characterTotal = Integer.parseInt(Settings.settingValue("CharacterTotal", "22"));
		try {
			start();
		}
		finally {
			end();
		}
	}
	
	public void start() throws InterruptedException {
		driver.get("https://ranking.skydrift.info/en/index");
		Thread.sleep(WEBSITE_BREAK);
		driver.findElement(By.id("prev-term")).click();
		Thread.sleep(WEBSITE_BREAK*10);
		ArrayList<Ranking> rankings = new ArrayList<Ranking>(100);
		boolean runningThrough;
		for (int i = 1; i <= characterTotal; i++) {
			changeChar(i);
			runningThrough = true;
			int page = 1;
			while (runningThrough) {
				changePage(page++);
				List<String> values = getValuesOnPage();
				for (int m = 0; m < 50 && runningThrough; m += 5) {
					if (values.get(m).equals("")) {
						runningThrough = false;
					}
					else {
						rankings.add(new Ranking(values.get(m),values.get(m+1),values.get(m+2),values.get(m+3),values.get(m+4)));
					}
				}
			}
		}
		Collections.sort(rankings);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder() + prefix + "MonthlyRankings.txt"))) {
			for (Ranking ranking: rankings) {
				bw.write(String.format("%-4s %-7d by %s  %s(%d) %s", ranking.getNumber()+".", ranking.getMonthlyPoints(), ranking.getUserName(), ranking.getRank(), ranking.getTotalPoints(), ranking.getCharactersUsed(), ranking.getCharactersUsed()));
				bw.newLine();
			}
			bw.write("");
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder() + prefix + JPTranslator.translateAll("MonthlyRankings.txt")))) {
			for (Ranking ranking: rankings) {
				bw.write(String.format("%-4s %-7d %s  %s(%d) %s", ranking.getNumber()+".", ranking.getMonthlyPoints(), ranking.getUserName(), JPTranslator.translateAll(ranking.getRank()), ranking.getTotalPoints(), JPTranslator.translateAll(ranking.getCharactersUsed())));
				bw.newLine();
			}
			bw.write("");
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void end() {
		driver.quit();
	}
	
	public List<String> getValuesOnPage() {
		List<String> output = new ArrayList<String>();
		int attempts = 0;
		while (attempts < 3) {
		try {
			WebElement rankedTable = driver.findElement(By.id("table_ranking"));
			List<WebElement> webElements = rankedTable.findElements(By.tagName("td"));
		for (WebElement hoi: webElements) {
			output.add(hoi.getText());
			//System.out.println(hoi.getText());
		}
			attempts++;
		} catch (StaleElementReferenceException sere) {
			System.out.println("Stale reached");
			output = new ArrayList<String>();
		} finally {
			attempts++;
		}
		}
		return output;
	}
	
	private void changeChar(int input) throws InterruptedException {
		WebElement element = driver.findElement(By.id("chara_search"));
		Select select = new Select(element);
		if (input > characterTotal) {
			select.selectByIndex(0);
		}
		else {
			select.selectByIndex(input);
		}
		Thread.sleep(WEBSITE_BREAK);
	}
	private void changePage(int input) throws InterruptedException {
		if (input > 10 || input < 1) {
			input = 1;
		}
		driver.findElement(By.id("ranking_page_"+input)).click();
		Thread.sleep(WEBSITE_BREAK);
	}
	
	private String prefixFolder() {
		String output = SettingsFolder.programDataFolder() + prefix.substring(0, prefix.length()-1) + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
}
