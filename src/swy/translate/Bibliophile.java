package swy.translate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import drafterdat.settings.SettingsFolder;

public class Bibliophile {
	private JPTranslation translator;
	private String prefix;
	public Bibliophile(String prefix) {
		translator = new JPTranslation();
		this.prefix = prefix;
		
		
		// TODO translatorThanks
		//writeTranslatorThanks();
		translateMainLeaderboard();
		translateRacerTimes();
		translateCourseTimes();
		translateComboCount();
		translateNewTimes();
		translatePollishedLeaderboards();
	}
	
	
	private void translateMainLeaderboard() {
		//TODO translate file name
		try {
			BufferedReader br = new BufferedReader(new FileReader(prefixFolder()+prefix+"TA-Leaderboards.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolderJP()+prefix+translator.translateAll("TA-Leaderboards.txt")));
			String nextLine;
			while ((nextLine = br.readLine()) != null) {
				//System.out.println(nextLine);
				if (nextLine.length() == 0 || nextLine.equals("    1. No Data")) {
					bw.write(translator.translateAll(nextLine));
					bw.newLine();
				}
				else if (nextLine.charAt(0) == ' ') {
					int index = nextLine.indexOf(" | ");
					if (index == -1) {
						System.out.println(nextLine);
					}
					bw.write(nextLine.substring(0, index));
					bw.write(translator.translateAll(nextLine.substring(index)));
					bw.newLine();
				}
				else {
					bw.write(translator.translateAll(nextLine));
					bw.newLine();
				}
			}
			
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IOException Occured in reading TA-leaderboards. Translation halted.");
			return;
		}
	}
	
	private void translateRacerTimes() {
		//TODO translate file name
		try {
			BufferedReader br = new BufferedReader(new FileReader(prefixFolder()+prefix+"RacerTimes.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolderJP()+prefix+translator.translateAll("RacerTimes.txt")));
			String nextLine;
			while ((nextLine = br.readLine()) != null) {
				//System.out.println(nextLine);
				if (nextLine.length() == 0) {
					bw.newLine();
				}
				else if (nextLine.charAt(0) == ' ') {
					bw.write(translator.translateAll(nextLine));
					bw.newLine();
				}
				else {
					bw.write(nextLine);
					bw.newLine();
				}
			}
			
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IOException Occured in reading RacerTimes. Translation halted.");
			return;
		}
	}
	
	private void translateCourseTimes() {
		//TODO translate file name
		try {
			BufferedReader br = new BufferedReader(new FileReader(prefixFolder()+prefix+"CourseTimes.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolderJP()+prefix+translator.translateAll("CourseTimes.txt")));
			String nextLine;
			while ((nextLine = br.readLine()) != null) {
				//System.out.println(nextLine);
				if (nextLine.length() == 0) {
					bw.newLine();
				}
				else if (nextLine.charAt(0) == ' ') {
					int index = nextLine.indexOf("by");
					if (index == -1) {
						System.out.println(nextLine);
					}
					bw.write(translator.translateAll(nextLine.substring(0, index+2)));
					bw.write(nextLine.substring(index+2));
					bw.newLine();
				}
				else {
					bw.write(nextLine);
					bw.newLine();
				}
			}
			
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IOException Occured in reading CourseTimes. Translation halted.");
			return;
		}
	}
	
	private void translateComboCount() {
		//TODO translate file name
		try {
			BufferedReader br = new BufferedReader(new FileReader(prefixFolder()+prefix+"CharacterComboCount.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolderJP()+prefix+translator.translateAll("CharacterComboCount.txt")));
			String nextLine;
			while ((nextLine = br.readLine()) != null) {
				//System.out.println(nextLine);
				if (nextLine.length() == 0 ) {
					bw.newLine();
				}
				else {
					bw.write(translator.translateAll(nextLine));
					bw.newLine();
				}
			}
			
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IOException Occured in reading CharacterComboCount. Translation halted.");
			return;
		}
	}
	
	private void translateNewTimes() {
		//TODO translate file name
		try {
			BufferedReader br = new BufferedReader(new FileReader(prefixFolder()+prefix+"NewTimes.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolderJP()+prefix+translator.translateAll("NewTimes.txt")));
			String nextLine;
			while ((nextLine = br.readLine()) != null) {
				//System.out.println(nextLine);
				if (nextLine.length() == 0) {
					bw.newLine();
				}
				else if (nextLine.charAt(0) == ' ' || nextLine.charAt(0) == '-') {
					int index = nextLine.indexOf("by");
					if (index == -1) {
						System.out.println(nextLine);
					}
					bw.write(translator.translateAll(nextLine.substring(0, index+2)));
					bw.write(nextLine.substring(index+2));
					bw.newLine();
				}
				else {
					bw.write(translator.translateAll(nextLine));
					bw.newLine();
				}
			}
			
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IOException Occured in reading NewTimes. Translation halted.");
			return;
		}
	}
	
	private void translatePollishedLeaderboards() {
		//TODO translate file name
		try {
			BufferedReader br = new BufferedReader(new FileReader(prefixFolder()+prefix+"PolishedLeaderboards.txt"));
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefixFolderJP()+prefix+translator.translateAll("PolishedLeaderboards.txt")));
			String nextLine;
			while ((nextLine = br.readLine()) != null) {
				//System.out.println(nextLine);
				if (nextLine.length() == 0 ) {
					bw.newLine();
				}
				else {
					bw.write(translator.translateAll(nextLine));
					bw.newLine();
				}
			}
			
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("IOException Occured in reading PollishedLeaderboards. Translation halted.");
			return;
		}
	}
	
	private String prefixFolder() {
		String output = SettingsFolder.programDataFolder() + prefix.substring(0, prefix.length()-1) + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
	

	private String prefixFolderJP() {
		// TODO Translate output folder
		String output = SettingsFolder.programDataFolder() + prefix.substring(0, prefix.length()-1) + "JP" + "\\";
		SettingsFolder.prepFolder(output);
		return output;
	}
}
