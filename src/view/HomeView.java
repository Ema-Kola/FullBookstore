
package view;

import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.User;
import model.Role;
import dao.PermissionsDAO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class HomeView extends Parent {
    private final User currentUser;
    private UserController prevView;
    ButtonWithPermission addUserButton;
    ButtonWithPermission addBookButton;
    ButtonWithPermission checkOutBookButton;
    ButtonWithPermission bookStatsButton;
    ButtonWithPermission librarianStatsButton;
    ButtonWithPermission financeStatsButton;
    ButtonWithPermission manageEmployeeButton;
    ButtonWithPermission paycheckButton;
    ButtonWithPermission updatePermissionsButton;
    ButtonWithPermission manageLibraryButton;
    ButtonWithPermission searchBookButton;
    private Button logOutButton = new Button("LogOut");



    public User getCurrentUser() {
        return currentUser;
    }


    public HomeView(User currentUser, UserController prevView){
        this.currentUser = currentUser;
        this.prevView=prevView;
        PermissionsDAO permissionsDAO = new PermissionsDAO();

        addUserButton= new ButtonWithPermission(permissionsDAO.getManageEmployeesPermission(),currentUser.getRole(),"+ Add User");

        addBookButton= new ButtonWithPermission(permissionsDAO.getAddBookPermission(), currentUser.getRole(),"+ Add Book");

        checkOutBookButton= new ButtonWithPermission(permissionsDAO.getCheckOutBookPermission(), currentUser.getRole(),"CheckOut Book");

         bookStatsButton= new ButtonWithPermission(permissionsDAO.getBookStatsPermission(), currentUser.getRole(),"Book Statistics");

        librarianStatsButton= new ButtonWithPermission(permissionsDAO.getLibrarianStatsPermission(), currentUser.getRole(),"Librarian Stats");

        financeStatsButton= new ButtonWithPermission(permissionsDAO.getFinanceStatsPermission(), currentUser.getRole(),"Finance Stats");

        manageEmployeeButton = new ButtonWithPermission(permissionsDAO.getManageEmployeesPermission(), currentUser.getRole(),"Manage Employees");

        paycheckButton= new ButtonWithPermission(Role.ADMINISTRATOR,currentUser.getRole(),"Paycheck Release");

        updatePermissionsButton = new ButtonWithPermission(Role.ADMINISTRATOR,currentUser.getRole(),"Update Permissions");

        manageLibraryButton = new ButtonWithPermission(permissionsDAO.getManageLibraryPermission(),currentUser.getRole(),"Manage Library");
        searchBookButton = new ButtonWithPermission(Role.LIBRARIAN,currentUser.getRole(), " Search Book");


    }
    public Scene showView(Stage st) {
        st.setTitle("Home");
        VBox mainBox = new VBox();


        mainBox.setPadding(new Insets(50,40,30,30));
        mainBox.setSpacing(30);

        //InnerShadow is= new InnerShadow();
        //is.setOffsetX(4.0f);
       // is.setOffsetY(4.0F);
        Label welcomeLbl = new Label();
        welcomeLbl.setFont(Font.font("Montserrat",FontWeight.BOLD,30));
        //welcomeLbl.setEffect(is);

        welcomeLbl.setText("Welcome "+getCurrentUser().getFirstName()+" "+getCurrentUser().getLastName()+"!");


        try(FileInputStream input = new FileInputStream("files/images/stati.png")) {
            Image stat = new Image(input);

            ImageView bookStatImg = new ImageView(stat);
            bookStatImg.setFitHeight(15);
            bookStatImg.setFitWidth(15);
            bookStatsButton.setGraphic(bookStatImg);

            ImageView libStatImg = new ImageView(stat);
            libStatImg.setFitHeight(15);
            libStatImg.setFitWidth(15);
            librarianStatsButton.setGraphic(libStatImg);

            ImageView financeStatImg = new ImageView(stat);
            financeStatImg.setFitHeight(15);
            financeStatImg.setFitWidth(15);
            financeStatsButton.setGraphic(financeStatImg);

        }catch (IOException i)  {
            System.out.println("Could not find image.");
        }


        try(FileInputStream input2= new FileInputStream("files/images/lupa.png")) {
            Image lupa = new Image(input2);
            ImageView searchImg = new ImageView(lupa);
            searchImg.setFitHeight(15);
            searchImg.setFitWidth(15);
            searchBookButton.setGraphic(searchImg);
        }catch (IOException i)  {
            System.out.println("Could not find image.");
        }

        try(FileInputStream input4= new FileInputStream("files/images/logOut.png")) {
            Image logOut = new Image(input4);
            ImageView logImg = new ImageView(logOut);
            logImg.setFitHeight(15);
            logImg.setFitWidth(15);
            logOutButton.setGraphic(logImg);
        }catch (IOException i)  {
            System.out.println("Could not find image.");
        }

        try(FileInputStream input3 = new FileInputStream("files/images/libri.png")) {
            Image book = new Image(input3);
            ImageView bookImg = new ImageView(book);
            bookImg.setFitWidth(18);
            bookImg.setFitHeight(18);
            checkOutBookButton.setGraphic(bookImg);
        }catch (IOException i)  {
            System.out.println("Could not find image.");
        }

        HBox resetbackBox = new HBox(welcomeLbl, logOutButton);
        resetbackBox.setSpacing(380);
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        HBox topHBox = new HBox(welcomeLbl, region, logOutButton);


        logOutButton.setAlignment(Pos.BOTTOM_RIGHT);
        welcomeLbl.setAlignment(Pos.TOP_LEFT);

        logOutButton.setStyle("-fx-background-radius: 7em;");
        mainBox.getChildren().add(topHBox);

        welcomeLbl.setAlignment(Pos.CENTER);

        //manageEmployeeButton.setStyle("-fx-background-color: #6e6d6d");
        //manageLibraryButton.setStyle("-fx-background-color: #6e6d6d");
        VBox operations = new VBox();
        operations.getChildren().add(checkOutBookButton);
        operations.getChildren().add(addBookButton);
        operations.getChildren().add(addUserButton);
        operations.getChildren().add(searchBookButton);
        operations.getChildren().add(bookStatsButton);
        operations.getChildren().add(librarianStatsButton);
        operations.getChildren().add(financeStatsButton);
        operations.getChildren().add(manageLibraryButton);
        operations.getChildren().add(manageEmployeeButton);
        operations.getChildren().add(updatePermissionsButton);
        operations.getChildren().add(paycheckButton);

        HBox g = new HBox();
        g.getChildren().add(operations);
        try(FileInputStream input1 = new FileInputStream("files/images/bb1-removebg-preview.png");) {
            Image books = new Image(input1);
            ImageView bookStack = new ImageView(books);
            bookStack.setFitWidth(450);
            bookStack.setFitHeight(450);
            g.getChildren().add(bookStack);

        }catch (IOException i)  {
            System.out.println("Could not find image.");
        }


        g.setAlignment(Pos.CENTER);
        operations.setSpacing(10);
        g.setSpacing(50);
        //g.setStyle("-fx-background-color: white");
        mainBox.getChildren().add(g);
        //this.setStyle("-fx-background-color: #e0e3f4;");
        mainBox.setStyle("-fx-padding: 10;" +
               // "-fx-background-color: #e0e3f4;"+
                "-fx-border-width: 2;" +
                "-fx-border-insets: 30;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: black;");
        Scene sc = new Scene(mainBox, 800, 600);



        return sc;
    }

    public UserController getPrevView() {
        return prevView;
    }

    public ButtonWithPermission getLibrarianStatsButton() {
        return librarianStatsButton;
    }

    public ButtonWithPermission getBookStatsButton() {
        return bookStatsButton;
    }

    public ButtonWithPermission getCheckOutBookButton() {
        return checkOutBookButton;
    }

    public ButtonWithPermission getAddBookButton() {
        return addBookButton;
    }

    public ButtonWithPermission getAddUserButton() {
        return addUserButton;
    }

    public ButtonWithPermission getFinanceStatsButton() {
        return financeStatsButton;
    }

    public ButtonWithPermission getPaycheckButton() { return paycheckButton; }
    public ButtonWithPermission getUpdatePermissionsButton(){ return updatePermissionsButton; }

    public ButtonWithPermission getManageEmployeeButton() {
        return manageEmployeeButton;
    }

    public ButtonWithPermission getManageLibraryButton() {
        return manageLibraryButton;
    }

    public ButtonWithPermission getSearchBookButton() {
        return searchBookButton;
    }

    public Button getLogOutButton() {
        return logOutButton;
    }



}


/*
package view;

import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import model.Role;
import dao.PermissionsDAO;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HomeView extends Parent {
    private final User currentUser;
    private UserController prevView;
    ButtonWithPermission addUserButton;
    ButtonWithPermission addBookButton;
    ButtonWithPermission checkOutBookButton;
    ButtonWithPermission bookStatsButton;
    ButtonWithPermission librarianStatsButton;
    ButtonWithPermission financeStatsButton;
    ButtonWithPermission manageEmployeeButton;
    ButtonWithPermission paycheckButton;
    ButtonWithPermission updatePermissionsButton;
    ButtonWithPermission manageLibraryButton;
    ButtonWithPermission searchBookButton;
    private Button logOutButton = new Button("LogOut");



    public User getCurrentUser() {
        return currentUser;
    }


    public HomeView(User currentUser, UserController prevView){
        this.currentUser = currentUser;
        this.prevView=prevView;
        //currentUser.setRole(Role.ADMINISTRATOR); per test
        PermissionsDAO permissionsDAO = new PermissionsDAO();

        addUserButton= new ButtonWithPermission(permissionsDAO.getManageEmployeesPermission(),currentUser.getRole(),"Add User");

        addBookButton= new ButtonWithPermission(permissionsDAO.getAddBookPermission(), currentUser.getRole(),"Add Book");

        checkOutBookButton= new ButtonWithPermission(permissionsDAO.getCheckOutBookPermission(), currentUser.getRole(),"CheckOut Book");

        bookStatsButton= new ButtonWithPermission(permissionsDAO.getBookStatsPermission(), currentUser.getRole(),"Book Stats");

        librarianStatsButton= new ButtonWithPermission(permissionsDAO.getLibrarianStatsPermission(), currentUser.getRole(),"Librarian Stats");

        financeStatsButton= new ButtonWithPermission(permissionsDAO.getFinanceStatsPermission(), currentUser.getRole(),"Finance Stats");

        manageEmployeeButton = new ButtonWithPermission(permissionsDAO.getManageEmployeesPermission(), currentUser.getRole(),"Manage Employees");

        paycheckButton= new ButtonWithPermission(Role.ADMINISTRATOR,currentUser.getRole(),"Paycheck Release");

        updatePermissionsButton = new ButtonWithPermission(Role.ADMINISTRATOR,currentUser.getRole(),"Update Permissions");

        manageLibraryButton = new ButtonWithPermission(permissionsDAO.getManageLibraryPermission(),currentUser.getRole(),"Manage Library");
        searchBookButton = new ButtonWithPermission(Role.LIBRARIAN,currentUser.getRole(), "Search Book");


    }

    public UserController getPrevView() {
        return prevView;
    }

    public ButtonWithPermission getLibrarianStatsButton() {
        return librarianStatsButton;
    }

    public ButtonWithPermission getBookStatsButton() {
        return bookStatsButton;
    }

    public ButtonWithPermission getCheckOutBookButton() {
        return checkOutBookButton;
    }

    public ButtonWithPermission getAddBookButton() {
        return addBookButton;
    }

    public ButtonWithPermission getAddUserButton() {
        return addUserButton;
    }

    public ButtonWithPermission getFinanceStatsButton() {
        return financeStatsButton;
    }

    public ButtonWithPermission getPaycheckButton() { return paycheckButton; }
    public ButtonWithPermission getUpdatePermissionsButton(){ return updatePermissionsButton; }

    public ButtonWithPermission getManageEmployeeButton() {
        return manageEmployeeButton;
    }

    public ButtonWithPermission getManageLibraryButton() {
        return manageLibraryButton;
    }

    public ButtonWithPermission getSearchBookButton() {
        return searchBookButton;
    }

    public Button getLogOutButton() {
        return logOutButton;
    }

    public Scene showView(Stage st) {
        st.setTitle("Home");
        GridPane g = new GridPane();


        g.setPadding(new Insets(20,30,20,20));
        Label frame = new Label();
        frame.setFont(new Font("Tim",20));
        InnerShadow is= new InnerShadow();
        is.setOffsetX(4.0f);
        is.setOffsetY(4.0F);
        Label frame1 = new Label();
        frame1.setFont(Font.font(null,FontWeight.BOLD,50));
        frame1.setEffect(is);
        frame.setText("Library Management System");
        frame1.setText("Welcome "+getCurrentUser().getFirstName()+" "+getCurrentUser().getLastName()+"!");

        FileInputStream input = null;
        FileInputStream input1 = null;
        FileInputStream input2 = null ;
        FileInputStream input3 = null;
        try {
            input = new FileInputStream("files/images/statistik.jpg");
            input2= new FileInputStream("files/images/lupa.png");
            input1 = new FileInputStream("files/images/stackBooks.png");
            input3 = new FileInputStream("files/images/libri.webp");

        }catch (FileNotFoundException e ){
            System.out.println("Could not find image.");
        }

       Image stat = new Image(input);
       ImageView bookStatImg = new ImageView(stat);
        bookStatImg.setFitHeight(15);
       bookStatImg.setFitWidth(15);
      bookStatsButton.setGraphic(bookStatImg);

       ImageView libStatImg = new ImageView(stat);
       libStatImg.setFitHeight(15);
      libStatImg.setFitWidth(15);
       librarianStatsButton.setGraphic(libStatImg);

       ImageView financeStatImg = new ImageView(stat);
       financeStatImg.setFitHeight(15);
       financeStatImg.setFitWidth(15);
       financeStatsButton.setGraphic(financeStatImg);


        Image search = new Image(input2);
        ImageView searchBookImg = new ImageView(search);
        searchBookImg.setFitHeight(15);
        searchBookImg.setFitWidth(15);
        searchBookButton.setGraphic(searchBookImg);

        Image book = new Image(input3);
        ImageView booksImg = new ImageView(book);
        booksImg.setFitHeight(15);
        booksImg.setFitWidth(15);
        addBookButton.setGraphic(booksImg);

        //bookStatsButton.setPrefSize(70,30);
        //bookStatsButton.setStyle("-fx-background-color: white");

        //g.add(frame1,0,0);
        g.add(frame1,0,1);
        frame1.setAlignment(Pos.CENTER);
        frame1.setAlignment(Pos.CENTER);
         Image books = new Image(input1);
        ImageView bookStack = new ImageView(books);
        bookStack.setFitWidth(400);
        bookStack.setFitHeight(400);
        bookStack.fitHeightProperty().bind(st.heightProperty());
        bookStack.fitWidthProperty().bind(st.widthProperty());
        VBox operations = new VBox();

        operations.getChildren().add(addUserButton);
        operations.getChildren().add(addBookButton);
        operations.getChildren().add(checkOutBookButton);
        operations.getChildren().add(bookStatsButton);
        //bookStatsButton.setStyle("-fx-background-color: #99e799");
        operations.getChildren().add(librarianStatsButton);
        //librarianStatsButton.setStyle("-fx-background-color: #99e799");
        operations.getChildren().add(financeStatsButton);
        //financeStatsButton.setStyle("-fx-background-color: #99e799");
        operations.getChildren().add(paycheckButton);
        operations.getChildren().add(manageEmployeeButton);
        operations.getChildren().add(updatePermissionsButton);
        operations.getChildren().add(manageLibraryButton);
        operations.getChildren().add(searchBookButton);
        //operations.setAlignment(Pos.CENTER);


        g.add(operations,0,2);
        g.add(bookStack,1,2);
        g.add(logOutButton,1,3);
        operations.setAlignment(Pos.CENTER);
        operations.setSpacing(10);
        g.setVgap(30);
        g.setStyle("-fx-background-color: white");
        Scene sc = new Scene(g, 800, 600);



        return sc;
    }

}

 */

