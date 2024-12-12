
package controller;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.*;
import view.AddUserView;
import view.HomeView;
import java.util.Date;

import dao.UsersDAO;
public class AddUserController {

    private UsersDAO userDao;
    private final AddUserView addUserView;

    public AddUserController (Stage stage, HomeView prevView)
    {
        this.addUserView = new AddUserView(prevView);

        addUserView.getSubmitBtn().setOnAction(e->{
            userDao = new UsersDAO();
            String firstName = addUserView.getFirstnameTF().getText();
            String lastName = addUserView.getLastnameTF().getText();
            Date birthdate = CustomFunctions.convertDate(addUserView.getbirthdayTF().getText());
            Gender gender = addUserView.getGender().getValue();
            String username = addUserView.getUsername().getText();
            String phoneNr = addUserView.getphoneTF().getText();
            String email = addUserView.getemailTF().getText();
            String password = addUserView.getPasswTF().getText();
            Role AccessLevel = addUserView.getaccessLevelTF().getValue();
            double salary =Double.parseDouble(addUserView.getsalaryTF().getText());
            User newUser = new User(firstName, lastName,birthdate,gender,username,email,password, AccessLevel,phoneNr,salary);

            if(userDao.searchUser(username)==null)
            {
                if(userDao.create(newUser)) {
                    System.out.println(newUser);
                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                    a.setContentText("User saved succesfully!");
                    a.showAndWait();
                }else{
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Failed to save user.");
                    a.showAndWait();
                }
                } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("User already exists.");
                a.showAndWait();
            }



        });


    }
    public AddUserView getView()
    {
        return this.addUserView;
    }

}


