
public class LogisticsCrew extends Staff implements Incidentable{
	private String code;
	private int cargoCapacity;
	private Queue<Landing> logisticsQ;

	public LogisticsCrew(String code, int capacity, Queue<Landing> logisticsQ){
		this.code = code;
		cargoCapacity = capacity;
		this.logisticsQ = logisticsQ;
	}
	//logistics crew receives landings from runway directors, unloads cargo and sends landing to security
	@Override
	public void run() {
		while(true){
			Landing flight = logisticsQ.extract();
			if(flight == null) break; //end day
			flight.SetSentBy(this);
			System.out.println(this+" logistics crew is unloading cargo of "+flight);
			if(capacityCheck(flight)){
				unload(flight);
			}
			else{
				waitForTruck(flight);
				unload(flight);
			}
			if(wasIncident()){
				System.out.println("INCIDENT! sending "+flight+" to repair");
				flight.sendToRepair();
			}
			else{
				System.out.println("flight " + flight+ "is being transfered to security");
				flight.SetSentBy(this); //updates sentBy field in landing, in order to put back in order after fix
				flight.sendToNextStep(); //next Queue
			}
		}
		System.out.println(this+"finished working");
	}
	
	//this method checks if the capacity is enough to unload the landing flight
	public boolean capacityCheck(Landing flight){
		if(flight.getCargo() > cargoCapacity)
			return false;
		return true;
	}
	
	public void unload(Landing flight){
		try {
			Thread.sleep(flight.getCargo()*100);
			flight.addAirportTime(flight.getCargo()*100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//this method stops the thread for 2 seconds until the truck arrives
	public void waitForTruck(Landing flight){
		try {
			Thread.sleep(2000);
			flight.addAirportTime(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		flight.truckWasCalled();
	}
	
	//this method randomized the chance for an incident during unloading
	@Override
	public boolean wasIncident() {
		double random = Math.random()*100;
		if(random <= 10) return true;
		return false;
	}

}
