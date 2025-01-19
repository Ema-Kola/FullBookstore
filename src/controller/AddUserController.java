
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

    private final Alert alert1;
    private final Alert alert2;
    private final Alert alert3;


    public AddUserController (Stage stage, HomeView prevView)
    {
        alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setContentText("User saved successfully!");

        alert2 = new Alert(Alert.AlertType.ERROR);
        alert2.setContentText("Failed to save user.");

        alert3 = new Alert(Alert.AlertType.ERROR);
        alert3.setContentText("This user already exists. ");
        this.addUserView = new AddUserView(prevView);
        setUserDao(new UsersDAO());
        addUserView.getSubmitBtn().setOnAction(e->{

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
                    alert1.showAndWait();
                }else{
                    alert2.showAndWait();
                }
                } else {
                    alert3.showAndWait();
            }



        });


    }


    public void setUserDao(UsersDAO userDao) {
        this.userDao = userDao;
    }
    public AddUserView getView()
    {
        return this.addUserView;
    }
    public Alert getAlert1() {
        return alert1;
    }

    public Alert getAlert2() {
        return alert2;
    }

    public Alert getAlert3() {
        return alert3;
    }

}


