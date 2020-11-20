package swy.core;

import swy.yoink.Yasova;

public class ID {
	public static int getCharacterId(String character) {
		switch (character.toLowerCase()) {
			case "reimu":
				return 0;
			case "marisa":
				return 1;
			case "sakuya":
				return 2;
			case "remi":
			case "remilia":
				return 3;
			case "sanae":
				return 4;
			case "suwako":
				return 5;
			case "koishi":
				return 6;
			case "kokoro":
				return 7;
			case "youmu":
				return 8;
			case "reisen":
			case "udonge":
				return 9;
			case "nue":
				return 10;
			case "futo":
				return 11;
			case "cirno":
				return 12;
			case "seija":
				return 13;
			case "suika":
				return 14;
			case "kasen":
				return 15;
			case "tenshi":
				return 16;
			case "yukari":
				return 17;
			case "clown":
				return 18;
			case "flan":
				return 19;
			case "any":
				return -1;
			default:
				System.out.printf("Didn't find character \"%s\"%s", character, System.lineSeparator());
				return -2;
		}
	}
	
	public static int getCourseID(String course) {
		for (int i = 0; i < Yasova.COURSE.length; i++) {
			if (Yasova.COURSE[i].equals(course)) {
				return i;
			}
		}
		System.out.printf("Didn't find course \"%s\"%s", course, System.lineSeparator());
		return -2;
	}
}
