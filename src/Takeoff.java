
public class Takeoff extends Flight{

	private String destination;
	private Queue<Takeoff> takeoffQ;

	public Takeoff(String code, int people, int arrivalTime, Object runwayKey, Queue<FlightDetails> flightDocs, Queue<Takeoff> takeoffQ, String destination){
		super(code,people,arrivalTime, flightDocs, runwayKey);
		this.destination = destination;
		this.takeoffQ = takeoffQ;
	}

	public void run(){
		super.run();
	}

	@Override
	public void enterRunway() {
		synchronized(runwayKey){
			takeoffQ.insert(this);
			runwayKey.notifyAll();
		}
	}

	public String getDestination(){
		return destination;
	}

}
