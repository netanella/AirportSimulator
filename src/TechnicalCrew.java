
public class TechnicalCrew extends Staff {

	private String code;
	private Queue<Landing> technicalQ;

	public TechnicalCrew(String code, Queue<Landing> technicalQ){
		this.code = code;
		this.technicalQ = technicalQ;
	}
	
	//technical crews receives landing flights who had an incident, repairs them and sends back to next process step
	@Override
	public void run() {
		while(true){
			Landing flight = technicalQ.extract();
			if(flight == null) break; //end day
			System.out.println("technical crew is repairing "+flight);
			fixFlight(flight);
			updateRepairCost(flight);
			if(flight.getSentBy() instanceof FuelCrew){//if transfered from fuel send document to management
				try { //wait one more second
					Thread.sleep(1000);
					flight.addAirportTime(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				FlightDetails doc = flight.createNewDoc();
				doc.sendToManagement();
			}
			else {
				System.out.println("done repairing, putting back to next order");
				flight.sendToNextStep();
			}
		}
		System.out.println(this+"finished working");
	}
	
	//this method randomizes a random fix time
	public void fixFlight(Flight flight){
		long random = 3 + (long)Math.random()*2;

		try {
			Thread.sleep(random);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		flight.addAirportTime(random);
	}

	public void updateRepairCost(Landing flight){
		double random = 500 + Math.random()*500;
		flight.addToRepairCost(random);
	}

}
