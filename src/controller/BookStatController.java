
/*
package controller;


import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.*;
import view.BookStatView;
import view.HomeView;
import java.util.*;

public class BookStatController {


        private final BookStatView view;
        private final BooksDAO booksDAO;
        private final CustomerBillDAO customerBillDAO;
        private final RestockBillDAO restockBillDAO;
        private String timeFilter;
        private String typeFilter;
        //private ObservableList<BookStatRecord> bookStatRecords;
        private  HashMap<String, ArrayList<BookStatRecord>> billsByBook = new HashMap<>();
        private AuthorsDAO authorsDAO;
        private  HashMap<String, ArrayList<BookStatRecord>> billsByAuthor = new HashMap<>();
        private CategoryDAO categoryDAO;
        private HashMap<String, ArrayList<BookStatRecord>> billsByCategory = new HashMap<>();
        private ObservableList<BookStatRecord> statRecords =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord> booksDaily =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord> booksMonthly =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord> booksYearly =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord>[] books = new ObservableList[3];
        private ObservableList<BookStatRecord> authorDaily =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord> authorMonthly =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord> authorYearly =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord>[] authors = new ObservableList[3];
        private ObservableList<BookStatRecord> categoryDaily =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord> categoryMonthly =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord> categoryYearly =  FXCollections.observableArrayList();
        private ObservableList<BookStatRecord>[] categories = new ObservableList[3];

        public BookStatController(Stage stage, HomeView prevView){
            this.view = new BookStatView(prevView);
            this.booksDAO = new BooksDAO();
            this.customerBillDAO = new CustomerBillDAO();
            this.restockBillDAO = new RestockBillDAO();
            this.authorsDAO = new AuthorsDAO();
            this.categoryDAO = new CategoryDAO();
            books[0]=booksDaily;
            books[1]=booksMonthly;
            books[2]=booksYearly;
            authors[0]=authorDaily;
            authors[1]=authorMonthly;
            authors[2]=authorYearly;
            categories[0]=categoryDaily;
            categories[1]=categoryMonthly;
            categories[2]=categoryYearly;


            this.view.getBtnHome().setOnAction(e -> {stage.setScene(prevView.showView(stage));});


            this.view.getChoiceBox().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)
                    -> {
                    typeFilter = newVal;
                    this.view.getTableView().getColumns().clear();
                    this.view.getTableView().getItems().clear();
                    filterBills();
            });


            this.view.getChoiceBoxPeriod().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)
                    -> {
                timeFilter = newVal;
                this.view.getTableView().getColumns().clear();
                this.view.getTableView().getItems().clear();
                filterBills();
            });

        }


        private void filterBills(){

            if(typeFilter.equals("Book")){
                filterDate(organiseBillsByBook(),books);
                this.view.getTableView().getColumns().addAll(view.getBookNameColumn(), view.getIsbnColumn(), view.getSoldColumn(), view.getBoughtColumn(), view.getTimePeriodColumn());
            }
            if(typeFilter.equals("Author")){
                filterDate(organiseBillsByAuthor(),authors);
                this.view.getTableView().getColumns().addAll(view.getAuthorColumn(),  view.getSoldColumn(), view.getBoughtColumn(),view.getTimePeriodColumn());
            }
            if(typeFilter.equals("Category")){
                filterDate(organiseBillByCategory(),categories);
                this.view.getTableView().getColumns().addAll(view.getCategoryColumn(),  view.getSoldColumn(), view.getBoughtColumn(),view.getTimePeriodColumn());
            }
            this.view.getTableView().setItems(statRecords);
        }

        private void filterDate(HashMap<String, ArrayList<BookStatRecord>> bills, ObservableList<BookStatRecord>[] cache){
            //HashMap<String, ArrayList<BookStatRecord>> bills = (HashMap<String, ArrayList<BookStatRecord>>) tempbills.clone();
            HashMap<String, HashMap<Date, BookStatRecord> > filteredByDate= new HashMap<>();
            ObservableList<BookStatRecord> statList = FXCollections.observableArrayList();
            //this.statRecords.clear();
            int cacheIndex=0;

                for(String x : bills.keySet()){
                    filteredByDate.put(x,new HashMap<>());
                    for(BookStatRecord b : bills.get(x)){
                        if(timeFilter=="Daily") {
                            if (!cache[0].isEmpty()) {
                                System.out.println("Cache Daily");
                                statList.addAll(cache[0]);
                                return;

                            } else {
                                System.out.println("Jo Cache Daily");
                                Date d = new Date(b.getTimePeriod().getYear(), b.getTimePeriod().getMonth(), b.getTimePeriod().getDate());
                                if (!isInSetDaily(b.getTimePeriod(), filteredByDate.get(x).keySet())) {
                                    filteredByDate.get(x).put(d, b);

                                } else {
                                    filteredByDate.get(x).get(d).updateQuantities(b.getSold(), b.getBought());
                                }
                                filteredByDate.get(x).get(d).setTimeForColumn(d.getDate() + " " + theMonth(d.getMonth()) + " " + (d.getYear() + 1900));
                            }
                            cacheIndex=0;
                        }

                        if(timeFilter=="Monthly") {
                            if (!cache[1].isEmpty()) {
                                System.out.println("Cache Monthly");
                                statList.addAll(cache[1]);
                                return;

                            } else {
                                System.out.println("Jo Cache Monthly");
                            Date d = new Date(b.getTimePeriod().getYear(), b.getTimePeriod().getMonth(), 1);
                            if (!isInSetMonthly(b.getTimePeriod(), filteredByDate.get(x).keySet())) {
                                filteredByDate.get(x).put(d, b);
                            } else {
                                filteredByDate.get(x).get(d).updateQuantities(b.getSold(), b.getBought());
                            }
                            filteredByDate.get(x).get(d).setTimeForColumn(theMonth(d.getMonth()) + " " + (d.getYear() + 1900));
                            }
                            cacheIndex=1;
                        }

                        if(timeFilter=="Total") {
                            if (!cache[2].isEmpty()) {
                                System.out.println("Cache Total");
                                statList.addAll(cache[2]);
                                return;

                            } else {
                                System.out.println("Cache Total");
                                Date d = new Date(b.getTimePeriod().getYear(), 1, 1);
                                if (!isInSetYearly(b.getTimePeriod(), filteredByDate.get(x).keySet())) {
                                    filteredByDate.get(x).put(d, b);
                                } else {
                                    filteredByDate.get(x).get(d).updateQuantities(b.getSold(), b.getBought());
                                }
                                filteredByDate.get(x).get(d).setTimeForColumn("" + (d.getYear() + 1900));
                            }
                            cacheIndex=2;
                        }
                    }

                    for(Date i : filteredByDate.get(x).keySet()){
                        statList.add(filteredByDate.get(x).get(i));
                    }

                }
            System.out.println("index "+cacheIndex);
                cache[cacheIndex].addAll(statList);
           this.statRecords = statList;
        }



        private HashMap<String, ArrayList<BookStatRecord>> organiseBillsByBook(){
            HashMap<String, ArrayList<BookStatRecord>> temp = new HashMap<>();
            if(!billsByBook.isEmpty()){
                System.out.println("Cache Book");
                return billsByBook;
            }else{
                System.out.println(" Jo Cache Book");
                for (Book b : booksDAO.getAll()) {
                    temp.put(b.getIsbn(), new ArrayList<>());
                }
                for (RestockBill x : restockBillDAO.getAll()) {
                    temp.get(x.getPurchasedBook().getIsbn()).add(new BookStatRecord(x.getPurchasedBook().getTitle(), x.getPurchasedBook().getIsbn(), 0, x.getQuantity(), x.getDate()));
                }
                for (CustomerBill x : customerBillDAO.getAll()) {
                    for (BillRecord b : x.getBillRecords()) {
                        temp.get(b.getBook().getIsbn()).add(new BookStatRecord(b.getBookName(), b.getBook().getIsbn(), b.getQuantity(), 0, x.getDate()));
                    }
                }
                this.billsByBook.putAll(temp);
                return billsByBook;
            }

        }

        private HashMap<String, ArrayList<BookStatRecord>> organiseBillsByAuthor(){
            //problem here, when using toString() or getFullName() it appears as null
            if(!billsByAuthor.isEmpty()) {
                System.out.println("Cache Author");
            }else{
                System.out.println("Jo Cache Author");
                for (Author a : authorsDAO.getAll()) {
                    billsByAuthor.put(a.getFirstName(), new ArrayList<>());
                }

                for (RestockBill x : restockBillDAO.getAll()) {
                    billsByAuthor.get(x.getPurchasedBook().getAuthor().getFirstName()).add(new BookStatRecord(x.getPurchasedBook().getAuthor().toString(), 0, x.getQuantity(), x.getDate()));
                }

                for (CustomerBill x : customerBillDAO.getAll()) {
                    for (BillRecord b : x.getBillRecords()) {
                        billsByAuthor.get(b.getBook().getAuthor().getFirstName()).add(new BookStatRecord(b.getBook().getAuthor().toString(), b.getQuantity(), 0, x.getDate()));
                    }
                }
            }
            return billsByAuthor;

        }
        private HashMap<String, ArrayList<BookStatRecord>> organiseBillByCategory(){
            if(!billsByCategory.isEmpty()) {
                System.out.println("Cache Category");
            }else{
                System.out.println("Jo Cache Category");
                for (String c : categoryDAO.getAll()) {
                    billsByCategory.put(c, new ArrayList<>());
                }

                for (RestockBill x : restockBillDAO.getAll()) {
                    billsByCategory.get(x.getPurchasedBook().getCategory()).add(new BookStatRecord(0, x.getQuantity(), x.getPurchasedBook().getCategory(), x.getDate()));
                }

                for (CustomerBill x : customerBillDAO.getAll()) {
                    for (BillRecord b : x.getBillRecords()) {
                        billsByCategory.get(b.getBook().getCategory()).add(new BookStatRecord(b.getQuantity(), 0, b.getBook().getCategory(), x.getDate()));
                    }

                }
            }
            return billsByCategory;

        }
    public static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }
    private boolean sameDate(Date x1, Date x2){
        if(x1.getDate()==x2.getDate() && x1.getMonth()==x2.getMonth() && x1.getYear()==x2.getYear()){
            return true;
        }
        return false;
    }
    private boolean sameMonth(Date x1, Date x2){
        if(x1.getMonth()==x2.getMonth() && x1.getYear()==x2.getYear()){
            return true;
        }
        return false;
    }
    private boolean isInSetDaily(Date x, Set<Date> keys){
        for(Date d : keys) {
            if (sameDate(x,d)) {
                return true;
            }
        }
        return false;
    }
    private boolean isInSetMonthly(Date x, Set<Date> keys){
        for(Date d : keys) {
            if (sameMonth(x,d)) {
                return true;
            }
        }
        return false;
    }
    private boolean isInSetYearly(Date x, Set<Date> keys){
        for(Date d : keys) {
            if (x.getYear()==d.getYear()) {
                return true;
            }
        }
        return false;
    }




    public BookStatView getView() {
            return view;
        }


        /*
        private void organiseBills(){

            for(Book b: booksDAO.getAll()){
                billsByBook.put(b.getIsbn(), new ArrayList<>());
            }

            for(Author a : authorsDAO.getAll()){
                billsByAuthor.put(a.getFirstName(),new ArrayList<>());
            }

            for(String c : categoryDAO.getAll()){
                billsByCategory.put(c,new ArrayList<>());
            }

            for (RestockBill x : restockBillDAO.getAll()){
                billsByBook.get(x.getPurchasedBook().getIsbn()).add(new BookStatRecord(x.getPurchasedBook().getTitle(), x.getPurchasedBook().getIsbn(),0,x.getQuantity(),x.getDate()));
                billsByAuthor.get(x.getPurchasedBook().getAuthor().getFirstName()).add(new BookStatRecord(x.getPurchasedBook().getAuthor().toString(),0,x.getQuantity(),x.getDate()));
                billsByCategory.get(x.getPurchasedBook().getCategory()).add(new BookStatRecord(0,x.getQuantity(),x.getPurchasedBook().getCategory(),x.getDate()));
            }

            for(CustomerBill x : customerBillDAO.getAll()){
                for(BillRecord b : x.getBillRecords()){
                    billsByBook.get(b.getBook().getIsbn()).add(new BookStatRecord(b.getBookName(),b.getBook().getIsbn(),b.getQuantity(),0,x.getDate()));
                    billsByAuthor.get(b.getBook().getAuthor().getFirstName()).add(new BookStatRecord(b.getBook().getAuthor().toString(),b.getQuantity(),0,x.getDate()));
                    billsByCategory.get(b.getBook().getCategory()).add(new BookStatRecord(b.getQuantity(),0,b.getBook().getCategory(),x.getDate()));
                }

            }
        }




}
*/

package controller;

import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.stage.Stage;
import model.*;
import view.BookStatView;
import view.HomeView;
import java.util.*;

public class BookStatController {


    private final BookStatView view;
    private final BooksDAO booksDAO;
    private final CustomerBillDAO customerBillDAO;
    private final RestockBillDAO restockBillDAO;
    private String timeFilter;
    private String typeFilter;
    private HashMap<String, ArrayList<BookStatRecord>> billsByBook = new HashMap<>();
    private AuthorsDAO authorsDAO;
    private HashMap<String, ArrayList<BookStatRecord>> billsByAuthor = new HashMap<>();
    private CategoryDAO categoryDAO;
    private HashMap<String, ArrayList<BookStatRecord>> billsByCategory = new HashMap<>();

    private ObservableList<BookStatRecord> statRecords = FXCollections.observableArrayList();

    public BookStatController(Stage stage, HomeView prevView) {
        this.view = new BookStatView(prevView);
        this.booksDAO = new BooksDAO();
        this.customerBillDAO = new CustomerBillDAO();
        this.restockBillDAO = new RestockBillDAO();
        this.authorsDAO = new AuthorsDAO();
        this.categoryDAO = new CategoryDAO();

        this.view.getBtnHome().setOnAction(e -> {
            stage.setScene(prevView.showView(stage));
        });



        this.view.getBtnSearch().setOnAction(e-> {
            this.view.getTableView().getColumns().clear();
            this.typeFilter=view.getChoiceBox().getValue();
            this.timeFilter=view.getChoiceBoxPeriod().getValue();
            filterBills();
            FilteredList<BookStatRecord> flUser = new FilteredList<>(statRecords, ev->true);

            this.view.getSearchBar().textProperty().addListener((ob,oldval,newval)->{
                if (typeFilter == "Book") {
                    flUser.setPredicate(bookStat->bookStat.getBookName().toLowerCase().trim().contains(newval.toLowerCase().trim()) || bookStat.getTimeForColumn().toLowerCase().trim().contains(newval.toLowerCase().trim()));
                }
                else if (typeFilter == "Author") {
                    flUser.setPredicate(bookStat->bookStat.getBook().getAuthor().toString().toLowerCase().trim().contains(newval.toLowerCase().trim()) || bookStat.getTimeForColumn().toLowerCase().trim().contains(newval.toLowerCase().trim()));
                }
                else if (typeFilter == "Category") {
                    flUser.setPredicate(bookStat->bookStat.getBook().getCategory().toLowerCase().trim().contains(newval.toLowerCase().trim()) || bookStat.getTimeForColumn().toLowerCase().trim().contains(newval.toLowerCase().trim()));
                }

            });
            this.view.getTableView().setItems(flUser);
        });
    }


    private void filterBills() {

        if (typeFilter.equals("Book")) {
            organiseBillsByBook();
            statRecords = filterDate(billsByBook);
            this.view.getTableView().getColumns().addAll(view.getBookNameColumn(), view.getIsbnColumn(), view.getSoldColumn(), view.getBoughtColumn(), view.getTimePeriodColumn());
        }
        if (typeFilter.equals("Author")) {
            organiseBillsByAuthor();
            statRecords = filterDate(billsByAuthor);
            this.view.getTableView().getColumns().addAll(view.getAuthorColumn(), view.getSoldColumn(), view.getBoughtColumn(), view.getTimePeriodColumn());
        }
        if (typeFilter.equals("Category")) {
            organiseBillByCategory();
            statRecords = filterDate(billsByCategory);
            this.view.getTableView().getColumns().addAll(view.getCategoryColumn(), view.getSoldColumn(), view.getBoughtColumn(), view.getTimePeriodColumn());
        }
    }

    private ObservableList<BookStatRecord> filterDate(HashMap<String, ArrayList<BookStatRecord>> bills) {
        HashMap<String, HashMap<Date, BookStatRecord>> filteredByDate = new HashMap<>();
        ObservableList<BookStatRecord> statList = FXCollections.observableArrayList();

        for (String x : bills.keySet()) {
            filteredByDate.put(x, new HashMap<>());
            for (BookStatRecord b : bills.get(x)) {
                if (timeFilter == "Daily") {
                    Date d = new Date(b.getTimePeriod().getYear(), b.getTimePeriod().getMonth(), b.getTimePeriod().getDate());
                    if (!CustomFunctions.isInSetDaily(b.getTimePeriod(), filteredByDate.get(x).keySet())) {
                        filteredByDate.get(x).put(d, b);
                    } else {
                        filteredByDate.get(x).get(d).updateQuantities(b.getSold(), b.getBought());
                    }
                    filteredByDate.get(x).get(d).setTimeForColumn(d.getDate() + " " + CustomFunctions.theMonth(d.getMonth()) + " " + (d.getYear() + 1900));
                }

                if (timeFilter == "Monthly") {
                    Date d = new Date(b.getTimePeriod().getYear(), b.getTimePeriod().getMonth(), 1);
                    if (!CustomFunctions.isInSetMonthly(b.getTimePeriod(), filteredByDate.get(x).keySet())) {
                        filteredByDate.get(x).put(d, b);
                    } else {
                        filteredByDate.get(x).get(d).updateQuantities(b.getSold(), b.getBought());
                    }
                    filteredByDate.get(x).get(d).setTimeForColumn(CustomFunctions.theMonth(d.getMonth()) + " " + (d.getYear() + 1900));
                }

                if (timeFilter == "Total") {
                    Date d = new Date(b.getTimePeriod().getYear(), 1, 1);
                    if (!CustomFunctions.isInSetYearly(b.getTimePeriod(), filteredByDate.get(x).keySet())) {
                        filteredByDate.get(x).put(d, b);
                    } else {
                        filteredByDate.get(x).get(d).updateQuantities(b.getSold(), b.getBought());
                    }
                    filteredByDate.get(x).get(d).setTimeForColumn("" + (d.getYear() + 1900));
                }
            }

            statList.removeAll();
            for (Date i : filteredByDate.get(x).keySet()) {
                statList.add(filteredByDate.get(x).get(i));
            }

        }

        return statList;
    }


    private void organiseBillsByBook() {
        for (Book b : booksDAO.getAll()) {
                billsByBook.put(b.getIsbn(), new ArrayList<>());
        }
        for (RestockBill x : restockBillDAO.getAll()) {
            billsByBook.get(x.getPurchasedBook().getIsbn()).add(new BookStatRecord(x.getPurchasedBook(), 0, x.getQuantity(), x.getDate()));
        }
        for (CustomerBill x : customerBillDAO.getAll()) {
            for (BillRecord b : x.getBillRecords()) {
                billsByBook.get(b.getBook().getIsbn()).add(new BookStatRecord(b.getBook(), b.getQuantity(), 0, x.getDate()));

            }
        }
    }

    private void organiseBillsByAuthor() {
        for (Author a : authorsDAO.getAll()) {
            billsByAuthor.put(a.toString(), new ArrayList<>());
        }

        for (RestockBill x : restockBillDAO.getAll()) {
            if (x.getPurchasedBook().isActive()) {
                billsByAuthor.get(x.getPurchasedBook().getAuthor().toString()).add(new BookStatRecord(x.getPurchasedBook(), 0, x.getQuantity(), x.getDate()));
            }
        }

        for (CustomerBill x : customerBillDAO.getAll()) {
            for (BillRecord b : x.getBillRecords()) {
                if (b.getBook().isActive()) {
                    billsByAuthor.get(b.getBook().getAuthor().toString()).add(new BookStatRecord(b.getBook(), b.getQuantity(), 0, x.getDate()));
                }
            }
        }
    }

    private void organiseBillByCategory() {
        for (String c : categoryDAO.getAll()) {
            billsByCategory.put(c, new ArrayList<>());
        }

        for (RestockBill x : restockBillDAO.getAll()) {
            if (x.getPurchasedBook().isActive()) {
                billsByCategory.get(x.getPurchasedBook().getCategory()).add(new BookStatRecord(x.getPurchasedBook(), 0, x.getQuantity(), x.getDate()));
            }
        }

        for (CustomerBill x : customerBillDAO.getAll()) {
            for (BillRecord b : x.getBillRecords()) {
                if (b.getBook().isActive()) {
                    billsByCategory.get(b.getBook().getCategory()).add(new BookStatRecord(b.getBook(), b.getQuantity(), 0, x.getDate()));
                }
            }

        }

    }


    public BookStatView getView() {
        return view;
    }

}










