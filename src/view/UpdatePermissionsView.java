package view;

import dao.PermissionsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Role;


public class UpdatePermissionsView {
    private ComboBox<Role>  checkOutBook = new ComboBox<>();
    private ComboBox<Role>  addBook = new ComboBox<>();
    private ComboBox<Role>  bookStat = new ComboBox<>();
    private ComboBox<Role>  manageEmployees = new ComboBox<>();
    private ComboBox<Role>  librarianStat = new ComboBox<>();
    private ComboBox<Role>  financeStat= new ComboBox<>();
    private ComboBox<Role>  manageLibrary= new ComboBox<>();
    private Button updateBtn = new Button("Update");
    private HomeButton homeBtn = new HomeButton("Home");

    public Scene showView(Stage stage)
    {
        PermissionsDAO permissionsDAO=new PermissionsDAO();
        stage.setTitle("Update Permissions");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(20);
        gridPane.add(homeBtn, 0,0);
        gridPane.add(new Label("CheckOutBook"), 0,1);
        gridPane.add(checkOutBook, 1,1);
        gridPane.add(new Label("AddBook"), 0,2);
        gridPane.add(addBook, 1,2);
        gridPane.add(new Label("BookStat"), 0,3);
        gridPane.add(bookStat, 1,3);
        gridPane.add(new Label("ManageEmployees"), 0,4);
        gridPane.add( manageEmployees, 1,4);
        gridPane.add(new Label("LibrarianStat"), 0,5);
        gridPane.add( librarianStat, 1,5);
        gridPane.add(new Label("FinanceStat"), 0,6);
        gridPane.add(financeStat, 1,6);
        gridPane.add(new Label("ManageLibrary"), 0,7);
        gridPane.add(manageLibrary, 1,7);
        gridPane.add(updateBtn, 1,8);
        ObservableList<Role> roles =FXCollections.observableArrayList(Role.ADMINISTRATOR,Role.MANAGER,Role.LIBRARIAN);
        checkOutBook.setItems(roles);
        checkOutBook.setValue(permissionsDAO.getCheckOutBookPermission());
        addBook.setItems(roles);
        addBook.setValue(permissionsDAO.getAddBookPermission());
        bookStat.setItems(roles);
        bookStat.setValue(permissionsDAO.getBookStatsPermission());
        manageEmployees.setItems(roles);
        manageEmployees.setValue(permissionsDAO.getManageEmployeesPermission());
        librarianStat.setItems(roles);
        librarianStat.setValue(permissionsDAO.getLibrarianStatsPermission());
        financeStat.setItems(roles);
        financeStat.setValue(permissionsDAO.getFinanceStatsPermission());
        manageLibrary.setItems(roles);
        manageLibrary.setValue(permissionsDAO.getManageLibraryPermission());

        gridPane.setAlignment(Pos.CENTER);
        updateBtn.setStyle("-fx-background-color: #73c273");

        return new Scene(gridPane,800,600);
    }

    public Button getUpdateBtn() {
        return updateBtn;
    }

    public HomeButton getHomeBtn() {
        return homeBtn;
    }

    public ComboBox<Role> getCheckOutBook() {
        return checkOutBook;
    }

    public ComboBox<Role> getAddBook() {
        return addBook;
    }

    public ComboBox<Role> getBookStat() {
        return bookStat;
    }

    public ComboBox<Role> getManageEmployees() {
        return manageEmployees;
    }

    public ComboBox<Role> getLibrarianStat() {
        return librarianStat;
    }

    public ComboBox<Role> getFinanceStat() {
        return financeStat;
    }

    public ComboBox<Role> getManageLibrary() {
        return manageLibrary;
    }
}
