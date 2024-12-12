package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.BillRecord;



public class CheckOutBookView extends BorderPane {

    private HomeView prevView;
    private final TableView<BillRecord> tableView = new TableView<>();
    private final TableColumn<BillRecord, String> bookNameColumn = new TableColumn<>("Book");
    private final TableColumn<BillRecord, Double> priceColumn= new TableColumn<>("Price");
    private final TableColumn<BillRecord, Integer> quantityColumn = new TableColumn<>("Quantity");
    private final TableColumn<BillRecord, Double> totalRecordColumn = new TableColumn<>("Total");

    private ValidatingTextField isbnTF = new ValidatingTextField(input -> input.matches("[0-9]{3}-[0-9]{2}-[0-9]{5}-[0-9]{2}-[0-9]"));

    private final ValidatingTextField quantityTf = new ValidatingTextField(input -> input.matches("\\d+"));
  ;

    private final Text totalTf = new Text("Total:\t 0 leke");

    private final Button btnAdd = new Button("Add");
    private final Button btnDelete = new Button("Delete");
    private final Button btnPrint = new Button("Print Bill");
    private final HomeButton btnHome = new HomeButton("Home");



        public CheckOutBookView(HomeView prevView){
            this.prevView = prevView;

        }

    public Text getTotalTf() {
        return totalTf;
    }

    public Scene showView(Stage s) {
        s.setTitle("CheckOut Book");
        this.setPadding(new Insets(20,20,20,20));
        tableView.setEditable(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        bookNameColumn.setMinWidth(100);
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<BillRecord, String>("bookName"));
        bookNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<BillRecord, Integer>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));


        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<BillRecord, Double>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        totalRecordColumn.setMinWidth(100);
        totalRecordColumn.setCellValueFactory(new PropertyValueFactory<BillRecord, Double>("total"));
        totalRecordColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));



        tableView.getColumns().addAll(bookNameColumn, priceColumn, quantityColumn,totalRecordColumn);

        btnDelete.setStyle("-fx-background-color: #c92121");
        btnAdd.setStyle("-fx-background-color: #73c273");
        FlowPane flowPane = new FlowPane(3, 3);
        isbnTF.setPrefColumnCount(18);
        quantityTf.setPrefColumnCount(5);
        isbnTF.setPromptText("13 digit format XXX-XX-XXXXX-XX-X");

        VBox vbox = new VBox();
        vbox.setSpacing(30);
        HBox hBox1 = new HBox();
        hBox1.setSpacing(20);
        HBox hbox2 = new HBox(totalTf,btnPrint);
        hbox2.setSpacing(40);
        hBox1.getChildren().addAll(new Label("ISBN: "), isbnTF, new Label("Quantity: "), quantityTf,btnAdd, btnDelete);
        vbox.getChildren().addAll(hbox2,hBox1);
        flowPane.getChildren().add(vbox);
        this.setTop(btnHome);
        this.setCenter(tableView);
        this.setBottom(flowPane);




        Scene sc = new Scene(this, 800, 600);
        return sc;
    }

    public HomeButton getBtnHome() {
        return btnHome;
    }

    public ValidatingTextField getIsbnTF() {
            return isbnTF;
        }


    public Button getBtnPrint() {
        return btnPrint;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }


    public ValidatingTextField getQuantityTf() {
        return quantityTf;
    }

    public TableView<BillRecord> getTableView() {
        return tableView;
    }

    public HomeView getPrevView() {
            return prevView;
        }




}
