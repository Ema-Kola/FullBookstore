package main;

import controller.UserController;
import dao.*;
import javafx.application.Application;
import javafx.stage.Stage;
import model.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import model.Role;


import java.util.ArrayList;
import java.util.Date;
public class Main extends Application {


    @Override
    public void start(Stage s) {
        UserController controller = new UserController(s);
        s.setTitle("Log In");
        s.setScene(controller.getView().showView(s));
        s.show();
    }

    public static void main(String[] args) {
        seedData();
        /*
        To check file content
        UsersDAO u= new UsersDAO();
        BooksDAO b = new BooksDAO();
        AuthorsDAO a = new AuthorsDAO();
        PermissionsDAO p = new PermissionsDAO();
        CategoryDAO c = new CategoryDAO();
        RestockBillDAO cb = new RestockBillDAO();
        CustomerBillDAO bb  = new CustomerBillDAO();
        PaycheckBillDAO pb = new PaycheckBillDAO();
        for(User x: u.getAll()){
            System.out.println(x+" "+x.getSalary());
        }
        for(Book x: b.getAll()){
            System.out.println(x+ " isbn:"+ x.getIsbn());
        }
        for(Author x: a.getAll()){
            System.out.println(x.toString());
        }
        for(Role x: p.getAll()){
            System.out.println(x);
        }
        for(String x: c.getAll()){
            System.out.println(x);
        }
        for(RestockBill x: cb.getAll()){
            System.out.println(x+" date: "+x.getDate()+" "+x.getEmployee());
        }
        for(CustomerBill x: bb.getAll()){
            System.out.println(x+" date: "+x.getDate()+" "+x.getEmployee());
        }

        for(PaycheckBill x: pb.getAll()){
            System.out.println(x);
        }

         */

        launch();
    }

    private static void seedData() {
        File file = new File(new UsersDAO().FILE_PATH);
        if (file.length() == 0) {
            User x = new User("Ema", "Kola",new Date(),Gender.FEMALE,"emko", "emakola22@gmail.com", "321",   Role.ADMINISTRATOR, "0695730257",0);
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(x);
            } catch (IOException ex) {
            }
        }

        File file1 = new File(new BooksDAO().FILE_PATH);
        if (file1.length() == 0) {
            Book x = new Book("123-12-12345-12-1","Net te bardha", "desc", "Pika Pa Siperfaqe",350,500,new Author("Fyodor", "Dostoevsky",Gender.MALE),23,"Novel");
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file1))) {
                outputStream.writeObject(x);
            } catch (IOException ex) {
            }
        }

        File file2 = new File(new AuthorsDAO().FILE_PATH);
        if (file2.length() == 0) {
            Author x = new Author("Fyodor", "Dostoevsky",Gender.MALE);
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file2))) {
                outputStream.writeObject(x);
            } catch (IOException ex) {
            }
        }

        File file3 = new File(new PermissionsDAO().FILE_PATH);
        if (file3.length() == 0) {
            ArrayList<Role> x = new ArrayList<Role>();
            x.add(Role.LIBRARIAN);
            x.add(Role.LIBRARIAN);
            x.add(Role.MANAGER);
            x.add(Role.MANAGER);
            x.add(Role.ADMINISTRATOR);
            x.add(Role.ADMINISTRATOR);
            x.add(Role.ADMINISTRATOR);

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file3))) {
                for(Role y : x){
                    outputStream.writeObject(y);
                }

            } catch (IOException ex) {
            }
        }

        File file4 = new File(new CategoryDAO().FILE_PATH);
        if (file4.length() == 0) {
            String x="Novel";
            String y="History";
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file4))) {
               outputStream.writeObject(x);
               outputStream.writeObject(y);
            } catch (IOException ex) {
            }
        }

    }
}


