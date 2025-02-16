package controller;

import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import model.*;
import view.FinanceView;
import view.HomeView;
import java.util.Date;
import java.util.HashMap;


public class FinanceController {


        private FinanceView view;
        private CustomerBillDAO customerBillDAO;
        private RestockBillDAO restockBillDAO;
        private PaycheckBillDAO paycheckBillDAO;
        private String timeFilter;
        private ObservableList<FinanceStatRecord> cacheMonthly;
        private ObservableList<FinanceStatRecord> cacheYearly;



        public FinanceController(Stage stage, HomeView prevView){
            this.view = new FinanceView(prevView);
            this.customerBillDAO = new CustomerBillDAO();
            this.restockBillDAO = new RestockBillDAO();
            this.paycheckBillDAO = new PaycheckBillDAO();
            cacheMonthly = FXCollections.observableArrayList();
            cacheYearly = FXCollections.observableArrayList();
            this.view.getBtnHome().setOnAction(e -> {stage.setScene(prevView.showView(stage));});

            this.view.getChoiceBoxPeriod().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)
                    -> {
                this.timeFilter = newVal;
                this.view.getTableView().setItems(filterDate(timeFilter));
            });
        }


        public ObservableList<FinanceStatRecord> filterDate(String timeFilter){
            HashMap<Date, FinanceStatRecord> filteredByDate= new HashMap<>();
            ObservableList<FinanceStatRecord> statList=FXCollections.observableArrayList();
            if(timeFilter.equals("Monthly")){
                if(!cacheMonthly.isEmpty()){
                    //System.out.println("Cache Muaj");
                    return cacheMonthly;
                }else{
                    //System.out.println("Jo Cache Muaj");
                    for(CustomerBill c : customerBillDAO.getAll()){
                        Date d = new Date(c.getDate().getYear(),c.getDate().getMonth(),1);
                        if (!CustomFunctions.isInSetMonthly(d,filteredByDate.keySet())){
                            filteredByDate.put(d,new FinanceStatRecord(c.getTotal(),0,d));
                            filteredByDate.get(d).setTimeColumn(CustomFunctions.theMonth(d.getMonth())+" "+(d.getYear()+1900));
                        }else{
                            filteredByDate.get(d).updateNums(0,c.getTotal());
                        }

                        filteredByDate.get(d).updateNetBalance(c.getTotal());
                    }
                    for(RestockBill c : restockBillDAO.getAll()){
                        Date d = new Date(c.getDate().getYear(),c.getDate().getMonth(),1);
                        if (!CustomFunctions.isInSetMonthly(d,filteredByDate.keySet())){
                            filteredByDate.put(d,new FinanceStatRecord(0,c.getTotal(),d));
                            filteredByDate.get(d).setTimeColumn(CustomFunctions.theMonth(d.getMonth())+" "+(d.getYear()+1900));
                        }else{
                            filteredByDate.get(d).updateNums(c.getTotal(),0);
                        }
                            filteredByDate.get(d).updateNetBalance((c.getTotal())*-1);
                    }
                    for(PaycheckBill c : paycheckBillDAO.getAll()){
                        Date d = new Date(c.getDate().getYear(),c.getDate().getMonth(),1);
                        if (!CustomFunctions.isInSetMonthly(d,filteredByDate.keySet())){
                            filteredByDate.put(d,new FinanceStatRecord(0,c.getTotal(),d));
                            filteredByDate.get(d).setTimeColumn(CustomFunctions.theMonth(d.getMonth())+" "+(d.getYear()+1900));
                        }else{
                            filteredByDate.get(d).updateNums(c.getTotal(),0);
                        }
                        filteredByDate.get(d).updateNetBalance((c.getTotal())*-1);
                    }

                    for(Date i : filteredByDate.keySet()){
                        statList.add(filteredByDate.get(i));
                    }
                    this.cacheMonthly.addAll(statList);
                    return cacheMonthly;
                }
            }

            if(timeFilter.equals("Yearly")){
                if(!cacheYearly.isEmpty()){
                    //System.out.println("Cache Vit");
                    return cacheYearly;
                }else{
                    //System.out.println("Jo Cache Vit");
                    for (CustomerBill c : customerBillDAO.getAll()) {
                        Date d = new Date(c.getDate().getYear(), 1, 1);
                        if (!CustomFunctions.isInSetYearly(d, filteredByDate.keySet())) {
                            filteredByDate.put(d, new FinanceStatRecord(c.getTotal(), 0, d));
                            filteredByDate.get(d).setTimeColumn("" + (d.getYear()+1900));
                        } else {
                            filteredByDate.get(d).updateNums(0, c.getTotal());
                        }
                        filteredByDate.get(d).updateNetBalance(c.getTotal());
                    }
                    for (RestockBill c : restockBillDAO.getAll()) {
                        Date d = new Date(c.getDate().getYear(), 1, 1);
                        if (!CustomFunctions.isInSetYearly(d, filteredByDate.keySet())) {
                            filteredByDate.put(d, new FinanceStatRecord(0, c.getTotal(), d));
                            filteredByDate.get(d).setTimeColumn("" + (d.getYear()+1900));
                        } else {
                            filteredByDate.get(d).updateNums(c.getTotal(), 0);
                        }
                        filteredByDate.get(d).updateNetBalance((c.getTotal()) * -1);

                    }

                    for (PaycheckBill c : paycheckBillDAO.getAll()) {
                        Date d = new Date(c.getDate().getYear(), 1, 1);
                        if (!CustomFunctions.isInSetYearly(d, filteredByDate.keySet())) {
                            filteredByDate.put(d, new FinanceStatRecord(0, c.getTotal(), d));
                            filteredByDate.get(d).setTimeColumn("" + (d.getYear()+1900));
                        } else {
                            filteredByDate.get(d).updateNums(c.getTotal(), 0);
                        }
                        filteredByDate.get(d).updateNetBalance((c.getTotal()) * -1);

                    }
                    for(Date i : filteredByDate.keySet()){
                        statList.add(filteredByDate.get(i));
                    }
                    this.cacheYearly.addAll(statList);
                    return cacheYearly;
                }

            }
            return null;
        }





        public FinanceView getView() {
            return view;
        }


    public void setCustomerBillDAO(CustomerBillDAO customerBillDao) {
            this.customerBillDAO = customerBillDao;
    }


    public void setRestockBillDAO(RestockBillDAO restockBillDAO) {
            this.restockBillDAO = restockBillDAO;
    }

    public void setPaycheckBillDAO(PaycheckBillDAO paycheckBillDAO) {
            this.paycheckBillDAO = paycheckBillDAO;
    }

    public void setCacheMonthly(ObservableList<FinanceStatRecord> cacheMonthly) {
            this.cacheMonthly = cacheMonthly;
    }

    public void setCacheYearly(ObservableList<FinanceStatRecord> cacheYearly) {
            this.cacheYearly = cacheYearly;
    }
}
