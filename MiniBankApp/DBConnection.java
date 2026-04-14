package Projects.MiniBankApp;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection(){

        Connection con = null;

        try{
            String url = "jdbc:postgresql://localhost:5432/MiniBankApp";
	        String username = "your_username";
	        String password = "your_password";
		    Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url,username,password);
        }catch(Exception e){
            System.out.println("Connection failed..." + e.getMessage());
        }
        return con;
        
    } 
}
