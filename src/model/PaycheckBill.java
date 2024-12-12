package model;

public class PaycheckBill extends Bill{
    private double salary;

    public PaycheckBill(User employee, double salary){
        super(employee);
        this.salary = salary;

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
