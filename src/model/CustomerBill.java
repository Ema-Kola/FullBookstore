package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.util.*;


public class CustomerBill extends Bill {


    private transient ObservableList<BillRecord> billRecords;

    private int billNo;


    public CustomerBill(User employee) {
        super(employee);
        billRecords=FXCollections.observableArrayList();
    }

    public ObservableList<BillRecord> getBillRecords() {
        return billRecords;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    @Override
    public double getTotal() {
        double total = 0;
        for (BillRecord b : billRecords) {
            total += b.getTotal();
        }
        return total;
    }

    public String toString() {
        String s = "\n\nBill no." + billNo + "\nPrice\t\tQuantity\tTotal\n-------------------------------------------\n";

        for (BillRecord b : this.billRecords) {
            s += b.toString() + "\n__________________________________________________\n";
        }
        s += "__________________________________________________\nTotal\t\t\t\t\t" + getTotal() + " leke.\n";
        return s;
    }


    @Serial
    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeObject(new ArrayList<>(billRecords));
    }

    @Serial
    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        ArrayList<?> list = (ArrayList<?>) inputStream.readObject();
        this.billRecords = (ObservableList<BillRecord>) FXCollections.observableArrayList(list);

    }
}
