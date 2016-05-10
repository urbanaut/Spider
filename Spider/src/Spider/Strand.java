package Spider;

public class Strand {
	private final String source;
	private final String destination;
	
	public Strand(String s, String d)
	{
		source = s;
		destination = d;
	}

	public String getSource() { return source; }
	public String getDestination() { return destination; }
	
	@Override
	public String toString() {
		return "Source: " + source + " Destination: " + destination;
	}
}
