package swy.complie.userReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import drafterdat.settings.Settings;
import drafterdat.settings.SettingsFolder;
import swy.compile.DataPoint;
import swy.yoink.RunYasova;

public class AdvancedUserReader {
	String prefix;
	UserIDManager YIDM;
	public AdvancedUserReader() {
		prefix = RunYasova.now;
		YIDM = new UserIDManager();
		ArrayList dataPointList;
		try {
			dataPointList = dataPointList(prefixFolder()+prefix+"TA-Leaderboards.txt");
			
		} catch (IOException e) {
			return;
		}
		//dataPointList;
	}
	
	
	
	
	
	public static ArrayList<DataPoint> dataPointList(String inputFile) throws IOException {
		
		ArrayList<DataPoint> dataPointList = new ArrayList<DataPoint>(4200);
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String nextLine;
			DataPoint lastDataPoint = null;
			while ((nextLine = br.readLine()) != null) {
				if (nextLine.length() > 0 && !(Character.isDigit(nextLine.charAt(0))) && !(nextLine.equals("    1. No Data"))) {
					if (nextLine.charAt(0) == ' ') {
						lastDataPoint.addData(nextLine, null);
					}
					else {
						lastDataPoint = new DataPoint(nextLine);
						dataPointList.add(lastDataPoint);
					}
				}
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IOException Occured in reading TA-leaderboards. User Data Reader halted.");
			throw e;
		}
		return dataPointList;
	}
	
	private String prefixFolder() {
		String output = SettingsFolder.programDataFolder() + prefix.substring(0, prefix.length()-1) + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
}
