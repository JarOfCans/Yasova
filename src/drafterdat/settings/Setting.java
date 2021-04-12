package drafterdat.settings;

public class Setting {
	private String name;
	private String value;
	/**
	 * Constructor for the Setting class.
	 * @param nameIn Name for new setting. Null or blank string isn't excepted
	 * @param valueIn New value for this
	 */
	public Setting(String nameIn, String valueIn) throws NullPointerException, IllegalArgumentException
	{
		if (nameIn == null) {
			throw new BetterNullPointerException("Can't give null value in Setting constructor name");
		}
		if (nameIn.equals("")) {
			//throw new IllegalArgumentException("Face");//"Cant give blank value in Setting constructor name");
		}
		name = nameIn;
		value = valueIn;
	}
	/**
	 * Sets new name for setting. Null or blank string will result in no change
	 * @param nameIn New name of the string.
	 */
	public void setName(String nameIn)
	{
		if (nameIn == null || name.equals("")) {
			;//Does nothing
		}
		else {
			name = nameIn;
		}
	}
	/**
	 * Name of the Setting.
	 * @return Name of the Setting.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets new value for setting.
	 * @param nameIn New value of the string.
	 */
	public void setValue(String valueIn) {
		value = valueIn;
	}
	/**
	 * Value of the Setting.
	 * @return Value of the Setting.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * Returns a string in the proper setting format.
	 * @return name = value
	 */
	@Override
	public String toString()
	{
		return String.format("%s=%s", name, value);
	}
}
