package swy.compile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import drafterdat.settings.Settings;
import drafterdat.settings.SettingsFolder;
import swy.compile.racers.RacerBatch;
import swy.core.RaceTime;
import swy.yeet.RunYasova;
import swy.yoink.Yasova;

public class DataRead {
	private String targetPrefix;
	private String destinationPrefix;
	public static int polishMax;
	RacerBatch racerTimes;
	//ArrayList<DataPoint> dataPointList;
	public static void main(String[] args) throws InterruptedException {
		RunYasova.main(new String[0]);
	}
	
	public DataRead(String destination) {
		//long time = System.currentTimeMillis();
		racerTimes = new RacerBatch();
		destinationPrefix = destination;
		targetPrefix = Settings.settingValue("OverrideReadLocation", "");
		if (targetPrefix.length() < 1) {
			targetPrefix = destinationPrefix;
		}
		polishMax = Integer.parseInt(Settings.settingValue("AveragePercentileCount", "10"));
		//System.out.printf("%s%s", targetFolder()+targetPrefix+"TA-Leaderboards.txt", System.lineSeparator());
		//dataPointList = new ArrayList<DataPoint>(4200);
		//try (BufferedReader br = new BufferedReader(new FileReader(targetFolder()+targetPrefix+"TA-Leaderboards.txt"))) {
		try (BufferedReader br = Files.newBufferedReader(Paths.get(targetFolder()+targetPrefix+"TA-Leaderboards.txt"), StandardCharsets.UTF_8)) {
			String nextLine;
			DataPoint lastDataPoint = null;
			while ((nextLine = br.readLine()) != null) {
				if (nextLine.length() > 0 && !(Character.isDigit(nextLine.charAt(0))) && !(nextLine.equals("    1. No Data"))) {
					if (nextLine.charAt(0) == ' ') {
						lastDataPoint.addData(nextLine);
					}
					else {
						lastDataPoint = new DataPoint(nextLine);
						racerTimes.addDataPoint(lastDataPoint);
						//dataPointList.add(lastDataPoint);
					}
				}
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IOException Occured in reading TA-leaderboards. DataRead halted.");
			return;
		}
		racerTimes.bake();
		outputDataPair();
		outputCourseTimes();
		outputRacerTimes();
		outputCompactRacerTimes();
		outputCompactCourseTimes();
		//outputRacerCourseTimes(dataPointList);
		createNewTimes();
		outputCSV();
		outputLeastPolished();
		racerAnyTimes();
		outputSuzunaanFile();
		
		System.out.println("DataRead done");
		//System.out.println(System.currentTimeMillis() - time);
	}
	
	
	public void outputDataPair() {
		//int charCount = Yasova.QUICKCHAR.length - 1;
		ArrayList<PairData> dataPairList = racerTimes.getPairData();
		/*int[][] characterCounts = new int[charCount][charCount];
		for (DataPoint hoi: racerTimes.getDataPoints()) {
			int charId1 = hoi.getCharacterId1();
			int charId2 = hoi.getCharacterId2();
			if (charId1 >= 0 && charId2 >= 0) {
				characterCounts[charId1][charId2] += hoi.getDataCount();
			}
		}
		for (int i = 0; i < characterCounts.length; i++) {
			for (int m = i; m < characterCounts.length; m++) {
				dataPairList.add(new PairData(i, m, characterCounts[i][m]));
			}
		}*/
		Collections.sort(dataPairList, new DataPairComparator());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"CharacterComboCount.txt"))) {
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
		//ArrayList<ArrayList<RaceTime>> raceTimeArray = new ArrayList<ArrayList<RaceTime>>();
		
		/*for (int i = 0; i < Yasova.COURSE.length; i++) {
			raceTimeArray.add(new ArrayList<RaceTime>());
		}
		
		for (DataPoint dataPoint : dataPointList) {
			if (dataPoint.charId1 != -1 && dataPoint.charId2 != -1) {
				for (RaceTime raceTime: dataPoint.data) {
					raceTimeArray.get(dataPoint.courseId).add(raceTime);
				}
			}
		}*/
		//Collections.sort(raceTimeArray.get(0));
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"CourseTimes.txt"))) {
			int i = 0;
			for (ArrayList<RaceTime> timeArray: racerTimes.getAllCourses()) {
				//Collections.sort(timeArray);
				bw.write(Yasova.COURSE[i++]);
				bw.newLine();
				//int n = 1;
				for (RaceTime racetime : timeArray) {
					//racetime.globalPosition = n++;
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
		//ArrayList<ArrayList<RaceTime>> raceTimeArray = new ArrayList<ArrayList<RaceTime>>();
		//ArrayList<RaceTime> racerTimeArray = new ArrayList<RaceTime>();
		
		/*for (int i = 0; i < Yasova.COURSE.length; i++) {
			raceTimeArray.add(new ArrayList<RaceTime>());
		}
		
		for (DataPoint dataPoint : dataPointList) {
			if (dataPoint.charId1 != -1 && dataPoint.charId2 != -1) {
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
				//racerTimeArray.add(racetime);
				racerTimes.addTime(racetime);
			}
		}*/
		/*for (RaceTime racetime : racerTimes.getAll()) {
			racetime.percentile = 1 - ((double) racetime.globalPosition)/((double)raceTimeArray.get(racetime.course).size()-1);
		}*/
		//TODO RacerManager
		//Collections.sort(racerTimeArray, new NameSortComparable());
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"RacerTimes.txt"))) {
			racerTimes.generateOutput(bw);
			/*String previousRacer = null;
			for (RaceTime racetime : racerTimeArray) {
				if (!racetime.racerName.equals(previousRacer)) {
					previousRacer = racetime.racerName;
					bw.write(previousRacer);
					bw.newLine();
				}
				bw.write(racetime.racerString());
				bw.newLine();
			}*/
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outputCompactCourseTimes() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"CompactCourseTimes.txt"))) {
			int i = 0;
			for (ArrayList<RaceTime> timeArray: racerTimes.getAllCourses()) {
				//Collections.sort(timeArray);
				bw.write(Yasova.COURSE[i++]);
				bw.newLine();
				for (RaceTime racetime : timeArray) {
					//racetime.globalPosition = n++;
					if (racetime.topTime) {
						bw.write(racetime.toCompressedString());
						bw.newLine();
					}
				}
			}
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void outputCompactRacerTimes() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"CompactRacerTimes.txt"))) {
			racerTimes.generateCompactOutput(bw);
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Deprecated
	public void outputRacerCourseTimes() {
		/*ArrayList<ArrayList<RaceTime>> raceTimeArray = new ArrayList<ArrayList<RaceTime>>();
		ArrayList<RaceTime> racerTimeArray = new ArrayList<RaceTime>();
		
		
		for (int i = 0; i < Yasova.COURSE.length; i++) {
			raceTimeArray.add(new ArrayList<RaceTime>());
		}
		
		for (DataPoint dataPoint : dataPointList) {
			if (dataPoint.charId1 != -1 && dataPoint.charId2 != -1) {
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
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"RacerCourseTimes.txt"))) {
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
		}*/
	}
	
	public void racerAnyTimes() {
		/*ArrayList<ArrayList<RaceTime>> raceTimeArray = new ArrayList<ArrayList<RaceTime>>();
		ArrayList<RaceTime> racerTimeArray = new ArrayList<RaceTime>();
		
		
		for (int i = 0; i < Yasova.COURSE.length; i++) {
			raceTimeArray.add(new ArrayList<RaceTime>());
		}
		
		for (DataPoint dataPoint : dataPointList) {
			if (dataPoint.charId1 == -1 && dataPoint.charId2 == -1) {
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
			//racetime.percentile = 1 - ((double) racetime.globalPosition)/((double)raceTimeArray.get(racetime.course).size()-1);
		}*/
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"RacerTop100Times.txt"))) {
			String previousRacer = null;
			for (RaceTime racetime : racerTimes.getAllAny()) {
				if (!racetime.racerName.equals(previousRacer)) {
					previousRacer = racetime.racerName;
					bw.write(previousRacer);
					bw.newLine();
				}
				bw.write(racetime.racerAnyString());
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
		//Prepare the old list
		ArrayList<DataPoint> oldList = new ArrayList<DataPoint>(4200);
		String inputPrefix = Settings.settingValue("Previous", destinationPrefix);
		if (inputPrefix.equals(destinationPrefix)) {
			System.out.println("Input prefix same as output prefix, skipped New Data");
			return;
		}
		if (inputPrefix != null) {
			//Print old data file
			System.out.println(SettingsFolder.programDataFolder()+inputPrefix.substring(0, inputPrefix.length()-1) + "\\"+inputPrefix+"TA-Leaderboards.txt");
		//Get a copy of the old data
		try (BufferedReader br = Files.newBufferedReader(Paths.get(SettingsFolder.programDataFolder()+inputPrefix.substring(0, inputPrefix.length()-1) + "\\"+inputPrefix+"TA-Leaderboards.txt"), StandardCharsets.UTF_8)) {
			String nextLine;
			DataPoint lastDataPoint = null;
			while ((nextLine = br.readLine()) != null) {
				//System.out.println(nextLine.length());
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
			e.printStackTrace();
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"NewTimes.txt"))) {
			try (BufferedWriter anybw = new BufferedWriter(new FileWriter(destinationFolder() + destinationPrefix + "Any+Any NewTimes.txt"))) {
			int i = 0;
			boolean topTenOnly = Settings.settingValue("NewTopTenOnly", "0").equals("1");
			bw.write(String.format("%s -> %s", inputPrefix.substring(0, inputPrefix.length()-1), destinationPrefix.substring(0, destinationPrefix.length()-1)));
			bw.newLine();
			anybw.write(String.format("%s -> %s", inputPrefix.substring(0, inputPrefix.length()-1), destinationPrefix.substring(0, destinationPrefix.length()-1)));
			anybw.newLine();
			for (DataPoint dataPoint : racerTimes.getDataPoints()) {
				//System.out.println(Yasova.COURSE[dataPoint.courseId]);
				DataPoint oldPoint = (oldList.contains(dataPoint))?oldList.get(i++):new DataPoint(dataPoint);
				ArrayList<RaceTime> times = oldPoint.newData(dataPoint);
				if (topTenOnly) {
					for (int m = 0; m < times.size();m++) {
						if (times.get(m).placement > 10) {
							times.remove(m--);
						}
					}
				}
				if (times.size() > 0) {
					bw.write(String.format("%s: %s + %s",Yasova.COURSE[dataPoint.getCourseId()],Yasova.getCharacter(dataPoint.getCharacterId1()+1),Yasova.getCharacter(dataPoint.getCharacterId2()+1)));
					bw.newLine();
					for (RaceTime hoi : times) {
						bw.write(hoi.positionlesstoString());
						bw.newLine();
					}
					if (dataPoint.getCharacterId2() == -1 && dataPoint.getCharacterId1() == -1) {
						anybw.write(String.format("%s:",Yasova.COURSE[dataPoint.getCourseId()]));
						anybw.newLine();
						for (RaceTime hoi : times) {
							anybw.write(hoi.positionlesstoString());
							anybw.newLine();
						}
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
					if (dataPoint.getCharacterId2() == -1 && dataPoint.getCharacterId1() == -1) {
						if (oldTimes.size() > 0) {
							anybw.write("Removed:");
							anybw.newLine();
							for (RaceTime hoi : oldTimes) {
								anybw.write("-");
								anybw.write(hoi.positionlesstoString());
								anybw.newLine();
							}
						}
						anybw.newLine();
					}
					bw.newLine();
				}
			}
			bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Settings.settingValue("UpdatePrevious", "1").equals("1")) {
			Settings.setSettingValue("Previous", destinationPrefix);
		}
		}
		
	}
	
	public void outputCSV() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"TA-times.csv"))) {
			//percentile*100"%", racerName, course, capFirstLowerRest(character1), capFirstLowerRest(character2), minutes, seconds, miliseconds);
			bw.write("Racer,Course,Character 1,Character 2,Time");
			bw.newLine();
			for (DataPoint dataPoint: racerTimes.getDataPoints()) {
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
		for (DataPoint point:racerTimes.getDataPoints()) {
			if (!(point.getCharacterId1() == -1 || point.getCharacterId2() == -1)) {
				sortedList.add(point);
			}
		}
		sortedList.sort(new DataPointPolishComparator());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+destinationPrefix+"TopCombos.txt"))) {
			for(DataPoint point:sortedList) {
				bw.write(String.format("%d - %.2f%s: %s - %s + %s",Math.min(point.getDataCount(), polishMax) ,point.averagePercentile()*100,"%",point.getCourse(),point.getCharacter1(),point.getCharacter2()));
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outputSuzunaanFile() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"Suzunaan.txt"))) {
			for(DataPoint point:racerTimes.getDataPoints()) {
				for (RaceTime rt:point.data) {
					//System.out.println(rt.globalUniquePosition);
					bw.write(String.format("%d,%d,%d,%s,%s,%s,%d,%d,%d",point.getCharacterId1(),point.getCharacterId2(),point.getCourseId(),rt.racerName.replaceAll(",", ""),rt.character1,rt.character2,rt.miliTime(),rt.placement,rt.globalUniquePosition));
					bw.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String destinationFolder() {
		String output = SettingsFolder.programDataFolder() + destinationPrefix.substring(0, destinationPrefix.length()-1) + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
	private String targetFolder() {
		String output = SettingsFolder.programDataFolder() + targetPrefix.substring(0, targetPrefix.length()-1) + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
}
