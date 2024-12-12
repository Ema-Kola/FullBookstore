package view;

import java.awt.*;
import java.util.Date;

import dao.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import model.Gender;
import model.Role;
import model.User;


public class ManageEmployeeView extends BorderPane{

    private HomeView prevView;
    public TableView<User> tableView = new TableView<>();

    private final TableColumn <User,String> firstNameColumn = new TableColumn<>("First Name");
    private final TableColumn<User,String> lastNameColumn = new TableColumn<>("Last Name");
    private final TableColumn <User,Gender> genderColumn = new TableColumn<>("Gender");
    private final TableColumn<User,String>  usernameColumn = new TableColumn<>("Username");
    private final TableColumn <User,String> emailColumn = new TableColumn<>("Email");
    private final TableColumn<User,String>  passwordColumn = new TableColumn<>("Password");
    private final TableColumn<User,Role>  accessLevelColumn = new TableColumn<>("Access Level");
    private final TableColumn <User,String> phoneColumn = new TableColumn<>("Phone Number");
    private final TableColumn <User,Double> salaryColumn = new TableColumn<>("Salary");
    private final Button updateBtn = new Button("Commit Changes");
    private final Button deleteBtn = new Button("Status Update");
    private final HomeButton btnHome = new HomeButton("Home");
    private final TextField search = new TextField();
    private final ChoiceBox<String> choiceBox = new ChoiceBox<>();
    public ManageEmployeeView(HomeView prevView){
        this.prevView = prevView;

    }
    public Scene showView(Stage s)
    {
        s.setTitle("Manage Employees");
        //this.setStyle("-fx-background-color: #eed5d9");
       this.setPadding(new Insets(20,20,20,20));
        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        genderColumn.setMinWidth(100);
        genderColumn.setCellValueFactory(new PropertyValueFactory<User,Gender>("gender"));
        genderColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Gender.MALE, Gender.FEMALE, Gender.OTHER));


        usernameColumn.setMinWidth(100);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        //usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        emailColumn.setMinWidth(100);
        emailColumn.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        passwordColumn.setMinWidth(100);
        passwordColumn.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        accessLevelColumn.setMinWidth(100);
        accessLevelColumn.setCellValueFactory(new PropertyValueFactory<User,Role>("role"));
        accessLevelColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Role.MANAGER, Role.LIBRARIAN));


        phoneColumn.setMinWidth(100);
        phoneColumn.setCellValueFactory(new PropertyValueFactory<User,String>("phoneNumber"));
        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        salaryColumn.setMinWidth(100);
        salaryColumn.setCellValueFactory(new PropertyValueFactory<User,Double>("salary"));
        salaryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));



        VBox vbox = new VBox();
        vbox.setSpacing(30);
        HBox hBox1 = new HBox();
        hBox1.setSpacing(20);

        search.setPromptText("Search Username");
        choiceBox.getItems().addAll("Active", "Inactive");
        choiceBox.setValue("Active");



        tableView.getColumns().addAll(firstNameColumn,lastNameColumn,genderColumn,usernameColumn,emailColumn,passwordColumn,accessLevelColumn,phoneColumn ,salaryColumn);
        hBox1.getChildren().addAll(btnHome,choiceBox,search,updateBtn,deleteBtn);
        hBox1.setAlignment(Pos.CENTER);
        this.setTop(hBox1);
        this.setCenter(tableView);
        updateBtn.setStyle("-fx-background-color: #73c273");
        deleteBtn.setStyle("-fx-background-color: #9b5f30");



        Scene sc = new Scene(this, 800, 600);
        return sc;
    }

    public ChoiceBox<String> getChoiceBox() {
        return choiceBox;
    }

    public TableColumn<User, String> getFirstNameColumn() {
        return firstNameColumn;
    }

    public TableColumn<User, String> getLastNameColumn() {
        return lastNameColumn;
    }

    public TableColumn<User, Gender> getGenderColumn() {
        return genderColumn;
    }

    public TableColumn<User, String> getUsernameColumn() {
        return usernameColumn;
    }

    public TableColumn<User, String> getEmailColumn() {
        return emailColumn;
    }

    public TableColumn<User, String> getPasswordColumn() {
        return passwordColumn;
    }

    public TableColumn<User, Role> getAccessLevelColumn() {
        return accessLevelColumn;
    }

    public TableColumn<User, String> getPhoneColumn() {
        return phoneColumn;
    }

    public TableColumn<User, Double> getSalaryColumn() {
        return salaryColumn;
    }



    public TableView<User> getTableView()
    {
        return tableView;
    }

    public Button getDeleteBtn()
    {
        return deleteBtn;
    }
    public Button getUpdateBtn()
    {
        return updateBtn;
    }
    public HomeButton getHomeBtn()
    {
        return btnHome;
    }
    public HomeView getPrevView() {
        return prevView;
    }

    public TextField getSearch() {
        return search;
    }
}

