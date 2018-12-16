package pavel.keyzik;
import java.util.Date;

public class Fine {
  private int ID;
  private String FineDescription;
  private String CarNumber;
  private String DateOfFine;

  public Fine(int id, String fineDescription, String carNumber, String dateOfFine) {
      this.ID = id;
      this.FineDescription = fineDescription;
      this.CarNumber = carNumber;
      this.DateOfFine = dateOfFine;
  }

  public int ID() {
      return ID;
  }

  public String getFineDescription() {
      return FineDescription;
  }

  public String getCarNumber() {
      return CarNumber;
  }

  public String getDateOfFine() {
      return DateOfFine;
  }
}