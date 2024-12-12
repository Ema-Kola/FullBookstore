package model;
import javafx.beans.property.*;

import java.io.*;
import java.util.Date;

public class Book implements Serializable {

        @Serial
        private static final long serialVersionUID = 5296705482940410483L;
        private transient StringProperty isbn;
        private transient StringProperty title;
        private transient StringProperty description;
        private transient StringProperty supplier;
        private Date purchasedDate;
        private double purchasedPrice;
        private double originalPrice;
        private transient DoubleProperty sellingPrice;
        private transient IntegerProperty stock;
        private Author author;
        private transient StringProperty category;
        private boolean active;


    public Book(String isbn, String title, String description, String supplier, double purchasedPrice, double sellingPrice, Author author, int stock, String category) {
            this.isbn = new SimpleStringProperty(isbn);
            this.title = new SimpleStringProperty(title);
            this.supplier = new SimpleStringProperty(supplier);
            this.purchasedDate= new Date();
            this.author = author;
            this.description = new SimpleStringProperty(description);
            this.sellingPrice=new SimpleDoubleProperty(sellingPrice);
            this.purchasedPrice=purchasedPrice;
            this.category=new SimpleStringProperty(category);
            this.stock=new SimpleIntegerProperty(stock);
            this.active=true;
        }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIsbn() {
            return this.isbn.get();
        }
        public void setIsbn(String isbn) {
            this.isbn.set(isbn);
        }

         public String getTitle() {
            return this.title.get();
        }
        public void setTitle(String title) {
            this.title.set(title);
        }
        public double getSellingPrice() {
            return this.sellingPrice.get();
        }

        public void setSellingPrice(double price){
            this.sellingPrice.set(price);
        }
        public double getOriginalPrice() {
        return this.originalPrice;
        }
        public void setOriginalPrice(double originalPrice){
            this.originalPrice=originalPrice;
        }
        public double getPurchasedPrice() {
        return this.purchasedPrice;
        }

        public void setPurchasedPrice(double purchasedPrice){
            this.purchasedPrice=purchasedPrice;
        }

        public Author getAuthor() {
            return author;
        }
        public void setAuthor(Author author) {
            this.author = author;
        }

        public String getDescription() {
            return this.description.get();
        }
        public void setDescription(String description) {
            this.description.set(description);
        }

        public String getSupplier() {
            return this.supplier.get();
        }

         public void setSupplier(String supplier) {
            this.supplier.set(supplier);
        }

        public int getStock() {
            return this.stock.get();
        }

         public void setStock(int stock) {
            this.stock.set(stock);
         }

         public String getCategory() {
            return category.get();
        }


    public void setCategory(String category) {
            this.category.set(category);
        }
    @Serial
    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeUTF(this.isbn.getValueSafe());
        outputStream.writeUTF(this.title.getValueSafe());
        outputStream.writeUTF(this.description.getValueSafe());
        outputStream.writeUTF(this.category.getValueSafe());
        outputStream.writeDouble(this.sellingPrice.getValue());
        outputStream.writeInt(this.stock.getValue());
        outputStream.writeUTF(this.supplier.getValueSafe());

    }

    @Serial
    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        this.isbn = new SimpleStringProperty(inputStream.readUTF());
        this.title = new SimpleStringProperty(inputStream.readUTF());
        this.description = new SimpleStringProperty(inputStream.readUTF());
        this.category = new SimpleStringProperty(inputStream.readUTF());
        this.sellingPrice = new SimpleDoubleProperty(inputStream.readDouble());
        this.stock = new SimpleIntegerProperty(inputStream.readInt());
        this.supplier = new SimpleStringProperty(inputStream.readUTF());


    }

        @Override
        public String toString() {
            return this.title.getValue() + " by " + this.author.toString() + ", " + this.sellingPrice.getValue() + " leke, stock: "+this.getStock()+", purchased price: "+this.purchasedPrice;
        }

        @Override
    public boolean equals(Object b){
        if(b instanceof Book){
            if(this.getIsbn().equals(((Book)b).getIsbn()))
                return true;
        }

        return false;
        }



    }

