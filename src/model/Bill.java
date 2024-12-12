package model;

import java.io.*;
import java.util.Date;

public abstract class Bill implements Serializable {
    @Serial
    private static final long serialVersionUID= 1;

    private User employee;
    private Date date;

    public Bill(User employee) {
        this.date = new Date();
        this.employee=employee;
    }


    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public Date getDate() { return date; }

    public abstract double getTotal();


}
