package swy.core;

import swy.yoink.Yasova;

public class RaceTime implements Comparable<RaceTime> {
	public String racerName;
	int minutes;
	int seconds;
	int miliseconds;
	public int placement;
	public String character1;
	public String character2;
	public int course;
	public int globalPosition;
	public double percentile;
	
	public RaceTime(String input, int inputCourse) {
		dataParse(input);
		course = inputCourse;
	}
	
	private void dataParse(String input) {
		//System.out.println(input);
		placement = Integer.parseInt(input.substring(4, input.indexOf(".")));
		input = input.substring(input.indexOf(".") + 2);
		racerName = input.substring(0, input.indexOf(" (0") - 1);
		input = input.substring(input.indexOf(" (0") + 2);
		minutes = Integer.parseInt(input.substring(0, 2));
		input = input.substring(3);
		seconds = Integer.parseInt(input.substring(0, 2));
		input = input.substring(3);
		miliseconds = Integer.parseInt(input.substring(0, 3));
		//System.out.println(input);
		input = input.substring(input.indexOf("|") + 2);
		character1 = input.substring(0, input.indexOf("/"));
		input = input.substring(input.indexOf("/") + 1);
		character2 = input;
	}
	public int miliTime() {
		return miliseconds+seconds*1000+minutes*60000;
	}

	public int compareTo(RaceTime other) {
		//System.out.println(String.format("%02d:%02d:%03d :- %02d:%02d:%03d == %d",minutes, seconds, miliseconds, other.minutes, other.seconds, other.miliseconds, miliseconds+seconds*1000+minutes*60000 - (other.miliseconds+other.seconds*1000+other.minutes*60000)));
		return miliTime() - (other.miliTime());
	}
	
	public String toString() {
		return (globalPosition != 0)? String.format("%5s. %-7s + %-7s #%02d (%02d:%02d:%03d) by %s", Integer.toString(globalPosition), capFirstLowerRest(character1), capFirstLowerRest(character2), placement, minutes, seconds, miliseconds, racerName):
				positionlesstoString();
	}
	
	public String positionlesstoString() {
		return String.format(" %-7s + %-7s #%02d (%02d:%02d:%03d) by %s", capFirstLowerRest(character1), capFirstLowerRest(character2), placement, minutes, seconds, miliseconds, racerName);
	}
	
	public String racerString() {
		return String.format(" %.2f%s %-7s + %-7s #%02d (%02d:%02d:%03d) on %s", percentile*100, "%", capFirstLowerRest(character1), capFirstLowerRest(character2), placement, minutes, seconds, miliseconds, Yasova.COURSE[course]);
	}
	
	public String csvString() {
		return String.format("\"%s\",%s,%s,%s,%02d:%02d.%03d",racerName, Yasova.COURSE[course], capFirstLowerRest(character1), capFirstLowerRest(character2), minutes, seconds, miliseconds);
	}
	
	public boolean equals(Object input) {
		RaceTime hoi;
		if (!(input instanceof RaceTime)) {
			return false;
		}
		else {
			hoi = (RaceTime) input;
		}
		
		return (/*hoi.racerName.equals(racerName)&&*/hoi.miliTime() == miliTime()&&hoi.character1.equals(character1)&&hoi.character2.equals(character2)&&hoi.course==course);
	}
	
	public static String capFirstLowerRest(String input) {
		return input.substring(0, 1) + input.substring(1).toLowerCase();
	}
}
