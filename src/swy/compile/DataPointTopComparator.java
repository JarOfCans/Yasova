package swy.compile;

import java.util.Comparator;

public class DataPointTopComparator implements Comparator<DataPoint> {

	@Override
	public int compare(DataPoint o1, DataPoint o2) {
		if (o1.getDataCount() == 0 || o2.getDataCount() == 0) {
			return o2.getDataCount() - o1.getDataCount();
		}
		return o1.getData().get(0).miliTime() - o2.getData().get(0).miliTime();
	}

}
