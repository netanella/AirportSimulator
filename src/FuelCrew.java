
public class FuelCrew extends Staff implements Incidentable {

	private String code;
	private int maxCapacity;
	private int currCapacity;
	private BoundedQueue<Landing> fuelQ;

	public FuelCrew(String code, int maxCapacity,BoundedQueue<Landing> fuelQ){
		this.code = code;
		this.currCapacity = maxCapacity;
		this.maxCapacity = maxCapacity;
		this.fuelQ = fuelQ;
	}
	
	//fuel crew receives landings from security, refuels and sends flight document to management 
	@Override
	public void run() {
		while(true){
			Landing flight = fuelQ.extract();
			if(flight == null) break; //end day
			System.out.println(this + " extracted "+ flight);
			System.out.println(this + " has curr "+currCapacity+" out of "+maxCapacity);
			//successful extract
			if(!fuelCheck()){
				System.out.println(this+" does not have enough fuel. going to station");
				fuelQ.insert(flight);// insert to end of queue
				goToStation(flight);
				refuel(flight);
				System.out.println("flight "+flight+" was successfully refueled");
				continue;
			}
			currCapacity-=1000;
			flight.addfuelUse(1000);
			if(wasIncident()){
				System.out.println("INCIDENT! sending to repair");
				flight.SetSentBy(this); //updates sentBy field in landing, in order to put back in order after fix
				flight.sendToRepair();
			}
			else{
				FlightDetails doc = flight.createNewDoc();
				doc.sendToManagement();
				System.out.println("doc transfered to management");
			}
		}
		System.out.println(this+" finished working");
	}
	
	//this method randomized the chance for an incident during refuel
		@Override
		public boolean wasIncident() {
			double random = Math.random()*100;
			if(random <= 30) return true;
			return false;
		}

	public boolean fuelCheck(){
		if(currCapacity<1000) return false;
		return true;
	}

	public void goToStation(Flight flight){
		try {
			Thread.sleep(5000);
			flight.addAirportTime(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//this method resets the current fuel capacity of fuel crew to its initial max capacity
	public void refuel(Flight flight){
		currCapacity = maxCapacity;
		long random = 3+ (long)Math.random();
		try {
			Thread.sleep(random*1000);
			flight.addAirportTime(random*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
