package view;


import dao.BooksDAO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Author;
import model.Book;


public class SearchBookView extends BorderPane{

    private HomeView prevView;
    public TableView<Book> tableView = new TableView<>();
    private final HomeButton btnHome = new HomeButton("Home");


    private final TableColumn <Book,StringProperty> isbnColumn = new TableColumn("ISBN");
    private final TableColumn<Book,StringProperty> titleColumn = new TableColumn("Title");
    private final TableColumn <Book,Author> authorColumn = new TableColumn("Author");
    private final TableColumn <Book,StringProperty> descriptionColumn = new TableColumn("Description");
    private final TableColumn<Book,StringProperty> categoryColumn = new TableColumn("Category");
    private final TableColumn<Book,String>  supplierColumn = new TableColumn("Supplier");
    private final TableColumn<Book,Double>  purchasedPriceColumn = new TableColumn("Purchased Price");
    private final TableColumn <Book,DoubleProperty> sellingPriceColumn = new TableColumn("Selling Price");
    private final TableColumn <Book,IntegerProperty> stockColumn = new TableColumn("Stock");
    public SearchBookView(HomeView prevView){

        this.prevView = prevView;
    }
    private final TextField searchBook = new TextField();

    public Scene showView(Stage s)
    {
        this.setPadding(new Insets(20,20,20,20));

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        isbnColumn.setMinWidth(100);
        isbnColumn.setCellValueFactory(new PropertyValueFactory<Book,StringProperty>("isbn"));

        titleColumn.setMinWidth(100);
        titleColumn.setCellValueFactory(new PropertyValueFactory<Book,StringProperty>("title"));

        authorColumn.setMinWidth(100);
        authorColumn.setCellValueFactory(new PropertyValueFactory<Book,Author>("author"));

        descriptionColumn.setMinWidth(100);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Book,StringProperty>("description"));

        categoryColumn.setMinWidth(100);
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Book,StringProperty>("category"));

        supplierColumn.setMinWidth(100);
        supplierColumn.setCellValueFactory(new PropertyValueFactory<Book,String>("supplier"));

        purchasedPriceColumn.setMinWidth(100);
        purchasedPriceColumn.setCellValueFactory(new PropertyValueFactory<Book,Double>("purchasedPrice"));

        sellingPriceColumn.setMinWidth(100);
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<Book,DoubleProperty>("sellingPrice"));

        stockColumn.setMinWidth(100);
        stockColumn.setCellValueFactory(new PropertyValueFactory<Book,IntegerProperty>("stock"));

        tableView.getColumns().addAll(isbnColumn,titleColumn,authorColumn,descriptionColumn,categoryColumn,supplierColumn,purchasedPriceColumn,sellingPriceColumn,stockColumn);



        searchBook.setPromptText("Search Book");
        HBox hBox = new HBox();
        hBox.setSpacing(50);
        hBox.getChildren().addAll(btnHome,new Label("Search"),searchBook);
        this.setTop(hBox);
        hBox.setAlignment(Pos.TOP_CENTER);

        this.setCenter(tableView);



        Scene st = new Scene(this,800,600);
        return st;

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

    public TextField getSearch() {
        return searchBook;
    }
}
