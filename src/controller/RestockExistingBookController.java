package controller;


import dao.RestockBillDAO;
import javafx.scene.control.Alert;
import dao.BooksDAO;
import model.*;
import view.AddBookView;
import view.RestockExistingBookView;


public class RestockExistingBookController {



        private BooksDAO booksDAO;
        private final RestockExistingBookView restockExistingBookView;


        public RestockExistingBookController(AddBookView prevView){
            this.restockExistingBookView = new RestockExistingBookView();
            restockExistingBookView.getRestockButton().disableProperty().bind((restockExistingBookView.getIsbnTF().isValid().not()).or(restockExistingBookView.getPurchasedpriceTF().isValid().not()).or(restockExistingBookView.getQuantityTF().isValid().not()));
            booksDAO=new BooksDAO();


                restockExistingBookView.getRestockButton().setOnAction(e -> {
                    String isbn = restockExistingBookView.getIsbnTF().getText();
                    Book updatedBook = booksDAO.searchBook(isbn);
                    if(updatedBook!=null && updatedBook.isActive()) {
                        int quantity = Integer.parseInt(restockExistingBookView.getQuantityTF().getText());
                        double price = Double.parseDouble(restockExistingBookView.getPurchasedpriceTF().getText());
                        updatedBook.setStock(updatedBook.getStock() + quantity);
                        updatedBook.setPurchasedPrice(price);


                        if (booksDAO.updateAll()) {
                            RestockBillDAO restockBillDAO = new RestockBillDAO();
                            if (restockBillDAO.create(new RestockBill(updatedBook, prevView.getPrevView().getCurrentUser(), quantity))) {
                                System.out.println("Restock Bill saved successfully");
                                restockBillDAO.printAll();
                            } else {
                                System.out.println("Failed to save restock bill.");
                            }

                            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                            a.setContentText("Book restocked successfully!");
                            a.showAndWait();
                        } else {
                            System.out.println("Failed to restock book.");
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setContentText("Failed to restock.");
                            a.showAndWait();
                        }
                    }else{
                        System.out.println("ISBN does not match any existing active book.");
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("ISBN does not match any existing active book.");
                        a.showAndWait();
                    }



                 });


        }

        public RestockExistingBookView getView(){
            return this.restockExistingBookView;
        }





}
