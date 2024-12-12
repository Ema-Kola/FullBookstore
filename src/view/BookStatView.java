package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.BookStatRecord;


public class BookStatView extends BorderPane {



        private final HomeView prevView;
        private final TableView<BookStatRecord> tableView = new TableView<>();
        private final TableColumn<BookStatRecord, String> bookNameColumn = new TableColumn<>("Book");
        private final TableColumn<BookStatRecord, String> authorColumn = new TableColumn<>("Author");
        private final TableColumn<BookStatRecord, String> categoryColumn = new TableColumn<>("Category");
        private final TableColumn<BookStatRecord, Integer> soldColumn= new TableColumn<>("Sold");
        private final TableColumn<BookStatRecord, Integer> boughtColumn = new TableColumn<>("Bought");
        private final TableColumn<BookStatRecord, String> isbnColumn = new TableColumn<>("ISBN");
        private final TableColumn<BookStatRecord, String> timePeriodColumn = new TableColumn<>("Period");

        private final ChoiceBox<String> choiceBox = new ChoiceBox<>();
       private final ChoiceBox<String> choiceBoxPeriod = new ChoiceBox<>();


        private final TextField searchBar = new TextField();

        private final HomeButton btnHome = new HomeButton("Home");
        private final Button btnSearch = new Button("Apply Filters");



        public BookStatView(HomeView prevView){
            this.prevView = prevView;

        }



        public Scene showView(Stage s) {
            s.setTitle("Book Stat");
            this.setPadding(new Insets(20,20,20,20));

            tableView.setEditable(true);
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            bookNameColumn.setMinWidth(200);
            bookNameColumn.setCellValueFactory(new PropertyValueFactory<BookStatRecord, String>("bookName"));
            bookNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

            isbnColumn.setMinWidth(150);
            isbnColumn.setCellValueFactory(new PropertyValueFactory<BookStatRecord, String>("isbn"));
            isbnColumn.setCellFactory(TextFieldTableCell.forTableColumn());


            authorColumn.setMinWidth(200);
            authorColumn.setCellValueFactory(new PropertyValueFactory<BookStatRecord, String>("authorName"));
            authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());


            categoryColumn.setMinWidth(200);
            categoryColumn.setCellValueFactory(new PropertyValueFactory<BookStatRecord, String>("category"));
            categoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());

            soldColumn.setMinWidth(100);
            soldColumn.setCellValueFactory(new PropertyValueFactory<BookStatRecord, Integer>("sold"));
            soldColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            boughtColumn.setMinWidth(100);
            boughtColumn.setCellValueFactory(new PropertyValueFactory<BookStatRecord, Integer>("bought"));
            boughtColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

            timePeriodColumn.setMinWidth(150);
            timePeriodColumn.setCellValueFactory(new PropertyValueFactory<BookStatRecord, String>("timeForColumn"));
            timePeriodColumn.setCellFactory(TextFieldTableCell.forTableColumn());



            choiceBox.getItems().addAll("Book", "Author", "Category");
            choiceBox.setValue("Book");
            choiceBoxPeriod.getItems().addAll("Daily","Monthly","Total");
            choiceBoxPeriod.setValue("Daily");

            searchBar.setPromptText("Search here!");

            VBox vbox = new VBox();
            vbox.setSpacing(30);
            HBox hBox1 = new HBox();
            hBox1.setSpacing(20);

            hBox1.getChildren().addAll(searchBar,new Label("Search by: "),choiceBox,choiceBoxPeriod,btnSearch);
            vbox.getChildren().addAll(btnHome,hBox1);

            this.setTop(vbox);
            this.setCenter(tableView);



            Scene sc = new Scene(this, 800, 600);
            return sc;
        }


        public HomeButton getBtnHome() {
            return btnHome;
        }

    public Button getBtnSearch() {
        return btnSearch;
    }

    public TableView<BookStatRecord> getTableView() {
            return tableView;
        }

        public HomeView getPrevView() {
            return prevView;
        }

    public TableColumn<BookStatRecord, String> getBookNameColumn() {
        return bookNameColumn;
    }

    public TableColumn<BookStatRecord, String> getAuthorColumn() {
        return authorColumn;
    }

    public TableColumn<BookStatRecord, String> getCategoryColumn() {
        return categoryColumn;
    }

    public TableColumn<BookStatRecord, Integer> getSoldColumn() {
        return soldColumn;
    }

    public TableColumn<BookStatRecord, Integer> getBoughtColumn() {
        return boughtColumn;
    }

    public TableColumn<BookStatRecord, String> getIsbnColumn() {
        return isbnColumn;
    }

    public TableColumn<BookStatRecord, String> getTimePeriodColumn() {
        return timePeriodColumn;
    }

    public ChoiceBox<String> getChoiceBox() {
        return choiceBox;
    }

    public TextField getSearchBar() {
        return searchBar;
    }

    public ChoiceBox<String> getChoiceBoxPeriod() {
        return choiceBoxPeriod;
    }
}
