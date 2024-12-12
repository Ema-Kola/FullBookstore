package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.LibrarianStatRecord;

public class LibrarianStatView extends BorderPane{


        private HomeView prevView;
        private final TableView<LibrarianStatRecord> tableView = new TableView<>();
        private final TableColumn<LibrarianStatRecord, String> nameColumn = new TableColumn<>("Name");
        private final TableColumn<LibrarianStatRecord, Integer> billsColumn = new TableColumn<>("Total Bills");
        private final TableColumn<LibrarianStatRecord, Integer> booksColumn = new TableColumn<>("Total Books");
        private final TableColumn<LibrarianStatRecord, Double> incomeColumn = new TableColumn<>("Total Income");
        private final ValidatingTextField fromTF = new ValidatingTextField(input -> (input.matches("^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(\\d{4})$") || input.isEmpty()));
        private final ValidatingTextField toTF = new ValidatingTextField(input -> (input.matches("^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(\\d{4})$")|| input.isEmpty()));

        private final Button search = new Button("Search");
        private Text wrongDate1 = new Text("* dd-mm-yyyy format");
        private Text wrongDate2 = new Text("* dd-mm-yyyy format");
        private final TextField searchTF = new TextField();
        private final HomeButton btnHome = new HomeButton("Home");


        public LibrarianStatView(HomeView prevView){
            this.prevView = prevView;
            wrongDate1.visibleProperty().bind(fromTF.isValid().not());
            wrongDate2.visibleProperty().bind(toTF.isValid().not());
        }


        public Scene showView(Stage s) {
            s.setTitle("Librarian Stat");
            this.setPadding(new Insets(20,20,20,20));


            nameColumn.setMinWidth(200);
            nameColumn.setCellValueFactory(new PropertyValueFactory<LibrarianStatRecord, String>("employeeName"));

            billsColumn.setMinWidth(100);
            billsColumn.setCellValueFactory(new PropertyValueFactory<LibrarianStatRecord, Integer>("bills"));

            booksColumn.setMinWidth(100);
            booksColumn.setCellValueFactory(new PropertyValueFactory<LibrarianStatRecord, Integer>("books"));

            incomeColumn.setMinWidth(100);
            incomeColumn.setCellValueFactory(new PropertyValueFactory<LibrarianStatRecord, Double>("total"));


            tableView.getColumns().addAll(nameColumn, billsColumn,booksColumn,incomeColumn);



            fromTF.setMinWidth(40);
            toTF.setMinWidth(40);
            fromTF.setPromptText("dd-mm-yyyy or empty");
            toTF.setPromptText("dd-mm-yyyy or empty");
            searchTF.setPromptText("Search here!");
            searchTF.setMinWidth(45);
           wrongDate1.setFill(Color.GRAY);
           wrongDate2.setFill(Color.GRAY);
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(btnHome,0,0);
            grid.add(wrongDate1,2,0);
            grid.add(wrongDate2,4,0);
            grid.add(searchTF,0,1);
            grid.add(new Label("From: "),1,1);
            grid.add(new Label("To: "),3,1);
            grid.add(fromTF,2,1);
            grid.add(toTF,4,1);
            grid.add(search,5,1);


            this.setTop(grid);
            this.setCenter(tableView);



            Scene sc = new Scene(this, 800, 600);
            return sc;
        }

    public TableColumn<LibrarianStatRecord, String> getNameColumn() {
        return nameColumn;
    }

    public TextField getSearchTF() {
        return searchTF;
    }

    public Button getSearch() {
        return search;
    }

    public HomeButton getBtnHome() {
            return btnHome;
        }

    public TableView<LibrarianStatRecord> getTableView() {
        return tableView;
    }

    public TableColumn<LibrarianStatRecord, Integer> getBillsColumn() {
        return billsColumn;
    }

    public TableColumn<LibrarianStatRecord, Integer> getBooksColumn() {
        return booksColumn;
    }

    public TableColumn<LibrarianStatRecord, Double> getIncomeColumn() {
        return incomeColumn;
    }

    public ValidatingTextField getFromTF() {
        return fromTF;
    }

    public ValidatingTextField getToTF() {
        return toTF;
    }

    public HomeView getPrevView() {
            return prevView;
        }





}
