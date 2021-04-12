package swy.complie.userReader;

public class UserID {
	String oldName;
	String newName;
	String ID;
	public UserID(String oldName, String newName) {
		this.oldName = oldName;
		this.newName = newName;
	}
	public UserID(String oldName, String newName, String ID) {
		this.oldName = oldName;
		this.newName = newName;
		this.ID = ID;
	}
	/**
	 * Returns and int equal to the equality of two user IDs
	 * @param input UserID to compare to
	 * @return 0: Different User ID and different names. 1: Same userID, different Names. 2: Different userID, same names, 3: Same UserID and same names
	 */
	public int equals(UserID input) {
		int output = 0;
		if (this.oldName.equals(input.oldName) && this.newName.equals(input.newName)) {
			output += 2;
		}
		if (this.ID.equals(input.ID)) {
			output++;
		}
		return output;
	}
	
	public boolean matchingNames(String inputOldName, String inputNewName) {
		return this.newName.equals(inputOldName) && this.newName.equals(inputNewName);
	}
	public String getOldName() {
		return oldName;
	}
	
	public String getNewName() {
		return newName;
	}
	
	public String getID() {
		return ID;
	}
}
