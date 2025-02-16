package dao;

import model.CustomerBill;

import java.io.*;

public class CustomerBillDAO extends DAO<CustomerBill>{


        public CustomerBillDAO(){
            super("files/customerBills.dat");
        }
        public CustomerBillDAO(String filepath){
            super(filepath);
        }



        public boolean printBill(CustomerBill bill, String filepath){
            File file = new File(filepath+"Bill"+bill.getBillNo()+".txt");
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
