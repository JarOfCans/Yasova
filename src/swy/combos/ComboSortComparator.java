package swy.combos;

import java.util.Comparator;

import swy.yoink.Yasova;

public class ComboSortComparator implements Comparator<Combo> {

	@Override
	public int compare(Combo o1, Combo o2) {
		/*return (o1.times.size() == o1.times.size())?
				((o1.times.size() == Yasova.COURSE.length)?
						o2.getTotalTime() - o1.getTotalTime():
						(int)(o1.getAveragePercentage()*10000 - o2.getAveragePercentage()*10000)):
				o1.times.size() - o2.times.size();*/
		return (int)(o2.getAveragePercentage()*10000 - o1.getAveragePercentage()*10000);
	}

}