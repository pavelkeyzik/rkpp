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
  private String INSERT_FINE = "UPDATE Fines SET FineDescription=?, CarNumber=?, DateOfFine=? WHERE id=?";

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

  public List<Type> getTypes() {
    List<Type> types = new ArrayList<Type>();
    try
    {
      String query = "SELECT * FROM Types";
      Statement st = this.connection.createStatement();
      ResultSet rs = st.executeQuery(query);
      while (rs.next())
      {
        int id = rs.getInt("ID");
        String title = rs.getString("Title");
        Type x = new Type(id, title);
        types.add(x);
      }
      st.close();
    }
    catch (SQLException e)
    {
      System.out.println(e);
    }
    return types;
  }

  public Boolean saveFine(String description, String carNumber, int typeId) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      String query = "INSERT INTO Fines (FineDescription, CarNumber, DateOfFine, TypeId) VALUES ('" + description + "', '" + carNumber + "', '" + java.time.LocalDate.now().format(formatter) + "', '" + typeId + "');";
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

  public Boolean removeFine(int id) {
    try {
      String query = "DELETE FROM Fines WHERE id='" + id + "'";
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

  public Boolean updateFine(Fine fine) {
    try {
      PreparedStatement ps = connection.prepareStatement(INSERT_FINE);
      ps.setString(1, fine.getFineDescription());
      ps.setString(2, fine.getCarNumber());
      ps.setString(3, fine.getDateOfFine());
      ps.setInt(4, fine.ID());
      ps.execute();
      ps.close();
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
