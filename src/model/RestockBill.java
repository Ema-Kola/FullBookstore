package model;


import java.io.Serial;

public class RestockBill extends Bill {

    //private static final long serialVersionUID= 1;

        private Book purchasedBook;
        private int quantity;

        public RestockBill(Book book, User employee, int quantity) {
            super(employee);
            setPurchasedBook(book);
            setQuantity(quantity);
        }

        public Book getPurchasedBook() {
            return purchasedBook;
        }

        public void setPurchasedBook(Book b) {
            this.purchasedBook = b;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getTotal(){
            return purchasedBook.getPurchasedPrice()* quantity;
        }

        @Override
        public String toString(){
           return purchasedBook+" user: "+this.getEmployee()+" Total "+ getTotal()+" leke.\n";

        }

}
