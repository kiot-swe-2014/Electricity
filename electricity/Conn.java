package electricity;

import java.sql.*;

public class Conn {
    Connection c;
    Statement s;
    
    Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost/electric_city", "root", "");
            s = c.createStatement(); // Initialize the Statement object
        } catch (Exception e) {
            System.out.print(" you are not connected");
        }
    }
}
