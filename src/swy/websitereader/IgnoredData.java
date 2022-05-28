package swy.websitereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import drafterdat.settings.SettingsFolder;

public class IgnoredData {
	ArrayList<String> times;
	ArrayList<String> courses;
	ArrayList<String> combos;
	ArrayList<String> comments;
	ArrayList<Integer> miliTimes;
	public IgnoredData() {
		
		times = new ArrayList<String>();
		courses = new ArrayList<String>();
		combos = new ArrayList<String>();
		comments = new ArrayList<String>();
		miliTimes = new ArrayList<Integer>();
		
		String nextLine;
		int firstIndex;
		int lastIndex;
		int commentIndex;
		File file = new File(SettingsFolder.programDataFolder()+"YasovaSettings/IgnoredTimes.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (BufferedReader br = Files.newBufferedReader(Paths.get(SettingsFolder.programDataFolder()+"YasovaSettings/IgnoredTimes.txt"), StandardCharsets.UTF_8)) {
			
			while ((nextLine = br.readLine())!= null && nextLine.length() > 0) {
				firstIndex = nextLine.indexOf(",");
				lastIndex = nextLine.lastIndexOf(",");
				commentIndex = nextLine.lastIndexOf(";");
				times.add(nextLine.substring(0, firstIndex));
				courses.add(nextLine.substring(firstIndex+1, lastIndex));
				combos.add(nextLine.substring(lastIndex+1, commentIndex));
				comments.add(nextLine.substring(commentIndex + 1));
			}
			
		} catch (IOException e) {
			System.err.print(e);
		}
		System.out.println("Times: " + times.toString());
		System.out.println("Courses: " + courses.toString());
		System.out.println("Combos: " + combos.toString());
	}
	/**
	 * 
	 * @param time
	 * @param course
	 * @param combo
	 * @return
	 */
	public boolean isValid(String time, String course, String combo) {
		for (int i = 0; i < times.size(); i++) {
			//System.out.println(time + " ?= " + times.get(i));
			//System.out.println(course + " ?= " + courses.get(i));
			//System.out.println(combo + " ?= " + combos.get(i));
			if (time.equals(times.get(i))&&course.equals(courses.get(i))&&combo.equals(combos.get(i))) {
				return false;
			}
		}
		return true;
	}
}
