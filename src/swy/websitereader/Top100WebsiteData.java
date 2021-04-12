package swy.websitereader;

import java.util.ArrayList;

public class Top100WebsiteData {
	ArrayList<String> data;
	String characterFilter = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\p{S}\\s<>]";
	
	public Top100WebsiteData() {
		data = new ArrayList<String>(100);
	}
	
	public void addData(ArrayList<String> input) {
		//System.out.println(input);
		data.addAll(input);
	}
	
	public ArrayList<String> getData(int position) {
		ArrayList<String> output = new ArrayList<String>(7);
		for (int i = 3+7*position; i < (position+1)*7+3; i++) {
			output.add(data.get(i).replaceAll(characterFilter, "").replace("&nbsp;", "").replaceAll("\n+","").replaceAll("\r+",""));
		}
		if (output.get(1).length() < 1) {
			output.set(1, "[YasovaInvalidName]");
		}
		return output;
	}
	public String getCourse() {
		return data.get(0);
	}

	public String getCharacter1() {
		return data.get(1);
	}

	public String getCharacter2() {
		return data.get(2);
	}
	
	public boolean hasData() {
		return data.size() > 3;
	}
	public boolean hasData(int position) {
		return data.size() > (position)*(7)+3;
	}
}
