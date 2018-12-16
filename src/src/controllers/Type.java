package pavel.keyzik;

public class Type {
  private int ID;
  private String Title;

  public Type(int id, String title) {
      this.ID = id;
      this.Title = title;
  }

  public int ID() {
      return ID;
  }

  public String getTitle() {
      return Title;
  }

  public void setTitle(String title) {
    this.Title = title;
  }

  @Override
  public String toString() {
    return Title;
  }
}