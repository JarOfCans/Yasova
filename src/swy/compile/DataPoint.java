package swy.compile;

import java.util.ArrayList;

import drafterdat.settings.Settings;
import swy.core.ID;
import swy.core.RaceTime;

public class DataPoint {
	private String character1;
	private String character2;
	private int charId1;
	private int charId2;
	private String course;
	private int courseId;
	ArrayList<RaceTime> data;
	
	public DataPoint(String init) {
		data = new ArrayList<RaceTime>(10);
		DataParse(init);
	}
	
	public DataPoint(DataPoint input) {
		data = new ArrayList<RaceTime>(10);
		course = input.course;
		character1 = input.character1;
		character2 = input.character2;
		charId1 = input.charId1;
		charId2 = input.charId2;
		courseId = input.courseId;
	}
	
	private void DataParse(String input) {
		//System.out.println(input.length()<50?input:input.substring(0, 49));
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
	
	public ArrayList<RaceTime> getData() {
		return data;
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
	
	@Override
	public boolean equals(Object hoi) {
		return (hoi instanceof DataPoint)?
				(((DataPoint)hoi).charId1 == this.charId1&&((DataPoint)hoi).charId2 == this.charId2&&((DataPoint)hoi).courseId == this.courseId)
				:false;
	}
	
	public int getCharacterId1() {
		return charId1;
	}
	
	public int getCharacterId2() {
		return charId2;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	public String getCharacter1() {
		return character1;
	}

	public String getCharacter2() {
		return character2;
	}

	public String getCourse() {
		return course;
	}
}
