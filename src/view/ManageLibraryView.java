package view;


import dao.BooksDAO;
import dao.CategoryDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Author;
import model.Book;
import javafx.util.converter.DoubleStringConverter;

public class ManageLibraryView extends BorderPane {


    private HomeView prevView;
    public TableView<Book> tableView = new TableView<>();
    private final HomeButton btnHome = new HomeButton("Home");
    private CategoryDAO categoryDao = new CategoryDAO();

    private final TableColumn <Book,String> isbnColumn = new TableColumn("ISBN");
    private final TableColumn<Book,String> titleColumn = new TableColumn("Title");
    private final TableColumn <Book,Author> authorColumn = new TableColumn("Author");
    private final TableColumn <Book,String> descriptionColumn = new TableColumn("Description");
    private final TableColumn<Book,String> categoryColumn = new TableColumn("Category");
    private final TableColumn<Book,String>  supplierColumn = new TableColumn("Supplier");
    private final TableColumn <Book,Double> sellingPriceColumn = new TableColumn("Selling Price");
    private final TableColumn <Book,Integer> stockColumn = new TableColumn("Stock");
    private final ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private final Button editBook = new Button("Commit Changes");
    private final Button deleteBook = new Button("Status Update");
    private final TextField searchBook = new TextField();

    public ManageLibraryView(HomeView prevView){

        this.prevView = prevView;
    }

    public Scene showView(Stage s)
    {
        s.setTitle("Manage Library");
        this.setPadding(new Insets(20,20,20,20));
        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        isbnColumn.setMinWidth(120);
        isbnColumn.setCellValueFactory(new PropertyValueFactory<Book,String>("isbn"));

        titleColumn.setMinWidth(100);
        titleColumn.setCellValueFactory(new PropertyValueFactory<Book,String>("title"));

        authorColumn.setMinWidth(100);
        authorColumn.setCellValueFactory(new PropertyValueFactory<Book,Author>("author"));


        descriptionColumn.setMinWidth(100);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Book,String>("description"));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        categoryColumn.setMinWidth(100);
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Book,String>("category"));
        categoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(categoryDao.getAll()));

        supplierColumn.setMinWidth(100);
        supplierColumn.setCellValueFactory(new PropertyValueFactory<Book,String>("supplier"));

        sellingPriceColumn.setMinWidth(100);
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<Book,Double>("sellingPrice"));
        sellingPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        stockColumn.setMinWidth(100);
        stockColumn.setCellValueFactory(new PropertyValueFactory<Book,Integer>("stock"));

        tableView.getColumns().addAll(isbnColumn,titleColumn,authorColumn,descriptionColumn,categoryColumn,supplierColumn,sellingPriceColumn,stockColumn);

        HBox hbox = new HBox();
        hbox.setSpacing(50);
        choiceBox.getItems().addAll("Active", "Inactive");
        choiceBox.setValue("Active");
        hbox.getChildren().addAll(btnHome,choiceBox,searchBook,editBook,deleteBook);
        hbox.setAlignment(Pos.CENTER);
        this.setTop(hbox);
        this.setCenter(tableView);
        editBook.setStyle("-fx-background-color: #73c273");
        deleteBook.setStyle("-fx-background-color: #9b5f30");



        Scene p = new Scene(this,800,600);
        return p;

    }

    public ChoiceBox<String> getChoiceBox() {
        return choiceBox;
    }

    public TextField getSearchBook() {
        return searchBook;
    }

    public TableColumn<Book,String> getDescriptionColumn()
    {
        return descriptionColumn;
    }
    public TableColumn<Book,String> getCategoryColumn()
    {
        return categoryColumn;
    }
    public TableColumn<Book,Double> getSellingPriceColumn()
    {
        return sellingPriceColumn;
    }
    public TableView<Book> getTableView()
    {
        return tableView;
    }

    public HomeButton getHomeBtn()
    {
        return btnHome;
    }
    public HomeView getPrevView() {
        return prevView;
    }

    public Button getEditBtn()
    {
        return editBook;
    }
    public Button getDeleteButton()
    {
        return deleteBook;
    }
}
