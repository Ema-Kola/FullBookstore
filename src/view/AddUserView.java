package view;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import dao.UsersDAO;
import javafx.stage.Stage;
import model.CustomFunctions;
import model.Gender;
import model.Role;
import model.User;



public class AddUserView extends GridPane {


    private HomeView prevView;
    private final ValidatingTextField FirstnameTF = new ValidatingTextField(input -> input.matches("[a-zA-Z]+"));
    private final Label FirstnameLbl = new Label("First Name");
    private final ValidatingTextField LastnameTF = new ValidatingTextField(input -> input.matches("[a-zA-Z]+"));
    private final Label LastnameLbl = new Label("Last Name");
    //private final ValidatingTextField birthdayTF = new ValidatingTextField(input -> input.matches("^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(\\d{4})$"));
    private final ValidatingTextField birthdayTF = new ValidatingTextField(input -> CustomFunctions.convertDate(input)!=null);
    private final Label birthdayLbl = new Label("Birthdate");
    private final ComboBox<Gender> gender = new ComboBox<>();
    private final Label genderLbl = new Label("Gender");
    private final ValidatingTextField UsernameTF = new ValidatingTextField(input -> input.matches(".+"));
    private final Label UsernameLbl = new Label("Username");
    private final ValidatingTextField phoneTF = new ValidatingTextField(input -> input.matches("\\+3556[7-9]\\d{3}\\d{2}\\d{2}"));
    private final Label phoneLbl = new Label("Phone Number");
    private final ValidatingTextField emailTF = new ValidatingTextField(input -> input.matches("[a-zA-Z]{2,}\\@lib\\.com"));
    private final Label emailLbl = new Label("Email");
    private final PasswordField emailPassTF = new PasswordField();
    private final Label emailPassLbl = new Label("Email Password");
    private final ValidatingTextField salaryTF = new ValidatingTextField(input -> input.matches("[0-9]+[.,]?[0-9]*"));
    private final Label salaryLbl = new Label("Salary");
    private final ComboBox<Role> accessLevelTF = new ComboBox<>();
    private final Label accessLevelLLbl = new Label("Access Level");
    private Button submitBtn = new Button("Submit");
    private HomeButton backToHomeBtn = new HomeButton("Home");
    private UsersDAO user = new UsersDAO();

    private final Label firstnameWarningLbl = new Label("*enter first name");
    private final Label lastnameWarningLbl = new Label("*enter last name");
    private final Label birthdayWarningLbl = new Label("*enter valid birthdate");
    private final Label genderWarningLbl = new Label("*choose gender");
    private final Label usernameWarningLbl = new Label("*enter username");
    private final Label phoneWarningLbl = new Label("*enter phone number");
    private final Label emailWarningLbl = new Label("*enter email");
    private final Label emailPassWarningLbl = new Label("*enter password");
    private final Label salaryWarningLbl = new Label("*enter salary");
    private final Label accessWarningLbl = new Label("*choose role");




    public AddUserView(HomeView prevView)
    {
        this.prevView = prevView;
        submitBtn.disableProperty().bind(gender.valueProperty().isNull().or(FirstnameTF.isValid().not()).or(accessLevelTF.valueProperty().isNull()).or(emailPassTF.textProperty().isEmpty()).or(LastnameTF.isValid().not()).or(phoneTF.isValid().not()).or(UsernameTF.isValid().not()).or(emailTF.isValid().not()).or(birthdayTF.isValid().not()).or(salaryTF.isValid().not()));
        accessLevelTF.getItems().setAll(Role.LIBRARIAN,Role.MANAGER);
        gender.getItems().setAll(Gender.values());
        gender.setPromptText("Gender");
        accessLevelTF.setPromptText("Access Level");
        emailTF.setPromptText("username@lib.com");
        phoneTF.setText("+3556");
        birthdayTF.setPromptText("DD-MM-YYYY");

        firstnameWarningLbl.visibleProperty().bind(FirstnameTF.isValid().not());
        lastnameWarningLbl.visibleProperty().bind(LastnameTF.isValid().not());
        birthdayWarningLbl.visibleProperty().bind(birthdayTF.isValid().not());
        usernameWarningLbl.visibleProperty().bind(UsernameTF.isValid().not());
        phoneWarningLbl.visibleProperty().bind(phoneTF.isValid().not());
        emailWarningLbl.visibleProperty().bind(emailTF.isValid().not());
        emailPassWarningLbl.visibleProperty().bind(emailPassTF.textProperty().isEmpty());
        salaryWarningLbl.visibleProperty().bind(salaryTF.isValid().not());
        accessWarningLbl.visibleProperty().bind(accessLevelTF.valueProperty().isNull());
        genderWarningLbl.visibleProperty().bind(gender.valueProperty().isNull());

        firstnameWarningLbl.setTextFill(Color.GRAY);
        firstnameWarningLbl.setFont(Font.font("Courier",  10));

        lastnameWarningLbl.setTextFill(Color.GRAY);
        lastnameWarningLbl.setFont(Font.font("Courier",  10));


        birthdayWarningLbl.setTextFill(Color.GRAY);
        birthdayWarningLbl.setFont(Font.font("Courier",  10));

        genderWarningLbl.setTextFill(Color.GRAY);
        genderWarningLbl.setFont(Font.font("Courier",  10));


        usernameWarningLbl.setTextFill(Color.GRAY);
        usernameWarningLbl.setFont(Font.font("Courier",  10));

        phoneWarningLbl.setTextFill(Color.GRAY);
        phoneWarningLbl.setFont(Font.font("Courier",  10));

        emailWarningLbl.setTextFill(Color.GRAY);
        emailWarningLbl.setFont(Font.font("Courier",  10));

        emailPassWarningLbl.setTextFill(Color.GRAY);
        emailPassWarningLbl.setFont(Font.font("Courier",  10));

        salaryWarningLbl.setTextFill(Color.GRAY);
        salaryWarningLbl.setFont(Font.font("Courier",  10));


        accessWarningLbl.setTextFill(Color.GRAY);
        accessWarningLbl.setFont(Font.font("Courier",  10));


        submitBtn.setStyle("-fx-background-color: #73c273");

    }





    public UsersDAO getUser()
    {
        return user;
    }
    public PasswordField getPasswTF() {
        return emailPassTF;
    }
    public ValidatingTextField getFirstnameTF()
    {
        return FirstnameTF;
    }
    public ValidatingTextField getLastnameTF()
    {
        return LastnameTF;
    }

    public ValidatingTextField getUsername()
    {
        return UsernameTF;
    }
    public ValidatingTextField getbirthdayTF()
    {
        return birthdayTF;
    }
    public ValidatingTextField getphoneTF()
    {
        return phoneTF;
    }
    public ValidatingTextField getemailTF()
    {
        return emailTF;
    }
    public ComboBox<Role> getaccessLevelTF()
    {
        return accessLevelTF;
    }
    public ComboBox<Gender> getGender()
    {
        return gender;
    }

    public ValidatingTextField getsalaryTF()
    {
        return salaryTF;
    }

    public Button getSubmitBtn() {
        return submitBtn;
    }
    public HomeButton getHomeBtn() {
        return backToHomeBtn;
    }


    public Scene showView(Stage s) {
        s.setTitle("Add User");
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 10, 0, 10));
        this.setVgap(10);
        this.setHgap(10);



        this.add(backToHomeBtn, 0, 0);
        this.add(FirstnameLbl, 0, 1);
        this.add( firstnameWarningLbl, 2, 1);
        this.add(FirstnameTF, 1, 1);
        this.add(LastnameLbl, 0, 2);
        this.add(lastnameWarningLbl, 2, 2);
        this.add(LastnameTF, 1, 2);
        this.add(birthdayLbl, 0, 3);
        this.add(birthdayTF, 1, 3);
        this.add(birthdayWarningLbl,2, 3);
        this.add(genderLbl, 0, 4);
        this.add(gender, 1, 4);
        this.add(genderWarningLbl, 2, 4);
        this.add(UsernameLbl, 0, 5);
        this.add(UsernameTF, 1, 5);
        this.add(usernameWarningLbl, 2, 5);
        this.add(phoneLbl, 0, 6);
        this.add(phoneTF, 1, 6);
        this.add(phoneWarningLbl, 2, 6);
        this.add(emailLbl, 0, 7);
        this.add(emailTF, 1, 7);
        this.add(emailWarningLbl, 2, 7);
        this.add(emailPassLbl, 0, 8);
        this.add(emailPassTF, 1, 8);
        this.add(emailPassWarningLbl, 2, 8);
        this.add(salaryLbl, 0, 9);
        this.add(salaryTF, 1, 9);
        this.add(salaryWarningLbl, 2, 9);
        this.add(accessLevelLLbl, 0, 10);
        this.add(accessLevelTF, 1, 10);
        this.add(accessWarningLbl, 2, 10);
        this.add(submitBtn, 1,11);



        backToHomeBtn.setOnAction(e -> {
            s.setScene(prevView.showView(s));
        });

        Scene sc = new Scene(this, 800, 600);
        return sc;
    }


}

