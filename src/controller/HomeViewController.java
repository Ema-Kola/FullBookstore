package controller;


import dao.BooksDAO;
import dao.PaycheckBillDAO;
import dao.UsersDAO;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.Book;
import model.PaycheckBill;
import model.Role;
import model.User;
import view.*;

import java.util.ArrayList;
import java.util.Optional;

public class HomeViewController {
        private final HomeView homeView;


        public HomeViewController(Stage stage, User currentUser, UserController prevView){
            this.homeView = new HomeView(currentUser,prevView);

            if(currentUser.getRole()== Role.MANAGER){
                ArrayList<Book> lowInStock=new ArrayList<>();
                BooksDAO booksDAO = new BooksDAO();
                for(Book b : booksDAO.getAllActive()){
                    if(b.getStock()<5){
                        lowInStock.add(b);
                    }
                }
                if(!lowInStock.isEmpty()){
                    String message="These books are low in stock: ";
                    for(Book b : lowInStock){
                        message+="\n"+b.getTitle()+", stock: "+b.getStock();
                    }
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setContentText(message);
                    a.showAndWait();
                }
            }


            homeView.getAddBookButton().setOnAction(e -> {
                AddBookController ac = new AddBookController(stage,this.homeView);
                stage.setScene(ac.getView().showView(stage));
            });

            homeView.getAddUserButton().setOnAction(e -> {
                AddUserController ac = new AddUserController(stage,this.homeView);
                stage.setScene(ac.getView().showView(stage));
            });

            homeView.getCheckOutBookButton().setOnAction(e -> {
                CheckOutBookController cc = new CheckOutBookController(stage,this.homeView);
                stage.setScene(cc.getView().showView(stage));
            });

            homeView.getBookStatsButton().setOnAction(e -> {
                BookStatController bc = new BookStatController(stage, this.homeView);
                stage.setScene(bc.getView().showView(stage));
            });

            homeView.getLibrarianStatsButton().setOnAction(e -> {
               LibrarianStatController lc = new LibrarianStatController(stage, this.homeView);
               stage.setScene(lc.getView().showView(stage));
            });

            homeView.getFinanceStatsButton().setOnAction(e -> {
                FinanceController fc = new FinanceController(stage, this.homeView);
                stage.setScene(fc.getView().showView(stage));
            });


            homeView.getManageEmployeeButton().setOnAction(e->{
                ManageEmployeeController ac = new ManageEmployeeController(stage,this.homeView);
                stage.setScene(ac.getView().showView(stage));
            });

            homeView.getUpdatePermissionsButton().setOnAction(e -> {
                UpdatePermissionsController uc = new UpdatePermissionsController(stage, this.homeView);
                stage.setScene(uc.getView().showView(stage));
            });

            homeView.getLogOutButton().setOnAction(e -> {
                UserController uc = new UserController(stage);
                stage.setScene(uc.getView().showView(stage));
            });


            homeView.getPaycheckButton().setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Do you want to release paychecks? Press OK to confirm.");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    UsersDAO usersDAO = new UsersDAO();
                    PaycheckBillDAO paycheckBillDAO = new PaycheckBillDAO();
                    for(User x : usersDAO.getAllActive()){
                        PaycheckBill newPaycheck = new PaycheckBill(x,x.getSalary());
                        System.out.println(newPaycheck.getEmployee());
                        if(paycheckBillDAO.create(newPaycheck)){
                            System.out.println(newPaycheck.getEmployee());
                        }else{
                            System.out.println("Could not save paycheck");
                            return;
                        }

                    }
                }
            });

            homeView.getManageLibraryButton().setOnAction(e->{
                ManageLibraryController ac = new ManageLibraryController(stage,this.homeView);
                stage.setScene(ac.getView().showView(stage));
            });


            homeView.getSearchBookButton().setOnAction(e->{
                SearchBookController gc = new SearchBookController(stage,this.homeView);
                stage.setScene(gc.getView().showView(stage));
            });


        }


        public HomeView getView(){
            return this.homeView;
        }

}






