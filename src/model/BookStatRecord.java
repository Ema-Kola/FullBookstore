package model;

import java.util.Date;

public class BookStatRecord {
    private final String authorName;
    private final String category;
    private final Book book;
    private final String bookName;
    private final String isbn;
    private int sold;
    private int bought;

    private final Date timePeriod;

    private String timeForColumn;



    public BookStatRecord(Book book, int sold, int bought, Date timePeriod){
        this.book=book;
        this.bookName = book.getTitle();
        this.isbn = book.getIsbn();
        this.sold = sold;
        this.bought = bought;
        this.timePeriod = timePeriod;
        this.authorName=book.getAuthor().toString();
        this.category=book.getCategory();

    }

    public void setTimeForColumn(String timeForColumn) {
        this.timeForColumn = timeForColumn;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCategory() {
        return category;
    }

    public String getBookName() {
        return bookName;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getSold() {
        return sold;
    }

    public int getBought() {
        return bought;
    }

    public Date getTimePeriod() {
        return timePeriod;
    }

    public String getTimeForColumn() {
        return timeForColumn;
    }

    public Book getBook() {
        return book;
    }

    public void updateQuantities(int sold, int bought){
        this.sold+=sold;
        this.bought+=bought;
    }

    public String toString(){
        return bookName+" author:"+authorName+" category:"+category+" bought:"+bought+" sold:"+sold+" time: "+timePeriod;
    }
}
