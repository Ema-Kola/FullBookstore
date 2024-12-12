package controller;

import dao.BooksDAO;
import dao.CustomerBillDAO;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.BillRecord;
import model.Book;
import model.CustomerBill;
import view.CheckOutBookView;
import view.HomeView;


public class CheckOutBookController {


        private final CheckOutBookView view;
        private final BooksDAO booksDAO;
        private final CustomerBillDAO customerBillDAO;
        private final CustomerBill customerBill;

        public CheckOutBookController(Stage stage, HomeView prevView){
            this.view = new CheckOutBookView(prevView);
            this.booksDAO = new BooksDAO();
            this.customerBillDAO = new CustomerBillDAO();
            this.customerBill = new CustomerBill(prevView.getCurrentUser());

            view.getBtnAdd().disableProperty().bind(view.getIsbnTF().isValid().not().or(view.getQuantityTf().isValid().not()));

            this.view.getTableView().setItems(customerBill.getBillRecords());
            this.view.getBtnHome().setOnAction(e -> {stage.setScene(prevView.showView(stage));});
            this.view.getBtnAdd().setOnAction(e -> onRecordAdd());
            this.view.getBtnDelete().setOnAction(e -> onRecordDelete());
            this.view.getBtnPrint().setOnAction(e -> onPrintBill());

        }


        private void onPrintBill(){
            customerBill.setBillNo(customerBillDAO.getAll().size()+1);
            if(customerBill.getBillRecords().isEmpty()){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Nothing to print. No books are checked out.");
                a.showAndWait();
            }else{
                if(customerBillDAO.create(customerBill)){
                    System.out.println("Customer Bill saved successfully.");
                    if(customerBillDAO.printBill(customerBill)){
                        System.out.println("BillNo.txt file created successfully.");
                        for (BillRecord b : customerBill.getBillRecords()) {
                            Book bookToUpdate=booksDAO.searchBook(b.getBook().getIsbn());
                            if(bookToUpdate!=null){
                                bookToUpdate.setStock(bookToUpdate.getStock()-b.getQuantity());
                            }else{
                                System.out.println("Book was not found.");
                                break;
                            }
                        }
                        if(booksDAO.updateAll()){
                            System.out.println("Books updated successfully.");
                        }else{
                            System.out.println("Could not update books.");
                        }

                        Stage billStage = new Stage();
                        billStage.setScene(new Scene(new Pane(new Text(customerBill.toString())), 400,400));
                        billStage.showAndWait();
                        this.view.getTableView().getItems().clear();
                        this.view.getIsbnTF().clear();
                        this.view.getQuantityTf().clear();
                    }
                }else{
                    System.out.println("Operation failed. Customer Bill was not created.");
                }

            }
            this.view.getTotalTf().setText("Total:\t "+customerBill.getTotal()+" leke");
        }


    private void onRecordAdd() {
        String bookIsbn = this.view.getIsbnTF().getText();
        int quantity = Integer.parseInt(this.view.getQuantityTf().getText());
        Book newbook=booksDAO.searchBook(bookIsbn);

        if(newbook==null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("No book with this Isbn.");
            a.showAndWait();
        }else {
            boolean bookAlreadyCheckedOut = false;
            for (BillRecord b : customerBill.getBillRecords()) {
                if (b.getBook().equals(newbook)) {
                    bookAlreadyCheckedOut = true;
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("You already added this book.");
                    a.showAndWait();
                    break;
                }
            }
            if (!bookAlreadyCheckedOut) {
                if (quantity > newbook.getStock()) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Not enough books in stock. Only " + newbook.getStock() + " left.");
                    a.showAndWait();
                } else {
                    customerBill.getBillRecords().add(new BillRecord(newbook, quantity));
                    this.view.getTotalTf().setText("Total:\t "+customerBill.getTotal()+" leke");
                }

            }
        }

    }


    private void onRecordDelete(){
        ObservableList<BillRecord> billRecords = this.view.getTableView().getSelectionModel().getSelectedItems();
        Alert alert;

        customerBill.getBillRecords().removeAll(billRecords);
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Deleted successfully");
        System.out.println("Deleted successfully");
        alert.setTitle("Delete result");
        alert.show();
        this.view.getTotalTf().setText("Total:\t "+customerBill.getTotal()+" leke");

    }

    public CheckOutBookView getView() {
        return view;
    }


}
