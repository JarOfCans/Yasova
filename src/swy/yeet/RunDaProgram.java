package swy.yeet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JOptionPane;

import drafterdat.settings.Settings;
import swy.compile.DataRead;
import swy.translate.Bibliophile;
import swy.yoink.InnocentTreasures;
import swy.yoink.Yasova;

public class RunDaProgram {

	public static void main(String[] args) throws InterruptedException {
		
		long time = System.currentTimeMillis();
		Settings.pullSettings();
		//System.out.println(System.getProperty("user.home"));
		//System.out.println(System.getProperty("webdriver.firefox.bin"));
		if (true) {
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.home")+"\\eclipse-workspace\\Yasova\\geckodriver-v0.27.0-win32\\geckodriver.exe");
		//System.out.println(System.getProperty("webdriver.firefox.bin"));
		//System.setProperty("webdriver.firefox.bin", "/path/to/another/firefox/dot/exe");
		TimeZone tz = TimeZone.getTimeZone("JST");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-");
		df.setTimeZone(tz); // strip timezone
		
		String overrideString = Settings.settingValue("OverrideString", "");
		String now = (overrideString == null || overrideString.length() <= 1)?df.format(new Date()) : overrideString;
		System.out.println(now);
		if (Settings.settingValue("RunInnocentTreasures", "1").equals("1")) {
			InnocentTreasures it = new InnocentTreasures(now);
		}
		else if (Settings.settingValue("RunYasova", "0").equals("1")) {
			Yasova yasova = new Yasova(now);
		}
		if (Settings.settingValue("RunDataRead", "0").equals("1")) {
			DataRead dataRead = new DataRead(now);
		}
		if (Settings.settingValue("RunTranslator", "1").equals("1")) {
			Bibliophile translator = new Bibliophile(now);
		}
		//System.out.println(System.currentTimeMillis() - time);
		}
	}

}
