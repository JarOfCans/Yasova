package swy.core;

import swy.yoink.Yasova;

public class RaceTime implements Comparable<RaceTime> {
	public String racerName;
	int minutes;
	int seconds;
	int miliseconds;
	int[] laps;
	public int placement;
	public String character1;
	public String character2;
	public int course;
	public int globalPosition;
	public int globalUniquePosition;
	public double percentile;
	public boolean perfectVision;
	public boolean topTime;
	
	public RaceTime(String input, int inputCourse) {
		laps = new int[3];
		dataParse(input);
		course = inputCourse;
		topTime = false;
	}
	
	private void dataParse(String input) {
		//System.out.println(input.length()<50?input:input.substring(0, 49));
		placement = Integer.parseInt(input.substring(4, input.indexOf(".")));
		input = input.substring(input.indexOf(".") + 2);
		racerName = input.substring(0, input.indexOf(" (0"));
		input = input.substring(input.indexOf(" (0") + 2);
		minutes = Integer.parseInt(input.substring(0, 2));
		input = input.substring(3);
		seconds = Integer.parseInt(input.substring(0, 2));
		input = input.substring(3);
		miliseconds = Integer.parseInt(input.substring(0, 3));
		input = input.substring(6);
		input = getLaps(input);
		character1 = input.substring(0, input.indexOf("/"));
		input = input.substring(input.indexOf("/") + 1);
		character2 = input;
	}
	private String getLaps(String input) {
		String tempinput = "";
		for (int i = 0; i <3; i++) {
			switch (i) {
			case 0:
			case 1:
				tempinput = input.substring(0, input.indexOf(',')).trim();
				break;
			case 2:
				tempinput = input.substring(0,input.indexOf('|')).trim();
				break;
			}
			//String tempinput = ((i>0)?input.substring(0, input.indexOf((i==2)? '|':',')):input).trim();
			if (tempinput.length() < 3) {
				break;
			}
				int tempMinutes = Integer.parseInt(tempinput.substring(input.indexOf('0'), 2));
				tempinput = input.substring(input.indexOf(':')+1);
				int tempSeconds = Integer.parseInt(tempinput.substring(0, 2));
				tempinput = tempinput.substring(3);
				int tempMili = Integer.parseInt(tempinput.substring(0, 3));
				laps[i] = miliTime(tempMinutes, tempSeconds, tempMili);
				if (i < 2) {
					input = input.substring(input.indexOf(',')+1);
				}
		}
		return input.substring(input.indexOf('|') + 2);
	}

	public int miliTime() {
		return miliseconds+seconds*1000+minutes*60000;
	}
	public static int miliTime(int minutes, int seconds, int miliseconds) {
		return miliseconds+seconds*1000+minutes*60000;
	}
	public String miliTimeString() {
		return String.format("%02d:%02d:%03d", minutes, seconds, miliseconds);
	}
	public static String miliTimeString(int miliTime) {
		return String.format("%02d:%02d:%03d", miliTime/60000, (miliTime/1000)%60000, miliTime%1000);
	}
	public static String miliTimeString(int minutes, int seconds, int miliseconds) {
		return String.format("%02d:%02d:%03d", minutes, seconds, miliseconds);
	}
	public int compareTo(RaceTime other) {
		//System.out.println(String.format("%02d:%02d:%03d :- %02d:%02d:%03d == %d",minutes, seconds, miliseconds, other.minutes, other.seconds, other.miliseconds, miliseconds+seconds*1000+minutes*60000 - (other.miliseconds+other.seconds*1000+other.minutes*60000)));
		return miliTime() - (other.miliTime());
	}
	
	public String toString() {
		return (globalPosition != 0)? String.format("%5s%s %-7s + %-7s #%02d (%02d:%02d:%03d) by %s", Integer.toString(globalPosition),(perfectVision)?".":"?", capFirstLowerRest(character1), capFirstLowerRest(character2), placement, minutes, seconds, miliseconds, racerName):
				positionlesstoString();
	}
	public String toCompressedString() {
		return String.format("%4s%s %-7s + %-7s (%02d:%02d:%03d) by %s", (globalUniquePosition == 0)?"":Integer.toString(globalUniquePosition),(perfectVision)?".":"?", capFirstLowerRest(character1), capFirstLowerRest(character2), minutes, seconds, miliseconds, racerName);
	}
	public String toCourseFileString() {
		return String.format("%5s %-7s + %-7s (%02d:%02d:%03d) by %s", Integer.toString(placement)+ ".", capFirstLowerRest(character1), capFirstLowerRest(character2), minutes, seconds, miliseconds, racerName);
	}
	public String toSmolCourseFileString() {
		return String.format("(%s) by %s", miliTimeString(), racerName);
	}
	public String positionlesstoString() {
		return String.format(" %-7s + %-7s #%02d (%02d:%02d:%03d) by %s", capFirstLowerRest(character1), capFirstLowerRest(character2), placement, minutes, seconds, miliseconds, racerName);
	}
	
	public String racerString() {
		return String.format(" %.2f%s %-7s + %-7s #%02d (%02d:%02d:%03d) on %s", percentile*100, "%", capFirstLowerRest(character1), capFirstLowerRest(character2), placement, minutes, seconds, miliseconds, Yasova.COURSE[course]);
	}

	public String compactString() {
		return String.format("%4s%s %-7s + %-7s (%02d:%02d:%03d) on %s", Integer.toString(globalUniquePosition),(perfectVision)?".":"?", capFirstLowerRest(character1), capFirstLowerRest(character2), minutes, seconds, miliseconds, Yasova.COURSE[course]);
	}
	public String racerAnyString() {
		return String.format(" %-7s + %-7s #%02d (%02d:%02d:%03d) on %s", capFirstLowerRest(character1), capFirstLowerRest(character2), placement, minutes, seconds, miliseconds, Yasova.COURSE[course]);
	}
	
	public String csvString() {
		return String.format("\"%s\",%s,%s,%s,%d,%d,%d,%d",racerName, Yasova.COURSE[course], capFirstLowerRest(character1), capFirstLowerRest(character2),laps[0],laps[1],laps[2], miliTime());
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
