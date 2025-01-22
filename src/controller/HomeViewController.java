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
        private BooksDAO booksdao;
        private UsersDAO usersDAO;
        private PaycheckBillDAO paycheckBillDAO;
        private ManageEmployeeController mec;
        private UserController uc;
        private AddUserController auc;
        private AddBookController addBookController;
        private SearchBookController searchBookController;
        private  CheckOutBookController checkOutBookController;



    public HomeViewController(Stage stage, User currentUser, UserController prevView){
            this.homeView = new HomeView(currentUser,prevView);
            if(currentUser.getRole()== Role.MANAGER){
                lowInStockWarning();
            }


            homeView.getAddBookButton().setOnAction(e -> {
                this.addBookController = new AddBookController(stage,this.homeView);
                stage.setScene(this.addBookController.getView().showView(stage));
            });

            homeView.getAddUserButton().setOnAction(e -> {
                 auc = new AddUserController(stage,this.homeView);
                stage.setScene(auc.getView().showView(stage));
            });

            homeView.getCheckOutBookButton().setOnAction(e -> {
                checkOutBookController = new CheckOutBookController(stage,this.homeView);
                stage.setScene(checkOutBookController.getView().showView(stage));
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
                mec = new ManageEmployeeController(stage,this.homeView, new UsersDAO());
                stage.setScene(mec.getView().showView(stage));
            });

            homeView.getUpdatePermissionsButton().setOnAction(e -> {
                UpdatePermissionsController uc = new UpdatePermissionsController(stage, this.homeView);
                stage.setScene(uc.getView().showView(stage));
            });

            homeView.getLogOutButton().setOnAction(e -> {
                this.uc = new UserController(stage);
                stage.setScene(uc.getView().showView(stage));
            });


            homeView.getPaycheckButton().setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Do you want to release paychecks? Press OK to confirm.");
                Optional<ButtonType> result = alert.showAndWait();
                 releasePaychecks(result);

            });

            homeView.getManageLibraryButton().setOnAction(e->{
                ManageLibraryController ac = new ManageLibraryController(stage,this.homeView, new BooksDAO());
                stage.setScene(ac.getView().showView(stage));
            });


            homeView.getSearchBookButton().setOnAction(e->{
                this.searchBookController = new SearchBookController(stage,this.homeView, new BooksDAO());
                stage.setScene(this.searchBookController.getView().showView(stage));
            });


        }

        public void releasePaychecks(Optional<ButtonType> result) {

            if (result.get() == ButtonType.OK) {
                if(this.usersDAO ==null) this.usersDAO = new UsersDAO();
                if(this.paycheckBillDAO==null) this.paycheckBillDAO = new PaycheckBillDAO();
                for (User x : usersDAO.getAllActive()) {
                    PaycheckBill newPaycheck = new PaycheckBill(x, x.getSalary());
                    System.out.println(newPaycheck.getEmployee());
                    if (paycheckBillDAO.create(newPaycheck)) {
                        System.out.println(newPaycheck.getEmployee());
                    } else {
                        System.out.println("Could not save paycheck");
                        return;
                    }

                }
            }
        }

        public void lowInStockWarning(){
            ArrayList<Book> lowInStock=new ArrayList<>();
            if(this.booksdao==null) this.booksdao = new BooksDAO();

            for(Book b : booksdao.getAllActive()){
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
        public HomeView getView(){
            return this.homeView;
        }

    public BooksDAO getBooksdao() {
        return booksdao;
    }

    public void setBooksdao(BooksDAO booksdao) {
        this.booksdao = booksdao;
    }
    public UsersDAO getUsersDAO() {
        return this.usersDAO;
    }

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public PaycheckBillDAO getPaycheckBillDAO() {
        return paycheckBillDAO;
    }

    public void setPaycheckBillDAO(PaycheckBillDAO paycheckBillDAO) {
        this.paycheckBillDAO = paycheckBillDAO;
    }

    public ManageEmployeeController getManageEmployeeController() {return this.mec;}
    public UserController getUserController() {return this.uc;}
    public AddUserController getAddUserController(){return this.auc;}
    public AddBookController getAddBookController() { return addBookController;}
    public SearchBookController getSearchBookController() {return searchBookController;}
    public CheckOutBookController getCheckOutBookController() {return checkOutBookController;}
}






