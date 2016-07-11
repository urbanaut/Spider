package Spider;

public class Strand {
	private final String source;
	private final String destination;
	
	Strand(String s, String d) {
		source = s;
		destination = d;
	}

	String getSource() { return source; }
	String getDestination() { return destination; }
	
	@Override
	public String toString() {
		return "Source: " + source + " Destination: " + destination;
	}
}
