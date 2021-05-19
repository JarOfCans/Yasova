package swy.compile.racers;

import java.util.ArrayList;
import java.util.Collections;

import swy.compile.NameSortComparable;
import swy.core.RaceTime;
import swy.yoink.Yasova;

public class RacerName implements Comparable<RacerName> {
	String name;
	ArrayList<RaceTime> times;
	RaceTime[] bestTimes;
	//ArrayList<RaceTime>[] courseTimes;
	
	
	public RacerName(String name) {
		this.name = name;
		times = new ArrayList<RaceTime>();
		bestTimes = new RaceTime[Yasova.COURSE.length];
	}
	
	public RacerName(RaceTime rt) {
		this.name = rt.racerName;
		times = new ArrayList<RaceTime>();
		bestTimes = new RaceTime[Yasova.COURSE.length];
		addTA(rt);
	}
	
	public void addTA(RaceTime input) {
		int course = input.course;
		if (bestTimes[course] == null || bestTimes[course].miliTime() > input.miliTime()) {
			bestTimes[course] = input;
		}
		times.add(input);
	}
	@Override
	public boolean equals(Object obj) {
		//System.out.println("Moo");
		if (obj instanceof String) {
			//System.out.println(obj + " + " + name + " = " + ((String) obj).equals(name));
			return ((String) obj).equals(name);
		}
		if (obj instanceof RacerName) {
			return ((RacerName)obj).name.equals(name);
		}
		return false;
	}
	
	public int totalTime() {
		int doom = 0;
		for (int i = 0; i < bestTimes.length; i++) {
			if (bestTimes[i] == null) {
				return 0;
			}
			doom += bestTimes[i].miliTime();
		}
		return doom;
	}
	
	public String timeName(int i) {
		String timeString = getTimeString();
		if (timeString != null)
			return String.format("%s - %s: %s, Average placement: %.2f%s", name, ordinal(i), timeString, averageSpot(), (allConfirmed())? "":"?");
		else {
			return name;
		}
	}
	public String getTimeString() {
		return getTimeString(totalTime());
		/*int total = totalTime();
		if (total == 0) {
			return null;
		}
		return String.format("%02d:%02d:%03d", total/60000,(total/1000)%60,total%1000);*/
	}
	public static String getTimeString(int input) {
		if (input == 0) {
			return null;
		}
		return String.format("%02d:%02d:%03d", input/60000,(input/1000)%60,input%1000);
	}
	public boolean allConfirmed() {
		for (int i = 0; i < bestTimes.length; i++) {
			RaceTime time = bestTimes[i];
			if (time == null || !time.perfectVision) {
				return false;
			}
		}
		return true;
	}
	public String raceOutput() {
		String output = "";
		Collections.sort(times, new NameSortComparable());
		for (RaceTime hoi: times) {
			output = String.format("%s%s%s", output, hoi.racerString(),System.lineSeparator());
		}
		return output;
	}
	public String topOutput() {
		String output = "";
		//Collections.sort(times, new NameSortComparable());
		for (int i = 0; i < bestTimes.length; i++) {
			RaceTime time = bestTimes[i];
			if (time != null) {
				output = String.format("%s%s%s", output, time.compactString(),System.lineSeparator());
			}
		}
		return output;
	}
	
	public float averageSpot() {
		int output = 0;
		int total = 0;
		for (int i = 0; i < bestTimes.length; i++) {
			RaceTime time = bestTimes[i];
			if (time != null) {
				total++;
				output += time.globalUniquePosition;
			}
		}
		return ((float) output )/ total;
	}
	/*public ArrayList<RaceTime> getAllCourse(int courseId) {
		ArrayList<RaceTime> output = new ArrayList<RaceTime>();
		for (RaceTime hoi: times) {
			if (hoi.course == courseId) {
				output.add(hoi);
			}
		}
		return output;
	}*/
	@Override
	public int compareTo(RacerName o) {
		int oTime = o.totalTime();
		int thisTotal = this.totalTime();
		if (thisTotal == 0 && oTime == 0) {
			return this.name.toLowerCase().compareTo(o.name.toLowerCase());
		}
		else if (thisTotal == 0) {
			return 1;
		}
		else if (oTime == 0) {
			return -1;
		}
		return thisTotal - oTime;
	}

	public void bake() {
		for (int i = 0; i < bestTimes.length; i++) {
			RaceTime time = bestTimes[i];
			if (time != null) {
				time.topTime = true;
			}
		}
	}
	
	public static String ordinal(int i) {
	    String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	    switch (i % 100) {
	    case 11:
	    case 12:
	    case 13:
	        return i + "th";
	    default:
	        return i + suffixes[i % 10];

	    }
	}
}
