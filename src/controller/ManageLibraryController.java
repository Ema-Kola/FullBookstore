package controller;

import dao.BooksDAO;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;

import javafx.stage.Stage;
import model.Book;
import view.HomeView;
import view.ManageLibraryView;

public class ManageLibraryController {

    private final BooksDAO booksDao;
    private final ManageLibraryView view;
    private boolean filterActive;


    public ManageLibraryController(Stage stage,HomeView prevView)
    {
        this.view = new ManageLibraryView(prevView);
        this.booksDao = new BooksDAO();
        filterTable();

        this.view.getDeleteButton().setOnAction(e-> {
            deleteBook();


        });
        this.view.getHomeBtn().setOnAction(e -> {
            stage.setScene(prevView.showView(stage));

        });

        setEditListeners();

    }
    private void filterTable(){
        FilteredList<Book> activeBooks = new FilteredList<>(booksDao.getAll(),book -> book.isActive()==filterActive);
        FilteredList<Book> books = new FilteredList<>(activeBooks, e -> true);
        view.getChoiceBox().valueProperty().addListener((ob, oldval, newval) -> {
            if (newval.equals("Active")) {
                filterActive = true;
            } else if (newval.equals("Inactive")) {
                filterActive = false;
            }
            activeBooks.setPredicate(book -> book.isActive()==filterActive);

            this.view.getSearchBook().textProperty().addListener((ops, oldVal, newVal) -> {

                books.setPredicate(book -> (book.getIsbn().toLowerCase().contains(newVal.toLowerCase().trim()) ||
                        book.getTitle().toLowerCase().contains(newVal.toLowerCase().trim())
                        || book.getAuthor().getFirstName().toLowerCase().contains(newVal.toLowerCase().trim()) ||
                        book.getAuthor().getLastName().toLowerCase().contains(newVal.toLowerCase().trim()) && book.isActive()==filterActive)
                );

            });

        });

        this.view.getTableView().setItems(books);
    }

    private void setEditListeners() {
        this.view.getDescriptionColumn().setOnEditCommit(e -> {
            if(e.getNewValue().isEmpty())
            {
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Description cannot be empty");
                a.show();
            }
            else
            {
                FilteredList<Book> activeBooks = new FilteredList<>(booksDao.getAll(),book -> book.isActive()==filterActive);
                FilteredList<Book> books = new FilteredList<>(activeBooks, e1 -> true);
                books.get(e.getTablePosition().getRow()).setDescription(e.getNewValue());
            }
        });

        this.view.getCategoryColumn().setOnEditCommit(e -> {
            if(e.getNewValue().isEmpty())
            {
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Category cannot be empty");
                a.show();
            }
            else {
                FilteredList<Book> activeBooks = new FilteredList<>(booksDao.getAll(),book -> book.isActive()==filterActive);
                FilteredList<Book> books = new FilteredList<>(activeBooks, e1 -> true);
                books.get(e.getTablePosition().getRow()).setCategory(e.getNewValue());
            }
        });

        this.view.getSellingPriceColumn().setOnEditCommit(e -> {
            if(e.getNewValue() == null)
            {
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Selling Price cannot be empty");
                a.show();
            }
            else if((e.getNewValue()<0))
            {
                System.out.println("not valid");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Selling Price is not valid");
                a.show();
            }
            else

            {
                FilteredList<Book> activeBooks = new FilteredList<>(booksDao.getAll(),book -> book.isActive()==filterActive);
                FilteredList<Book> books = new FilteredList<>(activeBooks, e1 -> true);

                books.get(e.getTablePosition().getRow()).setSellingPrice(e.getNewValue());
            }
        });

        this.view.getEditBtn().setOnAction(e -> {

            if(this.booksDao.updateAll()) {
                System.out.println("content updated");
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("Content successfully updated");
                a.show();

            } else {
                System.out.println("update failed");
            }
        });
    }


    private void deleteBook() {
        ObservableList<Book> selectedBooks = this.view.getTableView().getSelectionModel().getSelectedItems();
        Alert alert;
        for(Book b : selectedBooks){
            b.setActive(!filterActive);
        }

        if(booksDao.updateAll()) {
            filterTable();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Book status updated successfully");
            System.out.println("Successful update");
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Update failed");
        }
        alert.setTitle("Update");
        alert.show();

    }





    public ManageLibraryView getView()
    {
        return view;
    }


}
