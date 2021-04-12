package swy.complie.userReader;

import java.util.ArrayList;

public class UserIDManager {
	ArrayList<UserID> userIDs;
	int newIDnum;
	public UserIDManager() {
		userIDs = new ArrayList<UserID>();
		//TODO placeholder, change to a setting later;
		newIDnum = 0;
	}
	
	public ArrayList<String> getIDs(String oldName, String newName) {
		ArrayList<String> output = new ArrayList<String>();
		for (UserID hoi: userIDs) {
			if (hoi.matchingNames(oldName, newName)) {
				output.add(hoi.getID());
			}
		}
		if (output.size() == 0) {
			output.add(createNewID(oldName, newName).getID());
		}
		return output;
	}
	
	private UserID createNewID(String oldName, String newName) {
		// TODO Improve this later
		UserID newID = new UserID(oldName, newName, String.format("%05d", newIDnum++));
		userIDs.add(newID);
		return newID;
	}
}
