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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.chart.XYChart;
import javafx.util.Callback;
import javafx.scene.control.TableColumn.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
  private Button deleteTodo;
  private int currentType;
  public TableView todosTable;

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


    ComboBox<Type> typeComboBox = new ComboBox<Type>();
    List<Type> types = db.getTypes();
    for (Type temp : types) {
      typeComboBox.getItems().add(temp);
		}
    // this.currentType = typeComboBox.getSelectionModel().selectFirst();
    typeComboBox.valueProperty().addListener(new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
          // this.currentType = ((Type) (observable.getValue())).ID()
          // System.out.println();
        }
    });

    if(!connectedToDataBase) {
      System.out.println("====> Не могу подключиться к Базе Данных");
      window.close();
      System.exit(0);
    }

    saveTodo = new Button("Save");
    saveTodo.setStyle("-fx-background-color: #1f3e15; -fx-color: #1f3e15");
    saveTodo.setOnAction((ev) -> this.handleEvent(ev, typeComboBox));

    deleteTodo = new Button("Delete");
    deleteTodo.setStyle("-fx-background-color: #1f3e15; -fx-color: #1f3e15");
    deleteTodo.setOnAction((ev) -> this.handleRemoveEvent(ev));

    todosTable = new TableView();
    todosTable.setEditable(true);
    todosTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    TableColumn todoText = new TableColumn("Fine description");
    todoText.setMinWidth(120);
    todoText.setCellValueFactory(new PropertyValueFactory<>("FineDescription"));
    todoText.setCellFactory(TextFieldTableCell.forTableColumn());
    todoText.setOnEditCommit(event -> this.handleFineEdit("todoText", (CellEditEvent <Fine, String>)(event)));

    TableColumn carNumber = new TableColumn("Car number");
    carNumber.setCellValueFactory(new PropertyValueFactory<>("CarNumber"));
    carNumber.setCellFactory(TextFieldTableCell.forTableColumn());
    carNumber.setOnEditCommit(event -> this.handleFineEdit("carNumber", (CellEditEvent <Fine, String>)(event)));

    TableColumn todoAddedDate = new TableColumn("Added date");
    todoAddedDate.setCellValueFactory(new PropertyValueFactory<>("DateOfFine"));
    todoAddedDate.setCellFactory(TextFieldTableCell.forTableColumn());
    todoAddedDate.setOnEditCommit(event -> this.handleFineEdit("todoAddedDate", (CellEditEvent <Fine, String>)(event)));

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

    HBox deleteButtonLayout = new HBox();
    deleteButtonLayout.setAlignment(Pos.CENTER_RIGHT);
    deleteButtonLayout.getChildren().addAll(deleteTodo);
    deleteButtonLayout.setPadding(inputPadding);

    HBox selectTypeLayout = new HBox();
    selectTypeLayout.setAlignment(Pos.CENTER_RIGHT);
    selectTypeLayout.getChildren().addAll(typeComboBox);
    selectTypeLayout.setPadding(inputPadding);

    VBox layoutV = new VBox();
    layoutV.getChildren().addAll(fineDescriptionLayout, carNumberLayout, selectTypeLayout, saveButtonLayout, deleteButtonLayout);

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
    scene2 = new Scene(layout2, 300, 300);

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
    }
  }

  public void handleEvent(ActionEvent ev, ComboBox comboBox) {
    int currentType = (((Type)comboBox.valueProperty().getValue()).ID());
    String description = this.fineDescriptionField.getText();
    String carNumber = this.carNumberField.getText();

    if(this.db.saveFine(description, carNumber, currentType)) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      Fine temp = new Fine(1, description, carNumber, java.time.LocalDate.now().format(formatter));
      todosTable.getItems().add(temp);
    }
  }

  public void handleFineEdit(String type, CellEditEvent <Fine, String> event) {
    String value = event.getNewValue() != null ? event.getNewValue() : event.getOldValue();
    Fine fine = ((Fine) event.getTableView().getItems()
      .get(event.getTablePosition().getRow()));
    switch (type) {
      case "todoText":
        fine.setFineDescription(value);
        break;
      case "carNumber":
        fine.setCarNumber(value);
        break;
      case "todoAddedDate":
        fine.setDateOfFine(value);
        break;
    }
    if (db.updateFine(fine)) {
      todosTable.refresh();
    }
  }

  public void handleRemoveEvent(ActionEvent ev) {
    Fine selectedItem = (Fine)(todosTable.getSelectionModel().getSelectedItem());
    int id = selectedItem.ID();
    if(this.db.removeFine(id)) {
      todosTable.getItems().remove(selectedItem);
    }
  }
}