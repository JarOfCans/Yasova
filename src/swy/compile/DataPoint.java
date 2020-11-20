package swy.compile;

import java.util.ArrayList;

import drafterdat.settings.Settings;
import swy.core.ID;
import swy.core.RaceTime;

public class DataPoint {
	String character1;
	String character2;
	int charId1;
	int charId2;
	String course;
	int courseId;
	ArrayList<RaceTime> data;
	
	public DataPoint(String init) {
		data = new ArrayList<RaceTime>(10);
		DataParse(init);
	}
	
	private void DataParse(String input) {
		course = input.substring(0, input.indexOf(":"));
		input = input.substring(input.indexOf(":") + 2);
		character1 = input.substring(0, input.indexOf(" "));
		input = input.substring(input.indexOf(" ") + 3);
		character2 = input;
		charId1 = ID.getCharacterId(character1);
		charId2 = ID.getCharacterId(character2);
		courseId = ID.getCourseID(course);
	}
	
	public void addData(String dataPoint) {
		data.add(new RaceTime(dataPoint, courseId));
	}
	
	public int getDataCount() {
		return data.size();
	}
	
	public ArrayList<RaceTime> newData(DataPoint input) {
		ArrayList<RaceTime> output = new ArrayList<RaceTime>(2);
		for (RaceTime hoi :input.data) {
			if (!contains(data, hoi)) {
				output.add(hoi);
				//System.out.println(hoi.toString());
			}
		}
		
		return output;
	}
	
	public ArrayList<RaceTime> oldData(DataPoint input) {
		ArrayList<RaceTime> output = data;
		output.removeAll(input.data);
		return output;
	}
	
	private boolean contains(ArrayList<RaceTime> inputList, RaceTime input) {
		for (RaceTime hoi: inputList) {
			if(hoi.equals(input)) {
				//System.out.println(hoi);
				return true;
			}
			//System.out.printf("%s == %s%s", hoi.racerName, input.racerName, System.lineSeparator());
		}
		//System.out.println(input);
		return false;
	}
	
	public double averagePercentile() {
		if (getDataCount() == 0) {
			return 0.0;
		}
		double output = 0.0;
		for (int i = 0; i < Math.min(DataRead.polishMax, data.size()); i++) {
			output += data.get(i).percentile;
		}
		return output/(double)(Math.min(DataRead.polishMax, getDataCount()));
	}
}
