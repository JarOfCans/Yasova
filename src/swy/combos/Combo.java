package swy.combos;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import swy.compile.DataPoint;
import swy.compile.racers.RacerName;
import swy.core.RaceTime;
import swy.yoink.Yasova;

public class Combo {
	public int charId1, charId2;
	public ArrayList<RaceTime> times;
	int totalTime;
	double averagePercentage;
	boolean totalKnown;
	
	public Combo(DataPoint input) {
		times = new ArrayList<RaceTime>();
		totalKnown = false;
		charId1 = input.getCharacterId1();
		charId2 = input.getCharacterId2();
		times.add(input.getData().get(0));
	}
	
	public void add(DataPoint input) {
		totalKnown = false;
		times.add(input.getData().get(0));
	}
	
	public int getId() {
		return comboID(charId1, charId2);
	}
	
	public int getTotalTime() {
		return (totalKnown)? totalTime:calcTotalTime();
	}
	public double getAveragePercentage() {
		if (!totalKnown)
			calcTotalTime();
		return averagePercentage;
	}
	public int calcTotalTime() {
		totalTime = 0;
		averagePercentage = 0.0;
		for (RaceTime rt:times) {
			totalTime += rt.miliTime();
			averagePercentage += rt.percentile;
		}
		averagePercentage /= times.size();
		totalKnown = true;
		return totalTime;
	}
	
	public void writeAny(BufferedWriter bw, int i) throws IOException {
		//System.out.println(i);
		//String comboString = String.format("%s/%s", times.get(0).character1, times.get(0).character2);
		if (times.size() != Yasova.COURSE.length) {
			bw.write(String.format("%s: %s - ( %02d/%d ), Average WR/PR = %.2f%%",RacerName.ordinal(i),String.format("%s + %s", Yasova.QUICKCHAR[charId1+1], Yasova.QUICKCHAR[charId2+1]), times.size(), Yasova.COURSE.length, getAveragePercentage()*100));
			bw.newLine();
		}
		else {
			bw.write(String.format("%s: %s - %s, Average WR/PR = %.2f%%",RacerName.ordinal(i),String.format("%s + %s", Yasova.QUICKCHAR[charId1+1], Yasova.QUICKCHAR[charId2+1]), RacerName.getTimeString(getTotalTime()), getAveragePercentage()*100));
			bw.newLine();
		}
		boolean done;
		for (int j = 0; j < Yasova.COURSE.length; j++) {
			done = false;
			for (RaceTime time:times) {
				if (time.course == j) {
				bw.write(String.format(" %-10s %s %-9s w/ %-8sby %s", String.format("%s:", Yasova.SMOL_COURSE[j]), RacerName.getTimeString(time.miliTime()), String.format("(%.2f%%)", time.percentile*100), (Yasova.QUICKCHAR[charId1+1].equals(RaceTime.capFirstLowerRest(time.character1)))?RaceTime.capFirstLowerRest(time.character2):RaceTime.capFirstLowerRest(time.character1),time.racerName));
				bw.newLine();
				done = true;
				break;
				}
			}
			if(done) {
				continue;
			}
			bw.write(String.format(" %-10s -", String.format("%s:", Yasova.SMOL_COURSE[j])));
			bw.newLine();
		}
	}
	
	public void write(BufferedWriter bw, int i) throws IOException {
		//System.out.println(i);
		//String comboString = String.format("%s/%s", times.get(0).character1, times.get(0).character2);
		if (times.size() != Yasova.COURSE.length) {
			bw.write(String.format("%s: %s - ( %02d/%d ), Average WR/PR = %.2f%%",RacerName.ordinal(i),String.format("%s + %s", Yasova.QUICKCHAR[charId1+1], Yasova.QUICKCHAR[charId2+1]), times.size(), Yasova.COURSE.length, getAveragePercentage()*100));
			bw.newLine();
		}
		else {
			bw.write(String.format("%s: %s - %s, Average WR/PR = %.2f%%",RacerName.ordinal(i),String.format("%s + %s", Yasova.QUICKCHAR[charId1+1], Yasova.QUICKCHAR[charId2+1]), RacerName.getTimeString(getTotalTime()), getAveragePercentage()*100));
			bw.newLine();
		}
		boolean done;
		for (int j = 0; j < Yasova.COURSE.length; j++) {
			done = false;
			for (RaceTime time:times) {
				if (time.course == j) {
				bw.write(String.format(" %-10s %s %-9s by %s", String.format("%s:", Yasova.SMOL_COURSE[j]), RacerName.getTimeString(time.miliTime()), String.format("(%.2f%%)", time.percentile*100), time.racerName));
				bw.newLine();
				done = true;
				break;
				}
			}
			if(done) {
				continue;
			}
			bw.write(String.format(" %-10s -", String.format("%s:", Yasova.SMOL_COURSE[j])));
			bw.newLine();
		}
	}
	public void compactWrite(BufferedWriter bw, int i) throws IOException {
		if (times.size() != Yasova.COURSE.length) {
			bw.write(String.format("%5s: %s - (%02d/%d),   Average WR/PR = %.2f%%",RacerName.ordinal(i),String.format("%-7s + %-7s", Yasova.QUICKCHAR[charId1+1], Yasova.QUICKCHAR[charId2+1]), times.size(), Yasova.COURSE.length, getAveragePercentage()*100));
			bw.newLine();
		}
		else {
			bw.write(String.format("%5s: %s - %s, Average WR/PR = %.2f%%",RacerName.ordinal(i),String.format("%-7s + %-7s", Yasova.QUICKCHAR[charId1+1], Yasova.QUICKCHAR[charId2+1]), RacerName.getTimeString(getTotalTime()), getAveragePercentage()*100));
			bw.newLine();
		}
	}
	public static int comboID(int id1, int id2) {
		int charLength = Yasova.QUICKCHAR.length;
		if (id1 == -1) {
			id1 = charLength;
		}
		if (id2 == -1) {
			id2 = charLength;
		}
		return id1*(charLength+1)+id2;
	}
	
	public boolean equals(Object input) {
		//System.out.println("tested");
		if (input instanceof Combo) {
			return this.getId() == ((Combo)input).getId();
		}
		else if (input instanceof Integer) {
			return this.getId() == (Integer)input;
		}
		else if (input instanceof DataPoint) {
			return this.getId() == comboID(((DataPoint)input).getCharacterId1(),((DataPoint)input).getCharacterId2());
		}
		else
			return false;
	}
}