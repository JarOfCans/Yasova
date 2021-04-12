package swy.test;

import swy.compile.PairData;
import swy.compile.racers.RacerBatch;

public class YasovaTest {
	public static void main(String[] args) {
		int i = 0;
		for (int x = 0; x < 22; x++) {
			for (int y = x; y < 22; y++) {
				pTest(RacerBatch.combinedIndex(22, x, y), i++);
			}
		}
	}
	
	
	public static void pTest(Object input1, Object input2) {
		System.out.printf("%s = %s%s", input1.toString(), input2.toString(),System.lineSeparator());
	}
	
	public static int intendedId(int x, int y) {
		if (x == 0) {
			return y;
		}
		return intendedId(x-1, x-1) + y - x + 23 - x;
	}
}
