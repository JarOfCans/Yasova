package swy.compile.racers;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import drafterdat.settings.Settings;
import swy.combos.Combo;
import swy.combos.ComboSortComparator;
import swy.compile.DataPoint;
import swy.compile.NameCourseSortComparator;
import swy.compile.NameSortComparable;
import swy.compile.PairData;
import swy.core.RaceTime;
import swy.yoink.Yasova;

public class RacerBatch {
	//char startChar;
	private ArrayList<RacerName> racers;
	private ArrayList<DataPoint> tables;
	private ArrayList<Combo> anyCombos;
	private ArrayList<Combo> doubleCombos;
	private ArrayList<Combo> combos;
	
	private ArrayList<ArrayList<RaceTime>> courseTimes;
	
	public RacerBatch(/*char input*/) {
		//startChar = input;
		racers = new ArrayList<RacerName>(20);
		tables = new ArrayList<DataPoint>(400);
		
		courseTimes = new ArrayList<ArrayList<RaceTime>>();
		for (int i = 0; i < Yasova.COURSE.length; i++) {
			courseTimes.add(new ArrayList<RaceTime>());
		}
	}
	
	public void addDataPoint(DataPoint input) {
		tables.add(input);
	}
	/*public RacerBatch(String input) {
		new RacerBatch(input.charAt(0));
		racers = new ArrayList<RacerName>();
	}*/
	
	public void generateOutput(BufferedWriter bw) throws IOException {
		//String output = "";
		//System.out.println("Moo");
		//int i = 1;
		ArrayList<RaceTime> racerTimeArray = getAll();
		Collections.sort(racerTimeArray, new NameSortComparable());
		String previousRacer ="";
		for (RaceTime racetime : racerTimeArray) {
			if (!racetime.racerName.equals(previousRacer)) {
				previousRacer = racetime.racerName;
				bw.write(previousRacer);
				bw.newLine();
			}
			bw.write(racetime.racerString());
			bw.newLine();
		}
	}

	public void generateCompactOutput(BufferedWriter bw) throws IOException {
		int i = 1;
		int writeI = 1;
		RacerName lastRacer = null;
		for (RacerName hoi : racers) {
			if (lastRacer != null && lastRacer.compareTo(hoi) != 0) {
				writeI = i;
			}
			lastRacer = hoi;
			bw.write(hoi.timeName(writeI));
			bw.newLine();
			bw.write(hoi.topOutput());
			i++;
		}
	}
	
	/*public void generateCompactCourse(BufferedWriter bw) {
		ArrayList<RaceTime> races = new ArrayList<RaceTime>();
		for (RacerName hoi : racers) {
			for (int i = 0; i < hoi.bestTimes.length; i++) {
				if (hoi.bestTimes[i] != null) {
					races.add(hoi.bestTimes[i]);
				}
			}
		}
	}*/
	public void generateCombosBest(BufferedWriter bw) throws IOException {
		Collections.sort(combos, new ComboSortComparator());
		int i = 1;
		int writeI = 1;
		Combo lastCombo = null;
		for (Combo combo: combos) {
			if (lastCombo != null && lastCombo.getTotalTime() != combo.getTotalTime()) {
				writeI = i;
			}
			lastCombo = combo;
			combo.write(bw, writeI);
			i++;
		}
	}
	public void generateAnyCombosBest(BufferedWriter bw) throws IOException {
		Collections.sort(anyCombos, new ComboSortComparator());
		int i = 1;
		int writeI = 1;
		Combo lastCombo = null;
		for (Combo combo: anyCombos) {
			if (lastCombo != null && lastCombo.getTotalTime() != combo.getTotalTime()) {
				writeI = i;
			}
			lastCombo = combo;
			combo.writeAny(bw, writeI);
			i++;
		}
	}
	
	public void generateCombosCompact(BufferedWriter bw) throws IOException {
		if (anyCombos.size() > 0) {
			bw.write("Character + Any sorted:");
			bw.newLine();
			Collections.sort(anyCombos,new ComboSortComparator());
			int i = 1;
			int writeI = 1;
			Combo lastCombo = null;
			for (Combo combo: anyCombos) {
				if (lastCombo != null && lastCombo.getTotalTime() != combo.getTotalTime()) {
					writeI = i;
				}
				lastCombo = combo;
				combo.compactWrite(bw, writeI);
				i++;
			}
			bw.write("=================");
			bw.newLine();
		}
		if (doubleCombos.size() > 0) {
			bw.write("Character + Character sorted:");
			bw.newLine();
			Collections.sort(doubleCombos,new ComboSortComparator());
			int i = 1;
			int writeI = 1;
			Combo lastCombo = null;
			for (Combo combo: doubleCombos) {
				if (lastCombo != null && lastCombo.getTotalTime() != combo.getTotalTime()) {
					writeI = i;
				}
				lastCombo = combo;
				combo.compactWrite(bw, writeI);
				i++;
			}
			bw.write("=================");
			bw.newLine();
		}
		bw.write("All combos sorted:");
		bw.newLine();
		Collections.sort(combos,new ComboSortComparator());
		int i = 1;
		int writeI = 1;
		Combo lastCombo = null;
		for (Combo combo: combos) {
			if (lastCombo != null && lastCombo.getTotalTime() != combo.getTotalTime()) {
				writeI = i;
			}
			lastCombo = combo;
			combo.compactWrite(bw, writeI);
			i++;
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public static int combosIndex(ArrayList<Combo> hoi, DataPoint dp) {
		for (int i = 0; i < hoi.size(); i++) {
			if (hoi.get(i).equals(dp)) {
				return i;
			}
		}
		return -1;
	}
	
	
	public void addTime(RaceTime rt) {
		int index = indexOf(rt.racerName);
		//System.out.println(rt.racerName);
		if (index == -1) {
			racers.add(new RacerName(rt));
		}
		else {
			racers.get(index).addTA(rt);
		}
		courseTimes.get(rt.course).add(rt);
	}
	
	public ArrayList<RaceTime> getAll() {
		ArrayList<RaceTime> output = new ArrayList<RaceTime>();
		for (RacerName hoi1: racers) {
			output.addAll(hoi1.times);
		}
		
		return output;
	}
	
	public ArrayList<RaceTime> getAllAny() {
		ArrayList<RaceTime> output = new ArrayList<RaceTime>();
		for (DataPoint hoi1: tables) {
			if (hoi1.getCharacterId1() == -1 && hoi1.getCharacterId2() == -1) {
				output.addAll(hoi1.getData());
			}
		}
		Collections.sort(output, new NameCourseSortComparator());
		return output;
	}
	
	private int indexOf(String input) {
		for (int i = 0; i < racers.size(); i++) {
			if (racers.get(i).name.equals(input)) {
				return i;
			}
		}
		return -1;
		 //(input==null ? get(i)==null : o.equals(get(i)))
	}
	
	public ArrayList<PairData> getPairData() {
		int charCount = Yasova.QUICKCHAR.length - 1;
		ArrayList<PairData> output = new ArrayList<PairData>(complexTotal(charCount));
		for (int x = 0; x < charCount; x++) {
			for (int y = x; y < charCount; y++) {
				output.add(new PairData(x, y, 0));
				//System.out.println(x + "/" + y);
			}
		}
		int id1;
		int id2;
		PairData data;
		for (DataPoint hoi: tables) {
			id1 = hoi.getCharacterId1();
			id2 = hoi.getCharacterId2();
			if (id1 != -1 && id2 != -1) {
				//System.out.println(id1 + "," + id2);
				data = output.get(combinedIndex(charCount, id1, id2));
				if (data.char1() != id1 || data.char2() != id2) {
					System.out.printf("%d/%d != %d/%d%s", data.char1(),data.char2(), id1, id2, System.lineSeparator());
					return null;
				}
				data.add(hoi.getDataCount());
				if (hoi.getDataCount() == 100) {
					data.maxNoted();
				}
			}
		}
		return output;
	}
	
	public static int complexTotal(int input) {
		return input*(input+1)/2;
	}
	public static int combinedIndex(int total, int char1, int char2) {
		return complexTotal(total) - complexTotal(total - char1) +char2 - char1;
	}
	public ArrayList<DataPoint> getDataPoints() {
		return tables;
	}
	
	public ArrayList<ArrayList<RaceTime>> getAllCourses() {
		return courseTimes;
	}
	
	public void bake() {
		System.out.println("Baking");
		for (DataPoint data:tables) {
			if (data.getCharacterId1() != -1 && data.getCharacterId2() != -1) {
				//System.out.println("dp size: "+input.getDataCount());
				for (RaceTime hoi: data.getData()) {
					addTime(hoi);
				}
			}
		}
		
		for (RacerName name: racers) {
			name.bake();
		}
		//NormalDistribution dist = new NormalDistribution();
		for (ArrayList<RaceTime> hoi: courseTimes) {
			//System.out.println(hoi.size());
			int globalPosition = 1;
			int gpi = 1;
			int topPosition = 1;
			int tpi = 1;
			RaceTime lastGlobal = null;
			RaceTime lastTop = null;
			Collections.sort(hoi);
			
			int top = hoi.get(0).miliTime();
			boolean known = true;
			for (RaceTime racetime : hoi) {
				if (lastGlobal != null && lastGlobal.compareTo(racetime) != 0) {
					globalPosition = gpi;
				}
				lastGlobal = racetime;
				racetime.globalPosition = globalPosition;
				gpi++;
				if (racetime.topTime) {
					if (lastTop != null && lastTop.compareTo(racetime) != 0) {
						topPosition = tpi;
					}
					lastTop = racetime;
					racetime.globalUniquePosition = topPosition;
					tpi++;
				}
				racetime.perfectVision = known;
				
				//double zScore = (racetime.miliTime()-mean)/sd;
				racetime.percentile = ((double)top)/racetime.miliTime();//1.0 - dist.cumulativeProbability(zScore);//1 - ((double) racetime.globalPosition)/((double)courseTimes.get(racetime.course).size()-1);
				if (racetime.rawPlacement == 100) {
					known = false;
				}
			}
		}
		combos = new ArrayList<Combo>();
		anyCombos = new ArrayList<Combo>();
		doubleCombos = new ArrayList<Combo>();
		boolean doAny = Settings.settingValue("DoCompactComboAnys", "1").equals("1");
		boolean doDouble = Settings.settingValue("DoCompactComboDoubles", "1").equals("1");
		for (DataPoint dp: tables) {
			if (dp.getDataCount() > 0 && dp.getCharacterId1() != -1) {
				if (dp.getCharacterId2() == -1) {
					if (doAny) {
						int id = combosIndex(anyCombos, dp);
						if (id != -1) {
							anyCombos.get(id).add(dp);
						} else {
							anyCombos.add(new Combo(dp));
						}
					}
				} else {
					int id = combosIndex(combos, dp);
					if (id != -1) {
						combos.get(id).add(dp);
					} else {
						combos.add(new Combo(dp));
					}
					if (dp.getCharacterId2() == dp.getCharacterId1() && doDouble) {
						id = combosIndex(doubleCombos, dp);
						if (id != -1) {
							doubleCombos.get(id).add(dp);
						} else {
							doubleCombos.add(new Combo(dp));
						}
					}
				}
			}
		}
		for (Combo hoi: anyCombos) {
			for (RaceTime rt: hoi.times) {
				rt.percentile = ((double)courseTimes.get(rt.course).get(0).miliTime())/rt.miliTime();
			}
		}
		for (RacerName hoi: racers) {
			for (RaceTime rt: hoi.times) {
				rt.percentile = ((double)courseTimes.get(rt.course).get(0).miliTime())/rt.miliTime();
				//System.out.println(rt.percentile);
			}
		}
		Collections.sort(racers);


		System.out.println("Baked");
	}
	
	/*@Override
	public boolean equals(Object obj) {
		if (obj instanceof String) {
			return ((String) obj).charAt(0) == startChar;
		}
		if (obj instanceof RacerBatch) {
			return ((RacerBatch)obj).startChar == startChar;
		}
		return false;
	}*/

	
	
}
