package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Gender;


public class AddAuthorView  {

    private ValidatingTextField authorName = new ValidatingTextField(input -> input.matches("[a-zA-Z]+"));
    private Label authorNameLbl = new Label("Author's Name");
    private ValidatingTextField authorLastName = new ValidatingTextField(input -> input.matches("[a-zA-Z]+"));
    private Label authorLastNameLbl = new Label ("Author's LastName");
    private Label authorGender= new Label("Author's Gender");
    private ObservableList<Gender> options = FXCollections.observableArrayList(Gender.MALE, Gender.FEMALE, Gender.OTHER);
    private ComboBox<Gender> gender = new ComboBox(options);

    private final Label firstnameWarningLbl = new Label("*enter author's first name");
    private final Label lastnameWarningLbl = new Label("*enter author's last name");
    private final Label genderWarningLbl = new Label("*enter author's gender");
    private Button sbButton = new Button("Submit");


    public Scene showView(Stage stage)
    {
        stage.setTitle("AddAuthor");
        firstnameWarningLbl.visibleProperty().bind(authorName.isValid().not());
        lastnameWarningLbl.visibleProperty().bind(authorLastName.isValid().not());
        genderWarningLbl.visibleProperty().bind(gender.valueProperty().isNull());
        firstnameWarningLbl.setTextFill(Color.GRAY);
        firstnameWarningLbl.setFont(Font.font("Courier",  10));

        lastnameWarningLbl.setTextFill(Color.GRAY);
        lastnameWarningLbl.setFont(Font.font("Courier",  10));

        genderWarningLbl.setTextFill(Color.GRAY);
        genderWarningLbl.setFont(Font.font("Courier",  10));


        GridPane gr = new GridPane();
        gr.setHgap(10);
        gr.setVgap(10);
        gr.setPadding(new Insets(10,10,10,10));
        gr.setAlignment(Pos.CENTER);
        gr.add(authorNameLbl, 0, 0);
        gr.add(authorName, 1, 0);
        gr.add(firstnameWarningLbl, 2, 0);
        gr.add(authorLastNameLbl, 0, 1);
        gr.add(authorLastName, 1, 1);
        gr.add(lastnameWarningLbl, 2, 1);
        gr.add(authorGender, 0, 2);
        gr.add(gender, 1, 2);
        gr.add(genderWarningLbl, 2, 2);
        gr.add(sbButton, 1, 3);

        sbButton.setStyle("-fx-background-color: #73c273");


        return new Scene(gr,500,400);
    }

    public  String getAuthorName()
    {
        return authorName.getText();
    }

    public String getAuthorLastName()
    {
        return authorLastName.getText();
    }

    public Button getSubmitButton()
    {
        return sbButton;
    }

    public Gender getGender(){
        return gender.getValue();
    }

    public TextField getAuthorNameTf(){
        return authorName;
    }

    public TextField getAuthorSurnameTf(){
        return authorLastName;
    }

    public ComboBox<Gender> getGendercomboBox() {
        return gender;
    }
}
