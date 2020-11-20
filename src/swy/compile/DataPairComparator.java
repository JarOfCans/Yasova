package swy.compile;

import java.util.Comparator;

public class DataPairComparator implements Comparator<PairData> {

	@Override
	public int compare(PairData pair1, PairData pair2) {
		// TODO Auto-generated method stub
		return pair2.count - pair1.count;
	}

}
