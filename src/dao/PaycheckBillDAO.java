package dao;


import model.PaycheckBill;

public class PaycheckBillDAO extends DAO<PaycheckBill>{

        public PaycheckBillDAO(){
            super("files/paycheckBills.dat");
        }
        public PaycheckBillDAO(String path){
            super(path);
        }


}
