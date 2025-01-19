package test.unit;

import controller.FinanceController;
import controller.UserController;
import dao.CustomerBillDAO;
import dao.PaycheckBillDAO;
import dao.RestockBillDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;
import view.HomeView;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FinanceControllerTest extends ApplicationTest {
    private final ObservableList<FinanceStatRecord> cacheMonthly = FXCollections.observableArrayList();
    private final ObservableList<FinanceStatRecord> cacheYearly = FXCollections.observableArrayList();
    private FinanceController financeController;
    @Mock CustomerBillDAO customerBillDao;
    @Mock RestockBillDAO restockBillDAO;
    @Mock PaycheckBillDAO paycheckBillDAO;

    @Override
    public void start(Stage stage){
        MockitoAnnotations.openMocks(this);
        financeController = new FinanceController(stage, new HomeView(new User(), new UserController(stage)));

        stage.setScene(financeController.getView().showView(stage));
        stage.show();
    }

    @BeforeEach
    public void setup(){
        FinanceStatRecord record1 = mock(FinanceStatRecord.class);
        cacheYearly.add(record1);
        cacheMonthly.add(record1);

        financeController.setCustomerBillDAO(customerBillDao);
        financeController.setRestockBillDAO(restockBillDAO);
        financeController.setPaycheckBillDAO(paycheckBillDAO);
        financeController.setCacheMonthly(cacheMonthly);
        financeController.setCacheYearly(cacheYearly);
    }

    @Test
    public void cacheMonthly_filterDate(){
        assertEquals(cacheMonthly,financeController.filterDate("Monthly"));
        verify(customerBillDao, times(0)).getAll();
        verify(restockBillDAO, times(0)).getAll();
        verify(paycheckBillDAO, times(0)).getAll();


    }

    @Test
    public void cacheYearly_filterDate(){
        assertEquals(cacheYearly,financeController.filterDate("Yearly"));
        verify(customerBillDao, times(0)).getAll();
        verify(restockBillDAO, times(0)).getAll();
        verify(paycheckBillDAO, times(0)).getAll();

    }

    @Test
    public void emptyCacheMonthly_filterDate(){
        cacheMonthly.clear();

        financeController.setCacheMonthly(cacheMonthly);

        CustomerBill cBill1 = mock(CustomerBill.class);
        CustomerBill cBill2 = mock(CustomerBill.class);
        RestockBill rBill1 = mock(RestockBill.class);
        RestockBill rBill2 = mock(RestockBill.class);
        PaycheckBill pBill1 = mock(PaycheckBill.class);
        PaycheckBill pBill2 = mock(PaycheckBill.class);


        ObservableList<CustomerBill> cBills = FXCollections.observableArrayList();
        cBills.add(cBill1);
        cBills.add(cBill2);

        ObservableList<RestockBill> rBills = FXCollections.observableArrayList();
        rBills.add(rBill1);
        rBills.add(rBill2);

        ObservableList<PaycheckBill> pBills = FXCollections.observableArrayList();
        pBills.add(pBill1);
        pBills.add(pBill2);

        when(customerBillDao.getAll()).thenReturn(cBills);
        when(restockBillDAO.getAll()).thenReturn(rBills);
        when(paycheckBillDAO.getAll()).thenReturn(pBills);

        populateBills(cBills, 2, 2004);
        populateBills(rBills, 2, 2004);
        populateBills(pBills, 3, 2004);

        ObservableList<FinanceStatRecord> records = financeController.filterDate("Monthly");
        //income from cBills, cost from rBills and pBills
        assertEquals(2, records.size());
        FinanceStatRecord recordMarch2004 = records.getFirst();
        //March 2004+1900 there are a few issues with the getYear used since it is deprecated
        assertEquals("March 3904", recordMarch2004.getTimeColumn());
        assertEquals(20.0, recordMarch2004.getIncome());
        assertEquals(20.0, recordMarch2004.getCost());
        assertEquals(0.0, recordMarch2004.getNetBalance());


        FinanceStatRecord recordApril2005 = records.get(1);
        assertEquals("April 3904", recordApril2005.getTimeColumn());        //April 2004+1900
        assertEquals(0.0, recordApril2005.getIncome());
        assertEquals(20.0, recordApril2005.getCost());
        assertEquals(-20.0, recordApril2005.getNetBalance());

    }

    @Test
    public void emptyCacheYearly_filterDate(){
        cacheYearly.clear();

        financeController.setCacheYearly(cacheYearly);

        CustomerBill cBill1 = mock(CustomerBill.class);
        CustomerBill cBill2 = mock(CustomerBill.class);
        RestockBill rBill1 = mock(RestockBill.class);
        RestockBill rBill2 = mock(RestockBill.class);
        PaycheckBill pBill1 = mock(PaycheckBill.class);
        PaycheckBill pBill2 = mock(PaycheckBill.class);

        ObservableList<CustomerBill> cBills = FXCollections.observableArrayList();
        cBills.add(cBill1);
        cBills.add(cBill2);

        ObservableList<RestockBill> rBills = FXCollections.observableArrayList();
        rBills.add(rBill1);
        rBills.add(rBill2);

        ObservableList<PaycheckBill> pBills = FXCollections.observableArrayList();
        pBills.add(pBill1);
        pBills.add(pBill2);

        when(customerBillDao.getAll()).thenReturn(cBills);
        when(restockBillDAO.getAll()).thenReturn(rBills);
        when(paycheckBillDAO.getAll()).thenReturn(pBills);

        populateBills(cBills, 2, 2004);
        populateBills(rBills, 2, 2005);

        when(pBill1.getTotal()).thenReturn(100.0);
        when(pBill1.getDate()).thenReturn(new Date(2005,6,30));

        when(pBill2.getTotal()).thenReturn(200.0);
        when(pBill2.getDate()).thenReturn(new Date(2006,6,30));



        ObservableList<FinanceStatRecord> records = financeController.filterDate("Yearly");

        assertEquals(3,records.size());
        // 2004 cost 0, income 2*10.0 cBills
        // 2005 cost 2*10.0 rBills + 100.0 pBill 1, income 0
        // 2006 cost 200.0 pBill 2, income 0
        FinanceStatRecord record2004 = records.get(2);
        assertEquals("3904", record2004.getTimeColumn()); //2004+1900
        assertEquals(20.0, record2004.getIncome());
        assertEquals(0.0, record2004.getCost());
        assertEquals(20.0, record2004.getNetBalance());

        FinanceStatRecord record2005 = records.get(1);
        assertEquals("3905", record2005.getTimeColumn());//2005+1900
        assertEquals(0.0, record2005.getIncome());
        assertEquals(120.0, record2005.getCost());
        assertEquals(-120.0, record2005.getNetBalance());

        FinanceStatRecord record2006 = records.getFirst();
        assertEquals("3906", record2006.getTimeColumn());//2006+1900
        assertEquals(0.0, record2006.getIncome());
        assertEquals(200.0, record2006.getCost());
        assertEquals(-200.0, record2006.getNetBalance());
    }

    public<T extends Bill> void populateBills(ObservableList<T> bills, int m, int y){
        for(Bill b : bills){
            when(b.getTotal()).thenReturn(10.0);
            when(b.getDate()).thenReturn(new Date(y, m, 30));

        }
    }
}