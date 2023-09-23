package swy.yoink;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import drafterdat.settings.Settings;
import swy.compile.DataRead;
import swy.translate.Bibliophile;

public class RunYasova {
	public static String now;
	public static void main(String[] args) throws InterruptedException {
		
		long time = System.currentTimeMillis();
		Settings.pullSettings();
		
		boolean run = true;
		if (Settings.settingValue("AskToRun", "1").equals("1")) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Did you mean to run the program? (y/n)");
		String input = scanner.nextLine();
		if (!input.toLowerCase().equals("y")) {
			run = false;
			System.out.println("Not running Yasova");
		} else {
			System.out.println("Running Yasova");
		}
		scanner.close();
		}
		if (run) {
			//System.out.println(Charset.defaultCharset());
		//System.setProperty("webdriver.gecko.driver", System.getProperty("user.home")+"\\eclipse-workspace\\Yasova\\geckodriver-v0.27.0-win32\\geckodriver.exe");
		TimeZone tz = TimeZone.getTimeZone("JST");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(tz); // strip timezone
		
		String overrideString = Settings.settingValue("OverrideOutput", "");
		now = (overrideString == null || overrideString.length() < 1)?"YasovaDrop-"+df.format(new Date()) : overrideString;
		System.out.println(now);
		if (Settings.settingValue("RunInnocentTreasures", "0").equals("1")) {
			new InnocentTreasures(now);
		}
		else if (Settings.settingValue("RunYasova", "0").equals("1")) {
			new Yasova(now);
		}
		if (Settings.settingValue("RunDataRead", "0").equals("1")) {
			new DataRead(now);
		}
		if (Settings.settingValue("RunTranslator", "0").equals("1")) {
			new Bibliophile(now);
		}
		if (Settings.settingValue("RunMonthlyRankings", "0").equals("1")) {
			Date date = new Date();
			Calendar calendar = Calendar.getInstance(tz);
			calendar.setTime(date);
			new BelovedTomboyishGirl(calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
		}
		// @deprecated
		/*if (Settings.settingValue("RunUserParse", "0").equals("1")) {
			new AdvancedUserReader();
		}*/
		System.out.println("Time: " + (System.currentTimeMillis() - time));
		}
	}

}
