package swy.compile;

import java.util.Comparator;

import swy.core.RaceTime;

public class NameSortComparable implements Comparator<RaceTime> {


	@Override
	public int compare(RaceTime arg0, RaceTime arg1) {
		int output = arg0.racerName.toLowerCase().compareTo(arg1.racerName.toLowerCase());
		//Sort by name, else XX.XXX% (See: Large number)
		return (int) ((output == 0)? (arg1.percentile - arg0.percentile)*100000:output);
	}

}
