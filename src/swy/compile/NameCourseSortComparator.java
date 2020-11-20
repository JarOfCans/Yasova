package swy.compile;

import java.util.Comparator;

import swy.core.RaceTime;

public class NameCourseSortComparator implements Comparator<RaceTime> {


	@Override
	public int compare(RaceTime arg0, RaceTime arg1) {
		System.out.println("Called");
		int output = arg0.racerName.compareTo(arg1.racerName);
		int coursedif = arg0.course - arg1.course;
		//Sort by name, else XX.XXX% (See: Large number)
		if (output != 0) {
			return output;
		}
		else if (coursedif != 0) {
			return coursedif;
		}
		else {
			return (int)((arg1.percentile - arg0.percentile)*100000);
		}
		
	}

}
