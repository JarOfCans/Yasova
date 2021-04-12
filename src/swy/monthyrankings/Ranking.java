package swy.monthyrankings;

public class Ranking implements Comparable<Ranking> {
	
	private int number;
	private String userName;
	private int monthlyPoints;
	private String rank;
	private int totalPoints;
	private String charactersUsed;
	
	public Ranking(String box1, String box2, String box3, String box4, String box5) {
		number = Integer.parseInt(box1);
		userName = box2.trim();
		monthlyPoints = Integer.parseInt(box3);
		rank = box4.substring(0, box4.indexOf("/"));
		totalPoints = Integer.parseInt(box4.substring(box4.indexOf("/")+1));
		if (box5.contains(" ")) {
			charactersUsed = box5.substring(0, box5.indexOf(" "));
		}
		else {
			charactersUsed = box5;
		}
	}
	
	
	public int getNumber() {
		return number;
	}

	public String getUserName() {
		return userName;
	}

	public int getMonthlyPoints() {
		return monthlyPoints;
	}

	public String getRank() {
		return rank;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public String getCharactersUsed() {
		return charactersUsed;
	}

	@Override
	public int compareTo(Ranking arg0) {
		return this.number - arg0.number;
	}
}
