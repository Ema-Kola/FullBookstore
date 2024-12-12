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
    private final BooksDAO booksDAO ;
    private final SearchBookView view;

    public SearchBookController(Stage stage,HomeView prevView)
    {
        this.view =new SearchBookView(prevView);
        this.booksDAO=new BooksDAO();

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

}
