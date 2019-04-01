
public class RunwayDirector extends Staff implements Incidentable {

	public static int flightCounter = 0;
	private int totalFlights;
	private Queue<Landing> landingQ;
	private Queue<Takeoff> takeoffQ;
	private Object runwayKey;

	public RunwayDirector(int totalFlights, Object runwayKey, Queue<Landing> landingQ, Queue<Takeoff> takeoffQ){
		this.totalFlights = totalFlights;
		this.landingQ = landingQ;
		this.takeoffQ = takeoffQ;
		this.runwayKey = runwayKey;
	}
	
	//runway director receives landings that arrived to runway. if it is landing - sends to logistics crew. 
	//if takeoff - sends flight document to management
	@Override
	public void run() {
		while(true){
			Flight flight = null;
			synchronized(runwayKey){ //awaits on two queues
				while(landingQ.isEmpty() && takeoffQ.isEmpty() && flightCounter < totalFlights){
					try {
						runwayKey.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(flightCounter == totalFlights) break; //all flights have arrived
				else if(!landingQ.isEmpty()){
					flight = landingQ.extract();
				}
				else{
					flight = takeoffQ.extract();
				}
				runwayKey.notify();
			}
			flightCounter++; //successful extract
			System.out.println("Director "+this+" is handling flight "+flight);
			handleFlight(flight);
			if(flight instanceof Landing){
				((Landing)flight).SetSentBy(this); //updates sentBy field in landing, in order to put back in order after fix
				if(wasIncident()){
					System.out.println("INCIDENT! sending "+flight+" to repair");
					((Landing) flight).sendToRepair(); //send to technical crew
				}
				else{
					((Landing)flight).sendToNextStep();
				}
			}
			else{
				FlightDetails doc = flight.createNewDoc();
				doc.sendToManagement();
			} //send flight document to management
		}

		System.out.println(this+" is done working");
		synchronized(runwayKey){
			runwayKey.notifyAll(); //notify all sleeping runway directors to stop working
		}
	}
	
	//this method generates a random flight handling time for runway directors
	public void handleFlight(Flight flight) {
		long random = 5 + (long)Math.random()*5;

		try {
			Thread.sleep(random*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		flight.addAirportTime(random*1000);
	}

	//this method randomized the chance for an incident during runway handling
	@Override
	public boolean wasIncident() {
		double random = Math.random()*100;
		if(random<=25) return true;
		return false;
	}

}
