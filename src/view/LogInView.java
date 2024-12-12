package view;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;


public class LogInView extends Parent {
    private TextField emailF = new TextField();
    private PasswordField passwF = new PasswordField();
    private Button loginBtn = new Button("Log in");
    private Text wrongUsernameF=new Text();
    private Text wrongPasswordF=new Text();
    private Stage stage;

    public Scene showView(Stage stage) {
        this.stage=stage;
        loginBtn.setDefaultButton(true);


        loginBtn.setMinHeight(20);
        loginBtn.setMinWidth(120);
        loginBtn.setStyle("-fx-background-radius: 7em; ");



        VBox vbox = new VBox();
        Label frame2 = new Label("Bookstore Management System");
        frame2.setFont(new Font("Elephant",30));

        GridPane p = new GridPane();
        p.setHgap(10);
        p.setVgap(10);
        p.setPadding(new Insets(10, 10, 10, 10));
        p.setAlignment(Pos.CENTER);

        wrongUsernameF.setFill(Color.RED);
        wrongUsernameF.setFont(Font.font("Courier",  10));
        emailF.setPromptText("Username");



        try(FileInputStream input1 = new FileInputStream("files/images/password.png")) {
            Image passIcon = new Image(input1);
            ImageView pass = new ImageView(passIcon);
            pass.setFitHeight(27);
            pass.setFitWidth(38);
            p.add(pass, 0, 1);
        }catch (IOException i)  {
            System.out.println("Could not find image.");
        }

        p.add(emailF, 1, 0);
        p.add(wrongUsernameF,2,0);


        wrongPasswordF.setFont(Font.font("Courier",  10));
        wrongPasswordF.setFill(Color.RED);

        p.add(passwF, 1, 1);
        p.add(wrongPasswordF,2,1);

        HBox h = new HBox();
        h.getChildren().add(loginBtn);
        h.setSpacing(10);
        p.add(h, 1, 3);

        try(FileInputStream input= new FileInputStream("files/images/username.png")) {
            Image usernameIcon = new Image(input);
            ImageView usernameBig = new ImageView(usernameIcon);
            ImageView usernameSmall = new ImageView(usernameIcon);
            usernameSmall.setFitHeight(42);
            usernameSmall.setFitWidth(42);
            p.add(usernameSmall, 0, 0);
            usernameBig.setFitHeight(200);
            usernameBig.setFitWidth(200);
            vbox.getChildren().add(usernameBig);
        }catch (IOException i)  {
            System.out.println("Could not find image.");
        }

        vbox.getChildren().addAll(frame2,p);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(50);
        //p.setStyle("-fx-background-color: #e0e3f4;");
        //h.setStyle("-fx-background-color: #e0e3f4;");
        vbox.setStyle("-fx-padding: 10;" +
               // "-fx-background-color: #e0e3f4;"+
                "-fx-border-width: 2;" +
                "-fx-border-insets: 30;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: black;");
        return new Scene(vbox, 800, 600);

    }

    public TextField getEmailF() {
        return emailF;
    }
    public PasswordField getPasswF() {
        return passwF;
    }
    public Button getLoginBtn(){
        return loginBtn;
    }
    public Text getWrongUsernameF() {
        return wrongUsernameF;
    }
    public Text getWrongPasswordF() {
        return wrongPasswordF;
    }


    public Stage getStage(){
        return this.stage;
    }



}
