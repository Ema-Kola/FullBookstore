package controller;

import dao.CustomerBillDAO;
import dao.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.*;
import view.HomeView;
import view.LibrarianStatView;
import java.util.Date;
import java.util.HashMap;

import model.CustomFunctions;

public class LibrarianStatController {

        private final LibrarianStatView view;
        private final CustomerBillDAO customerBillDAO;
        private final UsersDAO usersDAO;


        public LibrarianStatController(Stage stage, HomeView prevView){
            this.view = new LibrarianStatView(prevView);
            this.customerBillDAO = new CustomerBillDAO();
            this.usersDAO = new UsersDAO();



            this.view.getBtnHome().setOnAction(e -> {stage.setScene(prevView.showView(stage));});
            this.view.getSearch().setOnAction(e -> {
                ObservableList<LibrarianStatRecord> emptyRecords= FXCollections.observableArrayList();
                Date dateFrom = CustomFunctions.convertDate(this.view.getFromTF().getText());
                Date dateTo = CustomFunctions.convertDate(this.view.getToTF().getText());

                if(this.view.getFromTF().getText().isEmpty()){
                    dateFrom=new Date(Long.MIN_VALUE);
                }
                if(this.view.getToTF().getText().isEmpty()){
                    dateTo=new Date(Long.MAX_VALUE);
                }
                if(dateTo==null || dateFrom==null){
                    this.view.getTableView().setItems(emptyRecords);
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Invalid Date.");
                    a.showAndWait();

                }else if(dateTo.compareTo(dateFrom)<0){
                    this.view.getTableView().setItems(emptyRecords);
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("Dates in wrong order.");
                    a.showAndWait();

                }else{
                    FilteredList<LibrarianStatRecord> flUser=new FilteredList<>(filterDate(dateFrom, dateTo), ev->true);
                    this.view.getSearchTF().textProperty().addListener((obs,oldVal,newVal)->{
                        flUser.setPredicate(librarianRecord->librarianRecord.getEmployeeName().toLowerCase().trim().contains(newVal.toLowerCase().trim()));
                    });
                    this.view.getTableView().setItems(flUser);
                }


            });
        }

        private ObservableList<LibrarianStatRecord> filterDate(Date dateFrom, Date dateTo){
            //System.out.println(dateFrom+ " " +dateTo);
            HashMap<String, LibrarianStatRecord> filteredByLibrarian= new HashMap<>();
            ObservableList<LibrarianStatRecord> statList=FXCollections.observableArrayList();
            for(User u : usersDAO.getAllActive()){
                if((u.getRole()==Role.LIBRARIAN)){
                    filteredByLibrarian.put(u.getUsername(),new LibrarianStatRecord(u,0,0,0));
                }
            }
            for(CustomerBill c : customerBillDAO.getAll()){
                if(c.getDate().compareTo(dateFrom)>=0 && c.getDate().compareTo(dateTo)<=0) {
                    if (filteredByLibrarian.containsKey(c.getEmployee().getUsername())) {
                        int totalBooks = 0;
                        for (BillRecord b : c.getBillRecords()) {
                            totalBooks += b.getQuantity();
                        }
                        filteredByLibrarian.get(c.getEmployee().getUsername()).updateNums(1, totalBooks, c.getTotal());
                    }
                }
            }
            for(String x : filteredByLibrarian.keySet()){
                statList.add(filteredByLibrarian.get(x));
            }
           return statList;
        }



        public LibrarianStatView getView() {
            return view;
        }




}
