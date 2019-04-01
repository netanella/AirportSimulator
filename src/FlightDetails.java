
public class FlightDetails {
	private String code;
	private int cargo; //landing
	private boolean suspiciousObject; //landing
	private boolean truckCalled; //landing
	private int totalAirportTime;
	private double totalRepairCost;
	private int passengers;
	private int fuelUse;
	private String destination; //takeoff
	private Queue<FlightDetails> flightDocs;

	public FlightDetails(String code, int cargo, boolean suspiciousObject, 
			boolean truckCalled, int totalAirportTime, double totalRepairCost,int passengers,
			int fuelUse,String destination, Queue<FlightDetails> flightDocs){
		this.code = code;
		this.cargo = cargo;
		this.suspiciousObject = suspiciousObject;
		this.truckCalled = truckCalled;
		this.totalAirportTime = totalAirportTime;
		this.passengers = passengers;
		this.fuelUse = fuelUse;
		this.destination = destination;
		this.flightDocs = flightDocs;
		this.totalRepairCost = totalRepairCost;
	}

	public int getCargo() {
		return cargo;
	}

	public boolean wasSuspiciousObject() {
		return suspiciousObject;
	}

	public boolean wasTruckCalled() {
		return truckCalled;
	}

	public int getFuelUse() {
		return fuelUse;
	}

	public String getDestination() {
		return destination;
	}

	public Queue<FlightDetails> getFlightDocs() {
		return flightDocs;
	}

	public String getCode(){
		return code;
	}

	public int getTotalAirportTime(){
		return totalAirportTime;
	}

	public double getTotalRepairCost(){
		return totalRepairCost;
	}

	public void sendToManagement(){
		synchronized(flightDocs){
			flightDocs.insert(this);
			flightDocs.notifyAll();
		}
	}

	public int getPassengers(){
		return passengers;
	}
}
