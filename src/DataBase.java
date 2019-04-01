import java.sql.*;

public class DataBase {	

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/test";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "root";

	Connection conn = null;
	Statement stmt = null;

	public DataBase(){
		getConnection();
		createTables();
	}//constructor
	
	//Register JDBC driver + Open a connection
	public void getConnection(){
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			System.out.println("successfully connected to DB! :)");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//this method creates landing and takeoff tables in database
	public void createTables() {
		try{
			stmt = conn.createStatement();
			
			String sql;
			
			sql = "DROP TABLE IF EXISTS Landings;";
			stmt.executeUpdate(sql);
			sql = "DROP TABLE IF EXISTS Takeoffs;";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE Landings(ID varchar(10), Passengers int, Cargo int, "
					+ "Cost int, isSecurityIssue bool, timeInAirfield int);";					
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE Takeoffs(ID varchar(10), Passengers int, Destination varchar(30), timeInAirfield int);";			
			stmt.executeUpdate(sql);

		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	//this method receives a flight document and inserts the data to the matching table
	public void insertToTable(FlightDetails doc) {
		String sql;
		if(doc.getDestination().isEmpty()){ //is landing
			try{
				sql="INSERT INTO landings VALUES('"+doc.getCode()+"', "+doc.getPassengers()+", "+doc.getCargo()+", "+
						doc.getTotalRepairCost()+", "+doc.wasSuspiciousObject()+", "+doc.getTotalAirportTime()+")";				
				stmt.executeUpdate(sql);
				System.err.println("DB - inserted doc to landing table");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else{ //is takeoff
			try{
				sql=" INSERT INTO takeoffs VALUES('"+doc.getCode()+"', "+doc.getPassengers()+", '"+doc.getDestination()+"', "+
						doc.getTotalAirportTime()+")";
				stmt.executeUpdate(sql);
				System.err.println("DB - inserted doc to takeoff table");
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
			
	}//insertToTable
}