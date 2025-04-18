import java.sql.*;

public class JdbcExample {

    public static void main(String[] args) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/your_database_name"; // Update with your database name
        String user = "root"; // Update with your MySQL username
        String password = "your_password"; // Update with your MySQL password

        // SQL query to fetch all records from the Employee table
        String query = "SELECT EmpID, Name, Salary FROM Employee";

        // Connection, Statement, and ResultSet objects
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 1. Load the JDBC driver (optional for recent versions of JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establish connection to the database
            conn = DriverManager.getConnection(url, user, password);

            // 3. Create a statement object
            stmt = conn.createStatement();

            // 4. Execute the query and get the result set
            rs = stmt.executeQuery(query);

            // 5. Display the results
            System.out.println("EmpID\tName\tSalary");
            System.out.println("---------------------------");
            while (rs.next()) {
                int empID = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");
                System.out.println(empID + "\t" + name + "\t" + salary);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 6. Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
