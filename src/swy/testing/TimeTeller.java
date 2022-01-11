package swy.testing;

import java.util.ArrayList;

public class TimeTeller {
	ArrayList<String> names;
	ArrayList<Long> totalTimes;
	ArrayList<Integer> runs;
	ArrayList<Long> timeWait;
	public TimeTeller() {
		names = new ArrayList<String>();
		totalTimes = new ArrayList<Long>();
		runs = new ArrayList<Integer>();
		timeWait = new ArrayList<Long>();
	}
	
	public int newSlot(String name) {
		names.add(name);
		totalTimes.add((long) 0);
		timeWait.add((long) 0);
		runs.add(0);
		return totalTimes.size() - 1;
	}
	
	public long start(int slot) {
		long nanoTime = System.nanoTime();
		timeWait.set(slot, nanoTime);
		return nanoTime;
	}
	
	public long stop(int slot) {
		long nanoTime = System.nanoTime()- timeWait.get(slot);
		totalTimes.set(slot, totalTimes.get(slot) + nanoTime);
		runs.set(slot, runs.get(slot) + 1);
		return nanoTime - timeWait.get(slot);
	}
	
	public void print() {
		for (int i = 0; i < names.size(); i++) {
			if (runs.get(i) > 0) {
				System.out.printf("%s; runs: %d; totalTime: %d; average: %d%s", names.get(i), runs.get(i), totalTimes.get(i), totalTimes.get(i)/(long)runs.get(i), System.lineSeparator());
			}
		}
	}
}
