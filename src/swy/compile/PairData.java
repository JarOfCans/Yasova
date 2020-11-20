package swy.compile;

import swy.yoink.Yasova;

public class PairData {
	int char1;
	int char2;
	int count;
	public PairData(int id1, int id2, int value) {
		char1 = id1;
		char2 = id2;
		count = value;
	}
	
	public String toString() {
		return String.format("%s + %s: %d",Yasova.QUICKCHAR[char1 + 1],Yasova.QUICKCHAR[char2 + 1], count);
	}
}
