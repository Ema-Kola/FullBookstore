
package controller;

import dao.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Role;
import model.User;
import view.HomeView;
import view.ManageEmployeeView;


public class ManageEmployeeController {

    private final UsersDAO usersDao;
    private ManageEmployeeView view ;

    private boolean filterActive;

    public ManageEmployeeController(Stage stage,HomeView prevView)
    {
        this.view = new ManageEmployeeView(prevView);
        this.usersDao = new UsersDAO();
        //filterActive = true;
        filterTable();
        this.view.getDeleteBtn().setOnAction(e -> FireEmployee());

        this.view.getHomeBtn().setOnAction(e->{
            stage.setScene(prevView.showView(stage));
        });
        setEditListeners();

    }
    private void filterTable(){
        FilteredList<User> flUser = new FilteredList<>(usersDao.getAll(), user->(user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR)));
        FilteredList<User> flUser2 = new FilteredList<>(flUser, e->true);

        view.getChoiceBox().valueProperty().addListener((ob, oldval, newval) -> {

            if(newval.equals("Active")){
                filterActive = true;
            }else if(newval.equals("Inactive")){
                filterActive=false;
            }
            flUser.setPredicate(user -> user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR));

            this.view.getSearch().textProperty().addListener((obs,oldVal,newVal)->{
                flUser2.setPredicate(user->(user.getFirstName().toLowerCase().trim().contains(newVal.toLowerCase().trim())
                        ||user.getLastName().toLowerCase().trim().contains(newVal.toLowerCase().trim()) ||
                        user.getEmail().toLowerCase().trim().contains(newVal.toLowerCase().trim())));
            });

        });


        this.view.getTableView().setItems(flUser2);


    }

    private void FireEmployee() {
        ObservableList<User> selectedEmployees = this.view.getTableView().getSelectionModel().getSelectedItems();
        for(User u : selectedEmployees){
            u.setActive(!filterActive);
        }

        if (usersDao.updateAll()) {
            filterTable();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("User status updated successfully");
            alert.setTitle("Successful");
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("User status not updated");
            alert.setTitle("ERROR");
            alert.showAndWait();

        }

    }






    private void setEditListeners() {

        this.view.getFirstNameColumn().setOnEditCommit(e -> {
            if(e.getNewValue().isEmpty()){
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("First name cannot be empty");
                a.show();
            }
            else if(!e.getNewValue().matches("[a-zA-Z]+")){

                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Invalid input");
                a.show();
            }

            else {
                FilteredList<User> flUser = new FilteredList<>(usersDao.getAll(), user->user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR));
                FilteredList<User> flUser2 = new FilteredList<>(flUser, e1->true);
                flUser2.get(e.getTablePosition().getRow()).setFirstName(e.getNewValue());
            }
        });

        this.view.getLastNameColumn().setOnEditCommit(e -> {
            if(e.getNewValue().isEmpty()){
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Last name cannot be empty");
                a.show();}
            else if(!e.getNewValue().matches("[a-zA-Z]+"))
            {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Invalid input");
                a.show();

            }else {
                FilteredList<User> flUser = new FilteredList<>(usersDao.getAll(), user->user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR));
                FilteredList<User> flUser2 = new FilteredList<>(flUser, e1->true);
                flUser2.get(e.getTablePosition().getRow()).setLastName(e.getNewValue());
            }
        });



        this.view.getGenderColumn().setOnEditCommit(e->{
            if(e.getNewValue()==null)
            {
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Gender cannot be empty");
                a.show();
            }
            else {
                FilteredList<User> flUser = new FilteredList<>(usersDao.getAll(), user->user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR));
                FilteredList<User> flUser2 = new FilteredList<>(flUser, e1->true);
                flUser2.get(e.getTablePosition().getRow()).setGender(e.getNewValue());
            }
        });

        this.view.getEmailColumn().setOnEditCommit(e -> {
            if(e.getNewValue().isEmpty()){
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Email cannot be empty");
                a.show();
            }
            else if(!e.getNewValue().matches("[a-zA-Z]{2,}\\@lib\\.com"))
            {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Invalid email input");
                a.show();
            }else {
                FilteredList<User> flUser = new FilteredList<>(usersDao.getAll(), user->user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR));
                FilteredList<User> flUser2 = new FilteredList<>(flUser, e1->true);
                flUser2.get(e.getTablePosition().getRow()).setEmail(e.getNewValue());
            }
        });

        this.view.getPasswordColumn().setOnEditCommit(e -> {
            if(e.getNewValue().isEmpty()){
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Password cannot be empty");
                a.show();

            }else {
                FilteredList<User> flUser = new FilteredList<>(usersDao.getAll(), user->user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR));
                FilteredList<User> flUser2 = new FilteredList<>(flUser, e1->true);
                flUser2.get(e.getTablePosition().getRow()).setPassword(e.getNewValue());
            }
        });

        this.view.getAccessLevelColumn().setOnEditCommit(e -> {

            if(e.getNewValue()==null)
            {
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Access Level cannot be empty");
                a.show();

            }
            else {
                FilteredList<User> flUser = new FilteredList<>(usersDao.getAll(), user->user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR));
                FilteredList<User> flUser2 = new FilteredList<>(flUser, e1->true);
                flUser2.get(e.getTablePosition().getRow()).setRole(e.getNewValue());
            }
        });

        this.view.getPhoneColumn().setOnEditCommit(e -> {
            if(e.getNewValue().isEmpty()){
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Phone Number cannot be empty");
                a.show();
            }
            else if( !e.getNewValue().matches("\\+3556[7-9]\\d{3}\\d{2}\\d{2}"))
            {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Phone Number is not valid");
                a.show();



            }else {
                FilteredList<User> flUser = new FilteredList<>(usersDao.getAll(), user->user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR));
                FilteredList<User> flUser2 = new FilteredList<>(flUser, e1->true);
                flUser2.get(e.getTablePosition().getRow()).setPhoneNumber(e.getNewValue());
            }
        });

        this.view.getSalaryColumn().setOnEditCommit(e -> {
            if(e.getNewValue() == null)
            {
                System.out.println("cannot be empty");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Selling Price cannot be empty");
                a.show();
            }
            else if(!(e.getNewValue()>0))
            {
                System.out.println("not valid ");
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Selling Price is not valid");
                a.show();
            }else {
                FilteredList<User> flUser = new FilteredList<>(usersDao.getAll(), user->user.isActive()==filterActive && !user.getRole().equals(Role.ADMINISTRATOR));
                FilteredList<User> flUser2 = new FilteredList<>(flUser, e1->true);
                flUser2.get(e.getTablePosition().getRow()).setSalary(e.getNewValue());
            }
        });

        this.view.getUpdateBtn().setOnAction(e1 -> {
            if(this.usersDao.updateAll()) {
                System.out.println("content updated");
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("Content successfully updated");
                a.show();
            } else {
                System.out.println("update failed");
            }
        });


    }




    public ManageEmployeeView getView() {
        return view;
    }

}
