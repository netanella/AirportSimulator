
public class Landing extends Flight {

	private int cargo;
	private Queue<Landing> landingQ;
	private Queue<Landing> technicalQ;
	private Queue<Landing> logisticsQ;
	private Queue<Landing> securityQ;
	private Staff sentBy; //who sent the landing to technical crew
	private short repairCost;
	private boolean suspiciousObject = false;
	private boolean truckCalled = false;
	private int fuelUse;

	public Landing(String code, int people, int arrivalTime, int cargo, Object runwayKey,
			Queue<Landing> landingQ, Queue<Landing> technicalQ, Queue<Landing> logisticsQ, 
			Queue<Landing> securityQ, Queue<FlightDetails> flightDocs) {
		super(code, people, arrivalTime, flightDocs, runwayKey);
		this.cargo = cargo;
		repairCost = 0;
		this.landingQ = landingQ;
		this.technicalQ = technicalQ;
		this.logisticsQ = logisticsQ;
		this.securityQ = securityQ;
	}

	public int getCargo(){
		return cargo;
	}

	@Override
	public void enterRunway() {
		synchronized(runwayKey){
			landingQ.insert(this);
			runwayKey.notifyAll();
		}
	}
	
	//this method inserts this landing to the called Queue
	public void sendTo(Queue<Landing> nextQ){
		synchronized(nextQ){
			nextQ.insert(this);
			nextQ.notifyAll();
		}
	}
	
	//this method sends this landing to technical crew
	public void sendToRepair(){
		this.sendTo(technicalQ);
	}

	public void SetSentBy(Staff t){
		sentBy = t;
	}

	public Staff getSentBy(){
		return sentBy;
	}
	
	public void addToRepairCost(double cost){
		repairCost+=cost;
	}
	
	public short getRepairCost(){
		return repairCost;
	}
	
	public int getFuelUse() {
		return fuelUse;
	}
	
	//this method sends this landing to next step in process
	public void sendToNextStep(){
		if (sentBy instanceof RunwayDirector){
			this.sendTo(logisticsQ);
		}

		else if (sentBy instanceof LogisticsCrew){
			this.sendTo(securityQ);
		}
		else{
			System.err.println("ERROR on"+ this+ " sentBy is: " +sentBy); //to delete
		}
	}
	
	public void truckWasCalled(){
		this.truckCalled = true;
	}
	
	public boolean getTruckCalled(){
		return truckCalled;
	}
	
	public void suspiciousObjectFound(){
		this.suspiciousObject = true;
	}
	
	public boolean getSuspiciousObject(){
		return suspiciousObject;
	}
	
	public void addfuelUse(int amount){
		this.fuelUse += amount;
	}
}
