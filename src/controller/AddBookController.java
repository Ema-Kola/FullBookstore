package controller;


import dao.RestockBillDAO;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.*;
import view.AddBookView;
import view.HomeView;
import dao.BooksDAO;


public class AddBookController {

        private BooksDAO booksDAO;
        private final AddBookView addBookView;


        public AddBookController(Stage stage, HomeView prevView){
            this.addBookView = new AddBookView(prevView);

            addBookView.getSubmitBtn().setOnAction(e -> {
                booksDAO = new BooksDAO();
                String isbn = addBookView.getIsbnTF().getText();
                String title = addBookView.getTitleTF().getText();
                String description = addBookView.getDescriptionTA().getText();
                String supplier = addBookView.getSupplierTF().getText();
                String category = addBookView.getCategoryComboBox().getValue();
                double sellingPrice = Double.parseDouble(addBookView.getSellingpriceTF().getText());
                double purchasedPrice = Double.parseDouble(addBookView.getPurchasedpriceTF().getText());
                int stock = Integer.parseInt(addBookView.getStockTF().getText());
                Author author = addBookView.getAuthorComboBox().getValue();
                Book newBook = new Book(isbn,title,description,supplier,purchasedPrice,sellingPrice,author,stock,category);
                if(booksDAO.searchBook(isbn)==null) {
                    if (booksDAO.create(newBook)) {
                        RestockBillDAO restockBillDAO = new RestockBillDAO();
                        if (restockBillDAO.create(new RestockBill(newBook, prevView.getCurrentUser(), newBook.getStock()))) {
                            System.out.println("Restock Bill saved successfully");
                            //restockBillDAO.printAll();
                        } else {
                            System.out.println("Operation failed.");
                        }

                        System.out.println("Book saved successfully");
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setContentText("Book saved successfully!");
                        a.showAndWait();
                    } else {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Failed to save book.");
                        a.showAndWait();
                        System.out.println("Operation failed");
                    }
                }else{
                    System.out.println("Book with this ISBN already exists in the library.");
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Book with this ISBN already exists in the library.\nGo to the restock button if you want to restock an existing book.");
                    a.showAndWait();

                }
            });

            addBookView.getAddAuthorBtn().setOnAction(e -> {
                AddAuthorController ac = new AddAuthorController(this.getView());
                Stage authorStage = new Stage();
                authorStage.setScene(ac.getView().showView(authorStage));
                authorStage.showAndWait();
            });

            addBookView.getAddCategoryButton().setOnAction(e -> {
                AddCategoryController cc = new AddCategoryController(this.getView());
                Stage categoryStage = new Stage();
                categoryStage.setScene(cc.getView().showView(categoryStage));
                categoryStage.showAndWait();
            });

            addBookView.getHomeBtn().setOnAction(e -> {
                stage.setScene(prevView.showView(stage));
            });

            addBookView.getRestockButton().setOnAction(e -> {
                RestockExistingBookController rc = new RestockExistingBookController(this.getView());
                Stage restockStage = new Stage();
                restockStage.setScene(rc.getView().showView(restockStage));
                restockStage.showAndWait();
            });

        }


        public AddBookView getView(){
            return this.addBookView;
        }


    }








