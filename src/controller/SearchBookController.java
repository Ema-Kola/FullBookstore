package controller;

import dao.BooksDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;
import model.Book;
import view.HomeView;
import view.SearchBookView;

public class SearchBookController {
    private BooksDAO booksDAO;
    private final SearchBookView view;

    public SearchBookController(Stage stage,HomeView prevView, BooksDAO booksDAO)
    {
        this.view =new SearchBookView(prevView);
        this.booksDAO=booksDAO;

        ObservableList<Book> b = FXCollections.observableArrayList(booksDAO.getAllActive());
        FilteredList <Book> books = new FilteredList<>(b,e->true);

        this.view.getSearch().textProperty().addListener((ops,oldVal,newVal)->{


            books.setPredicate(book->(book.getIsbn().toLowerCase().contains(newVal.toLowerCase().trim())||
                    book.getTitle().toLowerCase().contains(newVal.toLowerCase().trim())
                    ||book.getAuthor().getFirstName().toLowerCase().contains(newVal.toLowerCase().trim()) ||
                    book.getAuthor().getLastName().toLowerCase().contains(newVal.toLowerCase().trim())||book.getCategory().toLowerCase().contains(newVal.toLowerCase().trim()))
            );

        });

        this.view.getTableView().setItems(books);

        this.view.getHomeBtn().setOnAction(e->{
            stage.setScene(prevView.showView(stage));
        });
    }



    public SearchBookView getView() {
        return view;
    }
//
//    public void setBooksDAO(BooksDAO booksDao) {
//        this.booksDAO = booksDao;
//    }
    public void setBooksDAO(BooksDAO booksDao) {
        this.booksDAO = booksDao;

        // refresh the table view with new data, for system test
        ObservableList<Book> b = FXCollections.observableArrayList(booksDAO.getAllActive());
        FilteredList<Book> books = new FilteredList<>(b, e -> true);

        this.view.getSearch().textProperty().addListener((ops, oldVal, newVal) -> {
            books.setPredicate(book ->
                    book.getIsbn().toLowerCase().contains(newVal.toLowerCase().trim()) ||
                            book.getTitle().toLowerCase().contains(newVal.toLowerCase().trim()) ||
                            book.getAuthor().getFirstName().toLowerCase().contains(newVal.toLowerCase().trim()) ||
                            book.getAuthor().getLastName().toLowerCase().contains(newVal.toLowerCase().trim()) ||
                            book.getCategory().toLowerCase().contains(newVal.toLowerCase().trim())
            );
        });

        this.view.getTableView().setItems(books);
    }

}
