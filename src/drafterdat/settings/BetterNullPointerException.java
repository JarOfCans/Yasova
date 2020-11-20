package drafterdat.settings;

public class BetterNullPointerException extends NullPointerException {
	private String message;
	public BetterNullPointerException(String messageIn)
	{
		super();
		message = messageIn;
	}
	public String getLocalizedMessage()
	{
		return message;
	}
}
