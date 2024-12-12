package model;

import java.util.Date;

public class FinanceStatRecord {
    private double income;
    private double cost;
    private double restock;
    private double salaries;
    private double netBalance;
    private Date period;
    private String timeColumn;


    public FinanceStatRecord(double income, double cost, Date period) {
        this.income = income;
        this.cost = cost;
        this.period = period;
    }
    public void updateNums(double cost, double income){
        this.cost+=cost;
        this.income+=income;
    }

    public double getNetBalance() {
        return netBalance;
    }

    public void updateNetBalance(double netBalance) {
        this.netBalance += netBalance;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getCost() {
        return cost;
    }
    public double getRestock() {
        return restock;
    }

    public void setRestock(double restock) {
        this.restock = restock;
    }

    public double getSalaries() {
        return salaries;
    }

    public void setSalaries(double salaries) {
        this.salaries = salaries;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public String getTimeColumn() {
        return timeColumn;
    }

    public void setTimeColumn(String timeColumn) {
        this.timeColumn = timeColumn;
    }
}
