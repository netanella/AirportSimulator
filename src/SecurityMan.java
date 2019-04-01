
public class SecurityMan extends Staff implements Incidentable {
	private Queue<Landing> securityQ;
	private Queue<Landing> fuelQ;
	private String rank;
	private long workTime;

	public SecurityMan(long workTime, Queue<Landing> securityQ, Queue<Landing> fuelQ, String rank){
		this.securityQ = securityQ;
		this.fuelQ = fuelQ;
		this.workTime = workTime;
		this.rank = rank;
	}
	
	//security receives landings from logistics crew, checks for suspicious objects on flight and transfers it to fuel crew 
	@Override
	public void run() {
		while(true){
			Landing flight = securityQ.extract();
			if(flight==null) break; //end day
			System.out.println(this+ " is handling flight "+flight);
			handleFlight(flight);
			if(wasIncident()){ //if suspicious object found - disable it
				System.out.println("INCIDENT! security disabling the suspicious object at "+flight);
				flight.suspiciousObjectFound();
				disableObject(flight);
			}
			System.out.println("flight "+flight+" is being transfered to fuel");
			flight.sendTo(fuelQ); //directly sent - waits if no space
		}
		System.out.println(this+"finished working");
	}
	
	//randomizes a random security check time
	public void handleFlight(Flight flight){
		try {
			Thread.sleep(workTime*1000); //was input given in seconds?
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//this method randomized the chance for an incident during security check
	@Override
	public boolean wasIncident() {
		double random = Math.random()*100;
		if(random <= 5) return true;
		return false;
	}
	
	//if object was found, this method stops the thread for 2 seconds to disable it
	public void disableObject(Flight flight){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		flight.addAirportTime(2000);
	}

}
