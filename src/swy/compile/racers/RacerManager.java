package swy.compile.racers;

import java.util.ArrayList;

import swy.core.RaceTime;

public class RacerManager {
	ArrayList<RacerBatch> batch;
	@Deprecated
	public RacerManager() {
		batch = new ArrayList<RacerBatch>(26);
	}
	@Deprecated
	public RaceTime addTime(RaceTime input) {
		return input;
	}
}
