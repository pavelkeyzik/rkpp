package pavel.keyzik;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.Stage;
import java.util.List;
import java.util.Date;
import java.time.format.DateTimeFormatter;

// If application cannot to connect to the DataBase
// export CLASSPATH=$CLASSPATH:/usr/share/java/mysql-connector-java.jar

public class PavelKeyzikApplication extends Application {
  private Stage window;
  private BorderPane appPanel;
  private Scene scene;
  private Scene scene2;
  private DataBase db;
  private TextField fineDescriptionField;
  private TextField carNumberField;
  private TextField loginInput;
  private TextField passwordInput;
  private Button saveTodo;
  private TableView todosTable;

  @Override
  public void init() throws Exception {
    super.init();
    System.out.println("Inside init() method! Perform necessary initializations here.");
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    window = primaryStage;
    db = new DataBase();
    Boolean connectedToDataBase = db.connect();

    if(!connectedToDataBase) {
      System.out.println("====> Не могу подключиться к Базе Данных");
      window.close();
      System.exit(0);
    }

    saveTodo = new Button("Save");
    saveTodo.setStyle("-fx-background-color: #1f3e15; -fx-color: #1f3e15");
    saveTodo.setOnAction((ev) -> this.handleEvent(ev));

    todosTable = new TableView();
    todosTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    TableColumn todoText = new TableColumn("Fine description");
    todoText.setMinWidth(120);
    todoText.setCellValueFactory(new PropertyValueFactory<>("FineDescription"));

    TableColumn carNumber = new TableColumn("Car number");
    carNumber.setCellValueFactory(new PropertyValueFactory<>("CarNumber"));

    TableColumn todoAddedDate = new TableColumn("Added date");
    todoAddedDate.setCellValueFactory(new PropertyValueFactory<>("DateOfFine"));

    todosTable.getColumns().addAll(todoText, carNumber, todoAddedDate);

    List<Fine> fines = db.getFines();
    for (Fine temp : fines) {
      todosTable.getItems().add(temp);
		}

    Insets inputPadding = new Insets(5, 12, 5, 12);

    fineDescriptionField = new TextField();
    fineDescriptionField.setPromptText("Fine description");
    carNumberField = new TextField();
    carNumberField.setPromptText("Car number");

    HBox fineDescriptionLayout = new HBox();
    fineDescriptionLayout.setAlignment(Pos.CENTER_RIGHT);
    Label fineDescriptionLabel = new Label("Fine description:");
    fineDescriptionLayout.getChildren().addAll(fineDescriptionLabel, fineDescriptionField);
    fineDescriptionLayout.setSpacing(10);
    fineDescriptionLayout.setPadding(inputPadding);

    HBox carNumberLayout = new HBox();
    carNumberLayout.setAlignment(Pos.CENTER_RIGHT);
    Label carNumberLabel = new Label("Car Number:");
    carNumberLayout.getChildren().addAll(carNumberLabel, carNumberField);
    carNumberLayout.setSpacing(10);
    carNumberLayout.setPadding(inputPadding);

    HBox saveButtonLayout = new HBox();
    saveButtonLayout.setAlignment(Pos.CENTER_RIGHT);
    saveButtonLayout.getChildren().addAll(saveTodo);
    saveButtonLayout.setPadding(inputPadding);

    VBox layoutV = new VBox();
    layoutV.getChildren().addAll(fineDescriptionLayout, carNumberLayout, saveButtonLayout);

    appPanel = new BorderPane();
    appPanel.setTop(layoutV);
    appPanel.setBottom(todosTable);

    scene = new Scene(appPanel, 500, 350);

    Button button2 = new Button("Log In");
    button2.setOnAction((ev) -> this.Authorization(ev));

    loginInput = new TextField();
    loginInput.setPromptText("Login");
    passwordInput = new TextField();
    passwordInput.setPromptText("Password");

    VBox layout2 = new VBox();
    layout2.getChildren().addAll(loginInput, passwordInput, button2);
    scene2 = new Scene(layout2, 600, 600);

    window.setTitle("Hello World Application");
    window.setScene(scene2);
    window.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    System.out.println("Inside stop() method! Destroy resources. Perform Cleanup.");
  }

  public static void main(String[] args) {
      launch(args);
  }

  public void Authorization(ActionEvent ev) {
    String login = this.loginInput.getText();
    String password = this.passwordInput.getText();

    if(this.db.login(login, password)) {
      this.window.setScene(this.scene);
      // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      // Fine temp = new Fine(1, description, carNumber, java.time.LocalDate.now().format(formatter));
      // todosTable.getItems().add(temp);
    }
  }

  public void handleEvent(ActionEvent ev) {
    String description = this.fineDescriptionField.getText();
    String carNumber = this.carNumberField.getText();

    if(this.db.saveFine(description, carNumber)) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      Fine temp = new Fine(1, description, carNumber, java.time.LocalDate.now().format(formatter));
      todosTable.getItems().add(temp);
    }
  }
}
