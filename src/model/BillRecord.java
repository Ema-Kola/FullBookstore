package model;

import java.io.Serializable;

public class BillRecord implements Serializable {
    private final Book book;
    private final String bookName;
    private final double price;
    private final double total;
    private final int quantity;

    public BillRecord(Book book, int quantity){
        this.book=book;
        this.bookName = book.getTitle();
        this.price = book.getSellingPrice();
        this.quantity=quantity;
        this.total = price*quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total;
    }

    public double getPrice() {
        return price;
    }

    public String getBookName() {
        return bookName;
    }

    public String toString(){
        return bookName+"\n"+price+"\t\t"+quantity+"\t\t\t"+total;
    }
}
