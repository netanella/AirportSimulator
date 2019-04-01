
public abstract class Flight implements Runnable {
	
	protected String code;
	protected int passengers;
	protected int arrivalTime; //to get to airport
	protected Object runwayKey;
	protected Queue<FlightDetails> flightDocs;
	protected int totalAirportTime = 0; //in mili-seconds
		
	public Flight(String code, int passengers, int arrivalTime, Queue<FlightDetails> flightDocs, Object runwayKey){
		this.code = code;
		this.passengers = passengers;
		this.arrivalTime = arrivalTime;
		this.runwayKey = runwayKey;
		this.flightDocs = flightDocs;
	}
	
	//the method runs until the flight arrives to the airport
	public void run() {
		try {
			Thread.sleep(arrivalTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("flight "+code+" arrived to the runway");
		enterRunway();
	}
	
	public abstract void enterRunway();
	
	//to delete
	public String toString(){
		return this.code;
	}
	
	public String getCode(){
		return code;
	}
	
	public int getPeople(){
		return passengers;
	}
	
	public void addAirportTime(long milisec){
		totalAirportTime += milisec;
	}
	
	//this method creates a new flight document regarding this flight instance 
	public FlightDetails createNewDoc(){
		String code = this.code;
		int totalAirportTime = this.totalAirportTime;
		int passengers = this.passengers;
		int cargo = 0;
		boolean suspiciousObject = false;
		boolean truckCalled = false;
		double totalRepairCost = 0;
		int fuelUse = 0;
		String destination = "";
		if(this instanceof Landing){
			cargo = ((Landing)this).getCargo();
			suspiciousObject = ((Landing)this).getSuspiciousObject();
			truckCalled = ((Landing)this).getTruckCalled();
			totalRepairCost = ((Landing)this).getRepairCost();
			fuelUse = ((Landing)this).getFuelUse();
		}
		else{
			destination = ((Takeoff)this).getDestination();
		}
		
		return new FlightDetails(code, cargo, suspiciousObject, truckCalled, totalAirportTime, 
								totalRepairCost,passengers,	fuelUse,destination, flightDocs);
	}
	
}
