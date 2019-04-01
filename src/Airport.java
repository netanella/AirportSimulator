import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Airport {

	//ArrayLists of all threads
	private ArrayList<Flight> flights = new ArrayList<Flight>();
	private ArrayList<RunwayDirector> runWayDirectors = new ArrayList<RunwayDirector>();
	private ArrayList<LogisticsCrew> logisticsCrews = new ArrayList<LogisticsCrew>();
	private ArrayList<SecurityMan> securityMen = new ArrayList<SecurityMan>();
	private ArrayList<FuelCrew> fuelCrews = new ArrayList<FuelCrew>();
	private ArrayList<TechnicalCrew> technicalCrews = new ArrayList<TechnicalCrew>();
	private ManagementCrew managementCrew;
	private ArrayList <Runnable> threads = new ArrayList <Runnable>();

	//all Queues
	private Queue<Landing> landingQ = new Queue<Landing>();
	private Queue<Takeoff> takeoffQ = new Queue<Takeoff>();
	private Queue<Landing> technicalQ = new Queue<Landing>();
	private Queue<Landing> logisticsQ = new Queue<Landing>();
	private Queue<Landing> securityQ = new Queue<Landing>();
	private BoundedQueue<Landing> fuelQ = new BoundedQueue<Landing>(8);
	private Queue<FlightDetails> flightDocs = new Queue<FlightDetails>();
	private ArrayList<Queue<Landing>> Qs = new ArrayList<>(Arrays.asList(logisticsQ,securityQ,fuelQ,technicalQ));

	private static Object runwayKey = new Object(); //sync object
	private DataBase DB = new DataBase();

	public Airport() { //constructor

		try { //reads flightsData file 
			fileReader("src\\FlightsData.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		//initiate runway directors and add to collections
		for(int i=0; i<3 ; i++){
			RunwayDirector d = new RunwayDirector(flights.size(), runwayKey, landingQ, takeoffQ);
			runWayDirectors.add(d);
			threads.add(d);
		}

		//initiate Logistics Crews and add to collections
		LogisticsCrew l = new LogisticsCrew("logisticsCrew1",90, logisticsQ);
		logisticsCrews.add(l);
		threads.add(l);

		LogisticsCrew l2 = new LogisticsCrew("logisticsCrew2",70, logisticsQ);
		logisticsCrews.add(l2);
		threads.add(l2);

		LogisticsCrew l3 = new LogisticsCrew("logisticsCrew3",50, logisticsQ);
		logisticsCrews.add(l3);
		threads.add(l3);

		//initiate Fuel Crew and add to collections
		FuelCrew fuel1 = new FuelCrew("FuelCrew1",10000,fuelQ);
		fuelCrews.add(fuel1);
		threads.add(fuel1);
		FuelCrew fuel2 = new FuelCrew("FuelCrew2",5000,fuelQ);
		fuelCrews.add(fuel2);
		threads.add(fuel2);

		//Initialize management crew and add to collections
		managementCrew = new ManagementCrew("bigBoss",flights.size(),flightDocs,threads,Qs,DB);
		threads.add(managementCrew);
	}

	/*this method starts a new day in the airport using the technical crew number and security work time 
		GUI input from user*/
	public void newDay(int techCrewNumber, long securityWorkTime){
		//initiate technical
		for(int i=0; i<techCrewNumber ; i++){
			TechnicalCrew t = new TechnicalCrew("TechCrew"+i,technicalQ);
			technicalCrews.add(t);
			threads.add(t);
		}

		//initiate security
		for(int i=0; i<2 ; i++){
			SecurityMan s = new SecurityMan(securityWorkTime,securityQ,fuelQ,randomRank());
			securityMen.add(s);
			threads.add(s);
		}

		//start all threads
		for(Runnable f: threads){
			Thread t = new Thread(f);
			t.start();
		}
	}
	
	//this method reads text files by separating each row and sending it to a unique handle-line method 
	public void fileReader(String fileName) throws IOException {
		BufferedReader inFile=null;
		try
		{
			FileReader fr = new FileReader(fileName);
			inFile = new BufferedReader (fr);

			String lineData = inFile.readLine(); 

			while (true){
				lineData = inFile.readLine(); //skip first line
				if (lineData==null){
					break;
				}

				handleLine(lineData);
			}
		}

		catch (FileNotFoundException exception) {
			System.err.println ("The file " + fileName + " was not found.");
		}
		catch (IOException exception) {
			System.err.println (exception);
		}
		finally{
			inFile.close();
		}
	}
	
	//this method receives a single line from flight text file table, 
	//splits it to the respected variables and creates new flight instances
	public void handleLine(String line){

		String[] splitted = line.split("\t");
		String code = splitted[0];
		int people = Integer.parseInt(splitted[1]);
		int time = Integer.parseInt(splitted[2]);
		String destCargo = splitted[3];

		try{  //tries to convert to int - if successful, assign to cargo 
			int cargo = Integer.parseInt(destCargo);
			Flight f = new Landing(code,people,time,cargo,runwayKey,landingQ,technicalQ,logisticsQ,securityQ,flightDocs); //create landing instance
			flights.add(f);
			threads.add(f);
		}catch (NumberFormatException e){ //if convert is faulty - use as destination string
			String dest = destCargo;
			Flight f = new Takeoff(code,people,time,runwayKey,flightDocs,takeoffQ,dest); //create takeoff instance
			flights.add(f);
			threads.add(f);
		}
	}

	//this method randomizes the rank for the security
	public String randomRank(){
		double random = Math.random();
		if(random<0.5){
			return "Zutar";
		}
		else{
			return "Bachir";
		}
	}

}
