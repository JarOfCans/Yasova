package swy.compile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import drafterdat.settings.Settings;
import drafterdat.settings.SettingsFolder;
import swy.compile.racers.RacerBatch;
import swy.core.RaceTime;
import swy.websitereader.IgnoredData;
import swy.yoink.RunYasova;
import swy.yoink.Yasova;

public class DataRead {
	private String targetPrefix;
	private String destinationPrefix;
	public static int polishMax;
	ArrayList<RaceTime> droppedTimes;
	RacerBatch racerTimes;
	IgnoredData ignoredData;
	
	public static void main(String[] args) throws InterruptedException {
		RunYasova.main(new String[0]);
	}
	
	public DataRead(String destination) {
		racerTimes = new RacerBatch();
		ignoredData = new IgnoredData();
		droppedTimes = new ArrayList<RaceTime>();
		destinationPrefix = destination;
		targetPrefix = Settings.settingValue("OverrideReadLocation", "");
		if (targetPrefix.length() < 1) {
			targetPrefix = destinationPrefix;
		}
		polishMax = Integer.parseInt(Settings.settingValue("AveragePercentileCount", "10"));
		try (BufferedReader br = Files.newBufferedReader(Paths.get(targetFolder()+"TA-Leaderboards.txt"), StandardCharsets.UTF_8)) {
			String nextLine;
			DataPoint lastDataPoint = null;
			int removed = 0;
			while ((nextLine = br.readLine()) != null) {
				if (nextLine.length() > 0 && !(Character.isDigit(nextLine.charAt(0))) && !(nextLine.equals("    1. No Data"))) {
					if (nextLine.charAt(0) == ' ') {
						RaceTime valid = lastDataPoint.addData(nextLine, ignoredData);
						valid.placement -= removed;
						if (!valid.valid) {
							removed++;
							if (lastDataPoint.getCharacterId1() != -1 && lastDataPoint.getCharacterId2() != -1) {
								droppedTimes.add(valid);
							}
						}
					}
					else {
						lastDataPoint = new DataPoint(nextLine);
						racerTimes.addDataPoint(lastDataPoint);
						removed = 0;
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
		//outputCourseTimes();
		outputCourseTimesFiles();
		outputRacerTimes();
		outputCompactRacerTimes();
		outputCompactCourseTimes();
		outputCompactComboTimes();
		outputTopCombos();
		//outputRacerCourseTimes(dataPointList);
		createNewTimes();
		outputCSV();
		//outputLeastPolished();
		racerAnyTimes();
		outputSuzunaanFile();
		outputReadMe();
		outputDropped();
		
		//outputBabyYasova();
		System.out.println("DataRead done");
		//System.out.println(System.currentTimeMillis() - time);
	}
	
	
	public void outputDataPair() {
		//int charCount = Yasova.QUICKCHAR.length - 1;
		ArrayList<PairData> dataPairList = racerTimes.getPairData();
		Collections.sort(dataPairList, new DataPairComparator());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"ComboCount.txt"))) {
			for (PairData pair: dataPairList) {
				bw.write(pair.toString());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outputCourseTimes() {
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"CourseTimes.txt"))) {
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
	
	public void outputCourseTimesFiles() {

		int i = 0;
		SettingsFolder.prepFolder(destinationFolder() + "CourseTimes\\");
		for (ArrayList<RaceTime> timeArray: racerTimes.getAllCourses()) {
			//System.out.println(Yasova.COURSE_PROPER[i]);
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder() + "CourseTimes\\"+Yasova.COURSE_PROPER[i]+".txt"))) {
			//Collections.sort(timeArray);
				//bw.write("Ur Mom Gae");
				bw.write(Yasova.COURSE[i]);
				bw.newLine();
				bw.newLine();
				bw.write("Top 10 —");
				bw.newLine();
				final int courseID = i;
				List<RaceTime> points = racerTimes.getDataPoints().stream().filter(t -> t.getCourseId() == courseID && t.getCharacterId1() == -1 && t.getCharacterId2() == -1).collect(Collectors.toList()).get(0).data;
				boolean doMax = false;
				for (int n = 0; n < 10 && points.size() > n; n++) {
					//racetime.globalPosition = n++;
					//bw.write(String.format("%4s.",Integer.toString(n+1)));
					writeLine(bw, points.get(n).toCourseFileString());
					if (n == 9) {
						doMax = true;
					}
				}
				if (doMax) {
					writeLine(bw, " ...");
					//bw.newLine();
					writeLine(bw, points.get(points.size()-1).toCourseFileString());
				}
				bw.newLine();
				bw.newLine();
				writeLine(bw, "Characters —");

				int bestMilitime = points.get(0).miliTime();
				for (int c = 1; c < Yasova.QUICKCHAR.length; c++) {
					final int j = c;
					List<DataPoint> dps = racerTimes.getDataPoints().stream().filter(t -> t.getCourseId() == courseID && (t.getCharacterId1() == j-1 || t.getCharacterId2() == j-1)).collect(Collectors.toList());
					writeLine(bw, "-"+Yasova.QUICKCHAR[j]+":");
					int bestCharMilitime=0;
					for (DataPoint hoidp: dps) {
						if (hoidp.getCharacter2().equals("Any")) {
							bestCharMilitime = hoidp.data.get(0).miliTime();
						} else {
							bw.write("   +");
							bw.write(String.format("%-8s", ((hoidp.getCharacterId1() == j-1)?hoidp.getCharacter2():hoidp.getCharacter1())+":"));
							try {
								writeLine(bw, hoidp.data.get(0).toSmolCourseFileString());
								writeLine(bw, "     WR +"
										+RaceTime.miliTimeString(hoidp.data.get(0).miliTime()-bestMilitime) + ", CR +"
										+RaceTime.miliTimeString(hoidp.data.get(0).miliTime()-bestCharMilitime));
								//writeLine(bw, "      vs WR: +"+RaceTime.miliTimeString(hoidp.data.get(0).miliTime()-bestMilitime));
								//writeLine(bw, "      vs CR: +"+RaceTime.miliTimeString(hoidp.data.get(0).miliTime()-bestCharMilitime));
							} catch (IndexOutOfBoundsException e) {
								writeLine(bw, "No data");
							}
						}
					}
				}
					
				bw.newLine();
				bw.write("All times:");
				bw.newLine();
				//int n = 1;
				for (RaceTime racetime : timeArray) {
					//racetime.globalPosition = n++;
					bw.write(racetime.toCompressedString());
					bw.newLine();
				}
				bw.close();
				i++;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	
	
	public void outputRacerTimes() {
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"RacerTimes.txt"))) {
			racerTimes.generateOutput(bw);
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void outputCompactCourseTimes() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"CompactCourseTimes.txt"))) {
			int i = 0;
			for (ArrayList<RaceTime> timeArray: racerTimes.getAllCourses()) {
				//Collections.sort(timeArray);
				bw.newLine();
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
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"CompactRacerTimes.txt"))) {
			racerTimes.generateCompactOutput(bw);
			
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void outputCompactComboTimes() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"ComboTimes.txt"))) {
			racerTimes.generateCombosBest(bw);
			
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
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"RacerTop100Times.txt"))) {
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
			System.out.println(SettingsFolder.programDataFolder()+inputPrefix + "\\"+"TA-Leaderboards.txt");
		//Get a copy of the old data
		try (BufferedReader br = Files.newBufferedReader(Paths.get(SettingsFolder.programDataFolder()+inputPrefix + "\\"+"TA-Leaderboards.txt"), StandardCharsets.UTF_8)) {
			String nextLine;
			DataPoint lastDataPoint = null;
			while ((nextLine = br.readLine()) != null) {
				//System.out.println(nextLine.length());
				if (nextLine.length() > 0 && !(Character.isDigit(nextLine.charAt(0))) && !(nextLine.equals("    1. No Data"))) {
					if (nextLine.charAt(0) == ' ') {
						lastDataPoint.addData(nextLine, ignoredData);
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
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"NewTimes.txt"))) {
			try (BufferedWriter anybw = new BufferedWriter(new FileWriter(destinationFolder() +"Any+Any NewTimes.txt"))) {
			int i = 0;
			boolean topTenOnly = Settings.settingValue("NewTopTenOnly", "0").equals("1");
			bw.write(String.format("%s -> %s", removeYasovaDrop(inputPrefix), removeYasovaDrop(destinationPrefix)));
			bw.newLine();
			anybw.write(String.format("%s -> %s", removeYasovaDrop(inputPrefix), removeYasovaDrop(destinationPrefix)));
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
				ArrayList<RaceTime> oldTimes = oldPoint.oldData(dataPoint);
				if (times.size() > 0 || oldTimes.size() > 0) {
					bw.write(String.format("%s: %s + %s",Yasova.COURSE[dataPoint.getCourseId()],Yasova.getCharacter(dataPoint.getCharacterId1()+1),Yasova.getCharacter(dataPoint.getCharacterId2()+1)));
					bw.newLine();
					if (times.size() == 0) {
						bw.write(" No new times");
						bw.newLine();
					}
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
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Settings.settingValue("UpdatePrevious", "1").equals("1")) {
			Settings.setSettingValue("Previous", destinationPrefix);
		}
		}
		
	}
	
	public void outputCSV() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"TA-times.csv"))) {
			//percentile*100"%", racerName, course, capFirstLowerRest(character1), capFirstLowerRest(character2), minutes, seconds, miliseconds);
			bw.write("Racer,Course,Character 1,Character 2,Time");
			bw.newLine();
			for (DataPoint dataPoint: racerTimes.getDataPoints()) {
				if (dataPoint.getCharacterId1() != -1 && dataPoint.getCharacterId2() != -1) {
				for (RaceTime raceTime: dataPoint.data) {
					bw.write(raceTime.csvString());
					bw.newLine();
				}
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
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"Old-TopCombos.txt"))) {
			bw.write("Max amount used in calculations: " + polishMax);
			bw.newLine();
			for(DataPoint point:sortedList) {
				bw.write(String.format("%d - %.2f%s: %s - %s + %s",Math.min(point.getDataCount(), polishMax) ,point.averagePercentile()*100,"%",point.getCourse(),point.getCharacter1(),point.getCharacter2()));
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void outputTopCombos() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"CompactComboTimes.txt"))) {
			racerTimes.generateCombosCompact(bw);
			
			bw.close();
			
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
	public void outputDropped() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder() + "DroppedTimes.txt"))) {
			for (RaceTime hoi: droppedTimes) {
					writeLine(bw, hoi.csvString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void outputReadMe() {
		// TODO update read me
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFolder()+"ReadMe.txt"))) {
			writeLine(bw, "Yasova is a manually run program that reads the skydrift TA rankings and processes them into files.");
			bw.newLine();
			writeLine(bw, "Note that the program can't differentiate between names that are the same,");
			writeLine(bw, "and thus treat them as the same person, even if they are not alts");
			bw.newLine();
			writeLine(bw, "The '*' symbol marks the files where if there are racers or alts with the same name,");
			writeLine(bw, "the slower times are ignored");
			bw.newLine();
			writeLine(bw, "Placements with '###.' times means that the placement is known, whereas '###?' means that it's");
			writeLine(bw, "possible that there are others before it that are not on the leaderboard website.");
			bw.newLine();
			writeLine(bw, "The program cannot access times beyond the #100 rank on the character/character leaderboards,");
			writeLine(bw, "which may become relevant on popular combos.");
			bw.newLine();
			writeLine(bw, "Notepad ++ is heavely encouraged when reading these files, although obviously not mandatory");
			bw.newLine();
			bw.newLine();
			writeLine(bw, "Below are the files included:");
			writeLine(bw, "CourseTimes (Folder):");
			writeLine(bw, "    Folder that has each course's top 10 times and #100 time,");
			writeLine(bw, "    each character combo best time and their world record and character record comparison,");
			writeLine(bw, "    and all times. Racers with times without placements here have a faster time.");
			writeLine(bw, "TA-Leaderboards:");
			writeLine(bw, "    The normal leaderboards as writen on the website.");
			writeLine(bw, "Any+Any NewTimes:");
			writeLine(bw, "    See new times file, but only for leaderboards that ignore characters (aka any/any).");
			writeLine(bw, "ComboCount:");
			writeLine(bw, "    Fun file that counts how many records of each combo there are.");
			writeLine(bw, "    \"+\" indicates that there could be and likely are more than could be found.");
			writeLine(bw, "ComboTimes:");
			writeLine(bw, "    Posts the best time on each course for each combo.");
			writeLine(bw, "    Ranks based on the average of each record's \"WR/PR\" (World record time / That time.)");
			writeLine(bw, "CompactComboTimes:");
			writeLine(bw, "    Sorts the Anys, Double Characters, and all the (non-Any) rankings of each combo.");
			writeLine(bw, "    Ranks based on the average of each record's \"WR/PR\" (World record time / That time.)");
			writeLine(bw, "CompactCourseTimes:*");
			writeLine(bw, "    A reprocessed any/any leaderboard for each course. Goes through ALL known times.");
			writeLine(bw, "CompactRacerTimes:*");
			writeLine(bw, "    Show's each racer's best time on each course.");
			writeLine(bw, "    Ranks all racers with all courses based on total time, then the rest alphabetically.");
			writeLine(bw, "DroppedTimes.txt:");
			writeLine(bw, "    Shows all times removed from the analysis. There are updated manually.");
			writeLine(bw, "    If another file is weird/doesn't add up to 100, check this file to see if a time is here.");
			writeLine(bw, "New Times:");
			writeLine(bw, "    Shows ALL new times on each course and combo based on the time frame in the first line.");
			writeLine(bw, "    Uses JP time, with the first date in the top line inclusivly and the second date exclusivly.");
			writeLine(bw, "RacerTimes:");
			writeLine(bw, "    Shows ALL times for each racer, sorted by name.");
			writeLine(bw, "RacerTop100Times:");
			writeLine(bw, "    Shows ALL times for each racer that make top 100 on the any/any leaderboard.");
			writeLine(bw, "    Notably does not ignore duplicate names.");
			writeLine(bw, "TA-times.csv:");
			writeLine(bw, "    CSV file (spreadsheet file) of the times.");/*
			writeLine(bw, "TopCombos or Old-TopCombos:");
			writeLine(bw, "    Should be dead, if it still exists someone asked for it. Calculates the WR/PR of the top X times,");
			writeLine(bw, "    where X is the number given at the top of the document");*/
			writeLine(bw, "Suzunaan:");
			writeLine(bw, "    Used for another program. You can ignore it unless it ends up being released");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String destinationFolder() {
		String output = SettingsFolder.programDataFolder() + destinationPrefix + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
	private String targetFolder() {
		String output = SettingsFolder.programDataFolder() + targetPrefix + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
	public static String removeYasovaDrop(String input) {
		return (input.length()>11 && input.substring(0, 11).equals("YasovaDrop-"))?input.substring(11):input;
	}
	
	public static void writeLine(BufferedWriter bw, String line) throws IOException {
		bw.write(line);
		bw.newLine();
	}
}
