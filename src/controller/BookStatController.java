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
    private final HashMap<String, ArrayList<BookStatRecord>> billsByBook = new HashMap<>();
    private final AuthorsDAO authorsDAO;
    private final HashMap<String, ArrayList<BookStatRecord>> billsByAuthor = new HashMap<>();
    private final CategoryDAO categoryDAO;
    private final HashMap<String, ArrayList<BookStatRecord>> billsByCategory = new HashMap<>();

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
                if (Objects.equals(typeFilter, "Book")) {
                    flUser.setPredicate(bookStat->bookStat.getBookName().toLowerCase().trim().contains(newval.toLowerCase().trim()) || bookStat.getTimeForColumn().toLowerCase().trim().contains(newval.toLowerCase().trim()));
                }
                else if (Objects.equals(typeFilter, "Author")) {
                    flUser.setPredicate(bookStat->bookStat.getBook().getAuthor().toString().toLowerCase().trim().contains(newval.toLowerCase().trim()) || bookStat.getTimeForColumn().toLowerCase().trim().contains(newval.toLowerCase().trim()));
                }
                else if (Objects.equals(typeFilter, "Category")) {
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
                if (Objects.equals(timeFilter, "Daily")) {
                    Date d = new Date(b.getTimePeriod().getYear(), b.getTimePeriod().getMonth(), b.getTimePeriod().getDate());
                    if (!CustomFunctions.isInSetDaily(b.getTimePeriod(), filteredByDate.get(x).keySet())) {
                        filteredByDate.get(x).put(d, b);
                    } else {
                        filteredByDate.get(x).get(d).updateQuantities(b.getSold(), b.getBought());
                    }
                    filteredByDate.get(x).get(d).setTimeForColumn(d.getDate() + " " + CustomFunctions.theMonth(d.getMonth()) + " " + (d.getYear() + 1900));
                }

                if (Objects.equals(timeFilter, "Monthly")) {
                    Date d = new Date(b.getTimePeriod().getYear(), b.getTimePeriod().getMonth(), 1);
                    if (!CustomFunctions.isInSetMonthly(b.getTimePeriod(), filteredByDate.get(x).keySet())) {
                        filteredByDate.get(x).put(d, b);
                    } else {
                        filteredByDate.get(x).get(d).updateQuantities(b.getSold(), b.getBought());
                    }
                    filteredByDate.get(x).get(d).setTimeForColumn(CustomFunctions.theMonth(d.getMonth()) + " " + (d.getYear() + 1900));
                }

                if (Objects.equals(timeFilter, "Total")) {
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










