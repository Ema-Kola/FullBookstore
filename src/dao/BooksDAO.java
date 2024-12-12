package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Book;


public class BooksDAO extends DAO<Book>{


        public BooksDAO(){
            super("files/books.dat");
        }

    public ObservableList<Book> getAllActive() {
            ObservableList<Book> items = FXCollections.observableArrayList();
            for(Book b : getAll()){
                if(b.isActive()){
                    items.add(b);
                }
            }

        return items;
    }


        public Book searchBook(String isbn){
            for(Book b : this.getAll()){
                if(isbn.equals(b.getIsbn())){
                    return b;
                }
            }
            return null;

        }
    }


