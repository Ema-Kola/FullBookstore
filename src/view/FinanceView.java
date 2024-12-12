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
import javafx.util.converter.DoubleStringConverter;
import model.FinanceStatRecord;

public class FinanceView extends BorderPane{

        private HomeView prevView;
        private final TableView<FinanceStatRecord> tableView = new TableView<>();
        private final TableColumn<FinanceStatRecord, Double> incomeColumn = new TableColumn<>("Income");
        private final TableColumn<FinanceStatRecord, Double> costColumn = new TableColumn<>("Cost");
        private final TableColumn<FinanceStatRecord, Double> salariesColumn = new TableColumn<>("Salaries");
        private final TableColumn<FinanceStatRecord, Double> restockColumn= new TableColumn<>("Restocks");
         private final TableColumn<FinanceStatRecord, Double> netColumn= new TableColumn<>("Net Balance");
        private final TableColumn<FinanceStatRecord, String> timePeriodColumn = new TableColumn<>("Period");

        ChoiceBox<String> choiceBoxPeriod = new ChoiceBox();


        private final HomeButton btnHome = new HomeButton("Home");


        public FinanceView(HomeView prevView){
            this.prevView = prevView;
        }


        public Scene showView(Stage s) {
            s.setTitle("Finance Stat");
            this.setPadding(new Insets(20,20,20,20));

            tableView.setEditable(true);
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            incomeColumn.setMinWidth(200);
            incomeColumn.setCellValueFactory(new PropertyValueFactory<FinanceStatRecord, Double>("income"));
            incomeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

            costColumn.setMinWidth(200);
            costColumn.setCellValueFactory(new PropertyValueFactory<FinanceStatRecord, Double>("cost"));
            costColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));


            salariesColumn.setMinWidth(200);
            salariesColumn.setCellValueFactory(new PropertyValueFactory<FinanceStatRecord, Double>("salaries"));
            salariesColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

            restockColumn.setMinWidth(200);
            restockColumn.setCellValueFactory(new PropertyValueFactory<FinanceStatRecord, Double>("restock"));
            restockColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

            netColumn.setMinWidth(200);
            netColumn.setCellValueFactory(new PropertyValueFactory<FinanceStatRecord, Double>("netBalance"));
            netColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

            timePeriodColumn.setMinWidth(150);
            timePeriodColumn.setCellValueFactory(new PropertyValueFactory<FinanceStatRecord, String>("timeColumn"));
            timePeriodColumn.setCellFactory(TextFieldTableCell.forTableColumn());

            tableView.getColumns().addAll(incomeColumn,costColumn,netColumn,timePeriodColumn);


            choiceBoxPeriod.getItems().addAll("Monthly","Yearly");
            choiceBoxPeriod.setValue("Monthly");


            VBox vbox = new VBox();
            vbox.setSpacing(30);
            HBox hBox1 = new HBox();
            hBox1.setSpacing(20);

            hBox1.getChildren().addAll(new Label("Search by: "),choiceBoxPeriod);
            vbox.getChildren().addAll(btnHome,hBox1);

            this.setTop(vbox);
            this.setCenter(tableView);



            Scene sc = new Scene(this, 800, 600);
            return sc;
        }

        public HomeButton getBtnHome() {
            return btnHome;
        }


        public TableView<FinanceStatRecord> getTableView() {
            return tableView;
        }

        public HomeView getPrevView() {
            return prevView;
        }


    public TableColumn<FinanceStatRecord, Double> getIncomeColumn() {
        return incomeColumn;
    }

    public TableColumn<FinanceStatRecord, Double> getCostColumn() {
        return costColumn;
    }

    public TableColumn<FinanceStatRecord, Double> getSalariesColumn() {
        return salariesColumn;
    }

    public TableColumn<FinanceStatRecord, Double> getRestockColumn() {
        return restockColumn;
    }

    public TableColumn<FinanceStatRecord, Double> getNetColumn() {
        return netColumn;
    }

    public TableColumn<FinanceStatRecord, String> getTimePeriodColumn() {
        return timePeriodColumn;
    }

    public ChoiceBox<String> getChoiceBoxPeriod() {
            return choiceBoxPeriod;
        }


}
