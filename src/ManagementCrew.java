import java.util.*;
import java.util.Map.Entry;

public class ManagementCrew extends Staff {
	
	private String code;
	private Queue<FlightDetails> flightDocs;
	private int totalFlights;
	private static int flightCount;
	private ArrayList<Runnable> threads;
	private ArrayList<Queue<Landing>> Qs;
	
	private int dayPassengers;
	private int dayCargo;
	private ArrayList<String> dayDestinations = new ArrayList<String>();
	private double dayRepairCost;
	private int dayFuelUse;
	private int daySuspiciousObjects;
	private int dayTrucksCalled;
	private DataBase DB;
	
	public ManagementCrew(String code, int totalFlights, Queue<FlightDetails> flightDocs, ArrayList <Runnable> threads, 
			ArrayList<Queue<Landing>> Qs, DataBase DB){
		this.code = code;
		this.flightDocs = flightDocs;
		this.totalFlights = totalFlights;
		this.threads = threads;
		this.Qs = Qs;
		this.DB = DB;
	}
	
	//management crew receives flight documents, saves the data to database and calls end day when all flights are done processing
	@Override
	public void run() {
		while(true){
			FlightDetails doc = flightDocs.extract();
			flightCount++;
			System.out.println("management recieved document: "+doc);
			achieveData(doc); //update total day info fields
			importToDB(doc);
			printFlightData(doc);
			if(flightCount==totalFlights){ //all flights finished processing
				System.out.println("management ends day");
				callEndDay();
				break;
			}
		}
		printDailyReport();
		System.out.println(this+" finished working");
	}
	
	private void printDailyReport() {
		System.out.println("End of Day Summary Report:");
		System.out.println("Number of passengers: "+dayPassengers);
		System.out.println("Total number of Cargo: "+dayCargo);
		System.out.print("Most popular destination: ");
		if(dayDestinations.isEmpty())
			System.out.println("No takeoffs today");
		else 
			System.out.println(findPopular(dayDestinations));
		System.out.println("Total repair cost: "+dayRepairCost);
		System.out.println("Total fuel used: "+dayFuelUse);
		System.out.println("Number of suspicious objects found: "+daySuspiciousObjects);
		System.out.println("Number of called trucks: "+dayTrucksCalled);	
	}
	
	//this method returns the most repeated data in the given arraylist
	public <T> T findPopular(ArrayList<T> list) {
	    Map<T, Integer> map = new HashMap<>(); //key input, value count
	    for (T t : list) { 
	        Integer val = map.get(t);
	        map.put(t, val == null ? 1 : val + 1);
	    }
	    Entry<T, Integer> max = null; 
	    for (Entry<T, Integer> e : map.entrySet()) { //find biggest value
	        if (max == null || e.getValue() > max.getValue())
	            max = e;
	    }
	    return max.getKey();
	}
	
	//saves data to database
	public void importToDB(FlightDetails doc){
		try {
			Thread.sleep(2000);
			DB.insertToTable(doc);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//this method updates the total day info fields for end day report
	public void achieveData(FlightDetails doc){
		this.dayPassengers += doc.getPassengers();
		dayCargo += doc.getCargo();
		if(!doc.getDestination().isEmpty()){
			this.dayDestinations.add(doc.getDestination());
		}
		this.dayRepairCost += doc.getTotalRepairCost();
		this.dayFuelUse += doc.getFuelUse();
		if(doc.wasSuspiciousObject()) daySuspiciousObjects++;
		if(doc.wasTruckCalled()) dayTrucksCalled++;
	}
	
	//this method prints each flight data to the console
	public void printFlightData(FlightDetails doc){
		String code = doc.getCode();
		int totalAirportTime = doc.getTotalAirportTime();
		double totalRepairCost = doc.getTotalRepairCost();
		System.out.println("Flight: "+ code +"; Total time in airport: "+ totalAirportTime+" mili-seconds;"
							+" Total cost: "+ totalRepairCost);
	}
	
	//this method stops all threads
	public void callEndDay(){
		for(Runnable t : threads){
			if (t instanceof Staff){
				((Staff) t).endTheDay();
			}
		}
		
		for(Queue<Landing> queue: Qs){
			synchronized(queue){
				queue.endTheDay();
				queue.notifyAll();
			}
		}
	}

}
