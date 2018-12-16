package pavel.keyzik;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.time.format.DateTimeFormatter;

public class DataBase {
  private String DB_HOST;
  private String DB_NAME;
  private String DB_USERNAME;
  private String DB_PASSWORD;
  private Connection connection;

  public DataBase() {
    this.DB_USERNAME = "root";
    this.DB_PASSWORD = "root";
  }

  public Boolean connect() {
    try {
      String url = "jdbc:mysql://127.0.0.1:3306/rkpp";
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      this.connection = DriverManager.getConnection(url, this.DB_USERNAME, this.DB_PASSWORD);
      return true;
    } catch (Exception e) {
      System.out.println(e);
    }
    return false;
  }

  public List<Fine> getFines() {
    List<Fine> fines = new ArrayList<Fine>();
    try
    {
      String query = "SELECT * FROM Fines";
      Statement st = this.connection.createStatement();
      ResultSet rs = st.executeQuery(query);
      while (rs.next())
      {
        int id = rs.getInt("ID");
        String fineDescription = rs.getString("FineDescription");
        String carNumber = rs.getString("CarNumber");
        String dateOfFine = rs.getString("DateOfFine");
        Fine x = new Fine(id, fineDescription, carNumber, dateOfFine);
        fines.add(x);
      }
      st.close();
    }
    catch (SQLException e)
    {
      System.out.println(e);
    }
    return fines;
  }

  public Boolean saveFine(String description, String carNumber) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      String query = "INSERT INTO Fines (FineDescription, CarNumber, DateOfFine) VALUES ('" + description + "', '" + carNumber + "', '" + java.time.LocalDate.now().format(formatter) + "');";
      Statement st = this.connection.createStatement();
      st.executeUpdate(query);
      st.close();
      return true;
    }
    catch (SQLException e) {
      System.out.println(e);
    }
    return false;
  }

  public Boolean login(String login, String password) {
    try {
      String query = "SELECT COUNT(*) AS total FROM Users WHERE login='" + login + "' AND password='" + password + "';";
      Statement st = this.connection.createStatement();
      ResultSet rs = st.executeQuery(query);
      rs.next();
      int rows = rs.getInt("total");
      st.close();
      return rows >= 1;
    }
    catch (SQLException e) {
      System.out.println(e);
    }
    return false;
  }
}