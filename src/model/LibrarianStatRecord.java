package model;



public class LibrarianStatRecord {

        private final User employee;
        private final String employeeName;
        private int bills;
        private int books;
        private double total;




        public LibrarianStatRecord(User u,int bills, int books,double total) {
            this.employee=u;
            this.employeeName=u.getFirstName()+" "+u.getLastName();
            this.bills = bills;
            this.books = books;
            this.total = total;
        }

    public String getEmployeeName() {
        return employeeName;
    }

    public void updateNums(int bills, int books, double total){
            this.bills += bills;
            this.books += books;
            this.total += total;
        }

    public User getEmployee() {
        return employee;
    }

    public int getBills() {
        return bills;
    }

    public int getBooks() {
        return books;
    }

    public double getTotal() {
        return total;
    }


}
