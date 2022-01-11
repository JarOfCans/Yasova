package swy.websitereader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.stream.Collectors;

import swy.yoink.InnocentTreasures;

public class Top100Reader {
	
	static URLConnection connection = null;
	static int CONNECT_TRUE = -1;
	static int INPUT_STREAM = -1;
	static int READ_TRUE = -1;
	
	public static Top100WebsiteData getValues(int course, int char1, int char2) {
		Top100WebsiteData output = new Top100WebsiteData();
		//Scanner scanner;
		BufferedReader br;
		
		try {
			
			if (CONNECT_TRUE == -1) {
				CONNECT_TRUE = InnocentTreasures.check.newSlot("Connect to Website");
			}
			if (INPUT_STREAM == -1) {
				INPUT_STREAM = InnocentTreasures.check.newSlot("Website Input");
			}
			if (READ_TRUE == -1) {
				READ_TRUE = InnocentTreasures.check.newSlot("Read Website");
			}
			InnocentTreasures.check.start(CONNECT_TRUE);
			connection =  new URL(String.format("https://ranking.skydrift.info/en/records?course_id=%d&character1=%d&character2=%d", course, char1, char2)).openConnection();
			InnocentTreasures.check.stop(CONNECT_TRUE);
			InnocentTreasures.check.start(INPUT_STREAM);
			/*scanner = new Scanner(connection.getInputStream(),"UTF-8");
			scanner.useDelimiter("\\Z");
			String line = scanner.next();*/
			
			br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
			String line = br.lines().collect(Collectors.joining());
			
			InnocentTreasures.check.stop(INPUT_STREAM);
			InnocentTreasures.check.start(READ_TRUE);
			int indexStart;
			int indexEnd;
			while ((indexStart = line.indexOf("<p>")) > 0) {
				indexEnd = line.indexOf("</p>");
				output.add(line.substring(indexStart+3, indexEnd));
				line = line.substring(indexEnd+3);
			}
			while ((indexStart = line.indexOf("<td>")) > 0) {
				indexEnd = line.indexOf("</td>");
				output.add(line.substring(indexStart+4, indexEnd));
				line = line.substring(indexEnd+4);
			}
			//scanner.close();
			br.close();
			InnocentTreasures.check.stop(READ_TRUE);
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return output;
	}
	
	/*private static final Pattern P_REGEX = Pattern.compile("<p>(.+?)</p>", Pattern.DOTALL);
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
	}*/
}
