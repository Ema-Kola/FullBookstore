package dao;

import model.CustomerBill;

import java.io.*;

public class CustomerBillDAO extends DAO<CustomerBill>{


        public CustomerBillDAO(){
            super("files/customerBills.dat");
        }



        public boolean printBill(CustomerBill bill){
            File file = new File("files/printedBills/Bill"+bill.getBillNo()+".txt");
            if (file.exists()) {
                System.out.println("File already exists");
                return false;
            }
            try(PrintWriter output = new PrintWriter(file)) {
                output.print(bill);
                return true;
            }catch(Exception ex){
                return false;
            }


        }






}
