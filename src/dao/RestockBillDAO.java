package dao;

import model.RestockBill;


public class RestockBillDAO extends DAO<RestockBill>{

        public RestockBillDAO(){
            super("files/restockBills.dat");
        }

        public RestockBillDAO(String filepath){
            super(filepath);
        }


}










