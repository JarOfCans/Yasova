package swy.websitereader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Top100Reader {
	
	static URLConnection connection = null;
	
	public static Top100WebsiteData getValues(int course, int char1, int char2) {
		Top100WebsiteData output = new Top100WebsiteData();
		Scanner scanner;
		
		try {
			
			
			
			connection =  new URL(String.format("https://ranking.skydrift.info/en/records?course_id=%d&character1=%d&character2=%d", course, char1, char2)).openConnection();
			scanner = new Scanner(connection.getInputStream(),"UTF-8");
			String line;
			scanner.useDelimiter("\\Z");
			line = scanner.next();
			if (line.contains("<p>")) {
				output.addData(getPValues(line));
			}
			if (line.contains("<td>")) {
				output.addData(getTDValues(line));
			}
			//System.out.println(output.data);
			scanner.close();
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return output;
	}
	
	private static final Pattern P_REGEX = Pattern.compile("<p>(.+?)</p>", Pattern.DOTALL);
	private static final Pattern TD_REGEX = Pattern.compile("<td>(.+?)</td>", Pattern.DOTALL);

	private static ArrayList<String> getPValues(final String str) {
	    final ArrayList<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = P_REGEX.matcher(str);
	    while (matcher.find()) {
	        tagValues.add(matcher.group(1));
	    }
	    return tagValues;
	}
	private static ArrayList<String> getTDValues(final String str) {
	    final ArrayList<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = TD_REGEX.matcher(str);
	    while (matcher.find()) {
	        tagValues.add(matcher.group(1));
	    }
	    return tagValues;
	}
}
