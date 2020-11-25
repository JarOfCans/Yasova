package swy.compile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import drafterdat.settings.Settings;
import drafterdat.settings.SettingsFolder;
import swy.core.RaceTime;
import swy.yeet.RunDaProgram;
import swy.yoink.Yasova;

public class DataRead {
	private String prefix;
	public static int polishMax;
	ArrayList<DataPoint> dataPointList;
	public static void main(String[] args) throws InterruptedException {
		RunDaProgram.main(new String[0]);
	}
	
	public DataRead(String prefix) {
		//long time = System.currentTimeMillis();
		this.prefix = prefix;
		polishMax = Integer.parseInt(Settings.settingValue("AveragePercentileCount", "10"));
		
		dataPointList = new ArrayList<DataPoint>(4200);
		try (BufferedReader br = new BufferedReader(new FileReader(prefixFolder()+prefix+"TA-Leaderboards.txt"))) {
			String nextLine;
			DataPoint lastDataPoint = null;
			while ((nextLine = br.readLine()) != null) {
				if (nextLine.length() > 0 && !(Character.isDigit(nextLine.charAt(0))) && !(nextLine.equals("    1. No Data"))) {
					if (nextLine.charAt(0) == ' ') {
						lastDataPoint.addData(nextLine);
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
			System.err.print("IOException Occured in reading TA-leaderboards. DataRead halted.");
			return;
		}
		outputDataPair();
		outputCourseTimes();
		outputRacerTimes();
		//outputRacerCourseTimes(dataPointList);
		createNewTimes();
		outputCSV();
		outputLeastPolished();
		
		System.out.println("DataRead done");
		//System.out.println(System.currentTimeMillis() - time);
	}
	
	
	public void outputDataPair() {
		ArrayList<PairData> dataPairList = new ArrayList<PairData>(20*20);
		int[][] characterCounts = new int[20][20];
		for (DataPoint hoi: dataPointList) {
			if (hoi.charId1 >= 0 && hoi.charId2 >= 0) {
				characterCounts[hoi.charId1][hoi.charId2] += hoi.getDataCount();
			}
		}
		for (int i = 0; i < characterCounts.length; i++) {
			for (int m = i; m < characterCounts.length; m++) {
				dataPairList.add(new PairData(i, m, characterCounts[i][m]));
			}
		}
		Collections.sort(dataPairList, new DataPairComparator());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder()+prefix+"CharacterComboCount.txt"))) {
			for (PairData pair: dataPairList) {
				bw.write(pair.toString());
				bw.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void outputCourseTimes() {
		ArrayList<ArrayList<RaceTime>> raceTimeArray = new ArrayList<ArrayList<RaceTime>>();
		
		for (int i = 0; i < Yasova.COURSE.length; i++) {
			raceTimeArray.add(new ArrayList<RaceTime>());
		}
		
		for (DataPoint dataPoint : dataPointList) {
			if (dataPoint.charId2 != -1 && dataPoint.charId2 != -1) {
				for (RaceTime raceTime: dataPoint.data) {
					raceTimeArray.get(dataPoint.courseId).add(raceTime);
				}
			}
		}
		Collections.sort(raceTimeArray.get(0));
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder()+prefix+"CourseTimes.txt"))) {
			int i = 0;
			for (ArrayList<RaceTime> timeArray: raceTimeArray) {
				//System.out.println(timeArray.size());
				//System.out.println(Yasova.COURSE[i]);
				//System.out.println(timeArray);
				Collections.sort(timeArray);
				//System.out.println(timeArray);
				bw.write(Yasova.COURSE[i++]);
				bw.newLine();
				int n = 1;
				for (RaceTime racetime : timeArray) {
					racetime.globalPosition = n++;
					bw.write(racetime.toString());
					bw.newLine();
				}
			}
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outputRacerTimes() {
		ArrayList<ArrayList<RaceTime>> raceTimeArray = new ArrayList<ArrayList<RaceTime>>();
		ArrayList<RaceTime> racerTimeArray = new ArrayList<RaceTime>();
		
		
		for (int i = 0; i < Yasova.COURSE.length; i++) {
			raceTimeArray.add(new ArrayList<RaceTime>());
		}
		
		for (DataPoint dataPoint : dataPointList) {
			if (dataPoint.charId2 != -1 && dataPoint.charId2 != -1) {
				for (RaceTime raceTime: dataPoint.data) {
					raceTimeArray.get(dataPoint.courseId).add(raceTime);
				}
			}
		}
		for (ArrayList<RaceTime> timeArray: raceTimeArray) {
			Collections.sort(timeArray);
			int n = 0;
			for (RaceTime racetime : timeArray) {
				racetime.globalPosition = n++;
				racerTimeArray.add(racetime);
			}
		}
		for (RaceTime racetime : racerTimeArray) {
			racetime.percentile = 1 - ((double) racetime.globalPosition)/((double)raceTimeArray.get(racetime.course).size()-1);
		}
		Collections.sort(racerTimeArray, new NameSortComparable());
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder()+prefix+"RacerTimes.txt"))) {
			String previousRacer = null;
			for (RaceTime racetime : racerTimeArray) {
				if (!racetime.racerName.equals(previousRacer)) {
					previousRacer = racetime.racerName;
					bw.write(previousRacer);
					bw.newLine();
				}
				bw.write(racetime.racerString());
				bw.newLine();
			}
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outputRacerCourseTimes() {
		ArrayList<ArrayList<RaceTime>> raceTimeArray = new ArrayList<ArrayList<RaceTime>>();
		ArrayList<RaceTime> racerTimeArray = new ArrayList<RaceTime>();
		
		
		for (int i = 0; i < Yasova.COURSE.length; i++) {
			raceTimeArray.add(new ArrayList<RaceTime>());
		}
		
		for (DataPoint dataPoint : dataPointList) {
			if (dataPoint.charId2 != -1 && dataPoint.charId2 != -1) {
				for (RaceTime raceTime: dataPoint.data) {
					raceTimeArray.get(dataPoint.courseId).add(raceTime);
				}
			}
		}
		for (ArrayList<RaceTime> timeArray: raceTimeArray) {
			Collections.sort(timeArray);
			int n = 1;
			for (RaceTime racetime : timeArray) {
				racetime.globalPosition = n++;
				racerTimeArray.add(racetime);
			}
		}
		for (RaceTime racetime : racerTimeArray) {
			racetime.percentile = 1 - ((double) racetime.globalPosition)/((double)raceTimeArray.get(racetime.course).size() - 1);
		}
		Collections.sort(racerTimeArray, new NameCourseSortComparator());
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder()+prefix+"RacerCourseTimes.txt"))) {
			String previousRacer = null;
			for (RaceTime racetime : racerTimeArray) {
				if (!racetime.racerName.equals(previousRacer)) {
					previousRacer = racetime.racerName;
					bw.write(previousRacer);
					bw.newLine();
				}
				bw.write(racetime.racerString());
				bw.newLine();
			}
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Compares times between two TA-leaderboards.
	 * @param inputPrefix The prefix of the old TA leaderboard to check
	 * @param currentPrefix The prefix of the new TA leaderboard to check
	 * @param dataPointList The current TA leaderboard dataPoint ArrayList
	 */
	public void createNewTimes() {
		ArrayList<DataPoint> oldList = new ArrayList<DataPoint>(4200);
		String inputPrefix = Settings.settingValue("Previous", prefix);
		if (inputPrefix.equals(prefix)) {
			System.out.println("Input prefix same as output prefix, skipped New Data");
			return;
		}
		if (inputPrefix != null) {
		try (BufferedReader br = new BufferedReader(new FileReader(SettingsFolder.programDataFolder()+inputPrefix.substring(0, inputPrefix.length()-1) + "\\"+inputPrefix+"TA-Leaderboards.txt"))) {
			String nextLine;
			DataPoint lastDataPoint = null;
			while ((nextLine = br.readLine()) != null) {
				if (nextLine.length() > 0 && !(Character.isDigit(nextLine.charAt(0))) && !(nextLine.equals("    1. No Data"))) {
					if (nextLine.charAt(0) == ' ') {
						lastDataPoint.addData(nextLine);
					}
					else {
						lastDataPoint = new DataPoint(nextLine);
						oldList.add(lastDataPoint);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder()+prefix+"NewTimes.txt"))) {
			int i = 0;
			boolean topTenOnly = Settings.settingValue("NewTopTenOnly", "1").equals("1");
			bw.write(String.format("%s -> %s", inputPrefix.substring(0, inputPrefix.length()-1), prefix.substring(0, prefix.length()-1)));
			bw.newLine();
			for (DataPoint dataPoint : dataPointList) {
				//System.out.println(Yasova.COURSE[dataPoint.courseId]);
				DataPoint oldPoint = oldList.get(i++);
				ArrayList<RaceTime> times = oldPoint.newData(dataPoint);
				if (topTenOnly) {
					for (int m = 0; m < times.size();m++) {
						if (times.get(m).placement > 10) {
							times.remove(m--);
						}
					}
				}
				if (times.size() > 0) {
					bw.write(String.format("%s: %s + %s",Yasova.COURSE[dataPoint.courseId],Yasova.getCharacter(dataPoint.charId1+1),Yasova.getCharacter(dataPoint.charId2+1)));
					bw.newLine();
					for (RaceTime hoi : times) {
						bw.write(hoi.positionlesstoString());
						bw.newLine();
					}
					ArrayList<RaceTime> oldTimes = oldPoint.oldData(dataPoint);
					if (topTenOnly) {
						for (int m = 0; m < oldTimes.size();m++) {
							if (oldTimes.get(m).placement > 10) {
								oldTimes.remove(m--);
							}
						}
					}
					if (oldTimes.size() > 0) {
						bw.write("Removed:");
						bw.newLine();
						for (RaceTime hoi : oldTimes) {
							bw.write("-");
							bw.write(hoi.positionlesstoString());
							bw.newLine();
						}
					}
					bw.newLine();
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Settings.settingValue("UpdatePrevious", "1").equals("1")) {
			Settings.setSettingValue("Previous", prefix);
		}
		}
	}
	
	public void outputCSV() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder()+prefix+"TA-times.csv"))) {
			//percentile*100"%", racerName, course, capFirstLowerRest(character1), capFirstLowerRest(character2), minutes, seconds, miliseconds);
			bw.write("Racer,Course,Character 1,Character 2,Time");
			bw.newLine();
			for (DataPoint dataPoint: dataPointList) {
				for (RaceTime raceTime: dataPoint.data) {
					bw.write(raceTime.csvString());
					bw.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outputLeastPolished() {
		ArrayList<DataPoint> sortedList = new ArrayList<DataPoint>(4000);
		for (DataPoint point:dataPointList) {
			if (!(point.charId1 == -1 || point.charId2 == -1)) {
				sortedList.add(point);
			}
		}
		sortedList.sort(new DataPointPolishComparator());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolder()+prefix+"PolishedLeaderboards.txt"))) {
			for(DataPoint point:sortedList) {
				bw.write(String.format("%d - %.2f%s: %s - %s + %s",Math.min(point.getDataCount(), polishMax) ,point.averagePercentile()*100,"%",point.course,point.character1,point.character2));
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String prefixFolder() {
		String output = SettingsFolder.programDataFolder() + prefix.substring(0, prefix.length()-1) + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
}
