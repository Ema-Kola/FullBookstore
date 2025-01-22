package model;

import java.io.Serial;
import java.util.Date;

public class PaycheckBill extends Bill{
    @Serial
    private static final long serialVersionUID= 2;
    private double salary;

    public PaycheckBill(User employee, double salary){
        super(employee);
        this.salary = salary;

    }

    public PaycheckBill(User employee, double salary, Date d){
        super(employee);
        this.salary = salary;
        this.setDate(d);

    }




    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }


    @Override
    public double getTotal() {
        return salary;
    }

    public String toString(){
        return getEmployee()+" "+salary;
    }


}
