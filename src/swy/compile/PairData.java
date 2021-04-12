package swy.compile;

import swy.yoink.Yasova;

public class PairData {
	int char1;
	int char2;
	int count;
	boolean hasMax = false;
	public PairData(int id1, int id2, int value) {
		char1 = id1;
		char2 = id2;
		count = value;
		//System.out.println(idString());
	}
	public int char1() {
		return char1;
	}
	public int char2() {
		return char2;
	}
	public void increment() {
		count++;
	}
	public void add(int i) {
		count += i;
	}
	public void maxNoted() {
		hasMax = true;
	}
	
	public String idString() {
		return String.format("%d + 1 + %d + 1: %d",char1,char2, count);
	}
	public String toString() {
		return String.format("%s + %s: %d%s",Yasova.QUICKCHAR[char1 + 1],Yasova.QUICKCHAR[char2 + 1], count, (hasMax)?"+":"");
	}
}
