package model;
import java.io.*;

import java.util.Date;

import javafx.beans.property.*;
public class User implements Serializable {

    private static final long serialVersionUID = -3292385108400454436L;
    private transient StringProperty firstName, lastName, email, password,username,phoneNumber;
    private Gender gender;
    private Date birthday;
    private Role role;
    private boolean active;

    private transient DoubleProperty salary;
    private transient StringProperty dateFormatted;

    public User(){

    }
    public User(String firstName, String lastName,Date birthday, Gender gender,String username,
                String email, String password, Role role, String phoneNumber,double salary) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.birthday=birthday;
        this.gender = gender;
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.role = role;
        this.phoneNumber= new SimpleStringProperty(phoneNumber);
        this.salary= new SimpleDoubleProperty(salary);
        this.dateFormatted = new SimpleStringProperty(birthday.getDate()+"-"+(birthday.getMonth()+1)+"-"+birthday.getYear());
        this.active=true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getDateFormatted() {
        return dateFormatted.get();
    }


    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted.set(dateFormatted);
    }

    public String getEmail() {
        return this.email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }


    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public double getSalary() { return salary.get(); }

    public void setSalary(double salary) { this.salary.set(salary); }

    @Override
    public String toString() {
        return firstName.get()+" "+lastName.get();
        // return "User [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password
        //  + ", gender=" + gender + ", phone=" + phoneNumber+ ", birthday="+ birthday+", role=" +role+", salary=" +salary+ "]";
    }
    @Serial
    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        outputStream.defaultWriteObject();
        outputStream.writeUTF(this.firstName.getValueSafe());
        outputStream.writeUTF(this.lastName.getValueSafe());
        outputStream.writeUTF(this.email.getValueSafe());
        outputStream.writeUTF(this.password.getValueSafe());
        outputStream.writeUTF(this.username.getValueSafe());
        outputStream.writeDouble(this.salary.getValue());
        outputStream.writeUTF(this.phoneNumber.getValueSafe());

    }

    @Serial
    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();
        this.firstName = new SimpleStringProperty(inputStream.readUTF());
        this.lastName = new SimpleStringProperty(inputStream.readUTF());
        this.email = new SimpleStringProperty(inputStream.readUTF());
        this.password = new SimpleStringProperty(inputStream.readUTF());
        this.username =new SimpleStringProperty(inputStream.readUTF());
        this.salary = new SimpleDoubleProperty(inputStream.readDouble());
        this.phoneNumber = new SimpleStringProperty(inputStream.readUTF());

    }


    @Override

    public boolean equals(Object u)
    {
        if(u instanceof User)
        {
            if(this.getUsername().equals(((User) u).getUsername()))
                return true;
        }
        return false;
    }

}
